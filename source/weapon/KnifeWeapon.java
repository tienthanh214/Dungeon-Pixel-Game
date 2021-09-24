package Game.StrategyDemo.source.weapon;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class KnifeWeapon extends Weapon {
    static final private Image IMAGE = new Image("file:Game\\StrategyDemo\\assets\\image\\weapon_knife.png");
    static final private int basicDamage = 6;
    
    private TranslateTransition attackAnimation;

    public KnifeWeapon() {
        imageView = new ImageView(IMAGE);
        this.getChildren().add(imageView);

        attackAnimation = new TranslateTransition(Duration.millis(150), this);
        attackAnimation.setByX(20);
        attackAnimation.setByY(-5);
        attackAnimation.setAutoReverse(true);
        attackAnimation.setCycleCount(2);
    }

    @Override
    public void useWeapon() {
        attackAnimation.play();
        System.out.println("Knife attack: " + basicDamage + " point");
    }

    @Override
    public void setPosition(double width, double height) {
        this.setRotate(75);
        this.setLayoutY(height * 6 / 10);
        this.setLayoutX(width * 6 / 10);
    }

}
