package wtf.n1zamu.database.players.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import wtf.n1zamu.NEnderChest;
import wtf.n1zamu.database.players.PlayerDataBase;
import wtf.n1zamu.time.Time;
import wtf.n1zamu.util.ConfigUtil;

import java.sql.*;

public class MySQLDataBase extends PlayerDataBase {
    private Connection connection;

    public MySQLDataBase() {
        String url = "jdbc:mysql://"
                + NEnderChest.getINSTANCE().getConfig().getString("host") + ":"
                + NEnderChest.getINSTANCE().getConfig().getInt("port") + "/"
                + NEnderChest.getINSTANCE().getConfig().getString("playersTable") + "?useSSL=false&serverTimezone=UTC";

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

            String sql = "CREATE TABLE IF NOT EXISTS playerTimes " +
                    "(playerName VARCHAR(150) NOT NULL, " +
                    "forWipe INTEGER, " +
                    "forever INTEGER, " +
                    "rowNumber INTEGER);";

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
            connection.createStatement().executeUpdate("DELETE FROM playerTimes WHERE forever = 0");
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void clear() {
        try {
            connection.createStatement().executeUpdate("DROP TABLE IF EXISTS playerTimes;");
            String sql = "CREATE TABLE IF NOT EXISTS playerTimes " +
                    "(playerName VARCHAR(150) NOT NULL, " +
                    "forWipe INTEGER, " +
                    "forever INTEGER, " +
                    "rowNumber INTEGER);";

            connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public int getSlotsForPlayer(String player) {
        if (getCache().asMap().containsKey(player)) {
            return getCache().asMap().get(player);
        }
        int slotsCount = 27;
        try {
            String query = "SELECT * FROM playerTimes WHERE playerName = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, player);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    slotsCount += 9;
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
        }
        getCache().asMap().put(player, slotsCount);
        return slotsCount;
    }

    @Override
    public String getTime(String player, int row) {
        if (row > 3) {
            String sql = "SELECT forWipe FROM playerTimes WHERE playerName = ? AND rowNumber = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, player);
                preparedStatement.setInt(2, row);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int forWipe = resultSet.getInt("forWipe");
                    if (forWipe == 1) {
                        return ConfigUtil.getColoredString("leftWipe");
                    }
                }

            } catch (SQLException e) {
                Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return ConfigUtil.getColoredString("leftForever");
    }


    @Override
    public void givePlayerTime(Player player, Time time) {
        String sql = "INSERT INTO playerTimes(playerName, forWipe, forever, rowNumber) VALUES(?, ?, ?, ?)";
        int row = (getSlotsForPlayer(player.getName()) / 9) + 1;
        Bukkit.getScheduler().runTaskAsynchronously(NEnderChest.getINSTANCE(), () -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, player.getName());
                if (time == Time.WIPE) {
                    preparedStatement.setInt(2, 1);
                    preparedStatement.setInt(3, 0);
                }
                if (time == Time.FOREVER) {
                    preparedStatement.setInt(2, 0);
                    preparedStatement.setInt(3, 1);
                }
                preparedStatement.setInt(4, row);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Bukkit.getLogger().info(e.getClass().getName() + ": " + e.getMessage());
            }
        });
        Bukkit.getLogger().info(row * 9 + "");
        getCache().asMap().put(player.getName(), row * 9);
    }
}
