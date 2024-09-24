package luke.y.bananaquests;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.MobType;
import luke.y.bananaquests.events.QuestCompleteEvent;
import luke.y.bananaquests.objective.*;
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
    private int exp;

    public int getEXP() {
        return exp;
    }

    public int getMoney() {
        return money;
    }

    private int money;

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

    public QuestObjective getFirstUnfinishedObjective() {
        for (QuestObjective objective : currentObjectives) {
            if (!objective.isFinished()) {
                return objective;
            }
        }
        return null;
    }

    private boolean isTracked() {
        return (BananaQuests.trackedQuestMap.get(owner) == this);
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

        this.exp = 0;
        this.money = 0;

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
        stage++;
        currentObjectives.clear();
        if (stage > BananaQuests.questConfigs.get(id).getConfigurationSection("stages").getKeys(false).size()) {
            finishQuest();
            return;
        }

        initializeObjectives(new ArrayList<>());
    }

    public void finishQuest() {
        Bukkit.getServer().getPluginManager().callEvent(new QuestCompleteEvent(owner, id));
        finished = true;
        if (isTracked()) {
            owner.sendMessage("DEBUG: Tento quest byl tracklý. Pokusím se tracknout nový quest.");
            BananaQuests.trackNewQuest(owner);
        }
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
                int objectiveGoal = objectiveConfigSection != null ? objectiveConfigSection.getInt("goal") : 0;
                String objectiveDescription = objectiveConfigSection != null ? objectiveConfigSection.getString("description") : "MISSING DESC";
                String objectiveType = objectiveConfigSection != null ? objectiveConfigSection.getString("type") : null;

                if (objectiveProgress > objectiveGoal) {
                    Bukkit.getLogger().warning(owner.getName() + " má neplatný progress stage " + id + ":" + objectiveID);
                    continue;
                }

                switch (objectiveType) {
                    case "KillMob":
                        EntityType mob = EntityType.fromName(objectiveConfigSection.getString("mob"));
                        currentObjectives.add(new KillMobObjective(objectiveDescription, objectiveGoal, objectiveProgress, mob));
                        break;
                    case "KillMythicMob":
                        MythicMob mythicMob = MythicBukkit.inst().getMobManager().getMythicMob(objectiveConfigSection.getString("mythicMob")).orElse(null);
                        currentObjectives.add(new KillMythicMobObjective(objectiveDescription, objectiveGoal, objectiveProgress, mythicMob));
                        break;
                    case "FreeOutpost":
                        String outpostID = objectiveConfigSection.getString("outpost");
                        currentObjectives.add(new FreeOutpostObjective(objectiveDescription, objectiveGoal, objectiveProgress, outpostID));
                        break;
                    case "FinishShootingGame":
                        //String gameID = objectiveConfigSection.getString("outpost");
                        //Dodělat
                        currentObjectives.add(new FinishShootingGameObjective(objectiveDescription, objectiveGoal, objectiveProgress, null));
                        break;
                    case "EnterRegion":
                        String regionID = objectiveConfigSection.getString("region");
                        currentObjectives.add(new EnterRegionObjective(objectiveDescription, objectiveGoal, objectiveProgress, regionID));
                        break;
                    case "BlockBreak":
                        break;
                    default:
                        Bukkit.getLogger().warning("Stage " + id + ":" + objectiveID + " hráče" + owner.getName() + "má neplatný typ.");
                        break;
                }
            }
            for (QuestObjective questObjective : currentObjectives) {
                questObjective.setOwner(this);
            }
        }
    }
}
