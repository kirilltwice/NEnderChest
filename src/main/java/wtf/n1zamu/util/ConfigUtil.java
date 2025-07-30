package wtf.n1zamu.util;

import org.bukkit.ChatColor;
import wtf.n1zamu.NEnderChest;

public class ConfigUtil {
    public static String getColoredString(String path) {
        return ChatColor.translateAlternateColorCodes('&',NEnderChest.getINSTANCE().getConfig().getString(path));
    }
}
