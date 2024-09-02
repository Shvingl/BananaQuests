package luke.y.bananaquests;

import luke.y.bananaquests.objective.QuestObjective;

import java.util.ArrayList;

/**
 * This class represents an ongoin/finished quest.
 * Must belong to a specific online player's quest list.
 */
public class ActiveQuest {
    private int stage;
    private ArrayList<QuestObjective> currentObjectives;

    public ArrayList<QuestObjective> getCurrentObjectives() {
        return currentObjectives;
    }

    private boolean finished;

    public boolean isFinished() {
        return finished;
    }

    public ActiveQuest(String id, int stage, ArrayList<QuestObjective> currentObjectives) {
        this.stage = stage;
        this.currentObjectives = currentObjectives;
    }
    public void tryMoveToNextStage() {
        if (allObjectivesFinished())
            moveToNextStage();
    }

    private boolean allObjectivesFinished() {
        int finishedObjectives = 0;
        for (QuestObjective objective : currentObjectives) {
            if (objective.isFinished())
                finishedObjectives++;
        }
        return finishedObjectives >= currentObjectives.size();
    }

    private void moveToNextStage() {
        stage++;
        currentObjectives.clear();
        //currentObjectives.add();
    }
}
