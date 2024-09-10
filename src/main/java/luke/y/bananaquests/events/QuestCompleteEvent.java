package luke.y.bananaquests.events;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class QuestCompleteEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private String questID;

    public Player getPlayer() {
        return player;
    }

    public String getQuestID() {
        return questID;
    }

    public QuestCompleteEvent(Player player, String questID) {
        this.questID = questID;
        this.player = player;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
