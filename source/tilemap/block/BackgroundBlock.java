package Game.StrategyDemo.source.tilemap.block;

import Game.StrategyDemo.source.utils.BoundingBox;
import javafx.scene.image.Image;
import javafx.util.Pair;

public class BackgroundBlock extends Block {
    
    public BackgroundBlock(Image image, int row, int col, int posX, int posY, int width, int height, boolean canTouch) {
        super(image, row, col, posX, posY, width, height, canTouch);
    }

    public BackgroundBlock(BoundingBox[] boxes, Image image, int row, int col, int posX, int posY, int width, int height, boolean canTouch) {
        super(boxes, image, row, col, posX, posY, width, height, canTouch);
    }

    public BackgroundBlock(BoundingBox[] boxes, Pair<Integer[], Integer> frameList, int columns, Image image, int row, int col, int posX, int posY, int width, int height, boolean canTouch) { 
        super(boxes, frameList, columns, image, row, col, posX, posY, width, height, canTouch);
    }

    @Override
    public boolean isInsideMap() {
        return canTouch;
    }
}
