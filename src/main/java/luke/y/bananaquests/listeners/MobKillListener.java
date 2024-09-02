package luke.y.bananaquests.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobKillListener implements Listener {
    public void onMobKill(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null)
            return;

    }
}
