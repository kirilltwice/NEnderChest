package wtf.n1zamu.command;

import org.bukkit.entity.Player;

public interface ICommand {
    String getName();

    void execute(Player player, String[] args);
}
