package org.screamingsandals.bedwars.game;

public interface SynchronizableGameElement extends GameElement {
    void synchronize(String globalKey);
}
