package luke.y.bananaquests.objective;

import org.bukkit.entity.EntityType;

import javax.swing.text.html.parser.Entity;

public class KillMobObjective extends QuestObjective{

    private final EntityType mob;

    public EntityType getMob() {
        return mob;
    }

    public KillMobObjective(String desc, int goal, int progress, EntityType mob) {
        super(desc, goal, progress);
        this.mob = mob;
    }
}
