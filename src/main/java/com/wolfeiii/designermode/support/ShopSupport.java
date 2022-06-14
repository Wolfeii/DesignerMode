package com.wolfeiii.designermode.support;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface ShopSupport {

    double getBlockPrice(Player player, Material material);

    boolean isBlockEnabled(Material material);
}
