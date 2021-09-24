package Game.StrategyDemo.source.utils;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoundingBox extends Rectangle {
    
    public BoundingBox(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.setFill(Color.TRANSPARENT);
        // this.setOpacity(0.6);
        // this.setStroke(Color.GREEN);
        // this.setStrokeWidth(2.0);
    }

    public BoundingBox(double x, double y, double width, double height, double extendRange) {
        this(x - extendRange, y - extendRange, width + 2 * extendRange, height + 2 * extendRange);
    } 

    public double getMidX() {
        return this.getX() + this.getWidth() / 2d; 
    }
    public double getMidY() {
        return this.getY() + this.getHeight() / 2d;
    }

}
