package luke.y.bananaquests.objective;

public class QuestObjective {
    private int goal;
    private int progress;

    public void increaseProgress(int amount) {
        progress+=amount;
        if (progress >= goal) {

        }
    }
}
