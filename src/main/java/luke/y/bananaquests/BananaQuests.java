package luke.y.bananaquests;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Map;

public final class BananaQuests extends JavaPlugin {

    private Map<Player, ArrayList<ActiveQuest>> activeQuestsMap;

    @Override
    public void onEnable() {
        // Plugin startup logic

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
