package org.screamingsandals.bedwars.game;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.utils.BungeeUtils;
import org.screamingsandals.bedwars.lib.nms.entity.PlayerUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

public class GamePlayer {
    private final List<Player> hiddenPlayers = new LinkedList<>();
    private final List<ItemStack> permaItemsPurchased = new LinkedList<>();
    private final StoredInventory oldInventory = new StoredInventory();
    @Getter
    private final Player instance;
    private Game game = null;
    private String latestGame = null;
    private ItemStack[] armorContents = null;

    public boolean isSpectator = false;
    public boolean isTeleportingFromGame_justForInventoryPlugins = false;
    public boolean mainLobbyUsed = false;

    public GamePlayer(Player instance) {
        this.instance = instance;
    }

    public void changeGame(Game game) {
        if (this.game != null && game == null) {
            this.game.internalLeavePlayer(this);
            this.game = null;
            this.isSpectator = false;
            this.clean();
            if (Game.isBungeeEnabled()) {
                BungeeUtils.movePlayerToBungeeServer(instance, Main.isDisabling());
            } else {
                this.restoreInv();
            }
        } else if (this.game == null && game != null) {
            this.storeInv();
            this.clean();
            this.game = game;
            this.isSpectator = false;
            this.mainLobbyUsed = false;
            this.game.internalJoinPlayer(this);
            if (this.game != null) {
                this.latestGame = this.game.getName();
            }
        } else if (this.game != null) {
            this.game.internalLeavePlayer(this);
            this.game = game;
            this.isSpectator = false;
            this.clean();
            this.mainLobbyUsed = false;
            this.game.internalJoinPlayer(this);
            if (this.game != null) {
                this.latestGame = this.game.getName();
            }
        }
    }

    public Game getGame() {
        return game;
    }

    public String getLatestGameName() {
        return this.latestGame;
    }

    public boolean isInGame() {
        return game != null;
    }

    public boolean canJoinFullGame() {
        return instance.hasPermission("bw.vip.forcejoin");
    }

    public List<ItemStack> getPermaItemsPurchased() {
        return permaItemsPurchased;
    }

    private void resetPermaItems() {
        this.permaItemsPurchased.clear();
    }

    public void addPermaItem(ItemStack stack) {
        this.permaItemsPurchased.add(stack);
    }

    public void storeInv() {
        oldInventory.inventory = instance.getInventory().getContents();
        oldInventory.armor = instance.getInventory().getArmorContents();
        oldInventory.xp = instance.getExp();
        oldInventory.effects = instance.getActivePotionEffects();
        oldInventory.mode = instance.getGameMode();
        oldInventory.leftLocation = instance.getLocation();
        oldInventory.level = instance.getLevel();
        oldInventory.listName = instance.getPlayerListName();
        oldInventory.displayName = instance.getDisplayName();
        oldInventory.foodLevel = instance.getFoodLevel();
    }

    public void restoreInv() {
        isTeleportingFromGame_justForInventoryPlugins = true;
        if (!mainLobbyUsed) {
            teleport(oldInventory.leftLocation);
        }
        mainLobbyUsed = false;

        instance.getInventory().setContents(oldInventory.inventory);
        instance.getInventory().setArmorContents(oldInventory.armor);

        instance.addPotionEffects(oldInventory.effects);
        instance.setLevel(oldInventory.level);
        instance.setExp(oldInventory.xp);
        instance.setFoodLevel(oldInventory.foodLevel);

        for (PotionEffect e : instance.getActivePotionEffects())
            instance.removePotionEffect(e.getType());

        instance.addPotionEffects(oldInventory.effects);

        instance.setPlayerListName(oldInventory.listName);
        instance.setDisplayName(oldInventory.displayName);

        instance.setGameMode(oldInventory.mode);

        instance.setAllowFlight(oldInventory.mode == GameMode.CREATIVE);

        instance.updateInventory();
        instance.resetPlayerTime();
        instance.resetPlayerWeather();
    }

    public void resetLife() {
        this.instance.setAllowFlight(false);
        this.instance.setFlying(false);
        this.instance.setExp(0.0F);
        this.instance.setLevel(0);
        this.instance.setSneaking(false);
        this.instance.setSprinting(false);
        this.instance.setFoodLevel(20);
        this.instance.setSaturation(10);
        this.instance.setExhaustion(0);
        this.instance.setMaxHealth(20D);
        this.instance.setHealth(this.instance.getMaxHealth());
        this.instance.setFireTicks(0);
        this.instance.setFallDistance(0);
        this.instance.setGameMode(GameMode.SURVIVAL);

        if (this.instance.isInsideVehicle()) {
            this.instance.leaveVehicle();
        }

        for (PotionEffect e : this.instance.getActivePotionEffects()) {
            this.instance.removePotionEffect(e.getType());
        }
    }

    public void invClean() {
        PlayerInventory inv = this.instance.getInventory();
        inv.setArmorContents(new ItemStack[4]);
        inv.setContents(new ItemStack[]{});

        this.armorContents = null;
        this.instance.updateInventory();
    }

    public void clean() {
        invClean();
        resetLife();
        resetPermaItems();
        new ArrayList<>(this.hiddenPlayers).forEach(this::showPlayer);
    }

    public boolean teleport(Location location) {
    	return PlayerUtils.teleportPlayer(instance, location);
    }

    public boolean teleport(Location location, Runnable runnable) {
        return PlayerUtils.teleportPlayer(instance, location, runnable);
    }

    public void hidePlayer(Player player) {
        if (!hiddenPlayers.contains(player) && !player.equals(this.instance)) {
            hiddenPlayers.add(player);
            try {
                this.instance.hidePlayer(Main.getInstance(), player);
            } catch (Throwable t) {
                this.instance.hidePlayer(player);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void showPlayer(Player player) {
        if (hiddenPlayers.contains(player) && !player.equals(this.instance)) {
            hiddenPlayers.remove(player);
            try {
                this.instance.showPlayer(Main.getInstance(), player);
            } catch (Throwable t) {
                this.instance.showPlayer(player);
            }
        }

    }

    public void setGameArmorContents(ItemStack[] armorContents) {
        this.armorContents = armorContents;
    }

    public ItemStack[] getGameArmorContents() {
        return armorContents;
    }

}
