package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
    private final Main game;
    private final Player player;
    private final Array<Platform> platforms;
    private final AssetManager assetManager;
    private OrthographicCamera camera;
    private float highestY;
    private float viewportHeight;
    public GameScreen(Main game) {
        this.game = game;
        this.assetManager = new AssetManager();
        this.player = new Player(assetManager);
        this.platforms = new Array<>();

        // Configurar cámara
        viewportHeight = 800; // Altura visible de la pantalla
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), viewportHeight);
        camera.position.set(Gdx.graphics.getWidth()/2f, viewportHeight/2f, 0);

        // Crear plataforma inicial
        platforms.add(new Platform(0, 0, Gdx.graphics.getWidth(), 100, assetManager, true));

        // Generar primeras plataformas
        highestY = 0;
        generatePlatforms(viewportHeight * 2); // Generar hasta el doble de la altura visible
    }
    private void generatePlatforms(float upToY) {
        while (highestY < upToY) {
            // Añadir plataforma flotante
            float width = MathUtils.random(100, 200);
            float x = MathUtils.random(0, Gdx.graphics.getWidth() - width);
            float y = highestY + MathUtils.random(150, 300); // Espaciado vertical

            platforms.add(new Platform(x, y, width, 0, assetManager, false));

            highestY = y;
        }
    }
    private Array<Platform> createPlatforms(AssetManager assetManager) {
        Array<Platform> platforms = new Array<>();
        // Suelo (verde)
        platforms.add(new Platform(0, 0, Gdx.graphics.getWidth(), 100, assetManager, true));
        // Plataformas flotantes (grises)
        platforms.add(new Platform(100, 400, 150, 0, assetManager, false));
        platforms.add(new Platform(250, 800, 150, 0, assetManager, false));
        platforms.add(new Platform(150, 1200, 150, 0, assetManager, false));
        return platforms;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.handleInput();
        player.update(delta, platforms);

        // Actualizar cámara para seguir al jugador si sube
        if (player.getY() > camera.position.y - viewportHeight/4f) {
            camera.position.y = player.getY() + viewportHeight/4f;
        }
        camera.update();

        // Generar más plataformas si el jugador está cerca del límite superior
        if (player.getY() > highestY - viewportHeight) {
            generatePlatforms(highestY + viewportHeight);
        }

        // Eliminar plataformas que están muy abajo (fuera de la vista)
        removeOffscreenPlatforms();

        // Configurar el SpriteBatch para usar la cámara
        SpriteBatch batch = game.getBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Dibujar fondo (ajustado a la posición de la cámara)
        float bgY = camera.position.y - viewportHeight/2;
        batch.draw(assetManager.getBackgroundTexture(),
            0, bgY,
            Gdx.graphics.getWidth(), viewportHeight);

        // Dibujar plataformas
        for (Platform platform : platforms) {
            platform.render(batch);
        }

        // Dibujar jugador
        player.render(batch);
        batch.end();
    }
    private void removeOffscreenPlatforms() {
        // Eliminar plataformas que están más abajo del límite inferior de la cámara
        float lowestVisibleY = camera.position.y - viewportHeight/2 - 100; // Margen adicional

        for (int i = platforms.size - 1; i >= 0; i--) {
            Platform platform = platforms.get(i);
            if (!platform.isGround() && platform.getY() + platform.getHeight() < lowestVisibleY) {
                platforms.removeIndex(i);
            }
        }
    }


    @Override public void show() {}
    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = viewportHeight;
        camera.update();
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
