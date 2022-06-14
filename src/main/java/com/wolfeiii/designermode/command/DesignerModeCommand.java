package com.wolfeiii.designermode.command;

import com.wolfeiii.designermode.DesignerMode;
import com.wolfeiii.designermode.util.UtilString;
import com.wolfeiii.designermode.util.UtilWorld;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DesignerModeCommand implements CommandExecutor {

    private DesignerMode designerMode;

    public DesignerModeCommand(@NotNull DesignerMode designerMode) {
        this.designerMode = designerMode;
        this.designerMode.getCommand("designermode").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                UtilString.sendPrefix(sender, "only-player");
                return false;
            }

            if (!sender.hasPermission("designermode.toggle")) {
                UtilString.sendPrefix(sender, "noPermission");
                return false;
            }

            if (!UtilWorld.isWorldAllowed(player.getWorld())) {
                UtilString.sendPrefix(player, "disabledWorld");
                return false;
            }

            if (designerMode.getCreativeManager().toggle(player.getUniqueId())) {
                UtilString.sendPrefix(player, "toggleDesignerModeOn");
            } else {
                UtilString.sendPrefix(player, "toggleDesignerModeOff");
            }
        } else if (args.length == 1) {
            if (!sender.hasPermission("designermode.toggle.others")) {
                UtilString.sendPrefix(sender, "noPermission");
                return false;
            }

            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                UtilString.sendPrefix(sender, "playerNotFound", args[0]);
                return false;
            }

            if (designerMode.getCreativeManager().toggle(player.getUniqueId())) {
                UtilString.sendPrefix(sender, "toggleDesignerModeOnOther", player.getName());
            } else {
                UtilString.sendPrefix(sender, "toggleDesignerModeOffOther", player.getName());
            }
        } else {
            sender.sendMessage();
        }

        return false;
    }

}
