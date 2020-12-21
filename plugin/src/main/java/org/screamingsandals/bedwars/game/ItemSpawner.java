package org.screamingsandals.bedwars.game;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.config.ConfigurationContainer;
import org.screamingsandals.bedwars.api.events.BedwarsResourceSpawnEvent;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.lib.nms.holograms.Hologram;

import static org.screamingsandals.bedwars.lib.lang.I.i18nonly;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Item;

import com.google.common.base.Preconditions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ItemSpawner implements org.screamingsandals.bedwars.api.game.ItemSpawner {
    public final static String ARMOR_STAND_DISPLAY_NAME_HIDDEN = "BEDWARS_FLOATING_ROT_ENTITY";

    private final List<Item> spawnedItems = new LinkedList<>();
    private final Plugin plugin;
    private final Location spawnLocation;
    private final ItemSpawnerType type;
    private final String customName;
    private final boolean hologramEnabled;
    private final double startLevel;
    private final int maxSpawnedResources;
    private final boolean floatingEnabled;

    private int currentAmount;
    private int currentInterval;
    @Setter
    private Team team;
    @Setter
    private double currentLevel;
    private boolean isFullHologram = false;
    @Setter
    private boolean reRenderHologram = false;
    private double currentLevelOnHologram = -1;
    private ArmorStand floatingGenStand;

    private Game game;
    private Hologram spawnerHologram;
    private BukkitTask spawningTask;
    private BukkitTask hologramTask;
    private boolean started = false;
    private boolean firstTick = true;
    private boolean disabled = false;

    private int elapsedTime;
    private int remainingTimeToSpawn;

    public ItemSpawner(Plugin plugin, Location spawnLocation, ItemSpawnerType type, String customName,
                       boolean hologramEnabled, double startLevel, Team team,
                       int maxSpawnedResources, boolean floatingEnabled) {
        this.plugin = plugin;
        this.spawnLocation = spawnLocation;
        this.type = type;
        this.customName = customName;
        this.hologramEnabled = hologramEnabled;
        this.startLevel = startLevel;
        this.currentAmount = type.getAmount();
        this.currentInterval = type.getInterval();
        this.team = team;
        this.currentLevel = startLevel;
        this.maxSpawnedResources = maxSpawnedResources;
        this.floatingEnabled = floatingEnabled;

        this.remainingTimeToSpawn = currentInterval;
    }

    @Override
    public void initialize(Game game) {
        if (started) {
            return;
        }

        this.game = game;

        final var config = game.getConfigurationContainer();

        if (!config.getOrDefault(ConfigurationContainer.SPAWNER_HOLOGRAMS, Boolean.class, false)) {
            return;
        }

        if (config.getOrDefault(ConfigurationContainer.STOP_TEAM_SPAWNERS_ON_DIE, Boolean.class, false)
                && team != null
                && game.getRunningTeam(team) != null) {
            disabled = true;
            return;
        }

        if (hologramEnabled) {
            Location loc;

            if (floatingEnabled &&
                    Main.getConfigurator().config.getBoolean("floating-generator.enabled", true)) {
                loc = spawnLocation.clone().add(0,
                        Main.getConfigurator().config.getDouble("floating-generator.holo-height", 2.0), 0);
                spawnFloatingStand();
            } else {
                loc = spawnLocation.clone().add(0,
                        Main.getConfigurator().config.getDouble("spawner-holo-height", 0.25), 0);
            }

            spawnerHologram = Main.getHologramManager().spawnHologram(
                    game.getConnectedPlayers(), loc, type.getItemBoldName());

            if (config.getOrDefault(ConfigurationContainer.SPAWNER_COUNTDOWN_HOLOGRAM, Boolean.class, false)) {
                handleHologram();
            }
        }
    }

    @Override
    public void start() {
        if (started || disabled) {
            return;
        }

        started = true;

        if (spawningTask != null) {
            spawningTask.cancel();
            spawningTask = null;
        }

        if (hologramTask != null) {
            hologramTask.cancel();
            hologramTask = null;
        }

        Preconditions.checkNotNull(game, "game");
        final var config = game.getConfigurationContainer();

        spawningTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (firstTick) {
                    return;
                }

                if (config.getOrDefault(ConfigurationContainer.STOP_TEAM_SPAWNERS_ON_DIE, Boolean.class, false)
                        && team != null) {
                    final var spawnerTeam = game.getRunningTeam(team);
                    if (spawnerTeam == null || spawnerTeam.isDead()) {
                        return;
                    }
                }

                final var spawnEvent = new BedwarsResourceSpawnEvent(game, ItemSpawner.this,
                        type.getStack(currentAmount));
                Main.getInstance().getServer().getPluginManager().callEvent(spawnEvent);

                if (spawnEvent.isCancelled()) {
                    return;
                }

                final var resource = spawnEvent.getResource();
                resource.setAmount(nextMaxSpawn(resource.getAmount(), spawnerHologram));

                if (resource.getAmount() > 0) {
                    final var location = spawnLocation.clone().add(0, 0.05, 0);
                    final var item = location.getWorld().dropItem(location, resource);
                    final var spread = type.getSpread();

                    if (spread != 1.0) {
                        item.setVelocity(item.getVelocity().multiply(spread));
                    }

                    item.setPickupDelay(0);
                    spawnedItems.add(item);
                }
            }
        }.runTaskTimer(plugin, 5L, currentInterval * type.getTimeUnit().getTickValue());

        if (hologramEnabled) {
            hologramTask = new BukkitRunnable() {
                @Override
                public void run() {
                    if (firstTick || disabled) {
                        firstTick = false;
                        return;
                    }

                    elapsedTime++;
                    remainingTimeToSpawn = currentInterval - elapsedTime;

                    if (remainingTimeToSpawn == 0) {
                        elapsedTime = 0;
                        remainingTimeToSpawn = currentInterval;
                    }

                    handleHologram();
                }
            }.runTaskTimer(plugin, 5L, 20L);
        }
    }

    @Override
    public void stop() {
        started = false;

        if (spawningTask != null) {
            spawningTask.cancel();
            spawningTask = null;
        }

        if (hologramTask != null) {
            hologramTask.cancel();
            hologramTask = null;
        }

        if (floatingGenStand != null) {
            floatingGenStand.remove();
            floatingGenStand = null;
        }

        if (spawnerHologram != null) {
            spawnerHologram.destroy();
            spawnerHologram = null;
        }

        spawnedItems.clear();
        currentLevel = startLevel;
        elapsedTime = 0;
    }

    @Override
    public void changeAmount(int amount) {
        this.currentAmount = amount;

        stop();
        start();
    }

    @Override
    public void changeInterval(int interval) {
        this.currentInterval = interval;

        stop();
        start();
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    public int nextMaxSpawn(int calculated, Hologram hologram) {
        if (currentLevel <= 0) {
            if (hologram != null && (!isFullHologram || currentLevelOnHologram != currentLevel)) {
                isFullHologram = true;
                currentLevelOnHologram = currentLevel;
                hologram.setLine(1, i18nonly("spawner_not_enough_level").replace("%levels%", String.valueOf((currentLevelOnHologram * (-1)) + 1)));
            }
            return 0;
        }

        if (maxSpawnedResources <= 0) {
            if (isFullHologram && !reRenderHologram) {
                isFullHologram = false;
                reRenderHologram = true;
            }
            return calculated;
        }

        /* Update spawned items */
        spawnedItems.removeIf(Entity::isDead);

        int spawned = spawnedItems.size();

        if (spawned >= maxSpawnedResources) {
            if (hologram != null && !isFullHologram) {
                isFullHologram = true;
                hologram.setLine(1, i18nonly("spawner_is_full"));
            }
            return 0;
        }

        if ((maxSpawnedResources - spawned) >= calculated) {
            if (isFullHologram && !reRenderHologram) {
                reRenderHologram = true;
                isFullHologram = false;
            } else if (hologram != null && (calculated + spawned) == maxSpawnedResources) {
                isFullHologram = true;
                hologram.setLine(1, i18nonly("spawner_is_full"));
            }
            return calculated;
        }

        if (hologram != null && !isFullHologram) {
            isFullHologram = true;
            hologram.setLine(1, i18nonly("spawner_is_full"));
        }

        return maxSpawnedResources - spawned;
    }

    public void add(Item item) {
        if (maxSpawnedResources > 0 && !spawnedItems.contains(item)) {
            spawnedItems.add(item);
        }
    }

    public void remove(Item item) {
        if (maxSpawnedResources > 0 && spawnedItems.contains(item)) {
            spawnedItems.remove(item);
            if (isFullHologram && maxSpawnedResources > spawnedItems.size()) {
                isFullHologram = false;
                reRenderHologram = true;
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void spawnFloatingStand() {
        if (floatingEnabled) {
            floatingGenStand = (ArmorStand) spawnLocation.getWorld().spawnEntity(spawnLocation.clone().add(0,
                    Main.getConfigurator().config.getDouble("floating-generator.generator-height",
                            0.25), 0), EntityType.ARMOR_STAND
            );

            ItemStack helmetStack;
            try {
                //try to get block of item
                final String name = type.getMaterial().name().substring(0, type.getMaterial().name().indexOf("_"));
                helmetStack = new ItemStack(Material.valueOf(name.toUpperCase() + "_BLOCK"));
            } catch (Throwable t) {
                helmetStack = new ItemStack(type.getMaterial());
            }

            floatingGenStand.setHelmet(helmetStack);
            floatingGenStand.setGravity(false);
            floatingGenStand.setVisible(false);
            floatingGenStand.setCustomName(ARMOR_STAND_DISPLAY_NAME_HIDDEN);
        }
    }

    @Override
    public void onUpgradeRegistered(Game game) {

    }

    @Override
    public void onUpgradeUnregistered(Game game) {

    }

    private void handleHologram() {
        switch (type.getTimeUnit()) {
            case TICKS:
                if (currentInterval <= 20) {
                    spawnerHologram.setLine(1,
                            i18nonly("spawner_spawning_every_second"));
                    reRenderHologram = false;
                } else {
                    spawnerHologram.setLine(1,
                            i18nonly("spawner_remaining_seconds").replace("%seconds%",
                                    Integer.toString((int) (remainingTimeToSpawn / type.getTimeUnit().getTickValue()))));
                }
            case MINUTES:
                if (currentInterval <= 1) {
                    spawnerHologram.setLine(1,
                            i18nonly("spawner_spawning_every_minute"));
                    reRenderHologram = false;
                } else {
                    spawnerHologram.setLine(1,
                            i18nonly("spawner_remaining_minutes").replace("%minutes%", Integer.toString(remainingTimeToSpawn)));
                }
                break;
            default:
                if (currentInterval <= 1) {
                    spawnerHologram.setLine(1,
                            i18nonly("spawner_spawning_every_second"));
                    reRenderHologram = false;
                } else {
                    spawnerHologram.setLine(1,
                            i18nonly("spawner_remaining_seconds").replace("%seconds%", Integer.toString(remainingTimeToSpawn)));
                }
                break;
        }
    }
}
