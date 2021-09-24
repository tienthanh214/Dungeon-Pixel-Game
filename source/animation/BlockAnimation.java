package Game.StrategyDemo.source.animation;

import Game.StrategyDemo.source.tilemap.block.Block;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.util.Duration;
/**
 * animation read from tmx tile map file
 */
public class BlockAnimation extends Transition {
    private Integer[] frameList;
    private Block block;
    private int columns;
    private int width, height;

    private int lastIndex = -1;

    public BlockAnimation(Image image, int width, int height, int columns, Block block, Integer[] frameList, int duration) {
        this.block = block;
        this.width = width;
        this.height = height;
        this.columns = columns;
        this.frameList = frameList;
        setCycleDuration(Duration.millis(duration));
        setCycleCount(Animation.INDEFINITE);
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double frac) {
        int index = Math.min((int)(Math.floor(frac * frameList.length)), frameList.length - 1);
        if (index == lastIndex)
            return;
        int x = (frameList[index]) / columns;
        int y = (frameList[index]) % columns;
        block.imageView.setViewport(new Rectangle2D(y * width, x * height, width, height));
        lastIndex = index;
    }

}
