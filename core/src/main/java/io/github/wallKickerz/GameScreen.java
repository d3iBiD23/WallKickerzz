package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen {
    private final Main game;
    private final Player player;
    private final Array<Platform> platforms;
    private final AssetManager assetManager;

    private OrthographicCamera worldCamera;
    private OrthographicCamera hudCamera;  // Cámara para interfaz fija

    private float initialCameraY;
    private float highestPlatformY; // Guarda la Y más alta de las plataformas
    private static final float SPAWN_GAP_MIN = 250f;  // mínimo espacio vertical entre plataformas
    private static final float SPAWN_GAP_MAX = 300f;  // máximo espacio vertical

    private Texture buttonBg;
    private Texture pauseIcon;
    private Rectangle buttonBounds;
    private static final float PADDING = 100f;
    private static final float ICON_SCALE = 2f;
    private static final float BG_SCALE = 2f;
    private Score score;
    private Texture springTexture;
    private Array<Spring> springs;
    private Random random;

    // Constantes para la generación de muelles
    private static final float SPRING_PROBABILITY = 0.1f; // 10% de probabilidad de generar un muelle en una plataforma

    private Texture touchLeftTexture;
    private Texture touchRightTexture;
    private boolean showTouchInstructions = true;
    private float touchScale = 4f; // Aumentamos el tamaño
    private float touchYPosition = 750f; // Posición más arriba
    private float touchAlpha = 0.8f; // Transparencia para que no distraigan mucho
    private float touchInstructionsTimer = 5f; // Mostrar durante 3 segundos

    public GameScreen(Main game) {
        this.game = game;
        this.assetManager = new AssetManager();
        this.player = new Player();
        this.score = new Score();

        // Inicializar random primero
        this.random = new Random();

        // Carga de texturas HUD
        buttonBg = new Texture(Gdx.files.internal("PNG/Buttens and Headers/ButtonSquare_Beighe.png"));
        pauseIcon = new Texture(Gdx.files.internal("PNG/Numbers, Letters and Icons/PauseIcon_Black.png"));

        // Cámaras
        worldCamera = new OrthographicCamera();
        worldCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        initialCameraY = Gdx.graphics.getHeight() / 2f;
        worldCamera.position.set(worldCamera.viewportWidth / 2f, initialCameraY, 0);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        touchLeftTexture = new Texture(Gdx.files.internal("touch/touch_tap.png"));
        touchRightTexture = new Texture(Gdx.files.internal("touch/touch_tap.png"));


        // Calcula bounds del botón completo (fondo + icono)
        float bgW = buttonBg.getWidth() * BG_SCALE;
        float bgH = buttonBg.getHeight() * BG_SCALE;
        float x = hudCamera.viewportWidth - bgW - PADDING;
        float y = hudCamera.viewportHeight - bgH - PADDING;
        buttonBounds = new Rectangle(x, y, bgW, bgH);

        // SPRINGS texture
        this.springTexture = assetManager.getSpringTexture();
        springs = new Array<>();

        // Plataformas iniciales
        this.platforms = new Array<>();
        highestPlatformY = 0;

        // Crear suelo
        platforms.add(new Platform(0, 0, Gdx.graphics.getWidth(), 100, assetManager, true));

        // Genero plataformas iniciales aleatorias
        generateInitialPlatforms(5); // Genera 5 plataformas iniciales además del suelo
    }

    private void generateInitialPlatforms(int count) {
        float currentY = 100; // Comenzar justo encima del suelo

        for (int i = 0; i < count; i++) {
            float nextY = currentY + MathUtils.random(SPAWN_GAP_MIN, SPAWN_GAP_MAX);
            float nextX = MathUtils.random(
                0f,
                Gdx.graphics.getWidth() - Platform.DEFAULT_WIDTH
            );

            Platform newPlatform = new Platform(nextX, nextY, 150, 0, assetManager, false);
            platforms.add(newPlatform);
            currentY = nextY;
            highestPlatformY = Math.max(highestPlatformY, currentY);

            // Posibilidad de crear un muelle en la plataforma
            if (random.nextFloat() < SPRING_PROBABILITY) {
                createSpringOnPlatform(newPlatform);
            }
        }
    }

    /**
     * Crea un muelle encima de una plataforma
     */
    private void createSpringOnPlatform(Platform platform) {
        Texture springTexture = assetManager.getSpringTexture();
        float springWidth = springTexture.getWidth() * 1.5f;  // Usa el mismo SPRING_SCALE
        float springHeight = springTexture.getHeight() * 1.5f;

        float springX = platform.getX() + (platform.getWidth() / 2f) - (springWidth / 2f);
        float springY = platform.getY() + platform.getHeight();  // Justo encima

        springs.add(new Spring(springTexture, springX, springY));
    }

    @Override
    public void render(float delta) {
        // Input HUD
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            hudCamera.unproject(touch);
            if (buttonBounds.contains(touch.x, touch.y)) {
                // Pasa la instancia actual para mantener el estado
                game.setScreen(new PauseScreen(game, this));
                return;
            }
        }

        // Lógica juego
        player.handleInput();
        player.update(delta, platforms);
        updateFragilePlatforms(delta);
        updatePlatforms();
        updateSprings(delta);
        checkSpringCollisions(delta);

        // Mover cámara de mundo
        float camX = Gdx.graphics.getWidth() / 2f;
        float camY = worldCamera.position.y;
        float playerY = player.getHitbox().y + player.getHitbox().height / 2f;
        if (playerY > worldCamera.position.y) camY = playerY;
        worldCamera.position.set(camX, camY, 0);
        worldCamera.update();

        float playerCenterY = player.getHitbox().y + player.getHitbox().height / 2f;
        score.update(playerCenterY);

        // Game Over
        float bottomEdge = worldCamera.position.y - worldCamera.viewportHeight / 2f;
        if (player.getHitbox().y < bottomEdge) {
            game.setCurrentScore(score.getCurrentScore());
            game.setScreen(new GameOverScreen(game));
            return;
        }

        // Render mundo
        SpriteBatch batch = game.getBatch();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        assetManager.drawBackground(batch, worldCamera.position.y - worldCamera.viewportHeight / 2f);
        for (Platform platform : platforms) platform.render(batch);
        for (Spring spring : springs) spring.render(batch);
        player.render(batch);
        batch.end();

        // Render HUD (botón completo)
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        batch.draw(buttonBg, buttonBounds.x, buttonBounds.y, buttonBounds.width, buttonBounds.height);
        float iconW = pauseIcon.getWidth() * ICON_SCALE;
        float iconH = pauseIcon.getHeight() * ICON_SCALE;
        float iconX = buttonBounds.x + (buttonBounds.width - iconW) / 2f;
        float iconY = buttonBounds.y + (buttonBounds.height - iconH) / 2f;
        batch.draw(pauseIcon, iconX, iconY, iconW, iconH);


        // score
        score.render(batch,
            hudCamera.viewportWidth,
            hudCamera.viewportHeight
        );

        // Ocultar instrucciones después de unos segundos o al tocar
        if (showTouchInstructions) {
            touchInstructionsTimer -= delta;
            if (touchInstructionsTimer <= 0) {
                showTouchInstructions = false;
            }
        }

        if (showTouchInstructions) {
            // Tamaño de las imágenes
            float textureWidth = touchLeftTexture.getWidth() * touchScale;
            float textureHeight = touchLeftTexture.getHeight() * touchScale;

            // Posiciones (centradas horizontalmente)
            float leftX = hudCamera.viewportWidth * 0.25f - textureWidth / 2f;
            float rightX = hudCamera.viewportWidth * 0.75f - textureWidth / 2f;

            // Efecto de parpadeo suave
            float pulse = (float) (0.7f + 0.3f * Math.sin(touchInstructionsTimer * 5f));

            // color gris con pulsación suave
            float grey = 0.5f;
            batch.setColor(grey, grey, grey, touchAlpha * pulse);

            // dibujar íconos ya tintados
            batch.draw(touchLeftTexture, leftX, touchYPosition, textureWidth, textureHeight);
            batch.draw(touchRightTexture, rightX, touchYPosition, textureWidth, textureHeight);

            // restaurar color a blanco para el resto del HUD
            batch.setColor(1f, 1f, 1f, 1f);
        }

        batch.end();
    }

    /**
     * Comprueba colisiones con muelles
     */
    private void checkSpringCollisions(float delta) {
        Rectangle hitbox = player.getHitbox();
        for (Spring spring : springs) {
            Rectangle s = spring.getBounds();

            // 1) Sólo si el jugador está cayendo
            if (player.getVelocityY() > 0) continue;

            // 2) Altura de los pies antes y después de moverse
            float prevFeetY = hitbox.y - player.getVelocityY() * delta;
            boolean hitsFromAbove = hitbox.y <= s.y + s.height
                && prevFeetY >= s.y + s.height;

            // 3) Comprobación en X
            boolean overlapsX = hitbox.x + hitbox.width > s.x
                && hitbox.x < s.x + s.width;

            if (hitsFromAbove && overlapsX) {
                // Reposiciona justo encima
                player.setY(s.y + s.height);
                player.jumpHigher();
                break;
            }
        }
    }

    /**
     * Actualiza los muelles y elimina los que están fuera de la pantalla
     */
    private void updateSprings(float delta) {
        // Actualizar los muelles
        for (Spring spring : springs) {
            spring.update(delta);
        }

        // Eliminar muelles que estén fuera de la pantalla
        float bottomEdge = worldCamera.position.y - worldCamera.viewportHeight / 2f;
        Iterator<Spring> it = springs.iterator();
        while (it.hasNext()) {
            Spring spring = it.next();
            if (spring.getY() + spring.getHeight() < bottomEdge) {
                spring.dispose();
                it.remove();
            }
        }
    }

    /**
     * Genera nuevas plataformas arriba y elimina las viejas debajo
     */
    private void updatePlatforms() {
        float topEdge = worldCamera.position.y + worldCamera.viewportHeight / 2f;

        // A) Genera mientras la Y más alta esté por debajo del topEdge
        while (highestPlatformY < topEdge) {
            float nextY = highestPlatformY + MathUtils.random(SPAWN_GAP_MIN, SPAWN_GAP_MAX);
            float nextX = MathUtils.random(
                0f,
                Gdx.graphics.getWidth() - Platform.DEFAULT_WIDTH
            );

            if (random.nextFloat() < 0.2f) {
                FragilePlatform fp = new FragilePlatform(nextX, nextY, Platform.DEFAULT_WIDTH, assetManager);
                platforms.add(fp);
            } else {
                Platform newPlatform = new Platform(nextX, nextY, 150, 0, assetManager, false);
                platforms.add(newPlatform);
                highestPlatformY = nextY;
                // Posibilidad de crear un muelle en la plataforma
                if (random.nextFloat() < SPRING_PROBABILITY) {
                    createSpringOnPlatform(newPlatform);
                }
            }
        }

        // B) Elimina las plataformas que queden fuera de la parte inferior
        float bottomEdge = worldCamera.position.y - worldCamera.viewportHeight / 2f;
        Iterator<Platform> it = platforms.iterator();
        while (it.hasNext()) {
            Platform p = it.next();
            if (p.getY() + p.getHeight() < bottomEdge) {
                it.remove();
            }
        }
    }

    private void updateFragilePlatforms(float delta) {
        Iterator<Platform> it = platforms.iterator();
        while (it.hasNext()) {
            Platform p = it.next();
            if (p instanceof FragilePlatform) {
                FragilePlatform fp = (FragilePlatform) p;
                if (fp.updateAndCheckRemoval(delta)) {
                    it.remove(); // desaparece tras la animación
                }
            }
        }
    }

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
        for (Spring spring : springs) {
            spring.dispose();
        }
        touchLeftTexture.dispose();
        touchRightTexture.dispose();
        score.dispose();
    }
}
