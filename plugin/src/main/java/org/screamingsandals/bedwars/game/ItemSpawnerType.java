package org.screamingsandals.bedwars.game;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.screamingsandals.bedwars.lib.lang.I18n.i18n;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ItemSpawnerType implements org.screamingsandals.bedwars.api.game.ItemSpawnerType {
    private final String configKey;
    private final String name;
    private final String translatableKey;
    private final double spread;
    private final Material material;
    private final ChatColor color;
    private final int damage;

    private final int interval;
    private final int amount;

    public String getConfigKey() {
        return configKey;
    }

    public String getTranslatableKey() {
        if (translatableKey != null && !translatableKey.equals("")) {
            return i18n(translatableKey, name, false);
        }
        return name;
    }

    public String getItemName() {
        return color + getTranslatableKey();
    }

    public String getItemBoldName() {
        return color.toString() + ChatColor.BOLD.toString() + getTranslatableKey();
    }

    public ItemStack getStack() {
        return getStack(1);
    }

    @SuppressWarnings("deprecation")
    public ItemStack getStack(int amount) {
        ItemStack stack = new ItemStack(material, amount, (short) damage);
        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setDisplayName(getItemName());
        stack.setItemMeta(stackMeta);
        return stack;
    }
}
