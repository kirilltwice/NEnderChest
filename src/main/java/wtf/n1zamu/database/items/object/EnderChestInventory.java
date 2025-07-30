package wtf.n1zamu.database.items.object;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SerializableAs("enderChest")
public class EnderChestInventory implements ConfigurationSerializable {
    private String playerName;
    private List<InventoryItem> inventoryItems;

    public EnderChestInventory(String playerName, List<InventoryItem> inventoryItems) {
        this.playerName = playerName;
        this.inventoryItems = inventoryItems;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("playerName", this.playerName);
        map.put("inventoryItems", inventoryItems.stream().map(InventoryItem::serialize).collect(Collectors.toList()));
        return map;
    }

    public static EnderChestInventory deserialize(Map<String, Object> map) {
        String playerName = (String) map.get("playerName");
        List<Map<String, Object>> itemsMap = (List<Map<String, Object>>) map.get("inventoryItems");

        List<InventoryItem> inventoryItemList = itemsMap.stream().map(InventoryItem::deserialize).collect(Collectors.toList());
        return new EnderChestInventory(playerName, inventoryItemList);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }
}

