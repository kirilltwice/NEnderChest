package wtf.n1zamu.holder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import wtf.n1zamu.NEnderChest;
import wtf.n1zamu.util.ConfigUtil;
import wtf.n1zamu.util.NBTUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuyHolder implements InventoryHolder {
    private final Inventory rowsInventory;
    private final Inventory buyInventory;
    private Player player;

    public BuyHolder(Player player) {
        this.player = player;
        rowsInventory = createRowsInventory(player);
        buyInventory = createBuyInventory();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return rowsInventory;
    }

    public @NotNull Inventory getBuyInventory() {
        return buyInventory;
    }

    private Inventory createBuyInventory() {
        Inventory buyInventory = Bukkit.createInventory(this, 54, ConfigUtil.getColoredString("shopMenuName").replace("%player%", player.getName()));

        ItemStack buyForWipe = createShopItem("buyForWipeName", "buyForWipeLore", "forWipe", Material.valueOf(ConfigUtil.getColoredString("forWipeMaterial")));
        ItemStack buyForever = createShopItem("buyForeverName", "buyForeverLore", "forever", Material.valueOf(ConfigUtil.getColoredString("foreverMaterial")));
        ItemStack backItem = createShopItem("backItemName", "backItemLore", "back", Material.valueOf(ConfigUtil.getColoredString("backMaterial")));
        ItemMeta meta = backItem.getItemMeta();
        meta.setCustomModelData(NEnderChest.getINSTANCE().getConfig().getInt("backItemModelData"));
        backItem.setItemMeta(meta);
        buyInventory.setItem(NEnderChest.getINSTANCE().getConfig().getInt("buyForWipeSlot"), buyForWipe);
        buyInventory.setItem(NEnderChest.getINSTANCE().getConfig().getInt("backItemSlot"), backItem);
        buyInventory.setItem(NEnderChest.getINSTANCE().getConfig().getInt("buyForeverSlot"), buyForever);

        return buyInventory;
    }

    private ItemStack createShopItem(String nameKey, String loreKey, String nbtKey, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ConfigUtil.getColoredString(nameKey));
            List<String> lore = NEnderChest.getINSTANCE().getConfig().getStringList(loreKey).stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .collect(Collectors.toList());
            if (!loreKey.equalsIgnoreCase("backItemLore")) {
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }

        NBTUtil.addItemNBT(item, nbtKey);
        return item;
    }


    private Inventory createRowsInventory(Player player) {
        Inventory rowsInventory = Bukkit.createInventory(this, 54, ConfigUtil.getColoredString("shopRowsName").replace("%player%", player.getName()));
        int slots = NEnderChest.getINSTANCE().getPlayersDataBase().getSlotsForPlayer(player.getName());

        int rowsCount = slots / 9;
        int currentSlot = 0;
        for (int i = 1; i < 7; i++) {
            ItemStack item;
            if (i <= rowsCount) {
                item = createItem(player, Material.LIME_STAINED_GLASS_PANE, "buyedItemLore", "buyedItemName", i);
                NBTUtil.addItemNBT(item, "buyed");
            } else {
                item = createItem(player, Material.RED_STAINED_GLASS_PANE, "unbuyedItemLore", "unbuyedItemName", i);
                NBTUtil.addItemNBT(item, "unbuyed");
            }
            for (int j = 0; j < 9; j++) {
                rowsInventory.setItem(currentSlot, item);
                currentSlot++;
            }
        }

        return rowsInventory;
    }

    private ItemStack createItem(Player player, Material material, String loreKey, String nameKey, int row) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        String time = NEnderChest.getINSTANCE().getPlayersDataBase().getTime(player.getName(), row);
        NEnderChest.getINSTANCE().getConfig().getStringList(loreKey)
                .forEach(line -> lore.add(ChatColor.translateAlternateColorCodes('&',
                        line.replace("%time%", time))));

        meta.setLore(lore);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', NEnderChest.getINSTANCE().getConfig().getString(nameKey).replace("%row%", String.valueOf(row))));
        item.setItemMeta(meta);

        return item;
    }
}
