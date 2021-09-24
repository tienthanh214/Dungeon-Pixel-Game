package Game.StrategyDemo.source.weapon;

import javafx.scene.image.ImageView;

// should implement use Singleton pattern
final public class NullWeapon extends Weapon {
    public NullWeapon() {this.imageView = new ImageView();}
    public void useWeapon() {}
    public void setPosition(double width, double height) {}
}
