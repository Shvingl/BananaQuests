package luke.y.bananaquests;

import luke.y.bananaquests.objective.QuestObjective;

import java.util.ArrayList;

public class ActiveQuest {
    private int stage;
    private ArrayList<QuestObjective> currentObjectives;

    public void ActiveQuest(String id, int stage, ArrayList<QuestObjective> currentObjectives) {
        this.stage = stage;
        this.currentObjectives = currentObjectives;
    }
}
