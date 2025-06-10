package luke.y.bananaquests.objective;

import io.lumine.mythic.api.mobs.MythicMob;
import luke.y.bananaquests.QuestObjective;
import org.bukkit.Bukkit;

public class KillMythicMobObjective extends QuestObjective {

    private final MythicMob mythicMob;

    public MythicMob getMythicMob() {
        return mythicMob;
    }

    public KillMythicMobObjective(String desc, int goal, int progress, MythicMob mythicMob) {
        super(desc, goal, progress);
        this.mythicMob = mythicMob;
    }
}
