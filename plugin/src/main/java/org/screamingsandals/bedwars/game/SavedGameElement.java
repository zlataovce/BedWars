package org.screamingsandals.bedwars.game;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.bedwars.team.TeamColor;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Data
public final class SavedGameElement<T extends GameElement> {
    public static final Map<String, Supplier<GameElement>> TYPES = new HashMap<>();

    static {
        // use lowercase keys
        TYPES.put("team_color", TeamColor::new);
    }

    private UUID uuid;
    @Nullable
    private String globalKey;
    private String elementGroup;
    private boolean synchronize = true;
    private T gameElement;

    @SuppressWarnings("unchecked")
    public void deserialize(ConfigurationNode node) throws ConfigurateException {
        uuid = UUID.fromString(node.node("uuid").getString(""));
        globalKey = node.node("globalKey").getString();
        elementGroup = node.node("elementGroup").getString("").toLowerCase();
        synchronize = node.node("synchronize").getBoolean(true);

        var supplier = TYPES.get(elementGroup);

        if (supplier == null) {
            throw new UnsupportedOperationException("Invalid GameElement group: " + elementGroup);
        }

        gameElement = (T) supplier.get();

        gameElement.deserialize(node.node("data"));

        if (globalKey != null && synchronize && gameElement instanceof SynchronizableGameElement) {
            ((SynchronizableGameElement) gameElement).synchronize(globalKey);
        }
    }

    public void serialize(ConfigurationNode node) throws ConfigurateException {
        node.node("uuid").set(uuid);
        node.node("globalKey").set(globalKey);
        node.node("elementGroup").set(elementGroup);
        node.node("synchronize").set(synchronize);
        gameElement.serialize(node.node("data"));
    }
}
