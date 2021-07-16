package com.github.sirblobman.hook.epicspawners.shopguiplus;

import java.util.List;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.spawners.spawner.SpawnerData;
import com.songoda.epicspawners.spawners.spawner.SpawnerManager;
import com.songoda.epicspawners.spawners.spawner.SpawnerTier;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.spawner.external.provider.ExternalSpawnerProvider;

public final class HookProvider implements ExternalSpawnerProvider {
    private final HookPlugin plugin;
    private final EpicSpawners epicSpawners;

    public HookProvider(HookPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
        this.epicSpawners = JavaPlugin.getPlugin(EpicSpawners.class);
    }
    
    public void register() {
        ShopGuiPlusApi.registerSpawnerProvider(this);
    }
    
    @Override
    public String getName() {
        PluginDescriptionFile description = this.plugin.getDescription();
        return description.getPrefix();
    }
    
    @Override
    public ItemStack getSpawnerItem(EntityType entityType) {
        if(entityType == null) {
            return new ItemStack(Material.AIR);
        }

        SpawnerManager spawnerManager = this.epicSpawners.getSpawnerManager();
        SpawnerData spawnerData = spawnerManager.getSpawnerData(entityType);
        if(spawnerData != null) {
            SpawnerTier firstTier = spawnerData.getFirstTier();
            if(firstTier != null) {
                return firstTier.toItemStack(1, 1);
            }
        }

        return new ItemStack(Material.AIR);
    }
    
    @Override
    public EntityType getSpawnerEntityType(ItemStack item) {
        SpawnerManager spawnerManager = this.epicSpawners.getSpawnerManager();
        SpawnerTier spawnerTier = spawnerManager.getSpawnerTier(item);
        if(spawnerTier != null) {
            List<EntityType> entityTypeList = spawnerTier.getEntities();
            if(!entityTypeList.isEmpty()) {
                return entityTypeList.get(0);
            }
        }

        return EntityType.UNKNOWN;
    }
}
