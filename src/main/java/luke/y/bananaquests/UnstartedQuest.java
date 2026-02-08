package luke.y.bananaquests;

import org.bukkit.entity.Player;

public class UnstartedQuest {
    private final String id;

    public String getId() {
        return id;
    }

    private final String display;

    public String getDisplay() {
        return display;
    }

    private final String hint;

    public String getHint() {
        return hint;
    }

    private int startNPCID = -1;

    public int getStartNPCID() {
        return startNPCID;
    }

    private final int level;

    public int getLevel() {
        return level;
    }

    public UnstartedQuest(String id, String display, String hint, int startNPCID, int level) {
        this.id = id;
        this.display = display;
        this.hint = hint;
        this.startNPCID = startNPCID;
        this.level = level;
    }
}
