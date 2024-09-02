package luke.y.bananaquests.objective;

import org.bukkit.entity.EntityType;

import javax.swing.text.html.parser.Entity;

public class KillMobObjective extends QuestObjective{

    private EntityType mob;

    public EntityType getMob() {
        return mob;
    }

    public KillMobObjective(int goal, int progress, EntityType mob) {
        super(goal, progress);
        this.mob = mob;
    }
}
