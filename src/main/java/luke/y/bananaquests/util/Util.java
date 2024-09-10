package luke.y.bananaquests.util;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import luke.y.bananaquests.BananaQuests;
import org.bukkit.entity.Player;

import java.io.File;


public class Util {

    private static BananaQuests plugin;

    public Util(BananaQuests plugin) {
        Util.plugin = plugin;
    }

    public static String prefix = "[BANANAQUESTS]";

    private static final Song song = NBSDecoder.parse(new File(plugin.getDataFolder() + "/jingle.nbs"));

}
