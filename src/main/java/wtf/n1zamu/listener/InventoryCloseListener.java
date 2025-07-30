package wtf.n1zamu.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import wtf.n1zamu.NEnderChest;
import wtf.n1zamu.command.impl.CheckCommand;
import wtf.n1zamu.database.items.object.InventoryItem;
import wtf.n1zamu.holder.EnderChestHolder;

import java.util.ArrayList;
import java.util.List;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        Player player = ((Player) event.getPlayer()).getPlayer();
        if (!(event.getInventory().getHolder() instanceof EnderChestHolder)) {
            return;
        }
        ItemStack[] contents = event.getInventory().getContents();
        List<InventoryItem> inventoryItemList = new ArrayList<>();

        for (int index = 0; index < contents.length; index++) {
            ItemStack item = contents[index];

            if (item == null || item.getType() == Material.AIR) {
                continue;
            }
            inventoryItemList.add(new InventoryItem(index, item));
        }
        if (!CheckCommand.viewers.containsKey(player)) {
            List<InventoryItem> items = NEnderChest.getINSTANCE().getItemsDataBase().getPlayerItems(player.getName());
            if (inventoryItemList.equals(items)) {
                return;
            }
            NEnderChest.getINSTANCE().getItemsDataBase().updateForPlayer(player.getName(), inventoryItemList);
        } else {
            List<InventoryItem> items = NEnderChest.getINSTANCE().getItemsDataBase().getPlayerItems(CheckCommand.viewers.get(player));
            if (inventoryItemList.equals(items)) {
                return;
            }
            NEnderChest.getINSTANCE().getItemsDataBase().updateForPlayer(CheckCommand.viewers.get(player), inventoryItemList);
            CheckCommand.viewers.remove(player);
        }
    }
}
