package luke.y.bananaquests.listeners;

import luke.y.bananaquests.BananaQuests;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    BananaQuests plugin;

    public PlayerLeaveListener(BananaQuests plugin) {
        this.plugin = plugin;
    }

    public void onPlayerLeave(PlayerQuitEvent e) {
        plugin.savePlayersQuests(e.getPlayer());
    }
}
