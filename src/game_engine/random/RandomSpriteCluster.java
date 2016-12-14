package game_engine.random;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import game_object.core.Dimension;
import game_object.core.ISprite;
import game_object.core.Position;


public class RandomSpriteCluster {

    private List<SpriteInfo> mySprites;

    private double myXRange, myYRange;
    private double myRepeatTime, myCurrentTime;

    public RandomSpriteCluster (double xRange, double yRange, double repeatTime) {
        myXRange = xRange;
        myYRange = yRange;
        myRepeatTime = repeatTime;
        myCurrentTime = 0;
        mySprites = new ArrayList<SpriteInfo>();
    }

    public void addSprite (SpriteInfo spriteInfo) {
        mySprites.add(spriteInfo);
    }

    public boolean shouldRender (double elapsedTime) {
        System.out.println(elapsedTime);
        System.out.println(myCurrentTime);
        myCurrentTime += elapsedTime;
        if (myCurrentTime >= myRepeatTime) {
            myCurrentTime = 0;
            return true;
        }
        return false;
    }

    public List<ISprite> getSprites () {
        List<ISprite> createdSprites = new ArrayList<ISprite>();
        double randomX = new Random().nextDouble()*myXRange;
        double randomY = new Random().nextDouble()*myYRange;
        Position offset = new Position(randomX, randomY);
        for (SpriteInfo si : mySprites) {
            Class<? extends ISprite> c = si.getSpriteClass();
            List<String> imagePaths = si.getImagePaths();
            Dimension dim = si.getDimension();
            try {
                Constructor<? extends ISprite> ctor =
                        c.getConstructor(Position.class, Dimension.class, List.class);
                Position pos = new Position(si.getRelativePosition().getX(), si.getRelativePosition().getY());
                pos.addPosition(offset);
                ISprite sprite = ctor.newInstance(pos, dim, imagePaths);
                
                createdSprites.add(sprite);
            }
            catch (Exception e) {

            }
        }
        return createdSprites;
    }

}