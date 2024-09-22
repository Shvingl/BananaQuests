package luke.y.bananaquests.objective;

public class EnterRegionObjective extends QuestObjective{

    private final String regionID;

    public String getRegionID() {
        return regionID;
    }

    public EnterRegionObjective(String desc, int goal, int progress, String regionID) {
        super(desc, goal, progress);
        this.regionID = regionID;
    }
}
