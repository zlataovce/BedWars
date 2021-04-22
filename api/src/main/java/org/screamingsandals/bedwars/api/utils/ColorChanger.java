package org.screamingsandals.bedwars.api.utils;

import org.screamingsandals.bedwars.api.OldTeamColor;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.utils.Wrapper;


public interface ColorChanger<I extends Wrapper> {

    @Deprecated
    ItemStack applyColor(OldTeamColor color, ItemStack stack);


    /**
     * Apply color of team to ItemStack
     *
     * @param color Color of team
     * @param stack ItemStack that should be colored
     * @return colored ItemStack or normal ItemStack if ItemStack can't be colored
     */
    I applyColor(OldTeamColor color, Object stack);
}
