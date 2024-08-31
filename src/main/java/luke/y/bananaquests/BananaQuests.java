package luke.y.bananaquests;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public final class BananaQuests extends JavaPlugin {

    ArrayList<String> validQuests;
    private Map<Player, ArrayList<ActiveQuest>> activeQuestsMap;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //Projit všechny quest .yml soubory a dát je do validQuests
        for (File file : new File(this.getDataFolder().getAbsolutePath() + File.separator + "quests").listFiles()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            Bukkit.getLogger().info(file.getName().replace(".yml", ""));
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayersQuests(player);
        }
    }

    public void savePlayersQuests(Player player) {

    }
}
