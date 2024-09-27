package luke.y.bananaquests.objective;

import luke.y.bananaquests.QuestObjective;
import org.bukkit.entity.EntityType;

public class KillMobObjective extends QuestObjective {

    private final EntityType mob;

    public EntityType getMob() {
        return mob;
    }

    public KillMobObjective(String desc, int goal, int progress, EntityType mob) {
        super(desc, goal, progress);
        this.mob = mob;
    }
}
