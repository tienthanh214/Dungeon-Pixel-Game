package Game.StrategyDemo.source.weapon;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class SwordWeapon extends Weapon {
    static final private Image IMAGE = new Image("file:Game\\StrategyDemo\\assets\\image\\weapon_knight_sword.png");
    static final private int basicDamage = 10;
    
    private Timeline attackAnimation;


    public SwordWeapon() {
        imageView = new ImageView(IMAGE);
        this.getChildren().add(imageView);
        
        Rotate rotate = new Rotate(0, IMAGE.getWidth() / 2, IMAGE.getHeight() + 5);
        this.getTransforms().add(rotate);
        attackAnimation = new Timeline( new KeyFrame(Duration.ZERO, new KeyValue(rotate.angleProperty(), 0d)),
                                        new KeyFrame(Duration.millis(300), new KeyValue(rotate.angleProperty(), 300d)));
        attackAnimation.setCycleCount(2);
        attackAnimation.setAutoReverse(true);   
    }
    
    @Override
    public void useWeapon() {
        attackAnimation.play();
        System.out.println("Sword attack: " + basicDamage + " point");
    }
  
    @Override
    public void setPosition(double width, double height) {
        this.setRotate(-110);
        this.setLayoutY(height * 5 / 10);
        this.setLayoutX(-width * 8 / 10);
    }
    
}
