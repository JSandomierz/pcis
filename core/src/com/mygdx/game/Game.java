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
	SpriteBatch batch;
	OrthographicCamera camera;
	Viewport viewport;

	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;

	@Override
	public void create () {

		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		batch = new SpriteBatch();
		camera = new OrthographicCamera(800,600);
		viewport = new ExtendViewport(800, 600, camera);
		gameStage = new GameStage(viewport, batch);
		Gdx.input.setInputProcessor(gameStage);

		debugRenderer = new Box2DDebugRenderer();
		OrthographicCamera cam = new OrthographicCamera();
		cam.setToOrtho(false, Gdx.graphics.getWidth()/100.f, Gdx.graphics.getHeight()/100.f);
		debugMatrix = cam.combined;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		gameStage.act(Gdx.graphics.getDeltaTime());
		batch.setProjectionMatrix(camera.combined);
		gameStage.draw();
		debugRenderer.render(gameStage.world, debugMatrix);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
