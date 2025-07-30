package wtf.n1zamu.command.impl;

import org.bukkit.entity.Player;
import wtf.n1zamu.NEnderChest;
import wtf.n1zamu.command.ICommand;
import wtf.n1zamu.util.ConfigUtil;

import javax.naming.NamingEnumeration;

public class ReloadCommand implements ICommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("nEnderChest.admin")) {
            player.sendMessage(ConfigUtil.getColoredString("withoutPermission"));
            return;
        }
        NEnderChest.getINSTANCE().reloadConfig();
        player.sendMessage(ConfigUtil.getColoredString("reloaded"));
    }
}
