package authoring;

import game_object.framework.Game;
import game_object.level.Level;

/**
 * Supports creating and modifying game date
 * @author
 *
 */
public interface IAuthorEnvironment {

	/**
	 * add a game to the scene that the user
	 * can modify
	 * @param game interface
	 */
	void addGame(Game game);
	
	/**
	 * Set which game that is open should
	 * be in the front and able for the user
	 * to interact with
	 * @param game interface
	 */
	void setCurrentGame(int index);
	
	/**
	 * Returns the game that the 
	 * user is currently interacting with/
	 * modifying
	 * @return game interface
	 */
	Game getCurrentGame();
		
	/**
	 * add a certain level to the current game
	 * @param level
	 */
	void addLevel(Level level);
	
	/**
	 * set a level to be the one able to
	 * modify and have the user interact
	 * @param level interface
	 */
	void setCurrentLevel(int index);
	
	/**
	 * returns the current level that the user
	 * is interacting with
	 * @return level interface
	 */
	Level getCurrentLevel();
	
	void load();
	
	void setLanguage(String lang);
}