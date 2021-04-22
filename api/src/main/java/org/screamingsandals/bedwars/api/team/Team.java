package org.screamingsandals.bedwars.api.team;

import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.player.BWPlayer;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.List;

/**
 * @author Bedwars Team
 */
public interface Team<G extends Game, T extends TeamColor, L extends Wrapper, P extends BWPlayer, C extends Wrapper> {
    G getGame();

    String getName();

    T getColor();

    L getTeamSpawnLocation();

    L getTargetBlockLocation();

    int getMaxPlayers();

    int countConnectedPlayers();

    List<P> getConnectedPlayers();

    boolean isPlayerInTeam(P player);

    boolean isDead();

    boolean isAlive();

    boolean isTargetBlockStillAlive();

    @Deprecated
    org.bukkit.scoreboard.Team getScoreboardTeam();

    void addTeamChest(Object location);

    void removeTeamChest(Object location);

    boolean isTeamChestRegistered(Object location);

    C getTeamChestInventory();

    int countTeamChests();
}
