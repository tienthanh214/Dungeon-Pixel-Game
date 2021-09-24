package Game.StrategyDemo.source.utils;

import javafx.scene.image.Image;

final public class LoadImageData {
    public static void loadImageData(Image[] imageData, String fileDir) {
        for (int i = 0; i < 4; ++i) {
            imageData[i] = new Image(fileDir + "_idle_anim_f" + i + ".png");
        }
        for (int i = 0; i < 4; ++i) 
            imageData[i + 4] = new Image(fileDir + "_run_anim_f" + i + ".png");
        for (int i = 0; i < 1; ++i) 
            imageData[i + 8] = new Image(fileDir + "_hit_anim_f" + i + ".png");
    }
}
