package com.github.sirblobman.hook.epicspawners.shopguiplus;

import java.util.List;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.spawners.spawner.SpawnerData;
import com.songoda.epicspawners.spawners.spawner.SpawnerManager;
import com.songoda.epicspawners.spawners.spawner.SpawnerTier;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.exception.api.ExternalSpawnerProviderNameConflictException;
import net.brcdev.shopgui.spawner.external.provider.ExternalSpawnerProvider;

public final class HookProvider implements ExternalSpawnerProvider {
    private final HookPlugin plugin;
    private final EpicSpawners epicSpawners;

    public HookProvider(@NotNull HookPlugin plugin) {
        this.plugin = plugin;
        this.epicSpawners = JavaPlugin.getPlugin(EpicSpawners.class);
    }

    private @NotNull HookPlugin getPlugin() {
        return this.plugin;
    }

    private @NotNull EpicSpawners getEpicSpawners() {
        return this.epicSpawners;
    }

    private @NotNull Logger getLogger() {
        HookPlugin plugin = getPlugin();
        return plugin.getLogger();
    }

    public void register() {
        try {
            ShopGuiPlusApi.registerSpawnerProvider(this);
        } catch (ExternalSpawnerProviderNameConflictException ex) {
            Logger logger = getLogger();
            logger.warning("A spawner provider is already registered for EpicSpawners.");
        }
    }

    @Override
    public @NotNull String getName() {
        HookPlugin plugin = getPlugin();
        PluginDescriptionFile description = plugin.getDescription();
        String prefix = description.getPrefix();
        return (prefix != null ? prefix : plugin.getName());
    }

    @Override
    public @Nullable ItemStack getSpawnerItem(@Nullable EntityType entityType) {
        if (entityType == null) {
            return null;
        }

        EpicSpawners epicSpawners = getEpicSpawners();
        SpawnerManager spawnerManager = epicSpawners.getSpawnerManager();
        SpawnerData spawnerData = spawnerManager.getSpawnerData(entityType);
        if (spawnerData == null) {
            return null;
        }

        SpawnerTier firstTier = spawnerData.getFirstTier();
        if (firstTier == null) {
            return null;
        }

        return firstTier.toItemStack(1, 1);
    }

    @Override
    public @Nullable EntityType getSpawnerEntityType(@Nullable ItemStack item) {
        if (item == null) {
            return null;
        }

        EpicSpawners epicSpawners = getEpicSpawners();
        SpawnerManager spawnerManager = epicSpawners.getSpawnerManager();
        SpawnerTier spawnerTier = spawnerManager.getSpawnerTier(item);
        if (spawnerTier == null) {
            return null;
        }

        List<EntityType> entityTypeList = spawnerTier.getEntities();
        if (entityTypeList.isEmpty()) {
            return null;
        }

        return entityTypeList.get(0);
    }
}
