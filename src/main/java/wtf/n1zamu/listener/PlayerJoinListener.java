package wtf.n1zamu.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import wtf.n1zamu.NEnderChest;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void on(PlayerJoinEvent event) {
        NEnderChest.getINSTANCE().getItemsDataBase().getPlayerItems(event.getPlayer().getName());
    }
}
