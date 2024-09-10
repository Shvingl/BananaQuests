package luke.y.bananaquests.listeners;

import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import luke.y.bananaquests.BananaQuests;
import luke.y.bananaquests.events.QuestCompleteEvent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QuestCompleteListener implements Listener {
    private BananaQuests plugin;

    public QuestCompleteListener(BananaQuests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuestComplete(QuestCompleteEvent e) {
        Player player = e.getPlayer();
        YamlConfiguration questConfig = BananaQuests.questConfigs.get(e.getQuestID());
        player.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "QUEST SPLNĚN!", ChatColor.YELLOW + questConfig.getString("display"));
        RadioSongPlayer rsp = new RadioSongPlayer(plugin.getSong());
        rsp.addPlayer(player);
        rsp.setPlaying(true);
    }
}
