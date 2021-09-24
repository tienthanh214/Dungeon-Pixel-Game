package Game.StrategyDemo.source.utils;

final public class CheckInsideRange {
    static public boolean check(double x, double y, double lowerX, double lowerY, double upperX, double upperY) {
        return lowerX <= x && x <= upperX && lowerY <= y && y <= upperY;
    }
}
