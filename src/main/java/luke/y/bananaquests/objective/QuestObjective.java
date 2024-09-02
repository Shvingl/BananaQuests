package luke.y.bananaquests.objective;

import luke.y.bananaquests.ActiveQuest;
import org.bukkit.entity.Player;

public class QuestObjective {
    private ActiveQuest owner;
    private final int goal;
    private int progress;

    public int getGoal() {
        return goal;
    }

    public int getProgress() {
        return progress;
    }

    public boolean isFinished() {
        return finished;
    }

    private boolean finished;

    public QuestObjective(int goal, int progress) {
        this.goal = goal;
        this.progress = progress;
    }

    public void increaseProgress(int amount, Player player) {
        progress+=amount;
        player.sendMessage(progress + "/" + getGoal());
        if (progress >= goal) {
            finished = true;
            owner.tryMoveToNextStage();
        }
    }

    public void setOwner(ActiveQuest owner) {
        this.owner = owner;
    }
}
