package com.github.sirblobman.hook.epicspawners.shopguiplus;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.brcdev.shopgui.event.ShopGUIPlusPostEnableEvent;

public final class HookPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPostEnable(ShopGUIPlusPostEnableEvent e) {
        new HookProvider(this).register();
    }
}
