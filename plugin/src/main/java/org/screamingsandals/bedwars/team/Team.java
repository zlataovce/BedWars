package org.screamingsandals.bedwars.team;

import lombok.Data;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.lang.LangKeys;
import org.screamingsandals.bedwars.player.BedWarsPlayer;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.lang.Message;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.utils.InventoryType;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.ArrayList;
import java.util.List;

@Data
public class Team implements Cloneable, org.screamingsandals.bedwars.api.team.Team<Game, TeamColor, LocationHolder, BedWarsPlayer, Container> {
    private Game game;
    private TeamColor color;
    private String name;
    private LocationHolder targetBlockLocation;
    private LocationHolder teamSpawnLocation;
    private int maxPlayers;

    private final List<BedWarsPlayer> players = new ArrayList<>();
    private org.bukkit.scoreboard.Team scoreboardTeam;
    private Container teamChestInventory;
    private List<LocationHolder> chests = new ArrayList<>();
    private Hologram bedHolo;
    private Hologram protectHolo;

    private boolean isBed = true;

    public void prepareForGame() {
        teamChestInventory = ItemFactory.createContainer(InventoryType.CHEST, Message.of(LangKeys.SPECIALS_TEAM_CHEST_NAME).prefixOrDefault(game.getCustomPrefixComponent()).asComponent()).orElseThrow()
    }

    public void destroy() {
        chests.clear();
        players.clear();
        teamChestInventory = null;
    }

    public boolean isDead() {
        return players.isEmpty();
    }

    public boolean isAlive() {
        return !players.isEmpty();
    }

    public boolean hasBedHolo() {
        return this.bedHolo != null;
    }

    public boolean hasProtectHolo() {
        return this.protectHolo != null;
    }

    @Override
    public int countConnectedPlayers() {
        return players.size();
    }

    @Override
    public List<BedWarsPlayer> getConnectedPlayers() {
        return List.copyOf(this.players);
    }

    @Override
    public boolean isPlayerInTeam(BedWarsPlayer player) {
        return this.players.contains(player);
    }

    @Override
    public boolean isTargetBlockStillAlive() {
        return isBed;
    }

    @Override
    public void addTeamChest(Object location) {
        if (location instanceof LocationHolder) {
            if (!chests.contains((LocationHolder) location)) {
                chests.add((LocationHolder) location);
            }
        } else {
            var locationHolder = LocationMapper.wrapLocation(location);
            if (!chests.contains(locationHolder)) {
                chests.add(locationHolder);
            }
        }
    }

    @Override
    public void removeTeamChest(Object location) {
        if (location instanceof LocationHolder) {
            chests.remove((LocationHolder) location);
        } else {
            var locationHolder = LocationMapper.wrapLocation(location);
            chests.remove(locationHolder);
        }
    }

    @Override
    public boolean isTeamChestRegistered(Object location) {
        if (location instanceof LocationHolder) {
            return chests.contains(location);
        } else {
            var locationHolder = LocationMapper.wrapLocation(location);
            return chests.contains(locationHolder);
        }
    }

    @Override
    public int countTeamChests() {
        return chests.size();
    }
}
