package com.wolfeiii.designermode.util;

import com.wolfeiii.designermode.DesignerMode;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UtilString {

    private static final DesignerMode designerMode = DesignerMode.getInstance();

    public static String translateColors(@NotNull String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendPrefix(@NotNull Player player, String msgNode) {
        player.sendMessage(translateColors(designerMode.getMessageSystem().translate("messages.prefix")
                + " "
                + designerMode.getMessageSystem().translate("messages." + msgNode)));
    }

    public static void sendPrefix(@NotNull CommandSender commandSender, @NotNull String msgNode, Object... placeholders) {
        commandSender.sendMessage(translateColors(designerMode.getMessageSystem().translate("messages.prefix")
                + " "
                + designerMode.getMessageSystem().translate("messages." + msgNode, placeholders)));
    }

    public static void sendPrefix(@NotNull CommandSender commandSender, @NotNull String msgNode) {
        commandSender.sendMessage(translateColors(designerMode.getMessageSystem().translate("messages.prefix")
                + " "
                + designerMode.getMessageSystem().translate("messages." + msgNode)));
    }

    public static void send(@NotNull CommandSender commandSender, @NotNull String msgNode) {
        commandSender.sendMessage(translateColors(designerMode.getMessageSystem().translate("messages." + msgNode)));
    }
}
