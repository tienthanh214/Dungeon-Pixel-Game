package Game.StrategyDemo.source.animation;

import Game.StrategyDemo.source.entity.Entity;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {
    private final Entity character; // show
    private final Image[] imageData; // in this assets: 4 idle - 4 run - 1 hit
    private final int count; // general number animate of 1 action
    private final int columns; // general image: number of action in a row
    private int offset; // identify kind of action
    private boolean direction; // true: original | false: flip

    // private final int offsetX; // using this when have general image
    // private final int offsetY;
    // private final int width; 
    // private final int height;
    Scale scale = new Scale();

    public SpriteAnimation( Entity character, Image[] imageData, 
                            Duration duration, int count, 
                            int columns, int offset,
                            boolean direction) {
        this.character = character;
        this.imageData = imageData;
        this.count = count;
        this.columns = columns;
        this.offset = offset;
        this.direction = direction;
        // this.offsetX = offsetX;
        // this.offsetY = offsetY;
        // this.width = width;
        // this.height = height;
        setCycleDuration(duration);
        setCycleCount(Animation.INDEFINITE);
        setInterpolator(Interpolator.LINEAR);
        scale.setPivotX(imageData[0].getWidth() / 2);
        scale.setX(-1);
    }
    // animation
    protected void interpolate(double frac) {
        /* offset:
                0 -> idle [0, 1]
                1 -> run [4, 5, 6, 7]
                2 -> attack [8, 0]
                3 -> collect [0, 1, 2, 3]
        */
        // System.err.println(frac);
        final int index;
        switch (offset) {
            case 0: index = Math.min((int)(Math.floor(frac * 2)), 1); break;
            case 1: index = Math.min((int)(Math.floor(frac * count)), count - 1) + columns; break;
            case 2: index = ((int)(Math.floor(frac * count)) < 3) ? 8 : 0; break;
            case 3: index = Math.min((int)(Math.floor(frac * count)), 3); break;
            default: index = 0;
        }
        // System.err.println(frac + " " + offset + " " + index);
        character.imageView.setImage(imageData[index]);
        
        
        if (direction) {
            if (!character.getTransforms().contains(scale))
                character.getTransforms().add(scale);
        } else {
            if (character.getTransforms().contains(scale))
                character.getTransforms().remove(scale);
        }
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

}
