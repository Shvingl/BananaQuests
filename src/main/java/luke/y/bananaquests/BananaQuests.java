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
    private HashMap<String, YamlConfiguration> questConfigs;

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
                Bukkit.getLogger().info(questID);
                questConfigs.put(questID, config);
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning(Util.prefix + " Chybí quests složka!");
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
                if (isValidQuestID(questID)) {
                    ConfigurationSection questsSection = config.getConfigurationSection("active." + questID);
                    YamlConfiguration questConfig = questConfigs.get(questID);
                    int stage = questsSection.getInt("stage");
                    ArrayList<QuestObjective> objectivesToAdd = new ArrayList<>();

                    //questsToAdd.add(new ActiveQuest(questID, stage, ))
                }
                else {
                    Bukkit.getLogger().warning(Util.prefix + "Hráč " + player.getName() + " má v listu neplarný quest " + questID);
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().info(Util.prefix + " Hráč " + player.getName() + " nemá žádné aktivní questy.");
        }


        activeQuestsMap.put(player, questsToAdd);
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
