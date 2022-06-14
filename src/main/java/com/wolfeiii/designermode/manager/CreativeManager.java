package com.wolfeiii.designermode.manager;

import com.wolfeiii.designermode.DesignerMode;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class CreativeManager {

    private final HashSet<UUID> creative = new HashSet<>();
    private final HashMap<UUID, ItemStack[]> inventories = new HashMap<>();

    public boolean toggle(UUID player) {
        Player bukkitPlayer = Bukkit.getPlayer(player);
        if (bukkitPlayer == null)
            return false;

        if (creative.remove(player) && inventories.containsKey(player)) {
            bukkitPlayer.getInventory().setContents(inventories.get(player));
            bukkitPlayer.setGameMode(GameMode.SURVIVAL);

            if (DesignerMode.getInstance().getConfig().getBoolean("flyEnabled")) {
                bukkitPlayer.setAllowFlight(false);
                bukkitPlayer.setFallDistance(0f);
            }

            return false;
        }

        bukkitPlayer.setGameMode(GameMode.CREATIVE);
        inventories.put(player, bukkitPlayer.getInventory().getContents());
        bukkitPlayer.getInventory().clear();

        if (DesignerMode.getInstance().getConfig().getBoolean("flyEnabled")) {
            bukkitPlayer.setAllowFlight(true);
            bukkitPlayer.setFallDistance(0f);
        } else {
            bukkitPlayer.setAllowFlight(false);
        }

        return creative.add(player);
    }

    public void remove(UUID player) {
        Player bukkitPlayer = Bukkit.getPlayer(player);
        if (bukkitPlayer == null)
            return;

        bukkitPlayer.getInventory().setContents(inventories.get(player));
        bukkitPlayer.setGameMode(GameMode.SURVIVAL);

        if (DesignerMode.getInstance().getConfig().getBoolean("flyEnabled")) {
            bukkitPlayer.setAllowFlight(false);
            bukkitPlayer.setFallDistance(0f);
        }

        creative.remove(player);
    }

    public boolean hasCreative(UUID player) {
        return creative.contains(player);
    }
}
