package wtf.n1zamu.listener;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import wtf.n1zamu.NEnderChest;
import wtf.n1zamu.holder.BuyHolder;
import wtf.n1zamu.time.Time;
import wtf.n1zamu.util.ConfigUtil;
import wtf.n1zamu.util.NBTUtil;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getHolder() == null) return;
        if (!(event.getClickedInventory().getHolder() instanceof BuyHolder)) return;
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        if (!clickedItem.hasItemMeta()) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = ((Player) event.getWhoClicked()).getPlayer();
        if (NBTUtil.hasItemNBT(clickedItem, "back")) {
            player.closeInventory();
            Bukkit.dispatchCommand(player, NEnderChest.getINSTANCE().getConfig().getString("backCommand"));
        }
        if (NBTUtil.hasItemNBT(clickedItem, "buyed")) {
            player.sendMessage(ConfigUtil.getColoredString("unlockedRow"));
            return;
        }
        if (NBTUtil.hasItemNBT(clickedItem, "unbuyed")) {
            int slots = NEnderChest.getINSTANCE().getPlayersDataBase().getSlotsForPlayer(player.getName());
            if (event.getSlot() + 1 >= slots + 9) {
                player.sendMessage(ConfigUtil.getColoredString("buyPreviousRow"));
                return;
            }
            player.openInventory(new BuyHolder(player).getBuyInventory());
            return;
        }
        if (NBTUtil.hasItemNBT(clickedItem, "forWipe")) {
            int slots = NEnderChest.getINSTANCE().getPlayersDataBase().getSlotsForPlayer(player.getName());
            if (slots + 9 > 56) {
                return;
            }
            if (PlayerPoints.getPlugin(PlayerPoints.class).getAPI().look(player.getUniqueId()) < NEnderChest.getINSTANCE().getConfig().getInt("buyForWipePrice")) {
                player.sendMessage(ConfigUtil.getColoredString("notEnoughMoney"));
                return;
            }
            PlayerPoints.getPlugin(PlayerPoints.class).getAPI().take(player.getUniqueId(), NEnderChest.getINSTANCE().getConfig().getInt("buyForWipePrice"));
            NEnderChest.getINSTANCE().getPlayersDataBase().givePlayerTime(player, Time.WIPE);
            player.closeInventory();
            player.sendMessage(ConfigUtil.getColoredString("buyedSuccessfully"));

        }
        if (NBTUtil.hasItemNBT(clickedItem, "forever")) {
            int slots = NEnderChest.getINSTANCE().getPlayersDataBase().getSlotsForPlayer(player.getName());
            if (slots + 9 > 56) {
                return;
            }
            if (PlayerPoints.getPlugin(PlayerPoints.class).getAPI().look(player.getUniqueId()) < NEnderChest.getINSTANCE().getConfig().getInt("buyForeverPrice")) {
                player.sendMessage(ConfigUtil.getColoredString("notEnoughMoney"));
                return;
            }
            PlayerPoints.getPlugin(PlayerPoints.class).getAPI().take(player.getUniqueId(), NEnderChest.getINSTANCE().getConfig().getInt("buyForeverPrice"));
            NEnderChest.getINSTANCE().getPlayersDataBase().givePlayerTime(player, Time.FOREVER);
            player.closeInventory();
            player.sendMessage(ConfigUtil.getColoredString("buyedSuccessfully"));

        }
    }
}
