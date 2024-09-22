package luke.y.bananaquests.commands;

import luke.y.bananaquests.BananaQuests;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuestadminTabCompletion implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args.length) {
            case 1 -> {
                return new ArrayList<>(Arrays.asList("startquest", "forgetquest", "forcestartquest", "forgetall"));
            }
            case 2 -> {
                if (args[0].equalsIgnoreCase("startquest")) {
                    return new ArrayList<>(Arrays.asList(BananaQuests.validQuestIDs.toArray(new String[0])));
                }
            }
            case 3 -> {
                if (args[0].equalsIgnoreCase("startquest")) {
                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
                }
            }
        }
        return  null;
    }
}
