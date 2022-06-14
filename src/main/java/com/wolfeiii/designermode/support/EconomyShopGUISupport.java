package com.wolfeiii.designermode.support;

import com.wolfeiii.designermode.DesignerMode;
import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EconomyShopGUISupport implements ShopSupport {
    @Override
    public double getBlockPrice(Player player, Material material) {
        return EconomyShopGUIHook.getItemBuyPrice(player, new ItemStack(material));
    }

    @Override
    public boolean isBlockEnabled(Material material) {
        return EconomyShopGUIHook.getItemBuyPrice(new ItemStack(material)) == null;
    }
}
