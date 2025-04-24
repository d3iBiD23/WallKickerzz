package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class GameScreen implements Screen {
    private final Main game;
    private final Player player;
    private final Array<Platform> platforms;
    private final AssetManager assetManager;

    private OrthographicCamera camera;
    private float initialCameraY;
    private float highestPlatformY;       // Guarda la Y más alta de las plataformas
    private static final float SPAWN_GAP_MIN = 150f;  // mínimo espacio vertical entre plataformas
    private static final float SPAWN_GAP_MAX = 300f;  // máximo espacio vertical

    public GameScreen(Main game) {
        this.game = game;
        this.assetManager = new AssetManager();
        this.player = new Player(assetManager);

        // Inicializa la cámara al tamaño de pantalla
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        initialCameraY = Gdx.graphics.getHeight() / 2f;
        camera.position.set(camera.viewportWidth / 2f, initialCameraY, 0);

        // Crea las plataformas iniciales y calcula la Y más alta
        this.platforms = createInitialPlatforms(assetManager);
        highestPlatformY = 0;
        for (Platform p : platforms) {
            highestPlatformY = Math.max(highestPlatformY, p.getY());
        }
    }

    private Array<Platform> createInitialPlatforms(AssetManager assetManager) {
        Array<Platform> list = new Array<>();
        // Suelo
        list.add(new Platform(0, 0, Gdx.graphics.getWidth(), 100, assetManager, true));
        // Algunas plataformas flotantes iniciales
        list.add(new Platform(100, 400, 150, 0, assetManager, false));
        list.add(new Platform(250, 800, 150, 0, assetManager, false));
        list.add(new Platform(150, 1200, 150, 0, assetManager, false));
        return list;
    }

    @Override
    public void render(float delta) {
        // 1) Lógica de jugador
        player.handleInput();
        player.update(delta, platforms);

        // 2) Generación y limpieza de plataformas según la cámara
        updatePlatforms();

        // 3) Movimiento de la cámara
        float camX = Gdx.graphics.getWidth() / 2f;
        float camY = camera.position.y;

        // Sólo sube la cámara cuando el jugador supere su posición actual
        float playerY = player.getHitbox().y + player.getHitbox().height / 2f;
        if (playerY > camera.position.y) {
            camY = playerY;
        }

        camera.position.set(camX, camY, 0);
        camera.update();

        // Calcula la Y del fondo de cámara
        float bottomEdge = camera.position.y - camera.viewportHeight / 2f;

        // Si el jugador cae por debajo → Game Over
        if (player.getHitbox().y < bottomEdge) {
            game.setScreen(new GameOverScreen(game));
            return; // salimos de render para no dibujar la partida
        }

        // 4) Dibujado con la cámara
        SpriteBatch batch = game.getBatch();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // (Opcional) Si quieres que el fondo siga al jugador, dibújalo en Y = camY - viewportHeight/2
        assetManager.drawBackground(batch, camY - camera.viewportHeight / 2f);

        for (Platform platform : platforms) {
            platform.render(batch);
        }
        player.render(batch);
        batch.end();
    }

    /**
     * Genera nuevas plataformas arriba y elimina las viejas debajo
     */
    private void updatePlatforms() {
        float topEdge = camera.position.y + camera.viewportHeight / 2f;
        // A) Genera mientras la Y más alta esté por debajo del topEdge
        while (highestPlatformY < topEdge) {
            float nextY = highestPlatformY
                + MathUtils.random(SPAWN_GAP_MIN, SPAWN_GAP_MAX);
            float nextX = MathUtils.random(
                0f,
                Gdx.graphics.getWidth() - Platform.DEFAULT_WIDTH  // asume un ancho fijo o variable
            );
            platforms.add(new Platform(nextX, nextY, 150, 0, assetManager, false));
            highestPlatformY = nextY;
        }
        // B) Elimina las plataformas que queden fuera de la parte inferior
        float bottomEdge = camera.position.y - camera.viewportHeight / 2f;
        Iterator<Platform> it = platforms.iterator();
        while (it.hasNext()) {
            Platform p = it.next();
            if (p.getY() + p.getHeight() < bottomEdge) {
                it.remove();
            }
        }
    }

    // … resto de métodos obligatorios de Screen …
    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
