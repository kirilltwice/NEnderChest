package wtf.n1zamu.database.items.impl;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.n1zamu.NEnderChest;
import wtf.n1zamu.database.IDataBase;
import wtf.n1zamu.database.items.ItemDataBase;
import wtf.n1zamu.database.items.object.EnderChestInventory;
import wtf.n1zamu.database.items.object.InventoryItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YAMLDataBase extends ItemDataBase {
    private final JavaPlugin plugin;
    private File dataFile;
    private FileConfiguration dataConfig;
    private final List<EnderChestInventory> ENDER_CHEST_INVENTORIES = new ArrayList<>();

    public YAMLDataBase() {
        this.plugin = NEnderChest.getINSTANCE();
    }

    @Override
    public void connect() {
        dataFile = new File(plugin.getDataFolder(), "playerResources.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
                dataConfig = YamlConfiguration.loadConfiguration(this.dataFile);
                dataConfig.set("resources", new ArrayList<>());
            } catch (IOException e) {
                Bukkit.getLogger().info(e.getMessage());
            }
        }

        dataConfig = YamlConfiguration.loadConfiguration(this.dataFile);

        if (dataConfig.getList("resources") != null) {
            List<Map<String, Object>> itemStackMap = (List<Map<String, Object>>) dataConfig.getList("resources");
            itemStackMap.forEach(map -> ENDER_CHEST_INVENTORIES.add(EnderChestInventory.deserialize(map)));
        }
    }

    @Override
    public void disconnect() {
        try {
            if (ENDER_CHEST_INVENTORIES.isEmpty()) {
                dataConfig.save(dataFile);
                return;
            }
            List<Map<String, Object>> enderChestMap = new ArrayList<>();
            for (EnderChestInventory enderChestInventory : ENDER_CHEST_INVENTORIES) {
                enderChestMap.add(enderChestInventory.serialize());
            }
            dataConfig.set("resources", enderChestMap);
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void wipe() {
        try {
            ENDER_CHEST_INVENTORIES.clear();
            dataConfig.set("resources", new ArrayList<>());
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        try {
            ENDER_CHEST_INVENTORIES.clear();
            dataConfig.set("resources", new ArrayList<>());
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateForPlayer(String player, List<InventoryItem> items) {
        if (ENDER_CHEST_INVENTORIES.isEmpty()) {
            ENDER_CHEST_INVENTORIES.add(new EnderChestInventory(player, items));
            return;
        }

        boolean playerFound = false;

        for (EnderChestInventory enderChestInventory : ENDER_CHEST_INVENTORIES) {
            if (enderChestInventory.getPlayerName().equalsIgnoreCase(player)) {
                enderChestInventory.setInventoryItems(items);
                playerFound = true;
                break;
            }
        }

        if (!playerFound) {
            ENDER_CHEST_INVENTORIES.add(new EnderChestInventory(player, items));
        }
    }

    @Override
    public List<InventoryItem> getPlayerItems(String player) {
        for (EnderChestInventory enderChestInventory : ENDER_CHEST_INVENTORIES) {
            if (enderChestInventory.getPlayerName().equalsIgnoreCase(player)) {
                return enderChestInventory.getInventoryItems();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public boolean hasEnderChest(String player) {
        for (EnderChestInventory enderChestInventory : ENDER_CHEST_INVENTORIES) {
            if (enderChestInventory.getPlayerName().equalsIgnoreCase(player)) {
                return true;
            }
        }
        return false;
    }
}
