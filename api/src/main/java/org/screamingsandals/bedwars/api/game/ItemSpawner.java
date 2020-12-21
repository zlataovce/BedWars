package org.screamingsandals.bedwars.api.game;

import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.upgrades.Upgradeable;
import org.bukkit.Location;

/**
 * @author Bedwars Team
 */
public interface ItemSpawner extends Upgradeable {

    void initialize(Game game);

    void start();

    void stop();

    void changeAmount(int amount);

    void changeInterval(int interval);

    int getCurrentAmount();

    int getCurrentInterval();

    /**
     * @return
     */
    ItemSpawnerType getType();

    /**
     * @return
     */
    Location getSpawnLocation();

    /**
     * @return
     */
    boolean hasCustomName();

    /**
     * @return
     */
    String getCustomName();

    /**
     * @return
     */
    double getStartLevel();

    /**
     * @return
     */
    double getCurrentLevel();

    /**
     * @return
     */
    boolean isHologramEnabled();

    /**
     * @return
     */
    boolean isFloatingEnabled();

    /**
     * Sets team of this upgrade
     *
     * @param team current team
     */
    void setTeam(Team team);

    /**
     *
     * @return registered team for this upgrade
     */
    Team getTeam();

    /**
     * @param level
     */
    void setCurrentLevel(double level);

    default void addToCurrentLevel(double level) {
        setCurrentLevel(getCurrentLevel() + level);
    }

    default String getName() {
        return "spawner";
    }

    default String getInstanceName() {
        return getCustomName();
    }

    default double getLevel() {
        return getCurrentLevel();
    }

    default void setLevel(double level) {
        setCurrentLevel(level);
    }

    default void increaseLevel(double level) {
        addToCurrentLevel(level);
    }

    default double getInitialLevel() {
        return getStartLevel();
    }
}
