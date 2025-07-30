package wtf.n1zamu.holder;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import wtf.n1zamu.NEnderChest;
import wtf.n1zamu.database.items.object.InventoryItem;
import wtf.n1zamu.util.ConfigUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnderChestHolder implements InventoryHolder {

    public EnderChestHolder(String player) {
        int slots = NEnderChest.getINSTANCE().getPlayersDataBase().getSlotsForPlayer(player);
        List<InventoryItem> items = NEnderChest.getINSTANCE().getItemsDataBase().getPlayerItems(player);
        Inventory inventory = Bukkit.createInventory(this, slots, ConfigUtil.getColoredString("enderMenuName").replace("%player%", player));
        if (!items.isEmpty()) {
            items.forEach(inventoryItem -> inventory.setItem(inventoryItem.getSlot(), inventoryItem.getItemStack()));
        }
        Bukkit.getPlayerExact(player).openInventory(inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
