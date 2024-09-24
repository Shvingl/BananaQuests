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
                            sendPlayerIsntOnline(sender);
                        }
                    }
                }
                else {
                    sendQuestDoesntExist(sender);
                }
                break;
            case "forcestartquest":
                break;
            case "finishquest":
                if (args.length == 2) {
                    if (sender instanceof Player player) {
                        String id = args[1];
                        if (!BananaQuests.validQuestIDs.contains(id)) {
                            sendQuestDoesntExist(sender);
                            break;
                        }
                        BananaQuests.finishQuest(player, id);
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Chybí jméno hráče.");
                    }
                }
                else if (args.length == 3) {
                    Player player = Bukkit.getPlayer(args[2]);
                    if (player == null) {
                        sendPlayerIsntOnline(sender);
                        break;
                    }
                    String id = args[1];
                    if (!BananaQuests.validQuestIDs.contains(id)) {
                        sendQuestDoesntExist(sender);
                        break;
                    }
                    BananaQuests.finishQuest(player, id);
                }
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
            case "finishstage": //Bananaquests finishobjective id stage player
                break;
            case "finishobjective": //Bananaquests finishobjective id stage obj player
                Player player;
                if (args.length == 5) {
                    player = Bukkit.getPlayer(args[4]);
                    if (player == null) {
                        sendPlayerIsntOnline(sender);
                        return false;
                    }
                }
                else if (args.length == 4) {
                    if (sender instanceof Player) {
                        player = (Player) sender;
                    }
                    else {
                        sendPlayerIsntOnline(sender);
                        return false;
                    }
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Nesprávný počet argumentů.");
                    return false;
                }
                ActiveQuest quest = BananaQuests.getQuestById(player, args[1]);
                if (quest == null) {
                    sendQuestDoesntExist(sender);
                    return false;
                }

                int stage;
                try {
                    stage = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.DARK_RED + args[2] + " není číslo.");
                    return false;
                }
                if (quest.getStage() != stage) {
                    sender.sendMessage(ChatColor.RED + "Neplatná stage.");
                    return false;
                }

                int objective;
                try {
                    objective = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.DARK_RED + args[3] + " není číslo.");
                    return false;
                }
                if (quest.getCurrentObjectives().size() < objective) {
                    sender.sendMessage(ChatColor.RED + "Neplatný objective.");
                    return false;
                }

                BananaQuests.finishObjective(quest, stage, objective);
                sender.sendMessage(ChatColor.GREEN + "Objective byl úspěšně dokončen.");

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

    private void sendPlayerIsntOnline(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Tento hráč není online!");
    }

    private void sendQuestDoesntExist(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Takový Quest neexistuje!");
    }
}
