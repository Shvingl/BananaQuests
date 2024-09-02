package luke.y.bananaquests;

import luke.y.bananaquests.listeners.PlayerJoinListener;
import luke.y.bananaquests.listeners.PlayerLeaveListener;
import luke.y.bananaquests.objective.QuestObjective;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class BananaQuests extends JavaPlugin {

    private Set<String> validQuestIDs = new HashSet<>();
    private static final HashMap<Player, ArrayList<ActiveQuest>> activeQuestsMap = new HashMap<>();
    private final HashMap<String, YamlConfiguration> questConfigs = new HashMap<>();

    public static HashMap<Player, ArrayList<ActiveQuest>> getActiveQuestsMap() {
        return activeQuestsMap;
    }

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(this), this);

        // Plugin startup logic
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayersQuests(player);
        }
        try {
            //Projit všechny quest .yml soubory a dát je do validQuestIDs
            for (File file : new File(this.getDataFolder().getAbsolutePath() + File.separator + "quests").listFiles()) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                String questID = file.getName().replace(".yml", "");
                questConfigs.put(questID, config);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (questConfigs != null)
            validQuestIDs = questConfigs.keySet();

    }

    public boolean isValidQuestID(String id) {
        return validQuestIDs.contains(id);
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
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        try {
            for (String questID : config.getConfigurationSection("active").getKeys(false)) {
                if (!isValidQuestID(questID)) {
                    Bukkit.getLogger().warning(Util.prefix + " Hráč " + player.getName() + " má v listu neplatný quest " + questID);
                    continue;
                }

                ConfigurationSection activeQuestSection = config.getConfigurationSection("active." + questID);

                int stage = activeQuestSection.getInt("stage");
                YamlConfiguration questConfig = questConfigs.get(questID);

                if (stage > questConfig.getConfigurationSection("stages").getKeys(false).size()) {
                    Bukkit.getLogger().warning(player.getName() + " má neplatný stage questu " + questID);
                    continue;
                }



                ArrayList<QuestObjective> objectivesToAdd = new ArrayList<>();

                for (String objectiveID : activeQuestSection.getConfigurationSection(".objectives-progress").getKeys(false)) {
                    ConfigurationSection objectiveConfigSection = questConfig.getConfigurationSection("stages.stage-" + stage + ".objectives.objective-" + objectiveID);
                    int objectiveProgress = activeQuestSection.getInt(".objectives-progress." + objectiveID);
                    int objectiveGoal = objectiveConfigSection.getInt("goal");

                    if (objectiveProgress > objectiveGoal) {
                        Bukkit.getLogger().warning(player.getName() + " má neplatný progress questu " + questID);
                    }
                    objectivesToAdd.add(new QuestObjective(objectiveGoal, objectiveProgress));


                }
                questsToAdd.add(new ActiveQuest(questID, stage, objectivesToAdd));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        activeQuestsMap.put(player, questsToAdd);

        Bukkit.getLogger().info(player.getName() + " questy: " + activeQuestsMap.get(player).toString());
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
