package luke.y.bananaquests.objective;

import luke.y.bananaquests.QuestObjective;

public class RightClickNPCObjective extends QuestObjective {

    private final int npcID;

    public int getNpcID() {
        return npcID;
    }

    public RightClickNPCObjective(String desc, int goal, int progress, int npcID) {
        super(desc, goal, progress);
        this.npcID = npcID;
    }
}
