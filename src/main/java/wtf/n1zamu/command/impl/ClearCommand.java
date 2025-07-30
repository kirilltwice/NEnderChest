package wtf.n1zamu.command.impl;

import org.bukkit.entity.Player;
import wtf.n1zamu.NEnderChest;
import wtf.n1zamu.command.ICommand;
import wtf.n1zamu.database.IDataBase;
import wtf.n1zamu.util.ConfigUtil;

import java.util.Arrays;

public class ClearCommand implements ICommand {
    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("nEnderChest.admin")) {
            player.sendMessage(ConfigUtil.getColoredString("withoutPermission"));
            return;
        }
        Arrays.asList(NEnderChest.getINSTANCE().getItemsDataBase(), NEnderChest.getINSTANCE().getPlayersDataBase())
                .forEach(IDataBase::clear);
        player.sendMessage(ConfigUtil.getColoredString("wipedSuccessfully"));
    }
}
