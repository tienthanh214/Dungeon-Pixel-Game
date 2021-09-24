package Game.StrategyDemo.source.tilemap;

import java.util.HashMap;

import Game.StrategyDemo.source.Camera;
import Game.StrategyDemo.source.GamePlay;
import Game.StrategyDemo.source.entity.Entity;
import Game.StrategyDemo.source.utils.BoundingBox;
import Game.StrategyDemo.source.tilemap.block.BackgroundBlock;
import Game.StrategyDemo.source.tilemap.block.Block;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.util.Pair;

public class TileLayer extends TilePane {
    private int tileWidth, tileHeight;
    private int height, width;
    public Block[] tiles;
    // optimize render speed
    private int lastX = -1;
    private int lastY = -1;

    public TileLayer(String data, Image image, int width, int height, int tileWidth, int tileHeight, int columns, boolean canReach) {
        this(data, image, null, null, width, height, tileWidth, tileHeight, columns, canReach);
        
    }

    public TileLayer(String data, Image image, HashMap<Integer, Pair<Integer[], Integer>> animationList, HashMap<Integer, BoundingBox[]> boundList,int width, int height, int tileWidth, int tileHeight, int columns, boolean canReach) {
        
        
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        this.height = height;
        this.width = width;
        this.setPrefRows(GamePlay.HEIGHT / tileHeight + 1);
        this.setPrefColumns(GamePlay.WIDTH / tileWidth + 1);
        this.setPrefTileWidth(this.tileWidth);
        this.setPrefTileHeight(this.tileHeight);
        // setup animation


        // setup tile map
        String token[] = (data.replaceAll("\\s+", "")).split(",");
        tiles = new Block[width * height];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                Pair<Integer[], Integer> frameList = null;
                BoundingBox[] boxes = null;
                boolean tileCanAccess = canReach;
                int gridIndex = Integer.parseInt(token[i * height + j]) - 1;
                if (gridIndex < 0) {
                    gridIndex = 0;
                    tileCanAccess = !canReach;
                }
                if (animationList != null && animationList.containsKey(gridIndex)) {
                    frameList = animationList.get(gridIndex);
                }
                if (boundList != null && boundList.containsKey(gridIndex)) {
                    boxes = boundList.get(gridIndex);
                }
                
                tiles[i * height + j] = new BackgroundBlock(boxes, frameList, columns, image, (gridIndex / columns), (gridIndex % columns),
                                                                j, i, tileWidth, tileHeight, tileCanAccess);
            }
        }
        

        
    }

    public void render(Camera camera) {
        int x = (int)(camera.getMinX() / tileWidth);
        int y = (int)(camera.getMinY() / tileHeight);
        this.relocate(-camera.getMinX() % tileWidth, -camera.getMinY() % tileHeight);

        if (x == lastX && y == lastY) return;


        this.getChildren().clear();
        for (int i = y; i < y + GamePlay.HEIGHT / this.tileHeight + 1; ++i) {
            for (int j = x; j < x + GamePlay.WIDTH / this.tileWidth + 1; ++j) {
                if (0 <= i * height + j && i * height + j < width * height)
                    this.getChildren().add(tiles[i * height + j]);
                else 
                    this.getChildren().add(tiles[0]); // temporary
            }
        }
        lastX = x; lastY = y;
    }
    /**
     * Can player go inside tile x, y when player move by deltaX and deltaY
     * @param x
     * @param y
     * @param deltaX
     * @param deltaY
     * @param player
     * @return
     */
   
    public boolean canGoInside(int x, int y, int deltaX, int deltaY, Entity player) {
        double newUpperX = (player.localToParent(player.bounds[0].getBoundsInParent()).getMinX() + deltaX);
        double newUpperY = (player.localToParent(player.bounds[0].getBoundsInParent()).getMinY() + deltaY);
        // not intersect so whatever
        if (!tiles[x * height + y].isCollision(newUpperX, newUpperY, player.bounds[0].getWidth(), player.bounds[0].getHeight()))
            return true;
        // System.out.println(tiles[x * height + y].pos);
        // System.out.println(x + " " + y + " " + tiles[x * height + y].isInsideMap());
        // System.out.println(tiles[x * height + y].bounds[0]);
        // System.out.println(newUpperX + " " + newUpperY + " " + player.bounds[0].getWidth() + " " + player.bounds[0].getHeight());
        return tiles[x * height + y].isInsideMap();
    }

    public boolean isInsideMap(int x, int y) {
        return tiles[x * height + y].isInsideMap();
    }
}
