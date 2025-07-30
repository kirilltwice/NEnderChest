package wtf.n1zamu.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import wtf.n1zamu.holder.EnderChestHolder;

public class InteractListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block.getType() != Material.ENDER_CHEST) {
            return;
        }
        if (player.getOpenInventory().getTopInventory().getHolder() instanceof EnderChestHolder) {
            return;
        }
        new EnderChestHolder(player.getName());
        event.setCancelled(true);
    }
}
