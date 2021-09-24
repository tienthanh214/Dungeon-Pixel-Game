package Game.StrategyDemo.source.entity;

import Game.StrategyDemo.source.Camera;
import Game.StrategyDemo.source.GameObject;
import javafx.scene.image.ImageView;

public abstract class Entity extends GameObject {
    protected Camera camera;
    public ImageView imageView;

    private int DP; // defense point
    private int HP; // heart point

    public Entity() {
        this(0, 0);
    }

    public Entity(int HP, int DP) {
        this.HP = HP;
        this.DP = DP;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    // set position of center 
    public abstract void setPosition(double x, double y); 

    public void beDamaged(int damage) {
        if (damage > DP)
            this.HP -= damage - DP;
    }
}
