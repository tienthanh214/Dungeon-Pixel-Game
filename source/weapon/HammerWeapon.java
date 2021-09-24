package Game.StrategyDemo.source.weapon;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class HammerWeapon extends Weapon {
    static final private Image IMAGE = new Image("file:Game\\StrategyDemo\\assets\\image\\weapon_hammer.png");
    static final private int basicDamage = 10;
    
    private Timeline attackAnimation;


    public HammerWeapon() {
        imageView = new ImageView(IMAGE);
        this.getChildren().add(imageView);

        Rotate rotate = new Rotate(0, IMAGE.getWidth() / 2, IMAGE.getHeight() - 5);
        this.getTransforms().add(rotate);
        attackAnimation = new Timeline( new KeyFrame(Duration.ZERO, new KeyValue(rotate.angleProperty(), 0d)),
                                        new KeyFrame(Duration.millis(200), new KeyValue(rotate.angleProperty(), -60d)),
                                        new KeyFrame(Duration.millis(400), new KeyValue(rotate.angleProperty(), 45d)),
                                        new KeyFrame(Duration.millis(850), new KeyValue(rotate.angleProperty(), 0d)));
        // this.bounds = new BoundingBox[1];
    }

    @Override
    public void useWeapon() {
        attackAnimation.play();
        System.out.println("Hammer attack: " + basicDamage + " point");
    }

    @Override
    public void setPosition(double width, double height) {
        this.setRotate(80);
        this.setLayoutX(width * 3 / 4);
        this.setLayoutY(height * 4 / 11);
    }
    
}
