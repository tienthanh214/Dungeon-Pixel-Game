
/**
 * Management whole map
 * Draw map, animation
 */
package Game.StrategyDemo.source.tilemap;

import java.util.ArrayList;
import java.util.HashMap;

import Game.StrategyDemo.source.Camera;
import Game.StrategyDemo.source.entity.Entity;
import Game.StrategyDemo.source.utils.BoundingBox;
import javafx.scene.image.Image;
import javafx.util.Pair;

public class Map {
    private Image image;
    private int columns;
    private int width, height;
    private int tileHeight, tileWidth;
    private ArrayList<TileLayer> layers;

    private HashMap<Integer, Pair<Integer[], Integer>> animationList;
    private HashMap<Integer, BoundingBox[]> boundList;
    public Map() {
        this(null, 0, 0, 0, 0, 0, 0);
    }

    public Map(String imagePath, int width, int height, int tileWidth, int tileHeight, int columns, double factor) {
        this.width = width; 
        this.height = height;
        this.tileHeight = (int)(tileHeight * factor);
        this.tileWidth = (int)(tileWidth * factor);
        this.columns = columns;
        layers = new ArrayList<>();
        image = new Image(getClass().getResourceAsStream(imagePath));
        image = new Image(getClass().getResourceAsStream(imagePath), image.getWidth() * factor, image.getHeight() * factor, true, true);
        
        animationList = new HashMap<>();
        boundList = new HashMap<>();
    }

    public void addLayer(String data, boolean canReach) {
        // assume that all layer same size and equal to global Map size
        this.layers.add(new TileLayer(data, image, animationList, boundList, width, height, tileWidth, tileHeight, columns, canReach));
    }

    public void addLayer(TileLayer tileLayer) {
        this.layers.add(tileLayer);
    }

    public void addAnimation(int grid, Integer[] frameList, int duration) {
        animationList.put(grid, new Pair<>(frameList, duration));
    }

    public void addBoundingBox(int grid, BoundingBox[] box) {
        boundList.put(grid, box);
    }

    // ============== release to save memory after pass to all tiles =====================
    public void resetAnimation() {
        animationList.clear();
    }

    public void resetBoundingBox() {
        boundList.clear();
    }
    // ============ get fields ===================================
    public TileLayer getLayer(int index) {
        return this.layers.get(index);
    }

    public int getNumberOfLayer() {
        return this.layers.size();
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void render(int layerIndex, Camera camera) {
        if (layerIndex >= layers.size()) {
            System.out.println("Layer out of size");
            return;
        }
        layers.get(layerIndex).render(camera);
    }
    
    /* ===================  ======================= */
    /**
     * can player move deltaX and deltaY inside this map?
     * @param deltaX
     * @param deltaY
     * @param player
     * @return
     */
    public boolean canGo(int deltaX, int deltaY, Entity player) {
        // (x, y) in tile grid coordinate (not scene cord)
        int y = (int)(player.localToParent(player.bounds[0].getBoundsInParent()).getMinX() + deltaX) / tileWidth;
        int x = (int)(player.localToParent(player.bounds[0].getBoundsInParent()).getMinY() + deltaY) / tileHeight;

        for (int dx = 0; dx <= 1; ++dx) {
            for (int dy = 0; dy <= 1; ++dy) {
                if (!isTileIndexInside(x + dx, y + dy))
                    continue;
                for (int idx = 0; idx < 2; ++idx)
                    if (!layers.get(idx).canGoInside(x + dx, y + dy, deltaX, deltaY, player)) {
                        return false;
                    }
            }
        }
        return true;
    }

    private boolean isTileIndexInside(int x, int y) {
        return 0 <= x && x < width && 0 <= y && y < height;
    }

    public boolean isBackground(int x, int y) {
        return layers.get(0).isInsideMap(x, y);
    }

    final public int getMapWidth() {
        return width * tileWidth;
    }

    final public int getMapHeight() {
        return height * tileHeight;
    }

}
