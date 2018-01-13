package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Game extends ApplicationAdapter{
	GameStage gameStage;
	public static final Content content = new Content();
	public final static int WIDTH = 1080;
	public final static int HEIGHT = 1920;
	public final static float PPM = 100f;
	SpriteBatch batch;
	FollowingCamera camera;
	OrthographicCamera b2dCam;
	Viewport viewport;

	Box2DDebugRenderer debugRenderer;

	@Override
	public void create () {
		content.loadTexture("skybg", "skybg.png");
		content.loadTexture("logo", "logo.png");
		content.loadTexture("taptostart", "taptostart.png");
		content.loadTexture("taptotryagain", "taptotryagain.png");
		content.loadTexture("palace", "palace.png");
		content.loadTexture("clouds", "clouds.png");

		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		batch = new SpriteBatch();
		camera = new FollowingCamera();
		viewport = new ExtendViewport(WIDTH, HEIGHT, camera);


		debugRenderer = new Box2DDebugRenderer();

		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, WIDTH/100.f, HEIGHT/100.f);

		gameStage = new GameStage(viewport, batch, camera, b2dCam);
		Gdx.input.setInputProcessor(gameStage);
	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameStage.act(Gdx.graphics.getDeltaTime());
		b2dCam.position.y = camera.position.y/PPM;
		camera.update();
		b2dCam.update();
		batch.setProjectionMatrix(camera.combined);
		gameStage.draw();
		debugRenderer.render(gameStage.world, b2dCam.combined);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public void resize (int width, int height) {
		// viewport must be updated for it to work properly
		viewport.update(width, height);
	}

}
