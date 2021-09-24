package Game.StrategyDemo.source;

import Game.StrategyDemo.source.utils.BoundingBox;
import javafx.scene.layout.Pane;

public abstract class GameObject extends Pane {
    public BoundingBox[] bounds;

    public boolean isCollision(GameObject other) {
        // null -> default : that pane bound
        if (other.bounds == null) {
            if (this.bounds == null) {
                return this.getBoundsInParent().intersects(other.getBoundsInParent());
            } else {
                for (BoundingBox thisBox : this.bounds) 
                    if (this.localToParent(thisBox.getBoundsInParent()).intersects(other.getBoundsInParent()))
                        return true;
            }
            return false;
        }

        for (BoundingBox thisBox : this.bounds) {
            for (BoundingBox otherBox : other.bounds) {
                if (this.localToParent(thisBox.getBoundsInParent())
                            .intersects(other.localToParent(otherBox.getBoundsInParent()))) {
                                return true;
                }
            }
        }
        return false;
    }
    /*  distance from center to center 
        (assume just get distance between 1 bounding box object)
    */
    public double getDistance(GameObject other) {
        double x1, y1, x2, y2;
        if (this.bounds == null) {
            x1 = this.getBoundsInParent().getCenterX();
            y1 = this.getBoundsInParent().getCenterY();
        } else {
            x1 = this.localToParent(this.bounds[0].getBoundsInParent()).getCenterX();
            y1 = this.localToParent(this.bounds[0].getBoundsInParent()).getCenterY();            
        }

        if (other.bounds == null) {
            x2 = other.getBoundsInParent().getCenterX();
            y2 = other.getBoundsInParent().getCenterY();
        } else {
            x2 = other.localToParent(other.bounds[0].getBoundsInParent()).getCenterX();
            y2 = other.localToParent(other.bounds[0].getBoundsInParent()).getCenterY();
        }
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
