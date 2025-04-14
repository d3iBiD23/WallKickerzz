package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
    private final Main game;
    private final Player player;
    private final Array<Platform> platforms;
    private final AssetManager assetManager;

    public GameScreen(Main game) {
        this.game = game;
        this.assetManager = new AssetManager();
        this.player = new Player(assetManager);
        this.platforms = createPlatforms(assetManager);
    }

    private Array<Platform> createPlatforms(AssetManager assetManager) {
        Array<Platform> platforms = new Array<>();
        platforms.add(new Platform(0, 0, Gdx.graphics.getWidth(), 50, assetManager)); // Suelo
        platforms.add(new Platform(100, 500, 200, 50, assetManager)); // Plataforma flotante 1
        platforms.add(new Platform(250, 800, 200, 50, assetManager)); // Plataforma flotante 2
        platforms.add(new Platform(150, 1100, 200, 50, assetManager)); // Plataforma flotante 3
        return platforms;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.handleInput();
        player.update(delta, platforms);

        SpriteBatch batch = game.getBatch();
        batch.begin();
        assetManager.drawBackground(batch);

        for (Platform platform : platforms) {
            platform.render(batch);
        }

        player.render(batch);
        batch.end();
    }

    // Resto de métodos de Screen (show, resize, pause, resume, hide)
    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
