package wtf.n1zamu.command.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import wtf.n1zamu.command.ICommand;
import wtf.n1zamu.holder.EnderChestHolder;
import wtf.n1zamu.util.ConfigUtil;

public class OpenCommand implements ICommand {
    @Override
    public String getName() {
        return "open";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("nEnderChest.default")) {
            player.sendMessage(ConfigUtil.getColoredString("withoutPermission"));
            return;
        }
        if (player.getOpenInventory().getTopInventory().getHolder() instanceof EnderChestHolder) {
            return;
        }
        new EnderChestHolder(player.getName());
    }
}
