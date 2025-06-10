package luke.y.bananaquests;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import luke.y.bananaquests.events.QuestCompleteEvent;
import luke.y.bananaquests.objective.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an ongoin/finished quest.
 * Must belong to a specific online player's quest list.
 */
public class ActiveQuest {
    private final String id;

    public String getId() {
        return id;
    }

    private final Player owner;
    private final int exp;

    public int getEXP() {
        return exp;
    }

    public int getMoney() {
        return money;
    }

    private final int money;

    public Player getOwner() {
        return owner;
    }

    private final String display;

    public String getDisplay() {
        return display;
    }

    private int stage;

    public int getStage() {
        return stage;
    }

    private final ArrayList<String> rewards = new ArrayList<>();

    public ArrayList<String> getRewards() {
        return rewards;
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

    public boolean isTracked() {
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

        this.exp = BananaQuests.questConfigs.get(id).getInt("exp");
        this.money = BananaQuests.questConfigs.get(id).getInt("money");
        if (BananaQuests.questConfigs.get(id).getList("rewards") != null) {
            List<?> rawRewards = BananaQuests.questConfigs.get(id).getList("rewards");
            if (rawRewards != null) {
                for (Object reward : rawRewards) {
                    if (reward instanceof String) {
                        this.rewards.add((String) reward);
                    }
                }
            }
        }


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
        int previousStage = stage;
        stage++;
        currentObjectives.clear();

        if (stage > BananaQuests.questConfigs.get(id).getConfigurationSection("stages").getKeys(false).size()) {
            finishQuest();
            return;
        }
        executeEndStageCommands(previousStage);

        initializeObjectives(new ArrayList<>());
    }

    private void executeEndStageCommands(int stage) {
        ArrayList<String> consoleCommands = new ArrayList<>();
        List<?> rawEndCommands = BananaQuests.questConfigs.get(id).getConfigurationSection("stages.stage-" + stage).getList("endConsoleCommands");
        if (rawEndCommands != null) {
            for (Object rawEndCommand : rawEndCommands) {
                if (rawEndCommand instanceof String) {
                    consoleCommands.add((String) rawEndCommand);
                }
            }
        }
        for (String command : consoleCommands) {
            command = command.replace("%player%", owner.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    public void finishQuest() {
        Bukkit.getServer().getPluginManager().callEvent(new QuestCompleteEvent(owner, id));
        owner.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Odměny za Quest:");
        owner.sendMessage(ChatColor.GRAY + "    - " + exp + " RPG EXP");
        owner.sendMessage(ChatColor.GRAY + "    - " + money + " BananaCoinů");
        for (String reward : rewards) {
            owner.sendMessage(ChatColor.GRAY + "    - " + reward);
        }
        Bukkit.dispatchCommand
                (Bukkit.getConsoleSender().getServer().getConsoleSender(), "mmocore admin exp give LukeWhy135 main " + exp);
        BananaQuests.econ.depositPlayer(owner, money);
        finished = true;
        if (isTracked()) {
            if (owner.isOp()) owner.sendMessage("DEBUG: Tento quest byl tracklý. Pokusím se tracknout nový quest.");
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
                if (objectiveType == null)
                    continue;
                int npcID;
                switch (objectiveType) {
                    case "KillMob":
                        EntityType mob = EntityType.valueOf(objectiveConfigSection.getString("mob"));
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
                    case "RightClickNPC":
                        npcID = objectiveConfigSection.getInt("npc");
                        currentObjectives.add(new RightClickNPCObjective(objectiveDescription, objectiveGoal, objectiveProgress, npcID));
                        break;
                    case "GiveMythicItemToNPC":
                        npcID = objectiveConfigSection.getInt("npc");
                        MythicItem item;
                        item = MythicBukkit.inst().getItemManager().getItem(objectiveConfigSection.getString("mythicItem")).orElse(null);
                        if (item != null)
                            currentObjectives.add(new GiveMythicItemToNPCObjective(objectiveDescription, objectiveGoal, objectiveProgress, npcID, item));
                        break;
                    case "Dummy":
                        currentObjectives.add(new QuestObjective(objectiveDescription, objectiveGoal, objectiveProgress));
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
