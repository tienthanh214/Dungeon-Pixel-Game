package Game.StrategyDemo.source.weapon;

import javafx.scene.image.ImageView;
import Game.StrategyDemo.source.GameObject;

public abstract class Weapon extends GameObject {
    protected ImageView imageView;
    
    abstract public void useWeapon();
    abstract public void setPosition(double width, double height); 
}
