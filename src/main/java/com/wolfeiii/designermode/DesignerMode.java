package com.wolfeiii.designermode;

import com.wolfeiii.designermode.command.DesignerModeCommand;
import com.wolfeiii.designermode.listeners.DesignerModeEventHandler;
import com.wolfeiii.designermode.manager.CreativeManager;
import com.wolfeiii.designermode.messages.MessageSystem;
import com.wolfeiii.designermode.support.EconomyShopGUISupport;
import com.wolfeiii.designermode.support.ShopGUIPlusSupport;
import com.wolfeiii.designermode.support.ShopSupport;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public final class DesignerMode extends JavaPlugin {

    @Getter
    private static DesignerMode instance;

    private CreativeManager creativeManager;
    public MessageSystem messageSystem;

    private File configFile, messagesFile;
    private FileConfiguration mainConfig, messagesConfig;

    private ShopSupport shopSupport;
    private Economy economy;

    private List<Material> disabledBlocks;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        reloadConfig();

        messageSystem = new MessageSystem(this, getResource("internal_messages.yml"));
        registerMessages();

        this.creativeManager = new CreativeManager();
        this.shopSupport = registerShopSupport();
        new DesignerModeCommand(this);

        if (!setupEconomy() ) {
            getLogger().severe(String.format("%s shut down because Vault was not found.", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.disabledBlocks = getConfig().getStringList("disabledBlocks")
                .stream().map(Material::getMaterial)
                .collect(Collectors.toList());

        if (shopSupport == null) {
            getLogger().severe("DESIGNER MODE - SEVERE WARNING");
            getLogger().severe("No Shop plugin was found, and as a result, the plugin will shutdown.");
            getLogger().severe("Please install one of the supported shop plugins.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        } else {
            getLogger().info("Using Shop Support: " + shopSupport.getClass().getSimpleName());
        }

        Bukkit.getPluginManager().registerEvents(new DesignerModeEventHandler(this, creativeManager), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (creativeManager.hasCreative(player.getUniqueId()))
                creativeManager.remove(player.getUniqueId());
        });
    }

    public void registerMessages() {
        messagesConfig = new YamlConfiguration();
        messagesFile = new File(getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        try {
            messagesConfig.load(messagesFile);
            messageSystem.loadCustomMessages(messagesConfig);
        } catch (IOException | InvalidConfigurationException exception) {
            getLogger().severe("An error occurred whilst registering the messages config: " + exception.getMessage());
        }
    }

    private ShopSupport registerShopSupport() {
        if (Bukkit.getPluginManager().isPluginEnabled("EconomyShopGUI")) return new EconomyShopGUISupport();
        if (Bukkit.getPluginManager().isPluginEnabled("ShopGUIPlus")) return new ShopGUIPlusSupport();
        return null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;

        economy = rsp.getProvider();
        return true;
    }
}
