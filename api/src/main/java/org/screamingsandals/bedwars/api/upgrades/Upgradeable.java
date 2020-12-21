package org.screamingsandals.bedwars.api.upgrades;

import org.screamingsandals.bedwars.api.game.Game;

/**
 * @author Bedwars Team
 *
 */
public interface Upgradeable {
	
	/**
	 * 
	 * @return registered name of this upgrade
	 */
	String getName();
	
	/**
	 * 
	 * @return identification of this upgrade instance
	 */
	String getInstanceName();
	
	/**
	 * 
	 * @return current level of upgrade
	 */
	double getLevel();
	
	/**
	 * Sets level of this upgrade
	 * 
	 * @param level Current level
	 */
	void setLevel(double level);
	
	/**
	 * Add levels to this upgrade
	 * 
	 * @param level Levels that will be added to current level
	 */
	void increaseLevel(double level);
	
	/**
	 * 
	 * @return initial level of upgrade
	 */
	double getInitialLevel();

	/**
	 * Called when upgrade is registered
	 * 
	 * @param game Game when upgrade is activated
	 */
	void onUpgradeRegistered(Game game);
	
	/**
	 * Called when upgrade is unregistered
	 * 
	 * @param game Game when upgrade is deactivated
	 */
	void onUpgradeUnregistered(Game game);
}
