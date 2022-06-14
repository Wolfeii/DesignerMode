package com.wolfeiii.designermode.util;

import com.wolfeiii.designermode.DesignerMode;
import org.bukkit.World;

import java.util.stream.Collectors;

public class UtilWorld {

    public static boolean isWorldAllowed(World world) {
        if (world == null) return false;

        boolean isWhitelist = DesignerMode.getInstance().getConfig().getBoolean("worldWhitelist");
        return !DesignerMode.getInstance().getConfig().getStringList("selectedWorlds").contains(world.getName());
    }
}
