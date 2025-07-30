package wtf.n1zamu.command.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import wtf.n1zamu.NEnderChest;
import wtf.n1zamu.command.ICommand;
import wtf.n1zamu.time.Time;
import wtf.n1zamu.util.ConfigUtil;

public class GiveCommand implements ICommand {
    @Override
    public String getName() {
        return "give";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("nEnderChest.admin")) {
            player.sendMessage(ConfigUtil.getColoredString("withoutPermission"));
            return;
        }
        if (args.length != 3) {
            player.sendMessage(ConfigUtil.getColoredString("giveUsage"));
            return;
        }

        String targetName = args[1];
        String timeString = args[2];

        Player targetPlayer = Bukkit.getPlayerExact(targetName);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            player.sendMessage(ConfigUtil.getColoredString("playerDontOnline"));
            return;
        }

        if (NEnderChest.getINSTANCE().getPlayersDataBase().getSlotsForPlayer(targetName) + 9 > 56) {
            player.sendMessage(ConfigUtil.getColoredString("enderChestLimit"));
            return;
        }

        Time time = null;
        try {
            time = Time.valueOf(timeString);
        } catch (IllegalArgumentException e) {
            player.sendMessage(ConfigUtil.getColoredString("possibleTypes"));
        }
        if (time == null) {
            return;
        }
        NEnderChest.getINSTANCE().getPlayersDataBase().givePlayerTime(targetPlayer, time);
        player.sendMessage(ConfigUtil.getColoredString("givedSuccessfully")
                .replace("%size%", Integer.toString(NEnderChest.getINSTANCE().getPlayersDataBase().getSlotsForPlayer(targetName)))
                .replace("%target%", targetName));
    }
}
