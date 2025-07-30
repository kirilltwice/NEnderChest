package wtf.n1zamu.database.items.impl;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.n1zamu.NEnderChest;
import wtf.n1zamu.database.items.ItemDataBase;
import wtf.n1zamu.database.items.object.InventoryItem;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import java.util.List;

public class SQLiteDataBase extends ItemDataBase {
    private Connection connection;
    private final JavaPlugin plugin;

    public SQLiteDataBase() {
        this.plugin = NEnderChest.getINSTANCE();
        File dataFile = new File(plugin.getDataFolder(), "items.db");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/NEnderChest/items.db");
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");

            String sql = "CREATE TABLE IF NOT EXISTS itemsTable (" +
                    "player VARCHAR(255) NOT NULL, " +
                    "slot INT NOT NULL, " +
                    "item_data BLOB NOT NULL, " +
                    "PRIMARY KEY (player, slot));";
            connection.createStatement().executeUpdate(sql);
        } catch (Exception e) {
            Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void wipe() {
        try {
            connection.createStatement().executeUpdate("DELETE FROM itemsTable");
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void clear() {
        try {
            connection.createStatement().executeUpdate("DELETE FROM itemsTable");
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void updateForPlayer(String player, List<InventoryItem> items) {
        getCache().asMap().put(player, items);
        try {
            connection.createStatement().executeUpdate("DELETE FROM itemsTable WHERE player = '" + player + "'");
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO itemsTable (player, slot, item_data) VALUES (?, ?, ?)");
            for (InventoryItem inventoryItem : items) {
                ItemStack item = inventoryItem.getItemStack();
                byte[] itemData = item.serializeAsBytes();

                stmt.setString(1, player);
                stmt.setInt(2, inventoryItem.getSlot());
                stmt.setBytes(3, itemData);
                stmt.addBatch();
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public List<InventoryItem> getPlayerItems(String player) {
        if (getCache().asMap().containsKey(player)) {
            return getCache().asMap().get(player);
        }
        List<InventoryItem> items = new ArrayList<>();
        String query = "SELECT slot, item_data FROM itemsTable WHERE player = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, player);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int slot = rs.getInt("slot");
                byte[] itemData = rs.getBytes("item_data");
                ItemStack itemStack = ItemStack.deserializeBytes(itemData);
                InventoryItem item = new InventoryItem(slot, itemStack);
                items.add(item);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
        }
        getCache().asMap().put(player, items);
        return items;
    }

    @Override
    public boolean hasEnderChest(String player) {
        if (getCache().asMap().containsKey(player)) {
            return true;
        }
        try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM itemsTable WHERE player = ?")) {

            stmt.setString(1, player);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
        }

        return false;
    }
}