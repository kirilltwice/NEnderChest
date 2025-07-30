package wtf.n1zamu.command.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import wtf.n1zamu.NEnderChest;
import wtf.n1zamu.command.ICommand;
import wtf.n1zamu.holder.EnderChestHolder;
import wtf.n1zamu.util.ConfigUtil;

import java.util.HashMap;
import java.util.Map;

public class CheckCommand implements ICommand {
    public static Map<Player, String> viewers = new HashMap<>();

    @Override
    public String getName() {
        return "check";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("nEnderChest.admin")) {
            player.sendMessage(ConfigUtil.getColoredString("withoutPermission"));
            return;
        }
        if (args.length != 2) {
            player.sendMessage(ConfigUtil.getColoredString("checkUsage"));
            return;
        }

        String targetName = args[1];
        if (!NEnderChest.getINSTANCE().getItemsDataBase().hasEnderChest(targetName)) {
            player.sendMessage(ConfigUtil.getColoredString("withoutEnderChest"));
            return;
        }

        player.openInventory(new EnderChestHolder(targetName).getInventory());
        viewers.put(player, targetName);

    }
}
