package game_object;


import java.util.ArrayList;

import game_object.acting.ActionTrigger;
import game_object.acting.KeyEvent;
import game_object.block.StaticBlock;
import game_object.character.Enemy;
import game_object.character.Hero;
import game_object.core.Dimension;
import game_object.core.Position;
import game_object.core.Velocity;
import game_object.level.Level;
import javafx.scene.input.KeyCode;

/**
 * Use this generator to get a Level with some predefined sprites.
 * @author Jay
 */
public class LevelGenerator {
	
	/**
	 * A hero in the air, and a big chalk of block as ground.
	 * The hero can move left, right, and jump.
	 * @return
	 */
	public static Level getTestLevelA() {
		ArrayList<String> heroImages = new ArrayList<>();
		heroImages.add("data/img/blue_snail.png");
		
		ArrayList<String> enemyImages = new ArrayList<>();
		enemyImages.add("data/img/eliza.png");
		
		ArrayList<String> blockImages = new ArrayList<>();
		blockImages.add("data/img/stone_block.png");
		
		Level level = new Level();
		Hero hero = new Hero(new Position(30, 30), new Dimension(40, 60), heroImages);
		hero.setVelocity(new Velocity(50,0));
		Enemy enemy = new Enemy(new Position(300,400),new Dimension(40, 60), enemyImages);
		
		StaticBlock smackDown = new StaticBlock(new Position(340, 50), new Dimension(100, 200), blockImages);
		StaticBlock ground = new StaticBlock(new Position(0, 500), new Dimension(2000, 500), blockImages);
		
		level.addHero(hero);
		level.addEnemy(enemy);
		level.addStaticBlock(ground);
		level.addStaticBlock(smackDown);
		
		KeyEvent leftEvent = new KeyEvent(KeyCode.LEFT);
		KeyEvent rightEvent = new KeyEvent(KeyCode.RIGHT);
		KeyEvent spaceBarEvent = new KeyEvent(KeyCode.SPACE);
		
		level.getTriggers().add(new ActionTrigger(hero, leftEvent, "moveLeft"));
		level.getTriggers().add(new ActionTrigger(hero, rightEvent, "moveRight"));
		level.getTriggers().add(new ActionTrigger(hero, spaceBarEvent, "jumpUp"));
		
		return level;
	}
	
	/**
	 * A hero in the air but with a xVelocity, and a big chalk of block as ground.
	 * The hero can move left, right, and jump.
	 */
	public static Level getTestLevelB() {
		ArrayList<String> heroImages = new ArrayList<>();
		heroImages.add("data/img/slime.png");
		
		ArrayList<String> blockImages = new ArrayList<>();
		blockImages.add("data/img/stone_block.png");
		
		Level level = new Level();
		Hero hero = new Hero(new Position(30, 30), new Dimension(40, 60), heroImages);
		hero.setVelocity(new Velocity(50, 0));
		StaticBlock ground = new StaticBlock(new Position(0, 500), new Dimension(2000, 500), blockImages);
		
		level.addHero(hero);
		level.addStaticBlock(ground);
		
		KeyEvent leftEvent = new KeyEvent(KeyCode.LEFT);
		KeyEvent rightEvent = new KeyEvent(KeyCode.RIGHT);
		KeyEvent spaceBarEvent = new KeyEvent(KeyCode.SPACE);
		
		level.getTriggers().add(new ActionTrigger(hero, leftEvent, "moveLeft"));
		level.getTriggers().add(new ActionTrigger(hero, rightEvent, "moveRight"));
		level.getTriggers().add(new ActionTrigger(hero, spaceBarEvent, "jumpUp"));
		
		return level;
	}
	
	
	
}
