package com.wolfeiii.designermode.listeners;

import com.wolfeiii.designermode.DesignerMode;
import com.wolfeiii.designermode.manager.CreativeManager;
import com.wolfeiii.designermode.util.UtilString;
import com.wolfeiii.designermode.util.UtilWorld;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Container;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public record DesignerModeEventHandler(DesignerMode designerMode, CreativeManager creativeManager) implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();

        if (designerMode.getCreativeManager().hasCreative(player.getUniqueId())) {
            if (!designerMode.getShopSupport().isBlockEnabled(blockType) || designerMode.getDisabledBlocks().contains(blockType)) {
                UtilString.sendPrefix(event.getPlayer(), "blockNotEnabled");
                event.setCancelled(true);
                return;
            }

            double buyPrice = designerMode.getShopSupport().getBlockPrice(player, blockType);
            if (!designerMode.getEconomy().has(player, buyPrice)) {
                UtilString.sendPrefix(event.getPlayer(), "cantAfford");
                event.setCancelled(true);
                return;
            }

            designerMode.getEconomy().withdrawPlayer(player, buyPrice);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        handleDesignerModeEvent(event.getPlayer(), () -> creativeManager.remove(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerSwitchWorlds(PlayerChangedWorldEvent event) {
        handleDesignerModeEvent(event.getPlayer(), () -> creativeManager.remove(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR) return;

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        if (event.getClickedBlock().getState() instanceof Container || event.getMaterial() == Material.EXPERIENCE_BOTTLE) {
            handleDesignerModeEvent(event.getPlayer(), () -> {
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                event.getPlayer().closeInventory();
            });
        }
    }


    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        handleDesignerModeEvent(event.getPlayer(), () -> {
            event.setCancelled(true);
        });
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {
        handleDesignerModeEvent(event.getPlayer(), () -> {
            event.setCancelled(true);
        });
    }

    @EventHandler
    public void onPlayerUseBucket(PlayerBucketEmptyEvent event) {
        handleDesignerModeEvent(event.getPlayer(), () -> event.setCancelled(true));
    }

    @EventHandler
    public void onFramePlace(HangingPlaceEvent event) {
        if (event.getPlayer() == null) return;

        handleDesignerModeEvent(event.getPlayer(), () -> event.setCancelled(true));
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        handleDesignerModeEvent(event.getPlayer(), () -> event.setCancelled(true));
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        handleDesignerModeEvent(event.getPlayer(), () -> event.setCancelled(true));
    }

    public void handleDesignerModeEvent(@NotNull Player player, @NotNull Runnable cancel) {
        if (creativeManager.hasCreative(player.getUniqueId())) {
            World world = player.getWorld();
            if (!UtilWorld.isWorldAllowed(world)) {
                UtilString.sendPrefix(player, "disabledWorld");
                creativeManager.remove(player.getUniqueId());
            }

            UtilString.sendPrefix(player, "cantDoThis");
            cancel.run();
        }
    }
}
