package game_object;
import java.util.ArrayList;

import game_object.acting.ActionName;
import game_object.acting.ActionTrigger;
import game_object.acting.KeyEvent;
import game_object.block.Block;
import game_object.character.Enemy;
import game_object.character.Hero;
import game_object.constants.DefaultConstants;
import game_object.constants.GameObjectConstants;
import game_object.core.Dimension;
import game_object.core.Game;
import game_object.core.ImageStyle;
import game_object.core.Position;
import game_object.core.Velocity;
import game_object.level.Level;
import game_object.powerup.IPowerUp;
import game_object.powerup.NewWeaponPowerUp;
import game_object.weapon.ProjectileModel;
import game_object.weapon.WeaponModel;
import javafx.scene.input.KeyCode;
/**
 * Use this generator to get a Level with some predefined sprites.
 * @author Jay
 */
public class LevelGenerator {
	
	private static Level levelA;
	private static Level levelB;
	private static Game game = new Game("StaticGame");
	
	public static Game getTestGame() {
		if (game.getAllLevelsReadOnly().size() < 2) game = initTestGame();
		return game;
	}
	public static Level getTestLevelA() {
		if (levelA == null) levelA = initTestLevelA();
		return levelA;
	}
	
	public static Level getTestLevelB() {
		if (levelB == null) levelB = initTestLevelB();
		return levelB;
	}
	
	private static Game initTestGame() {
		Game game = new Game("AGeneratedGame");
		game.addLevel(getTestLevelA());
		game.addLevel(getTestLevelB());
		game.setCurrentLevel(getTestLevelA());
		game.setFirstSceneAsLevel(game.getCurrentLevel());
		return game;
	}
	/**
	 * A hero in the air, and a big chalk of block as ground.
	 * The hero can move left, right, jump, (and upon acquiring power-up, can shoot).
	 * @return
	 */
	private static Level initTestLevelA() {
		ArrayList<String> heroImages = new ArrayList<>();
		heroImages.add(GameObjectConstants.BLUE_SNAIL_FILE);
		ArrayList<String> enemyImages = new ArrayList<>();
		enemyImages.add(GameObjectConstants.ORANGE_MUSHROOM_FILE);
		ArrayList<String> blockImages = new ArrayList<>();
		blockImages.add(GameObjectConstants.MARIO_GROUND_FILE);
		levelA = new Level(game, "TestLevelA");
		

                levelA.getBoundary().getDimension().setWidth(game.getScreenSize().getWidth());
                levelA.getBoundary().getDimension().setHeight(game.getScreenSize().getHeight());
		
		Hero hero = new Hero(new Position(165, 100), new Dimension(40, 40), heroImages);
		hero.setVelocity(new Velocity(0, 0));
		hero.setImageStyle(ImageStyle.FIT);
		hero.setWeaponDisplacementX(40);
		hero.setWeaponDisplacementY(10);
		
		ArrayList<String> fwpuImg = new ArrayList<String>();
		fwpuImg.add(GameObjectConstants.NEW_WEAPON_POWER_UP_FILE);
		IPowerUp fastWeaponPowerUp = new NewWeaponPowerUp(
			new Position(300, 100),
			new Dimension(40, 40),
			fwpuImg
		);
		levelA.addSprite(fastWeaponPowerUp);
		
		Enemy enemy = new Enemy(new Position(300,400),new Dimension(40, 60), enemyImages);
		enemy.setImageStyle(ImageStyle.FIT);
		enemy.setHasAI(true);

		Block smackDown = new Block(new Position(340, 50), new Dimension(100, 200), blockImages);
		smackDown.setImageStyle(ImageStyle.TILE);
		Block ground = new Block(new Position(0, 500), new Dimension(2000, 500), blockImages);
		ground.setImageStyle(ImageStyle.TILE);
		Block goalBlock = new Block(new Position(400,450),new Dimension(50,50),blockImages);
		// NewWeaponPowerUp powerUp = new NewWeaponPowerUp(new Position(), dimension, imagePaths, w)
		levelA.addSprite(hero);
		levelA.addSprite(enemy);
		levelA.addSprite(ground);
		levelA.addSprite(smackDown);
		levelA.addSprite(goalBlock);
		//AbstractGoal goal = new ReachPointGoal(hero,new Position(400,450));
		//levelA.getAllGoals().add(goal);
		levelA.setNextLevel(getTestLevelB());
		KeyEvent leftEvent = new KeyEvent(KeyCode.A);
		KeyEvent rightEvent = new KeyEvent(KeyCode.D);
		KeyEvent spaceBarEvent = new KeyEvent(KeyCode.W);
		KeyEvent shootEvent = new KeyEvent(KeyCode.J);
		levelA.getAllTriggers().add(new ActionTrigger(leftEvent, hero, ActionName.MOVE_LEFT));
		levelA.getAllTriggers().add(new ActionTrigger(rightEvent, hero, ActionName.MOVE_RIGHT));
		levelA.getAllTriggers().add(new ActionTrigger(spaceBarEvent, hero, ActionName.JUMP));
		levelA.getAllTriggers().add(new ActionTrigger(shootEvent, hero, ActionName.SHOOT));
		levelA.init();
		return levelA;
	}
	/**
	 * A hero in the air but with a xVelocity, and a big chalk of block as ground.
	 * The hero can move left, right, and jump.
	 */
	private static Level initTestLevelB() {
		ArrayList<String> blockImages = new ArrayList<>();
		blockImages.add(GameObjectConstants.MARIO_GROUND_FILE);
		levelB = new Level(game, "TestLevelB");
		levelB.getBoundary().getDimension().setWidth(game.getScreenSize().getWidth());
		levelB.getBoundary().getDimension().setHeight(game.getScreenSize().getHeight());
		levelB.replaceAllHerosAndTriggersWithLevel(levelA);
		Block ground = new Block(new Position(0, 500), new Dimension(2000, 200), blockImages);
		ground.setImageStyle(ImageStyle.TILE);
		levelB.addSprite(ground);
		levelB.init();
		return levelB;
	}
	
}