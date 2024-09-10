package luke.y.bananaquests.commands;

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
                //Removes quest from Map
                sender.sendMessage("Forgetquest command");
                break;
            case "forcestartquest":
                //Forgets the quest, then starts it again
                sender.sendMessage("Forcestartquest");
                break;
            case "startquest":
                sender.sendMessage("Startquest");
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
}
