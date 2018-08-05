package pl.sanszo.pcis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;



public class Content {
    private HashMap<String, String> fileNames = new HashMap<>();
    private HashMap<String, SoundExt> sounds = new HashMap<>();
    private String fontFileName;
    private AssetManager assetManager;
    private Music backgroundMusic;

    public class SoundExt {
        public String path;
        public int duration;
        public long playTime;
        public long id;
        public SoundExt(String path, int duration) {
            this.path = path;
            this.duration = duration;
        }
        public Sound getSound() {
            return assetManager.get(path);
        }
    }


    public Content() {
        assetManager = new AssetManager();
        Texture.setAssetManager(assetManager);
    }

    public void loadTexture(String name, String path) {
        fileNames.put(name, path);
        assetManager.load(path, Texture.class);
    }

    public void loadSound(String name, int duration, String path) {
        sounds.put(name, new SoundExt(path, duration));
        assetManager.load(path, Sound.class);
    }

    public void loadSound(String name, String path) {
        loadSound(name, 0, path);
    }

    public SoundExt getSound(String name) {
        return sounds.get(name);
    }

    public void loadFont(String path) {
        fontFileName = path;
        assetManager.load(path, BitmapFont.class);
    }

    public BitmapFont getFont() {
        if(fontFileName==null) return null;
        return assetManager.get(fontFileName);
    }


    public void loadBackgroundMusic(String path) {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(SoundManager.BACKGROUND_MUSIC_VOLUME);
    }

    public Music getMusic() {
        return backgroundMusic;
    }

    public Texture getTexture(String name) {
        return assetManager.get(fileNames.get(name));
    }

    public void waitForLoad() {
        while(!assetManager.update(10)) {
            //Gdx.app.log("LOADING ASSETS", String.valueOf(assetManager.getProgress()));
        };
    }

    public void dispose() {
        assetManager.dispose();
    }

}
