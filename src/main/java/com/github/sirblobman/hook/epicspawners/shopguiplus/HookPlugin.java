package com.github.sirblobman.hook.epicspawners.shopguiplus;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class HookPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        Logger logger = getLogger();
        if(checkMissingHook("ShopGUIPlus")) {
            logger.warning("Could not find the ShopGUIPlus plugin.");
            setEnabled(false);
            return;
        }

        if(checkMissingHook("EpicSpawners")) {
            logger.warning("Could not find the EpicSpawners plugin.");
            setEnabled(false);
            return;
        }

        HookProvider hookProvider = new HookProvider(this);
        hookProvider.register();
    }

    private boolean checkMissingHook(String pluginName) {
        PluginManager manager = Bukkit.getPluginManager();
        Plugin plugin = manager.getPlugin(pluginName);
        if(plugin == null || !plugin.isEnabled()) {
            return true;
        }

        PluginDescriptionFile description = plugin.getDescription();
        String fullName = description.getFullName();

        Logger logger = getLogger();
        logger.info("Successfully hooked into plugin '" + fullName + "'.");
        return false;
    }
}
