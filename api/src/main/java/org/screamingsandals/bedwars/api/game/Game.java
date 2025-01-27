package org.screamingsandals.bedwars.api.game;

import net.kyori.adventure.text.Component;
import org.bukkit.WeatherType;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.bedwars.api.ArenaTime;
import org.screamingsandals.bedwars.api.Region;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.boss.StatusBar;
import org.screamingsandals.bedwars.api.config.ConfigurationContainer;
import org.screamingsandals.bedwars.api.player.BWPlayer;
import org.screamingsandals.bedwars.api.special.SpecialItem;
import org.screamingsandals.bedwars.api.utils.DelayFactory;
import org.screamingsandals.lib.utils.Wrapper;

import java.io.File;
import java.util.List;


/**
 * @author Bedwars Team
 */
public interface Game<P extends BWPlayer, B extends Wrapper, L extends Wrapper, W extends Wrapper, C extends Wrapper, E extends Wrapper> {
    /**
     * @return Arena name
     */
	String getName();

    /**
     * @return GameStatus of the arena
     */
	GameStatus getStatus();

    /**
     *
     */
	void start();

    /**
     *
     */
	void stop();

    /**
     * @return true if GameStatus is different than DISABLED
     */
    default boolean isActivated() {
        return getStatus() != GameStatus.DISABLED;
    }

    // PLAYER MANAGEMENT

    /**
     * @param player
     */
	void joinToGame(L player);

    /**
     * @param player
     */
	void leaveFromGame(L player);

    /**
     * @param player
     * @param team
     */
	void selectPlayerTeam(L player, Team team);

    /**
     * @param player
     */
	void selectPlayerRandomTeam(L player);

    /**
     * @return defined world of the game
     */
	W getGameWorld();

    /**
     * @return
     */
	C getPos1();

    /**
     * @return
     */
	C getPos2();

    /**
     * @return
     */
	C getSpectatorSpawn();

    /**
     * @return configured time of the game
     */
	int getGameTime();

    /**
     * @return configured minimal players to start the game
     */
	int getMinPlayers();

    /**
     * @return configured maximal players of the arena
     */
	int getMaxPlayers();

    /**
     * @return players in game
     */
	int countConnectedPlayers();

    /**
     * @return list of players in game
     */
	List<L> getConnectedPlayers();

    /**
     * @return list of game stores
     */
	List<GameStore> getGameStores();

    /**
     * @return
     */
	int countGameStores();

    /**
     * @return Team instance from the name
     */
    Team getTeamFromName(String name);

    /**
     * @return
     */
	List<Team> getAvailableTeams();

    /**
     * @return
     */
	int countAvailableTeams();

    /**
     * @return
     */
	List<RunningTeam> getRunningTeams();

    /**
     * @return
     */
	int countRunningTeams();

    /**
     * @param player
     * @return
     */
	RunningTeam getTeamOfPlayer(L player);

    /**
     * @param player
     * @return
     */
	boolean isPlayerInAnyTeam(L player);

    /**
     * @param player
     * @param team
     * @return
     */
	boolean isPlayerInTeam(L player, RunningTeam team);

    /**
     * @param location
     * @return
     */
	boolean isLocationInArena(C location);

    /**
     * @param location
     * @return
     */
	boolean isBlockAddedDuringGame(Object location);

    /**
     * @return
     */
	List<SpecialItem> getActivedSpecialItems();

    /**
     * @param type
     * @return
     */
	List<SpecialItem> getActivedSpecialItems(Class<? extends SpecialItem> type);

    /**
     * @param team
     * @return
     */
	List<SpecialItem> getActivedSpecialItemsOfTeam(Team team);

    /**
     * @param team
     * @param type
     * @return
     */
	List<SpecialItem> getActivedSpecialItemsOfTeam(Team team, Class<? extends SpecialItem> type);

    /**
     * @param team
     * @return
     */
	SpecialItem getFirstActivedSpecialItemOfTeam(Team team);

