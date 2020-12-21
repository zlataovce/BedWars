package org.screamingsandals.bedwars.api.events;

import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.upgrades.Upgradeable;
import org.screamingsandals.bedwars.api.upgrades.UpgradeStorage;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Bedwars Team
 */
public class BedwarsUpgradeImprovedEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Game game;
    private UpgradeStorage storage;
    private Upgradeable upgradeable;
    private double oldLevel;
    private double newLevel;

    /**
     * @param game
     * @param storage
     * @param upgradeable
     * @param oldLevel
     * @param newLevel
     */
    public BedwarsUpgradeImprovedEvent(Game game, UpgradeStorage storage, Upgradeable upgradeable, double oldLevel,
                                       double newLevel) {
        this.game = game;
        this.storage = storage;
        this.upgradeable = upgradeable;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        upgradeable.setLevel(newLevel);
    }

    public static HandlerList getHandlerList() {
        return BedwarsUpgradeImprovedEvent.handlers;
    }

    /**
     * @return game
     */
    public Game getGame() {
        return this.game;
    }

    @Override
    public HandlerList getHandlers() {
        return BedwarsUpgradeImprovedEvent.handlers;
    }

    @Override
    public boolean isCancelled() {
        return upgradeable.getLevel() == oldLevel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        upgradeable.setLevel(cancel ? oldLevel : newLevel);
    }

    /**
     * @return upgrade
     */
    public Upgradeable getUpgrade() {
        return upgradeable;
    }

    /**
     * @return storage of this upgrades type
     */
    public UpgradeStorage getStorage() {
        return storage;
    }

    /**
     * @return new level
     */
    public double getNewLevel() {
        return upgradeable.getLevel();
    }

    /**
     * @return old level
     */
    public double getOldLevel() {
        return oldLevel;
    }

    /**
     * @return new level (not edited by this event)
     */
    public double getOriginalNewLevel() {
        return newLevel;
    }

    /**
     * @param newLevel
     */
    public void setNewLevel(double newLevel) {
        upgradeable.setLevel(newLevel);
    }

}
