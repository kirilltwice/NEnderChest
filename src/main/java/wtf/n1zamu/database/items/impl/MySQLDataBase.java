package wtf.n1zamu.database.items.impl;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import wtf.n1zamu.NEnderChest;
import wtf.n1zamu.database.items.ItemDataBase;
import wtf.n1zamu.database.items.object.InventoryItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLDataBase extends ItemDataBase {

    private Connection connection;

    public MySQLDataBase() {
        String url = "jdbc:mysql://"
                + NEnderChest.getINSTANCE().getConfig().getString("host") + ":"
                + NEnderChest.getINSTANCE().getConfig().getInt("port") + "/"
                + NEnderChest.getINSTANCE().getConfig().getString("itemsTable") + "?useSSL=false&serverTimezone=UTC";

        String user = NEnderChest.getINSTANCE().getConfig().getString("username");
        String password = NEnderChest.getINSTANCE().getConfig().getString("password");
        try {
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

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
        Bukkit.getScheduler().runTaskAsynchronously(NEnderChest.getINSTANCE(), () -> {
            try {
                connection.setAutoCommit(false);
                try (PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM itemsTable WHERE player = ?")) {
                    deleteStmt.setString(1, player);
                    deleteStmt.executeUpdate();
                }

                try (PreparedStatement insertStmt = connection.prepareStatement(
                        "INSERT INTO itemsTable (player, slot, item_data) VALUES (?, ?, ?)")) {
                    for (InventoryItem inventoryItem : items) {
                        ItemStack item = inventoryItem.getItemStack();
                        byte[] itemData = item.serializeAsBytes();

                        insertStmt.setString(1, player);
                        insertStmt.setInt(2, inventoryItem.getSlot());
                        insertStmt.setBytes(3, itemData);
                        insertStmt.addBatch();
                    }
                    insertStmt.executeBatch();
                }
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
            }
        });
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