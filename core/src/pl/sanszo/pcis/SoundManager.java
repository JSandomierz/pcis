package pl.sanszo.pcis;

import com.badlogic.gdx.audio.Music;


public class SoundManager {

    final public static float BACKGROUND_MUSIC_VOLUME = 0.1f;

    public static void playSingle(String soundName, float volume) {
        Content.SoundExt soundExt = Game.content.getSound(soundName);
        soundExt.getSound().setVolume(soundExt.id, volume);
        long now = System.currentTimeMillis();
        if(soundExt.duration == 0 || now - soundExt.playTime > soundExt.duration) {
            soundExt.id = soundExt.getSound().play();
            soundExt.playTime = now;
        }
    }

    public static void playSingle(String soundName) {
        playSingle(soundName, 1f);
    }

    public static void playBackgroundMusic() {
        Game.content.getMusic().play();
    }

    public static void pauseBackgroundMusic() {
        Game.content.getMusic().pause();
    }
}
