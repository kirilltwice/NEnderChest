package wtf.n1zamu.database.items.object;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("inventoryItem")
public class InventoryItem implements ConfigurationSerializable {
    private int slot;
    private ItemStack itemStack;

    public InventoryItem(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("slot", this.slot);
        map.put("itemStack", this.itemStack);
        return map;
    }

    public static InventoryItem deserialize(Map<String, Object> map) {
        int slot = (int) map.get("slot");
        ItemStack itemStack = (ItemStack) map.get("itemStack");
        return new InventoryItem(slot, itemStack);
    }
}
