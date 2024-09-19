package luke.y.bananaquests.objective;

public class FinishShootingGameObjective extends QuestObjective{

    private final String gameID;

    public String getGameID() {
        return gameID;
    }

    public FinishShootingGameObjective(String desc, int goal, int progress, String gameID) {
        super(desc, goal, progress);
        this.gameID = gameID;
    }
}

