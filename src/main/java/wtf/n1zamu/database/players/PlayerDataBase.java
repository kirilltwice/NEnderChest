package wtf.n1zamu.database.players;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;
import wtf.n1zamu.database.IDataBase;
import wtf.n1zamu.database.items.object.InventoryItem;
import wtf.n1zamu.time.Time;

import java.util.List;

public abstract class PlayerDataBase implements IDataBase {
    public abstract int getSlotsForPlayer(String player);

    public abstract void givePlayerTime(Player player, Time time);

    public abstract String getTime(String player, int row);

    Cache<String, Integer> cache = CacheBuilder.newBuilder().build();

    public Cache<String, Integer> getCache() {
        return cache;
    }
}
