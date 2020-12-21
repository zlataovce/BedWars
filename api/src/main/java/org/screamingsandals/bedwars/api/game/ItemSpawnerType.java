package org.screamingsandals.bedwars.api.game;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Bedwars Team
 */
public interface ItemSpawnerType {
    /**
     * Configuration key for loading the spawner
     * @return String
     */
    String getConfigKey();

    /**
     * Color of the spawner (text, etc)
     * @return ChatColor
     */
    ChatColor getColor();

    /**
     * Amount to spawn per one interval
     * @return Amount
     */
    int getAmount();

    /**
     * @return
     */
    int getInterval();

    /**
     * @return
     */
    double getSpread();

    /**
     * Possible: TICKS, SECONDS, MINUTES
     * @return time unit of this spawner
     */
    SpawnerTime getTimeUnit();

    /**
     * @return
     */
    String getName();

    /**
     * @return
     */
    Material getMaterial();

    /**
     * @return
     */
    String getTranslatableKey();

    /**
     * @return
     */
    String getItemName();

    /**
     * @return
     */
    String getItemBoldName();

    /**
     * @return
     */
    ItemStack getStack();

    /**
     * @param amount
     * @return
     */
    ItemStack getStack(int amount);

    @RequiredArgsConstructor
    enum SpawnerTime {
        TICKS(1),
        SECONDS(20),
        MINUTES(1200);

        @Getter
        private final long tickValue;
    }
}
