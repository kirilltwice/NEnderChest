package wtf.n1zamu.command.impl;

import org.bukkit.entity.Player;
import wtf.n1zamu.command.ICommand;
import wtf.n1zamu.holder.BuyHolder;
import wtf.n1zamu.holder.EnderChestHolder;
import wtf.n1zamu.util.ConfigUtil;

public class BuyCommand implements ICommand {
    @Override
    public String getName() {
        return "buy";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("nEnderChest.default")) {
            player.sendMessage(ConfigUtil.getColoredString("withoutPermission"));
            return;
        }
        player.openInventory(new BuyHolder(player).getInventory());
    }
}
