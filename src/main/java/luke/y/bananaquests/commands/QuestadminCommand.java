package luke.y.bananaquests.commands;

import luke.y.bananaquests.ActiveQuest;
import luke.y.bananaquests.BananaQuests;
import luke.y.bananaquests.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestadminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Nedostatek argumentů.");
            return false;
        }
        switch (args[0]) {
            case "forgetquest":
                if (BananaQuests.validQuestIDs.contains(args[1])) {
                    String questID = args[1];
                    if (args.length == 2 && sender instanceof Player player) {
                        forgetQuestCommand(sender, player, questID);
                    }
                    else if (args.length == 3) {
                        Player player = Bukkit.getPlayer(args[2]);
                        if (player != null) {
                            forgetQuestCommand(sender, player, questID);
                        }
                        else {
                            sender.sendMessage(ChatColor.RED + "Tento hráč není online.");
                        }
                    }
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Takový Quest neexistuje.");
                }
                break;
            case "forcestartquest":
                break;
            case "forgetall":
                if (args.length == 2) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player != null) {
                        player.sendMessage(ChatColor.RED + "Všechny tvé questy byly zapomenuty.");
                        BananaQuests.forgetAllQuests(player);
                    }
                }
                break;
            case "startquest":
                //Only starts quest if not started before
                if (BananaQuests.validQuestIDs.contains(args[1])) {
                    if (args.length == 2) {
                        if (sender instanceof Player player) {
                            BananaQuests.beginQuest(args[1], player);
                        }
                    }
                    else if (args.length == 3) {
                        Player player = Bukkit.getPlayer(args[2]);
                        BananaQuests.beginQuest(args[1], player);
                    }
                }
                else
                    sender.sendMessage("BANANAQUESTS Error starting quest");
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Špatný příkaz.");
                return false;
        }
        return false;
    }

    private static void forgetQuestCommand(CommandSender sender, Player player, String questID) {
        ActiveQuest toForget = null;
        for (ActiveQuest activeQuest : BananaQuests.activeQuestsMap.get(player)) {
            if (activeQuest.getId().equalsIgnoreCase(questID)) {
                toForget = activeQuest;
                break;
            }
        }
        if (toForget != null) {
            BananaQuests.forgetQuest(player, toForget);
        }
        else {
            sender.sendMessage(ChatColor.RED + "Hráč takový quest nezačal");
        }
    }
}
