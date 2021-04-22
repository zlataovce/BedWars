package org.screamingsandals.bedwars.game;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;

public interface GameElement {
    void deserialize(ConfigurationNode node) throws ConfigurateException;

    void serialize(ConfigurationNode node) throws ConfigurateException;
}
