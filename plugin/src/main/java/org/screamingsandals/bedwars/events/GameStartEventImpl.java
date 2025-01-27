package org.screamingsandals.bedwars.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.bedwars.api.events.GameStartEvent;
import org.screamingsandals.bedwars.game.GameImpl;
import org.screamingsandals.lib.event.CancellableAbstractEvent;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameStartEventImpl extends CancellableAbstractEvent implements GameStartEvent<GameImpl> {
    private final GameImpl game;
}
