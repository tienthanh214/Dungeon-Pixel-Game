package Game.StrategyDemo.source.tilemap.block;

import Game.StrategyDemo.source.GameObject;
import Game.StrategyDemo.source.animation.BlockAnimation;
import Game.StrategyDemo.source.utils.BoundingBox;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

public abstract class Block extends GameObject {
    // private int width;
    // private int height;
    protected boolean canTouch;
    public Point2D pos; // position by column and row
    
    public ImageView imageView;
    private BlockAnimation animation;
    
    public Block(Image image, int row, int col, int posX, int posY, int width, int height, boolean canTouch) {
        // this.width = width;
        // this.height = height;
        this.canTouch = canTouch;
        this.animation = null;
        this.pos = new Point2D(posX, posY);
        this.imageView = new ImageView(image);
        this.imageView.setViewport(new Rectangle2D(col * width, row * height, width, height));
        this.getChildren().add(this.imageView);
    }
    /**
     * 
     * @param boxes bounding box in tmx format (relative position), need to be absolute position
     * @param image image tile set source
     * @param row row index in tile set
     * @param col col index in tile set
     * @param posX x in scene
     * @param posY y in scene
     * @param width tile width
     * @param height tile height
     * @param canTouch can player access this tile?
     */
    public Block(BoundingBox[] boxes, Image image, int row, int col, int posX, int posY, int width, int height, boolean canTouch) {
        this(image, row, col, posX, posY, width, height, canTouch);
        if (boxes == null) {
            this.bounds = new BoundingBox[1];
            this.bounds[0] = new BoundingBox(posX * width, posY * height, width, height);
        } else {
            this.bounds = new BoundingBox[boxes.length];
            for (int i = 0; i < boxes.length; ++i) { // * cho factor
                bounds[i] = new BoundingBox(posX * width + boxes[i].getX(), posY * height + boxes[i].getY(), boxes[i].getWidth(), boxes[i].getHeight());
            }
            // this.bounds = boxes;
        }
    }

    public Block(BoundingBox[] boxes, Pair<Integer[], Integer> frameList, int columns, Image image, int row, int col, int posX, int posY, int width, int height, boolean canTouch) {
        this(boxes, image, row, col, posX, posY, width, height, canTouch);
        if (frameList != null) {
            this.animation = new BlockAnimation(image, width, height, columns, this, frameList.getKey(), frameList.getValue());
            this.animation.play();
        }
    }

    public Point2D getPosition() {
        return this.pos;
    }

    public abstract boolean isInsideMap(); // can access

    @Override
    public boolean isCollision(GameObject player) {
        for (BoundingBox thisBox : this.bounds) {
            if (thisBox.intersects(player.localToParent(player.bounds[0].getBoundsInParent())))
                return true;
        }
        return false;
    }

    public boolean isCollision(double minX, double minY, double width, double height) {
        if (bounds[0].getWidth() + bounds[0].getHeight() == 0)
            return false;
        if (Math.max(minX, bounds[0].getX()) >= Math.min(minX + width, bounds[0].getX() + bounds[0].getWidth()))
            return false;
        if (Math.max(minY, bounds[0].getY()) >= Math.min(minY + height, bounds[0].getY() + bounds[0].getHeight()))
            return false;
        return true;
    }
}
