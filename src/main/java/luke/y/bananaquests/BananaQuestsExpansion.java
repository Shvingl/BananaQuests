package luke.y.bananaquests;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BananaQuestsExpansion extends PlaceholderExpansion {
    private final BananaQuests plugin;

    public BananaQuestsExpansion(BananaQuests plugin) {
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public String getAuthor() {
        return "Author"; //
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return "bananaquests"; //
    }

    @NotNull
    @Override
    public String getVersion() {
        return "1.0.0"; //
    }

    @Override
    public boolean persist() {
        return true; //
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("questcount")) {
            if (player.isOnline()) {
                Player onlinePlayer = (Player) player;
                return String.valueOf(BananaQuests.getOngoingQuests(onlinePlayer).size());
            }
        }
        if (params.equalsIgnoreCase("trackedname")) {
            if (player.isOnline()) {
                Player onlinePlayer = (Player) player;
                if (BananaQuests.trackedQuestMap.get(onlinePlayer) == null) {
                    return "Žádný Quest";
                }
                return String.valueOf(BananaQuests.trackedQuestMap.get(onlinePlayer).getDisplay());
            }
        }
        if (params.equalsIgnoreCase("trackedobjective")) {
            if (player.isOnline()) {
                Player onlinePlayer = (Player) player;
                return String.valueOf(BananaQuests.trackedQuestMap.get(onlinePlayer).getFirstUnfinishedObjective().getDescription());
            }
        }
        if (params.equalsIgnoreCase("trackedprogress")) {
            if (player.isOnline()) {
                Player onlinePlayer = (Player) player;
                return String.valueOf(BananaQuests.trackedQuestMap.get(onlinePlayer).getFirstUnfinishedObjective().getProgress());
            }
        }
        if (params.equalsIgnoreCase("trackedgoal")) {
            if (player.isOnline()) {
                Player onlinePlayer = (Player) player;
                return String.valueOf(BananaQuests.trackedQuestMap.get(onlinePlayer).getFirstUnfinishedObjective().getGoal());
            }
        }
        if (params.equalsIgnoreCase("hastracked")) {
            if (player.isOnline()) {
                Player onlinePlayer = (Player) player;
                return String.valueOf(BananaQuests.trackedQuestMap.containsKey(onlinePlayer) && BananaQuests.trackedQuestMap.get(onlinePlayer) != null);
            }
        }
        if (params.contains("has_started_")) {
            String questID = params.replace("has_started_", "").toLowerCase();
            if (!(BananaQuests.validQuestIDs.contains(questID)))
                return null;
            if (player.isOnline()) {
                Player onlinePlayer = (Player) player;
                for (ActiveQuest activeQuest : BananaQuests.activeQuestsMap.get(onlinePlayer)) {
                    if (activeQuest.getId().equalsIgnoreCase(questID)) {
                        return "true";
                    }
                }
                return "false";
            }
        }
        if (params.contains("has_finished_")) {
            String questID = params.replace("has_finished_", "").toLowerCase();
            if (!(BananaQuests.validQuestIDs.contains(questID)))
                return null;
            if (player.isOnline()) {
                Player onlinePlayer = (Player) player;
                for (ActiveQuest activeQuest : BananaQuests.activeQuestsMap.get(onlinePlayer)) {
                    if (activeQuest.getId().equalsIgnoreCase(questID) && activeQuest.isFinished()) {
                        return "true";
                    }
                }
                return "false";
            }
        }

        return null; //
    }
}
