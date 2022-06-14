package com.wolfeiii.designermode.support;

import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopGUIPlusSupport implements ShopSupport {

    @Override
    public double getBlockPrice(Player player, Material material) {
        return ShopGuiPlusApi.getItemStackPriceBuy(player, new ItemStack(material));
    }

    @Override
    public boolean isBlockEnabled(Material material) {
        return !(ShopGuiPlusApi.getItemStackPriceBuy(new ItemStack(material)) == -1);
    }
}
