package luke.y.bananaquests.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
                //Only starts quest if not started before
                sender.sendMessage("Startquest");
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Špatný příkaz.");
                return false;
        }
        return false;
    }
}
