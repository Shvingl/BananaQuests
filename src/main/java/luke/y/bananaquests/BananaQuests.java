package luke.y.bananaquests;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import luke.y.bananaquests.commands.QuestadminCommand;
import luke.y.bananaquests.commands.QuestadminTabCompletion;
import luke.y.bananaquests.commands.QuestsCommand;
import luke.y.bananaquests.listeners.InventoryClickListener;
import luke.y.bananaquests.listeners.QuestCompleteListener;
import luke.y.bananaquests.listeners.objectivelisteners.*;
import luke.y.bananaquests.listeners.PlayerJoinListener;
import luke.y.bananaquests.listeners.PlayerLeaveListener;
import luke.y.bananaquests.objective.QuestObjective;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class BananaQuests extends JavaPlugin {

    public static Set<String> validQuestIDs = new HashSet<>();
    public static final HashMap<Player, ArrayList<ActiveQuest>> activeQuestsMap = new HashMap<>();
    public static final HashMap<Player, ActiveQuest> trackedQuestMap = new HashMap<>();
    public static final HashMap<String, YamlConfiguration> questConfigs = new HashMap<>();
    private final Song song = NBSDecoder.parse(new File(getDataFolder() + "/jingle.nbs"));

    public Song getSong() {
        return song;
    }

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new QuestCompleteListener(this), this);

        getServer().getPluginManager().registerEvents(new MobKillListener(), this);
        getServer().getPluginManager().registerEvents(new MythicMobKillListener(), this);
        getServer().getPluginManager().registerEvents(new OutpostFreeListener(), this);
        getServer().getPluginManager().registerEvents(new FinishShootingGameListener(), this);
        getServer().getPluginManager().registerEvents(new RegionEnterListener(), this);

        Objects.requireNonNull(getCommand("quests")).setExecutor(new QuestsCommand());
        Objects.requireNonNull(getCommand("questadmin")).setExecutor(new QuestadminCommand());
        getCommand("questadmin").setTabCompleter(new QuestadminTabCompletion());


        //Projit všechny quest .yml soubory a dát je do validQuestIDs
        for (File file : Objects.requireNonNull(new File(this.getDataFolder().getAbsolutePath() + File.separator + "quests").listFiles())) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String questID = file.getName().replace(".yml", "");
            questConfigs.put(questID, config);
        }
        validQuestIDs = questConfigs.keySet();

        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayersQuests(player);
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new BananaQuestsExpansion(this).register();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayersQuests(player);
        }
    }

    /**
     * Save data to player.yml
     */
    public void savePlayersQuests(Player player) {
        File playerFile = getPlayerFile(player);
        YamlConfiguration playerConfig = new YamlConfiguration();

        final ArrayList<String> finishedIDS = new ArrayList<>();
        for (ActiveQuest activeQuest : activeQuestsMap.get(player)) {
            if (activeQuest.isFinished()) {
                finishedIDS.add(activeQuest.getId());
                playerConfig.set("active." + activeQuest.getId(), null);
            }
            else {
                playerConfig.set("active." + activeQuest.getId() + ".stage", activeQuest.getStage());
                for (int i = 0; i < activeQuest.getCurrentObjectives().size(); i++) {
                    int objectiveID = i+1;
                    playerConfig.set("active." + activeQuest.getId() + ".objectives-progress." + objectiveID, activeQuest.getCurrentObjectives().get(i).getProgress());
                }
            }
        }

        playerConfig.set("finished", finishedIDS);

        try {
            playerConfig.save(playerFile);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Vyskytl se problém s uložením questů hráče " + player.getName());
        }

        activeQuestsMap.remove(player);
    }

    /**
     * Method to be called on each player join.
     * Loads player's active quests from the .yml file to the plugin's
     * player quest map.
     */
    public void loadPlayersQuests(Player player) {

        File playerFile = getPlayerFile(player);

        ArrayList<ActiveQuest> questsToAdd = new ArrayList<>();
        YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);

        ArrayList<String> finishedQuests = (ArrayList<String>) playerConfig.getList("finished");
        if (finishedQuests == null || finishedQuests.isEmpty()) {
            Bukkit.getLogger().info("Hráč " + player.getName() + " nemá žádné hotové questy.");
        }
        else {
            for (String questID : finishedQuests) {

                ActiveQuest quest = new ActiveQuest(questID, player, 999 /*tady asi zjistit z configu max stage??*/, null, true);
                questsToAdd.add(quest);
            }
        }

        ConfigurationSection activeSection = playerConfig.getConfigurationSection("active");
        if (activeSection == null || activeSection.getKeys(false).isEmpty()) {
            Bukkit.getLogger().info("Hráč " + player.getName() + " nemá žádné aktivní questy.");
        }
        else {
            for (String questID : activeSection.getKeys(false)) {

                ConfigurationSection thisQuestSection = playerConfig.getConfigurationSection("active." + questID);

                if (thisQuestSection == null)
                    continue;

                int stage = thisQuestSection.getInt("stage");

                //Check if stage is null here

                YamlConfiguration questConfig = questConfigs.get(questID);

                if (stage > questConfig.getConfigurationSection("stages").getKeys(false).size()) {
                    Bukkit.getLogger().warning(player.getName() + " má neplatný stage questu " + questID);
                    continue;
                }


                ArrayList<Integer> objectivesProgress = new ArrayList<>();

                for (String objectiveID : thisQuestSection.getConfigurationSection(".objectives-progress").getKeys(false)) {
                    int objectiveProgress = thisQuestSection.getInt(".objectives-progress." + objectiveID);
                    objectivesProgress.add(objectiveProgress);
                }
                questsToAdd.add(new ActiveQuest(questID, player, stage, objectivesProgress, false));
            }
        }

        activeQuestsMap.put(player, questsToAdd);
        trackQuest(player, getOngoingQuests(player).get(0));
    }

    public static void finishQuest(Player player, String id) {
        ActiveQuest questToFinish = getQuestById(player, id);
        if (questToFinish == null) {
            player.sendMessage(ChatColor.RED + "Error: plugin se ti snaží dokončit quest, který jsi nezačal. Kontaktuj admina s aktuálním časem.");
        }
        else {
            questToFinish.finishQuest();
        }
    }

    public static void finishQuest(Player player, ActiveQuest activeQuest) {
        finishQuest(player, activeQuest.getId());
    }

    public static ActiveQuest getQuestById(Player player, String id) {
        for (ActiveQuest activeQuest : activeQuestsMap.get(player)) {
            if (activeQuest.getId().equalsIgnoreCase(id)) {
                return activeQuest;
            }
        }
        return null;
    }

    public static ArrayList<ActiveQuest> getOngoingQuests(Player player) {
        ArrayList<ActiveQuest> toReturn = new ArrayList<>();
        for (ActiveQuest activeQuest : BananaQuests.activeQuestsMap.get(player)) {
            if (!(activeQuest.isFinished())) {
                toReturn.add(activeQuest);
            }
        }
        return toReturn;
    }

    public static ArrayList<ActiveQuest> getFinishedQuests(Player player) {
        ArrayList<ActiveQuest> toReturn = new ArrayList<>();
        for (ActiveQuest activeQuest : BananaQuests.activeQuestsMap.get(player)) {
            if (activeQuest.isFinished()) {
                toReturn.add(activeQuest);
            }
        }
        return toReturn;
    }

    public static void forceBeginQuest(String id, Player player) {
        if (!validQuestIDs.contains(id)) {
            player.sendMessage(ChatColor.RED + "Plugin se ti pokusil odstartovat neexistujicí quest. Napiš to adminovi s aktuálním časem.");
            return;
        }

    }

    public static void beginQuest(String id, Player player) {
        if (!validQuestIDs.contains(id)) {
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "FATÁLNÍ CHYBA: Plugin se ti pokusil odstartovat neexistujicí Quest. Napiš to adminovi společně s aktuálním časem.");
            return;
        }
        ArrayList<ActiveQuest> questList = activeQuestsMap.get(player);
        for (ActiveQuest activeQuest : questList) {
            if (activeQuest.getId().equalsIgnoreCase(id)) {
                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                player.sendMessage("");
                player.sendMessage(ChatColor.RED + "Tento Quest už je aktivní.");
                return;
            }
        }

        ActiveQuest newQuest = new ActiveQuest(id, player, 1, new ArrayList<>(), false);
        questList.add(newQuest);
        player.sendMessage("");
        player.playSound(player, Sound.ENTITY_VILLAGER_WORK_CARTOGRAPHER, 1, 1);
        player.sendMessage(ChatColor.DARK_GREEN + "Začal jsi nový Quest " + ChatColor.GOLD + newQuest.getDisplay());
        activeQuestsMap.put(player, questList);
        trackQuest(player, newQuest);
    }

    public static void trackNewQuest(Player player) {
        if (!getOngoingQuests(player).isEmpty()) {
            trackQuest(player, getOngoingQuests(player).get(0));
        }
        else {
            player.sendMessage("DEBUG: Už žádný quest nelze trackovat...");
            trackQuest(player, null);
        }
    }

    public static void finishStage(ActiveQuest quest, int stage) {
        if (quest.getStage() != stage)
            return;
        for (QuestObjective objective : quest.getCurrentObjectives()) {
            objective.setFinished(true);
        }
        quest.tryMoveToNextStage();
    }

    public static void finishObjective(ActiveQuest quest, int stage, int objective) {
        if (quest.getStage() != stage)
            return;
        QuestObjective toFinish = quest.getCurrentObjectives().get(objective);
        if (toFinish == null)
            return;
        toFinish.setFinished(true);
        quest.tryMoveToNextStage();
    }

    public static void forgetQuest(Player player, ActiveQuest activeQuest) {
        activeQuestsMap.get(player).remove(activeQuest);
    }

    public static void forgetAllQuests(Player player) {
        activeQuestsMap.get(player).clear();
    }

    public static void trackQuest(Player player, ActiveQuest questToTrack) {
        trackedQuestMap.put(player, questToTrack);
    }

    public File getPlayerFile(Player player) {
        File playerFile = new File(this.getDataFolder().getAbsoluteFile() + File.separator + "playerdata" + File.separator + player.getName() + ".yml");
        if (!playerFile.exists()) {
            createEmptyFile(playerFile);
        }
        return playerFile;
    }

    private void createEmptyFile(File file) {
        YamlConfiguration empty = new YamlConfiguration();
        try {
            empty.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
