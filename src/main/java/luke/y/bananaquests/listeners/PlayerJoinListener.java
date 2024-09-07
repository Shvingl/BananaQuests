package luke.y.bananaquests.listeners;

import luke.y.bananaquests.BananaQuests;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

public class PlayerJoinListener implements Listener {
    private BananaQuests plugin;

    public PlayerJoinListener(BananaQuests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        plugin.loadPlayersQuests(e.getPlayer());
        e.getPlayer().sendMessage("Valid quest configs: " + BananaQuests.questConfigs);
    }
}
