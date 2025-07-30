package wtf.n1zamu.util;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wtf.n1zamu.NEnderChest;

public class NBTUtil {
    public static void addItemNBT(org.bukkit.inventory.ItemStack item, String key) {
        ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
        meta.getPersistentDataContainer().set(new NamespacedKey(NEnderChest.getINSTANCE(), key), PersistentDataType.STRING, "");
        item.setItemMeta(meta);
    }

    public static boolean hasItemNBT(ItemStack item, String key) {
        return item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(NEnderChest.getINSTANCE(), key), PersistentDataType.STRING);
    }
}
