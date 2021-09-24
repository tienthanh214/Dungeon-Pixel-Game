package Game.StrategyDemo.source.entity;

import Game.StrategyDemo.source.Camera;
import Game.StrategyDemo.source.GameObject;
import Game.StrategyDemo.source.GamePlay;
import Game.StrategyDemo.source.animation.SpriteAnimation;
import Game.StrategyDemo.source.tilemap.Map;
import Game.StrategyDemo.source.utils.BoundingBox;
import Game.StrategyDemo.source.utils.LoadImageData;
import Game.StrategyDemo.source.weapon.NullWeapon;
import Game.StrategyDemo.source.weapon.Weapon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Knight extends Entity {
    static public Image[] imageData = null;
    static private  int count; // num animate per action 
    static private  int columns; // number per row, but in this case not necessary
    
    private SpriteAnimation animation;
    private Weapon weapon;
    private int collectRange = 64;

    static {
        count = 4;
        columns = 4;
        imageData = new Image[9];
        LoadImageData.loadImageData(imageData, "file:Game\\StrategyDemo\\assets\\image\\knight_m");
    }
    
    public Knight() {
        this(0);
    }

    public Knight(int offset) {
        this(offset, imageData[0].getWidth(), imageData[0].getHeight());
    }

    public Knight(int offset, double posX, double posY) {
        this(null, offset, posX, posY);

    }

    public Knight(Camera camera, int offset, double posX, double posY) {
        super(10, 1);
        this.imageView = new ImageView();
        this.weapon = new NullWeapon();
        imageView.setImage(imageData[offset * columns]);
        this.animation = new SpriteAnimation(this, imageData, Duration.millis(4000 / GamePlay.FPS), 
                                            count, columns, offset, false);
        
        // this.setPosition(posX, posY);    
        this.relocate(posX - imageData[0].getWidth() / 2, posY - imageData[0].getHeight() / 2);

        this.camera = camera;
        // setup shadow bound
        this.bounds = new BoundingBox[1];
        this.bounds[0] = new BoundingBox(0, imageData[0].getHeight() * 3 / 4, imageData[0].getWidth(), imageData[0].getHeight() / 3, -2);
        
        Circle cir = new Circle(imageData[0].getWidth() / 2, (imageData[0].getHeight() * 3 / 4 + imageData[0].getHeight() / 6), collectRange);
        cir.setOpacity(0.5);
        this.getChildren().addAll(imageView, bounds[0], weapon);
        //System.out.println(this.localToParent(bounds[0].getBoundsInParent()));
        
    }
    
    public Weapon setWeapon(Weapon w) { // set new weapon w and  throw old weapon
        Weapon temp = w; w = this.weapon; this.weapon = temp;      
        this.getChildren().remove(w);
        this.getChildren().add(this.weapon);
        
        this.weapon.relocate(0, 0);
        this.weapon.setTranslateX(0);
        this.weapon.setTranslateY(0);

        this.weapon.setPosition(imageData[0].getWidth(), imageData[0].getHeight());

        if (camera != null) {
            camera.removeObserver(weapon);
            camera.registerObserver(w);
            w.relocate(this.getLayoutX(), this.getLayoutY() + imageData[0].getWidth());
        } else {
            w.relocate(this.getTranslateX() + this.getLayoutX(), this.getTranslateY() + this.getLayoutY() + imageData[0].getWidth());
        }

        return w;      
    }
    /** 
     * set new position translate 
    */
    public void setPosition(double x, double y) {
        this.setTranslateX(x);
        this.setTranslateY(y);
        // this.relocate(x - imageData[0].getWidth() / 2, y - imageData[0].getHeight() / 2);
    }


    /* ========= perform action ============= */
    public void idle() {
        this.animation.setOffset(0);
        this.animation.play();
    }

    public void attack() {
        if (animation.getOffset() != 2) {
            this.animation.playFromStart();
            this.animation.setOffset(2);
        }
        this.weapon.useWeapon();
    }
    /**
     * collect item nearby in itemList in gameState pane
     * @param itemList
     * @param gameState
     */
    public void collectItem(GameObject[] itemList, Pane gameState) {
        System.out.println("you call me");

        for (int idx = 0; idx < itemList.length; ++idx) {
            if (!(itemList[idx] instanceof Weapon) || itemList[idx] instanceof NullWeapon) 
                continue;
            Weapon item = (Weapon)itemList[idx];
            // System.out.println(item + " " + this.getDistance(item));
            if (this.getDistance(item) <= this.collectRange) {
                gameState.getChildren().remove(item);
                itemList[idx] = this.setWeapon(item);
                gameState.getChildren().add(itemList[idx]);
                break;
            }
        }

        this.animation.play();
        if (animation.getOffset() != 3) {
            this.animation.playFromStart(); 
            this.animation.setOffset(3);
        }
    }

    /**
     * move horizontal by delta pixel on map
     * @param delta
     * @param map
     */
    public void moveX(int delta, Map map) {
        this.animation.play();
        this.animation.setOffset(1);        
        this.animation.setDirection(delta < 0);

        if (!map.canGo(delta, 0, this))
            return;
        if (camera != null) 
            camera.move(delta, 0);
        this.setLayoutX(this.getLayoutX() + delta);
    }
    
    public void moveY(int delta, Map map) {
        this.animation.play();
        this.animation.setOffset(1);

        if (!map.canGo(0, delta, this))
            return;
        if (camera != null) 
            camera.move(0, delta);
        this.setLayoutY(this.getLayoutY() + delta);
        
    }
}
