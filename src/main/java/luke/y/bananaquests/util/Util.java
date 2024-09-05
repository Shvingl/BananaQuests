package luke.y.bananaquests.util;

import org.bukkit.entity.Player;


public class Util {
    public static String prefix = "[BANANAQUESTS]";

    /**
     * Creates an instance of the ActiveQuest class and
     * puts it in the map of player's active quests.
     * @param id - quest id
     * @param player - player
     */

    public static void startQuest(String id, Player player) {
        createQuest(id, 0, player);
    }

    public static void createQuest(String id, int stage, Player player) {

        //new ActiveQuest(id, 0, )
    }


}
