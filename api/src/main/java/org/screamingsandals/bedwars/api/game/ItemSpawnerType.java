package org.screamingsandals.bedwars.api.game;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
}
