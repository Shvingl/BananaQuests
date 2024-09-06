package luke.y.bananaquests;

import luke.y.bananaquests.commands.QuestadminCommand;
import luke.y.bananaquests.commands.QuestsCommand;
import luke.y.bananaquests.listeners.InventoryClickListener;
import luke.y.bananaquests.listeners.objectivelisteners.MobKillListener;
import luke.y.bananaquests.listeners.PlayerJoinListener;
import luke.y.bananaquests.listeners.PlayerLeaveListener;
import luke.y.bananaquests.listeners.objectivelisteners.MythicMobKillListener;
import luke.y.bananaquests.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    public static final HashMap<String, YamlConfiguration> questConfigs = new HashMap<>();

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);

        getServer().getPluginManager().registerEvents(new MobKillListener(), this);
        getServer().getPluginManager().registerEvents(new MythicMobKillListener(), this);

        Objects.requireNonNull(getCommand("quests")).setExecutor(new QuestsCommand());
        Objects.requireNonNull(getCommand("questadmin")).setExecutor(new QuestadminCommand());


        //Projit všechny quest .yml soubory a dát je do validQuestIDs
        for (File file : new File(this.getDataFolder().getAbsolutePath() + File.separator + "quests").listFiles()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String questID = file.getName().replace(".yml", "");
            questConfigs.put(questID, config);
        }
        validQuestIDs = questConfigs.keySet();

        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayersQuests(player);
        }
    }

    public boolean isInvalidQuestID(String id) {
        return !validQuestIDs.contains(id);
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
        //Tohle ještě bude sranda...
    }

    /**
     * Method to be called on each player join.
     * Loads player's active quests from the .yml file to the plugin's
     * player quest map.
     */
    public void loadPlayersQuests(Player player) {

        File playerFile = new File(this.getDataFolder().getAbsoluteFile() + File.separator + "playerdata" + File.separator + player.getName() + ".yml");
        if (!playerFile.exists()) {
            createEmptyFile(playerFile);
        }

        ArrayList<ActiveQuest> questsToAdd = new ArrayList<>();
        YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        ArrayList<String> finishedQuests = (ArrayList<String>) playerConfig.getList("finished");

        if (finishedQuests == null || finishedQuests.isEmpty()) {
            Bukkit.getLogger().info("Hráč " + player.getName() + " nemá žádné hotové questy.");
        }
        else {
            for (String questID : finishedQuests) {
                if (isInvalidQuestID(questID)) {
                    Bukkit.getLogger().warning(Util.prefix + " Hráč " + player.getName() + " má v listu neplatný hotový quest " + questID);
                    continue;
                }

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
                if (isInvalidQuestID(questID)) {
                    Bukkit.getLogger().warning(Util.prefix + " Hráč " + player.getName() + " má v listu neplatný quest " + questID);
                    continue;
                }

                ConfigurationSection activeQuestSection = playerConfig.getConfigurationSection("active." + questID);

                if (activeQuestSection == null)
                    continue;

                int stage = activeQuestSection.getInt("stage");

                //Check if stage is null here

                YamlConfiguration questConfig = questConfigs.get(questID);

                if (stage > questConfig.getConfigurationSection("stages").getKeys(false).size()) {
                    Bukkit.getLogger().warning(player.getName() + " má neplatný stage questu " + questID);
                    continue;
                }


                ArrayList<Integer> objectivesProgress = new ArrayList<>();

                for (String objectiveID : activeQuestSection.getConfigurationSection(".objectives-progress").getKeys(false)) {
                    int objectiveProgress = activeQuestSection.getInt(".objectives-progress." + objectiveID);
                    objectivesProgress.add(objectiveProgress);
                }
                questsToAdd.add(new ActiveQuest(questID, player, stage, objectivesProgress, false));
            }
        }

        activeQuestsMap.put(player, questsToAdd);

        //Test purposes
        Bukkit.getLogger().info(player.getName() + " questy: ");
        for (ActiveQuest activeQuest : activeQuestsMap.get(player)) {
            Bukkit.getLogger().info(activeQuest.getId() + " finished: " + activeQuest.isFinished());
        }
    }

    public static void forceBeginQuest(String id, Player player) {
        if (!validQuestIDs.contains(id)) {
            player.sendMessage(ChatColor.RED + "Plugin se ti pokusil odstartovat neexistujicí quest. Napiš to adminovi s aktuálním časem.");
            return;
        }

    }

    public static void beginQuest(String id, Player player) {
        if (!validQuestIDs.contains(id)) {
            player.sendMessage(ChatColor.RED + "Plugin se ti pokusil odstartovat neexistujicí quest. Napiš to adminovi s aktuálním časem.");
            return;
        }
        ArrayList<ActiveQuest> questList = activeQuestsMap.get(player);
        for (ActiveQuest activeQuest : questList) {
            if (activeQuest.getId().equalsIgnoreCase(id)) {
                player.sendMessage(ChatColor.RED + "Tento quest už je aktivní.");
                return;
            }
        }

        ActiveQuest newQuest = new ActiveQuest(id, player, 1, new ArrayList<>(), false);
        questList.add(newQuest);
        player.sendMessage(ChatColor.GREEN + "Začal jsi quest " + newQuest.getDisplay());
        activeQuestsMap.put(player, questList);
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
