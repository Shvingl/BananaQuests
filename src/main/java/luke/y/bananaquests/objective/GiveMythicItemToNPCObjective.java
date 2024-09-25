package luke.y.bananaquests.objective;

import io.lumine.mythic.core.items.MythicItem;

public class GiveMythicItemToNPCObjective extends QuestObjective{

    private final int npcID;

    public int getNpcID() {
        return npcID;
    }

    private final MythicItem mythicItem;

    public MythicItem getMythicItem() {
        return mythicItem;
    }

    public GiveMythicItemToNPCObjective(String desc, int goal, int progress, int npcID, MythicItem mythicItem) {
        super(desc, goal, progress);
        this.npcID = npcID;
        this.mythicItem = mythicItem;
    }
}
