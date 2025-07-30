package wtf.n1zamu.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.n1zamu.command.impl.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Command implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        if (args.length == 0) {
            return false;
        }
        Arrays.asList(new GiveCommand(), new OpenCommand(), new CheckCommand(), new ClearCommand(), new ReloadCommand(), new BuyCommand(), new WipeCommand()).forEach(iCommand -> {
            if (args[0].equalsIgnoreCase(iCommand.getName())) iCommand.execute(p, args);
        });
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> commands = new ArrayList<>();
            Arrays.asList(new OpenCommand(), new GiveCommand(), new ClearCommand(), new ReloadCommand(), new CheckCommand(), new BuyCommand(), new WipeCommand()).forEach(cmd -> commands.add(cmd.getName()));
            return commands;
        }
        if (args.length == 2) {
            if (Arrays.asList("give", "check").contains(args[1])) {
                List<String> playerNames = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach(player -> playerNames.add(player.getName()));
                return playerNames;
            }
            if (Arrays.asList("open", "wipe", "buy").contains(args[1])) {
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }
}
