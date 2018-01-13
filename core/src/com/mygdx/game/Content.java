package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Created by szostakm on 10.01.18.
 */

public class Content {
    private HashMap<String, Texture> textureHashMap = new HashMap<>();
    public void loadTexture(String name, String path) {
        textureHashMap.put(name, new Texture(path));
    }

    public Texture getTexture(String name) {
        return textureHashMap.get(name);
    }
}
