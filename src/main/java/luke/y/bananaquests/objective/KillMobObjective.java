package luke.y.bananaquests.objective;

import javax.swing.text.html.parser.Entity;

public class KillMobObjective extends QuestObjective{

    private Entity mob;

    public Entity getMob() {
        return mob;
    }

    public KillMobObjective(int goal, int progress, Entity mob) {
        super(goal, progress);
        this.mob = mob;
    }
}
