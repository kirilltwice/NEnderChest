package wtf.n1zamu;

import org.bukkit.plugin.java.JavaPlugin;
import wtf.n1zamu.command.Command;
import wtf.n1zamu.database.IDataBase;
import wtf.n1zamu.database.items.ItemDataBase;
import wtf.n1zamu.database.items.impl.SQLiteDataBase;
import wtf.n1zamu.database.items.impl.YAMLDataBase;
import wtf.n1zamu.database.players.PlayerDataBase;
import wtf.n1zamu.database.players.impl.MySQLDataBase;
import wtf.n1zamu.listener.InteractListener;
import wtf.n1zamu.listener.InventoryClickListener;
import wtf.n1zamu.listener.InventoryCloseListener;
import wtf.n1zamu.listener.PlayerJoinListener;
import wtf.n1zamu.util.InventoryUtil;

import java.util.Arrays;

public final class NEnderChest extends JavaPlugin {
    private static NEnderChest INSTANCE;
    private ItemDataBase itemsDataBase;
    private PlayerDataBase playersDataBase;


    // TRASHCODE WARNINGS
    @Override
    public void onEnable() {
        INSTANCE = this;
        itemsDataBase = getItemsDataBaseFromConfig();
        playersDataBase = getPlayersDataBaseFromConfig();
        Arrays.asList(itemsDataBase, playersDataBase).forEach(IDataBase::connect);
        Arrays.asList(new InventoryCloseListener(), new PlayerJoinListener(), new InteractListener(), new InventoryClickListener()).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
        getCommand("nEnderChest").setExecutor(new Command());
        getCommand("nEnderChest").setTabCompleter(new Command());
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        Arrays.asList(itemsDataBase, playersDataBase).forEach(IDataBase::disconnect);
        InventoryUtil.closeEnderChest();
    }

    public static NEnderChest getINSTANCE() {
        return INSTANCE;
    }

    public ItemDataBase getItemsDataBase() {
        return itemsDataBase;
    }

    public PlayerDataBase getPlayersDataBase() {
        return playersDataBase;
    }
    // CRINGECODE
    private ItemDataBase getItemsDataBaseFromConfig() {
        if (this.getConfig().getString("itemsDatabase").equalsIgnoreCase("MySQL")) {
            return new wtf.n1zamu.database.items.impl.MySQLDataBase();
        } else if (this.getConfig().getString("itemsDatabase").equalsIgnoreCase("SQLite")) {
            return new SQLiteDataBase();
        } else if (this.getConfig().getString("itemsDatabase").equalsIgnoreCase("YAML")) {
            return new YAMLDataBase();
        }
        return new SQLiteDataBase();
    }

    private PlayerDataBase getPlayersDataBaseFromConfig() {
        if (this.getConfig().getString("playersDatabase").equalsIgnoreCase("MySQL")) {
            return new MySQLDataBase();
        } else if (this.getConfig().getString("playersDatabase").equalsIgnoreCase("SQLite")) {
            return new wtf.n1zamu.database.players.impl.SQLiteDataBase();
        }
        return new wtf.n1zamu.database.players.impl.SQLiteDataBase();
    }
}
