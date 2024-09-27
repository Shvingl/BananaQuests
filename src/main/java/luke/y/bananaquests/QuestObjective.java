package luke.y.bananaquests;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class QuestObjective {
    private ActiveQuest owner;
    private final int goal;
    private int progress;
    private final String description;

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

    public void setFinished(boolean finished) {
        this.finished = finished;
        if (owner != null && owner.getOwner() != null) {
            if (owner.isTracked()) {
                owner.getOwner().sendMessage(ChatColor.DARK_GREEN + description + " ✔");
            }
            else {
                owner.getOwner().sendMessage(ChatColor.DARK_GREEN + owner.getDisplay() + ": " + description + " ✔");
            }
            owner.getOwner().playSound(owner.getOwner(), Sound.ITEM_BUNDLE_INSERT, 1, 1);
        }
    }

    private boolean finished;

    public QuestObjective(String desc, int goal, int progress) {
        this.description = desc;
        this.goal = goal;
        this.progress = progress;

        if (progress >= goal) {
            setFinished(true);
        }
    }

    public void increaseProgress(int amount, Player player) {
        if (player != owner.getOwner()) {
            player.sendMessage(ChatColor.DARK_RED + "Fatální chyba: Tvá akce se snaží splnit quest jiného hráče. Kontaktuj admina se screenshotem.");
            return;
        }
        progress+=amount;
        if (progress >= goal) {
            setFinished(true);
            owner.tryMoveToNextStage();
        }
    }

    public void setOwner(ActiveQuest owner) {
        this.owner = owner;
    }
}
