package luke.y.bananaquests.objective;

import luke.y.bananaquests.ActiveQuest;
import luke.y.bananaquests.BananaQuests;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class QuestObjective {
    private ActiveQuest owner;
    private final int goal;
    private int progress;
    private String description;

    public String getDescription() {
        return description;
    }

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

    public QuestObjective(String desc, int goal, int progress) {
        this.description = desc;
        this.goal = goal;
        this.progress = progress;

        if (progress >= goal) {
            finished = true;
        }
    }

    public void increaseProgress(int amount, Player player) {
        progress+=amount;
        player.sendMessage(ChatColor.DARK_GREEN + description + " " + progress + "/" + getGoal());
        player.playSound(player, Sound.ITEM_BUNDLE_INSERT, 1, 1);
        if (progress >= goal) {
            finished = true;
            owner.tryMoveToNextStage();
        }
    }

    public void setOwner(ActiveQuest owner) {
        this.owner = owner;
    }
}
