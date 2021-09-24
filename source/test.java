package Game.StrategyDemo.source;

import java.util.Arrays;

public class test {
    static void run(Integer[] a) {
        a[1] = 5;
    }
    public static void main(String[] args) {
        
        Integer[] a = new Integer[4];
        a[1] = 2; a[2] = 4; a[3] = 5; a[0] = 9;
        System.out.println(Arrays.toString(a));
        run(a);
        System.out.println(Arrays.toString(a));
    }  
}
