package wtf.n1zamu.database.items;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import wtf.n1zamu.database.IDataBase;
import wtf.n1zamu.database.items.object.InventoryItem;

import java.util.List;

public abstract class ItemDataBase implements IDataBase {
    Cache<String, List<InventoryItem>> cache = CacheBuilder.newBuilder().build();

    public Cache<String, List<InventoryItem>> getCache() {
        return cache;
    }

    public abstract void updateForPlayer(String player, List<InventoryItem> items);

    public abstract List<InventoryItem> getPlayerItems(String player);

    public abstract boolean hasEnderChest(String player);
}
