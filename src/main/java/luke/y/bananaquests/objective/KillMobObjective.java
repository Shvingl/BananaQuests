package luke.y.bananaquests.objective;

import javax.swing.text.html.parser.Entity;

public class KillMobObjective extends QuestObjective{

    private Entity mob;

    public KillMobObjective(int goal, int progress, Entity mob) {
        super(goal, progress);
        this.mob = mob;
    }
}