    /**
     * @param team
     * @param type
     * @return
     */
	SpecialItem getFirstActivedSpecialItemOfTeam(Team team, Class<? extends SpecialItem> type);

    /**
     * @param player
     * @return
     */
	List<SpecialItem> getActivedSpecialItemsOfPlayer(L player);

    /**
     * @param player
     * @param type
     * @return
     */
	List<SpecialItem> getActivedSpecialItemsOfPlayer(L player, Class<? extends SpecialItem> type);

    /**
     * @param player
     * @return
     */
	SpecialItem getFirstActivedSpecialItemOfPlayer(L player);

    /**
     * @param player
     * @param type
     * @return
     */
	SpecialItem getFirstActivedSpecialItemOfPlayer(L player, Class<? extends SpecialItem> type);

    /**
     * @return
     */
    List<DelayFactory> getActiveDelays();

    /**
     * @param player
     * @return
     */
    List<DelayFactory> getActiveDelaysOfPlayer(GameParticipant player);

    /**
     * @param player
     * @param specialItem
     * @return
     */
    DelayFactory getActiveDelay(GameParticipant player, Class<? extends SpecialItem> specialItem);

    /**
     * @param delayFactory
     */
    void registerDelay(DelayFactory delayFactory);

    /**
     * @param delayFactory
     */
    void unregisterDelay(DelayFactory delayFactory);

    /**
     * @param player
     * @param specialItem
     * @return
     */
    boolean isDelayActive(GameParticipant player, Class<? extends SpecialItem> specialItem);

    /**
     * @param item
     */
	void registerSpecialItem(SpecialItem item);

    /**
     * @param item
     */
	void unregisterSpecialItem(SpecialItem item);

    /**
     * @param item
     * @return
     */
	boolean isRegisteredSpecialItem(SpecialItem item);

    /**
     * @return
     */
	List<ItemSpawner> getItemSpawners();

    /**
     * @return
     */
	Region<B> getRegion();

    /**
     * @return
     */
	StatusBar<?> getStatusBar();

    // LOBBY

    /**
     * @return
     */
	W getLobbyWorld();

    /**
     * @return
     */
	C getLobbySpawn();

    /**
     * @return
     */
	int getLobbyCountdown();

    /**
     * @return
     */
	int countTeamChests();

    /**
     * @param team
     * @return
     */
	int countTeamChests(RunningTeam team);

    /**
     * @param location
     * @return
     */
	RunningTeam getTeamOfChest(C location);

    /**
     * @param block
     * @return
     */
	RunningTeam getTeamOfChestBlock(B block);

    /**
     * @param entity
     * @return
     */
	boolean isEntityShop(E entity);

    /**
     * @return
     */
	boolean getBungeeEnabled();

    /**
     * @return
     */
	ArenaTime getArenaTime();

    /**
     * @return
     */
	WeatherType getArenaWeather();

    /**
     * @return
     */
	BarColor getLobbyBossBarColor();

    /**
     * @return
     */
	BarColor getGameBossBarColor();

    /**
     * @return
     */
    boolean isProtectionActive(P player);

    int getPostGameWaiting();

    default boolean hasCustomPrefix() {
        return getCustomPrefix() != null;
    }

    String getCustomPrefix();

    /**
     * Returns configuration container for this game
     *
     * @return game's configuration container
     * @since 0.3.0
     */
    ConfigurationContainer getConfigurationContainer();

    /**
     * Checks if game is in edit mode
     *
     * @return true if game is in edit mode
     * @since 0.3.0
     */
    boolean isInEditMode();

    /**
     * This methods allows you to save the arena to config (useful when using custom config options)
     *
     * @since 0.3.0
     */
    void saveToConfig();

    /**
     * Gets file with this game
     *
     * @since 0.3.0
     * @return file where game is saved
     */
    File getFile();

    /**
     * @since 0.3.0
     * @return
     */
    Component getCustomPrefixComponent();

    /**
     * @since 0.3.0
     * @return
     */
    @Nullable C getLobbyPos1();

    /**
     * @since 0.3.0
     * @return
     */
    @Nullable C getLobbyPos2();
}
