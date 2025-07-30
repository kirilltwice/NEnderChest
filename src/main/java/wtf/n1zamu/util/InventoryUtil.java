package wtf.n1zamu.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import wtf.n1zamu.NEnderChest;
import wtf.n1zamu.database.items.object.InventoryItem;
import wtf.n1zamu.holder.EnderChestHolder;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtil {
    public static void closeEnderChest() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof EnderChestHolder)) {
                return;
            }
            Inventory inventory = player.getOpenInventory().getTopInventory();
            ItemStack[] contents = inventory.getContents();
            List<InventoryItem> inventoryItemList = new ArrayList<>();

            for (int index = 0; index < contents.length; index++) {
                ItemStack item = contents[index];

                if (item == null || item.getType() == Material.AIR) continue;
                inventoryItemList.add(new InventoryItem(index, item));
            }
            if (inventoryItemList.equals(NEnderChest.getINSTANCE().getItemsDataBase().getPlayerItems(player.getName()))) {
                player.closeInventory();
                return;
            }
            NEnderChest.getINSTANCE().getItemsDataBase().updateForPlayer(player.getName(), inventoryItemList);
            player.closeInventory();
        });
    }
}
