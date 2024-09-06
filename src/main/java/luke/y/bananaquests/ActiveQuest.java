package luke.y.bananaquests;

import luke.y.bananaquests.objective.KillMobObjective;
import luke.y.bananaquests.objective.QuestObjective;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * This class represents an ongoin/finished quest.
 * Must belong to a specific online player's quest list.
 */
public class ActiveQuest {
    private String id;

    public String getId() {
        return id;
    }

    private Player owner;

    public Player getOwner() {
        return owner;
    }

    private String display;

    public String getDisplay() {
        return display;
    }

    private int stage;

    public int getStage() {
        return stage;
    }

    private final ArrayList<QuestObjective> currentObjectives = new ArrayList<>();

    public ArrayList<QuestObjective> getCurrentObjectives() {
        return currentObjectives;
    }

    private boolean finished;

    public boolean isFinished() {
        return finished;
    }

    public ActiveQuest(String id, Player owner, int stage, ArrayList<Integer> progress, boolean finished) {
        this.id = id;
        this.stage = stage;
        this.owner = owner;
        this.finished = finished;

        this.display = BananaQuests.questConfigs.get(id).getString("display");

        initializeObjectives(progress);

        for (QuestObjective questObjective : currentObjectives) {
            questObjective.setOwner(this);
        }
    }

    /**
     * This method checks if all objectives are finished and if so
     * increases the stage. Should be called each time an Active Quests'
     * objective's progress increases.
     */
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
        owner.sendMessage("Moving to next stage...");
        stage++;
        currentObjectives.clear();
        if (stage > BananaQuests.questConfigs.get(id).getConfigurationSection("stages").getKeys(false).size()) {
            finishQuest();
            return;
        }

        initializeObjectives(new ArrayList<>());
    }

    private void finishQuest() {
        finished = true;
        owner.sendMessage("");
        owner.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "QUEST SPLNĚN!");
        owner.sendMessage(ChatColor.GREEN + display);
        owner.sendMessage("");
        owner.playSound(owner.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
    }

    public void initializeObjectives(ArrayList<Integer> progress) {
        if (!finished) {
            int progressLength = progress.size();
            YamlConfiguration questConfig = BananaQuests.questConfigs.get(id);
            for (int i = 0; i < questConfig.getConfigurationSection("stages.stage-" + stage + ".objectives").getKeys(false).size(); i++) {
                int objectiveID = i + 1;
                ConfigurationSection objectiveConfigSection = questConfig.getConfigurationSection("stages.stage-" + stage + ".objectives.objective-" + objectiveID);
                int objectiveProgress;

                if (progressLength <= i) {
                    objectiveProgress = 0;
                } else {
                    objectiveProgress = progress.get(i);
                }

                //Values from quest's .yml
                int objectiveGoal = objectiveConfigSection.getInt("goal");
                String objectiveDescription = objectiveConfigSection.getString("description");
                String objectiveType = objectiveConfigSection.getString("type");

                if (objectiveProgress > objectiveGoal) {
                    Bukkit.getLogger().warning(owner.getName() + " má neplatný progress stage " + id + ":" + objectiveID);
                    continue;
                }

                switch (objectiveType) {
                    case "KillMob":
                        EntityType mob = EntityType.fromName(objectiveConfigSection.getString("mob"));
                        currentObjectives.add(new KillMobObjective(objectiveDescription, objectiveGoal, objectiveProgress, mob));
                        break;
                    case "BlockBreak":
                        break;
                    default:
                        Bukkit.getLogger().warning("Stage " + id + ":" + objectiveID + " hráče" + owner.getName() + "má neplatný typ.");
                        continue;
                }
            }
            for (QuestObjective questObjective : currentObjectives) {
                questObjective.setOwner(this);
            }
        }
    }
}
