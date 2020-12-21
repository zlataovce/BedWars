package org.screamingsandals.bedwars.api.upgrades;

import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.ItemSpawner;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.events.BedwarsUpgradeRegisteredEvent;
import org.screamingsandals.bedwars.api.events.BedwarsUpgradeUnregisteredEvent;
import org.screamingsandals.bedwars.api.game.ItemSpawnerType;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bedwars Team
 */
public final class UpgradeStorage {
    private final String upgradeName;
    private final Class<? extends Upgradeable> upgradeClass;

    private final Map<Game, List<Upgradeable>> upgradeRegistry = new HashMap<>();

    /**
     * @param upgradeName  Upgrade Name
     * @param upgradeClass Upgrade Class
     */
    UpgradeStorage(String upgradeName, Class<? extends Upgradeable> upgradeClass) {
        this.upgradeName = upgradeName;
        this.upgradeClass = upgradeClass;
    }

    /**
     * @return upgrade name
     */
    public String getUpgradeName() {
        return upgradeName;
    }

    /**
     * @return upgrade class type
     */
    public Class<? extends Upgradeable> getUpgradeClass() {
        return upgradeClass;
    }

    /**
     * Register active upgrade in game
     *
     * @param game    Game
     * @param upgradeable Upgrade
     */
    public void addUpgrade(Game game, Upgradeable upgradeable) {
        if (!upgradeClass.isInstance(upgradeable)) {
            return;
        }

        if (!upgradeRegistry.containsKey(game)) {
            upgradeRegistry.put(game, new ArrayList<>());
        }
        if (!upgradeRegistry.get(game).contains(upgradeable)) {
            upgradeable.onUpgradeRegistered(game);
            Bukkit.getPluginManager().callEvent(new BedwarsUpgradeRegisteredEvent(game, this, upgradeable));
            upgradeRegistry.get(game).add(upgradeable);
        }
    }

    /**
     * Unregister active upgrade
     *
     * @param game    Game
     * @param upgradeable Upgrade
     */
    public void removeUpgrade(Game game, Upgradeable upgradeable) {
        if (!upgradeClass.isInstance(upgradeable)) {
            return;
        }

        if (upgradeRegistry.containsKey(game)) {
            if (upgradeRegistry.get(game).contains(upgradeable)) {
                upgradeable.onUpgradeUnregistered(game);
                Bukkit.getPluginManager().callEvent(new BedwarsUpgradeUnregisteredEvent(game, this, upgradeable));
                upgradeRegistry.get(game).remove(upgradeable);
            }
        }
    }

    /**
     * @param game    Game
     * @param upgradeable Upgrade
     * @return true if upgrade is registered
     */
    public boolean isUpgradeRegistered(Game game, Upgradeable upgradeable) {
        if (!upgradeClass.isInstance(upgradeable)) {
            return false;
        }

        return upgradeRegistry.containsKey(game) && upgradeRegistry.get(game).contains(upgradeable);
    }

    /**
     * This is automatically used while game is ending
     *
     * @param game Game
     */
    public void resetUpgradesForGame(Game game) {
        if (upgradeRegistry.containsKey(game)) {
            for (Upgradeable upgradeable : upgradeRegistry.get(game)) {
                upgradeable.onUpgradeUnregistered(game);
                Bukkit.getPluginManager().callEvent(new BedwarsUpgradeUnregisteredEvent(game, this, upgradeable));
            }
            upgradeRegistry.get(game).clear();
            upgradeRegistry.remove(game);
        }
    }

    /**
     * Get all upgrades of this type that is registered in game as active
     *
     * @param game Game
     * @return ĺist of registered upgrades of game
     */
    public List<Upgradeable> getAllUpgradesOfGame(Game game) {
        List<Upgradeable> upgradeable = new ArrayList<>();
        if (upgradeRegistry.containsKey(game)) {
            upgradeable.addAll(upgradeRegistry.get(game));
        }
        return upgradeable;
    }

    /**
     * Find active upgrades with this instanceName
     *
     * @param game         Game
     * @param instanceName name of spawner
     * @return list of upgrades with same name
     */
    @Deprecated
    public List<Upgradeable> findUpgradeByName(Game game, String instanceName) {
        List<Upgradeable> upgradeables = new ArrayList<>();

        if (upgradeRegistry.containsKey(game)) {
            for (Upgradeable upgradeable : upgradeRegistry.get(game)) {
                if (instanceName.equals(upgradeable.getInstanceName())) {
                    upgradeables.add(upgradeable);
                }
            }
        }

        return upgradeables;
    }

    public List<Upgradeable> findItemSpawnerUpgrades(Game game, String spawnerInstanceName) {
        List<Upgradeable> upgradeables = new ArrayList<>();

        if (upgradeRegistry.containsKey(game)) {
            for (Upgradeable upgradeable : upgradeRegistry.get(game)) {
                if (upgradeable instanceof ItemSpawner) {
                    ItemSpawner itemSpawner = (ItemSpawner) upgradeable;

                    if (spawnerInstanceName.equals(itemSpawner.getInstanceName())) {
                        upgradeables.add(itemSpawner);
                    }
                }
            }
        }
        return upgradeables;
    }

    public List<Upgradeable> findItemSpawnerUpgrades(Game game, Team team) {
        List<Upgradeable> upgradeables = new ArrayList<>();

        if (upgradeRegistry.containsKey(game)) {
            for (Upgradeable upgradeable : upgradeRegistry.get(game)) {
                if (upgradeable instanceof ItemSpawner) {
                    ItemSpawner itemSpawner = (ItemSpawner) upgradeable;
                    if (itemSpawner.getTeam() == null) {
                        continue;
                    }

                    if (team.getName().equals(itemSpawner.getTeam().getName())) {
                        upgradeables.add(upgradeable);
                    }
                }
            }
        }
        return upgradeables;
    }

    public List<Upgradeable> findItemSpawnerUpgrades(Game game, Team team, ItemSpawnerType itemSpawnerType) {
        List<Upgradeable> upgradeables = new ArrayList<>();

        if (upgradeRegistry.containsKey(game)) {
            for (Upgradeable upgradeable : upgradeRegistry.get(game)) {
                if (upgradeable instanceof ItemSpawner) {
                    ItemSpawner itemSpawner = (ItemSpawner) upgradeable;
                    if (itemSpawner.getTeam() == null) {
                        continue;
                    }

                    if (team.getName().equals(itemSpawner.getTeam().getName()) && itemSpawnerType.getName().equals(itemSpawner.getType().getName())) {
                        upgradeables.add(upgradeable);
                    }
                }
            }
        }
        return upgradeables;
    }
}
