package Game.StrategyDemo.source;

import java.util.ArrayList;

import Game.StrategyDemo.source.entity.Entity;
import Game.StrategyDemo.source.utils.CheckInsideRange;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;

/**
 * Camera at the center of the world
 */

public class Camera {
    private int width, height; // size of scene
    private int mapLimitWidth, mapLimitHeight; // size of whole map
    private DoubleProperty posX; // (left screen position) in of whole map
    private DoubleProperty posY; // (upper screen position) in of whole map
    private Point2D collisionPos; // save and use for locked camera

    Entity target;
    private ArrayList<GameObject> objectList;

    private enum LockType {
        NONE, WALL_LOCK, USER_LOCK;
    }
    private LockType locked;

    public Camera() {
        this(null, 0, 0, 0, 0, 0, 0);
    }

    public Camera(Entity entity, double x, double y, int width, int height, int mapLimitWidth, int mapLimitHeight) {
        this.posX = new SimpleDoubleProperty(this, "posX", x);
        this.posY = new SimpleDoubleProperty(this, "posY", y);
        locked = LockType.NONE;
        this.width = width;
        this.height = height;
        this.mapLimitWidth = mapLimitWidth;
        this.mapLimitHeight = mapLimitHeight;
        this.target = null;
        this.setTarget(entity);
        System.out.println("yeah");
        objectList = new ArrayList<>();
    }

    /* =============== Management Screen =================== */

    public void setTarget(Entity entity) {
        if (this.target != null) {
            this.objectList.remove(entity);
            this.objectList.add(target);
            target.setCamera(null);
        }
        entity.setCamera(this);
        this.target = entity;
        posX.set(target.getLayoutX() - width / 2);
        posY.set(target.getLayoutY() - height / 2);
        // collisionPos = collisionPos.multiply(0).add(posX.get(), posY.get());
        collisionPos = new Point2D(posX.get(), posY.get());

    }

    public void registerObserver(GameObject object) {
        this.objectList.add(object);
    }

    public void removeObserver(GameObject object) {
        this.objectList.remove(object);
    }

    // ============= for rendering =============

    public void render() {
        System.out.println("Camera: ");
        System.out.println(this.posX.get() + " " + this.posY.get());
        System.out.println(target.localToParent(target.bounds[0].getBoundsInParent()));
        System.out.println(this.target.getLayoutX() + " " + this.target.getLayoutY());
    }

    public void move(int x, int y) {
        collisionPos = collisionPos.add(x, y);
        if (this.locked == LockType.USER_LOCK) 
            return; 
        final double newX = posX.get() + x;
        final double newY = posY.get() + y;
        if (!CheckInsideRange.check(newX, newY, 0, 0, mapLimitWidth, mapLimitHeight)) {
            locked = LockType.WALL_LOCK;
            return;
        }
        if (!CheckInsideRange.check(newX + width, newY + height, 0, 0, mapLimitWidth, mapLimitHeight)) {
            locked = LockType.WALL_LOCK;
            return;
        }
        
        posX.set(newX);
        posY.set(newY);
        // render();
    }
    /**
     * update focus camera on center using accumulation
     * @return false if none update
     */
    public boolean update() {
        if (locked == LockType.NONE)
            return false;
        double accX = ((collisionPos.getX() - posX.get()) * 0.02);
        double accY = ((collisionPos.getY() - posY.get()) * 0.02); 
        if (Math.abs(accX) < 0.1 && Math.abs(accY) < 0.1) {
            posX.set(collisionPos.getX());
            posY.set(collisionPos.getY());
            locked = LockType.NONE;
            return false; 
        }
        if (CheckInsideRange.check(collisionPos.getX(), collisionPos.getY(), 0, 0, mapLimitWidth, mapLimitHeight)) {
            if (CheckInsideRange.check(collisionPos.getX() + width, collisionPos.getY() + height, 0, 0, mapLimitWidth, mapLimitHeight)) {
                posX.set(posX.get() + accX);
                posY.set(posY.get() + accY);
                return true;
            }
        }
        return false;
    }

    public double getMinX() {
        return this.posX.get();
    }

    public double getMinY() {
        return this.posY.get();
    }

    public DoubleProperty getXProperty() {
        return posX;
    }

    public DoubleProperty getYProperty() {
        return posY;
    }

    public void lockCamera() {
        this.locked = LockType.USER_LOCK;
    }
    public void unlockCamera() {
        this.locked = LockType.NONE;
        // posX.set(target.getLayoutX() - width / 2);
        // posY.set(target.getLayoutY() - height / 2);
    }
}
