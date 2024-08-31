package luke.y.bananaquests.objective;

public class QuestObjective {
    private int goal;
    private int progress;

    public boolean isFinished() {
        return finished;
    }

    private boolean finished;

    public QuestObjective(int goal, int progress) {
        this.goal = goal;
        this.progress = progress;
    }

    public void increaseProgress(int amount) {
        progress+=amount;
        if (progress >= goal) {

        }
    }
}
