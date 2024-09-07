package luke.y.bananaquests.objective;

import org.bukkit.entity.EntityType;

public class FreeOutpostObjective extends QuestObjective{

    private final String outpostID;

    public String getOutpostID() {
        return outpostID;
    }

    public FreeOutpostObjective(String desc, int goal, int progress, String outpostID) {
        super(desc, goal, progress);
        this.outpostID = outpostID;
    }
}
