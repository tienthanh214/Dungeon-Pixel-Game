package Game.StrategyDemo.source;

import java.util.HashMap;
import java.util.Random;

import Game.StrategyDemo.source.entity.Character;
import Game.StrategyDemo.source.entity.Knight;
import Game.StrategyDemo.source.entity.RedDemon;
import Game.StrategyDemo.source.tilemap.Map;
import Game.StrategyDemo.source.tilemap.TileMapLoader;
import Game.StrategyDemo.source.weapon.HammerWeapon;
import Game.StrategyDemo.source.weapon.KnifeWeapon;
import Game.StrategyDemo.source.weapon.SwordWeapon;
import Game.StrategyDemo.source.weapon.Weapon;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * GamePlay view order:
 *      background layer (solid - from tile map)
 *      upper wall layer (contains decorate - from tile map)
 *      entity layer (character, weapon, .. - from code)
 *      object layer (item, ... - from tile map)
 *      lower wall layer (contains decorate - from tile map)
 */

public class GamePlay extends Application {
    static final public int FPS = 20;
    static final public int HEIGHT = 720;
    static final public int WIDTH = 1200;
    
    final private int speed = 5;
    final private HashMap<KeyCode, Boolean> keys = new HashMap<>(); 
    final static public Character player = new Character(0, 32 * 25, 32 * 40);
    final private Random rnd = new Random();

    public Map map;
    private Camera camera;
    private Pane gameState;
    private Pane root;

    private Weapon[] weaponList;
    Knight jug0 = new Knight(0, 32 * 40, 32 * 35);
    RedDemon enemy = new RedDemon(0, 32 * 70, 32 * 50);

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void init() {
        root = new Pane();
        root.setStyle("-fx-background-color: #0e1111;");
        map = TileMapLoader.load("Game\\StrategyDemo\\assets\\TileMap\\tilemap.tmx", 3);
        camera = new Camera(player, 32 * 25, 32 * 35, WIDTH, HEIGHT, map.getMapWidth(), map.getMapHeight());
        gameState = new Pane();
        gameState.getChildren().addAll(player, jug0, enemy);
        for (int i = 0; i < map.getNumberOfLayer(); ++i)
            map.render(i, camera);
        root.getChildren().addAll(map.getLayer(0), map.getLayer(1), gameState, map.getLayer(2));
        gameState.translateXProperty().bind(camera.getXProperty().multiply(-1));
        gameState.translateYProperty().bind(camera.getYProperty().multiply(-1));
    }

    @Override
    public void start(Stage primaryStage) {
        
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        // setup keys event
        scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
        scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));
        primaryStage.setTitle("Pixel game");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        Label FPSLbl = new Label("0 FPS");
        FPSLbl.setStyle("-fx-font-size:25; -fx-text-fill:white;");
        FPSLbl.relocate(WIDTH - 200, 10);
        root.getChildren().add(FPSLbl);


        AnimationTimer playGameTimer = new AnimationTimer() {
            long lastTime = -1;
            long delta;
            long prevUpdate = -1;
            @Override
            public void handle(long now) {
                delta = now - lastTime;
                lastTime = now;
                if (prevUpdate == -1) prevUpdate = now;

                if (prevUpdate + 1e9 <= now) {
                    FPSLbl.setText((int)Math.round(getFrameRate()) + " FPS");
                    prevUpdate = now;
                }

                
                updateGameState();
                
            }
            public double getFrameRate() {
                double frameRate = 1d / delta;
                return frameRate * 1e9;
            }
        };
        
        playGameTimer.start();

        camera.registerObserver(jug0);
        jug0.moveX(-1, map);
        jug0.idle();
        enemy.moveX(-1, map);
        enemy.animation.setOffset(3);
        generateWeapon(6);
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    public void updateGameState() {
        handlePlayer();
        handleItem();
    }

    public void handlePlayer() {
        boolean isMove = true;
        if (isPressed(KeyCode.UP)) {
            player.moveY(-speed, map);
        } else if (isPressed(KeyCode.DOWN)) {
            player.moveY(speed, map);
        } else if (isPressed(KeyCode.LEFT)) {
            player.moveX(-speed, map);
        } else if (isPressed(KeyCode.RIGHT)) {
            player.moveX(speed, map);
        } else {
            player.idle();    
            isMove = false;   
        }
        if (isPressed(KeyCode.SPACE)) { // attack
            player.attack();
            keys.put(KeyCode.SPACE, false);
        }
        if (isPressed(KeyCode.F)) { // collect
            player.collectItem(weaponList, gameState);
            keys.put(KeyCode.F, false);
        }
        if (isPressed(KeyCode.ENTER)) { 
            isMove = true;
            camera.setTarget(jug0);
            keys.put(KeyCode.ENTER, false);
        }
        if (isPressed(KeyCode.TAB)) {
            isMove = true;
            camera.setTarget(player);
            keys.put(KeyCode.TAB, false);
        }
        isMove |= camera.update();
        if (isMove == false) return;
        for (int i = 0; i < map.getNumberOfLayer(); ++i) 
            map.render(i, camera);
    }

    public void handleItem() {
       
    }
    
    public void generateWeapon(int n) {
        if (n < 3) n = 3;
        weaponList = new Weapon[n];
        weaponList[0] = new SwordWeapon();
        weaponList[1] = new KnifeWeapon();
        weaponList[2] = new HammerWeapon();
        
        for (int i = 3; i < n; ++i) {
            int x = rnd.nextInt(3);
            switch (x) {
                case 0: weaponList[i] = new SwordWeapon(); break;
                case 1: weaponList[i] = new KnifeWeapon(); break;
                case 2: weaponList[i] = new HammerWeapon();  break;
            }
        }

        for (int i = 0; i < n; ++i) {
            int x, y;
            do {
                x = rnd.nextInt(50 - 25) + 20;
                y = rnd.nextInt(50 - 15) + 15;
            } while (!map.isBackground(x, y));
            weaponList[i].setLayoutX(y * map.getTileWidth());
            weaponList[i].setLayoutY(x * map.getTileHeight());
            weaponList[i].setRotate(rnd.nextInt(360));
            // root.getChildren().add(weaponList[i]);
            gameState.getChildren().add(weaponList[i]);
            camera.registerObserver(weaponList[i]);
        }
        System.err.println("load weapon done");

    }
}
