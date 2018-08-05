package pl.sanszo.pcis;

public class Utilities {
    public static int randomBetween(int min, int max) {
        return (int)(Math.random() * (max - min)) + min;
    }

    public static float randomBetween(float min, float max) {
        return (float)(Math.random() * (max - min)) + min;

    }
}
