package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen implements Screen {
    private final Main game;
    private final GameScreen previousScreen;
    private Stage stage;
    private Skin skin;
    private OrthographicCamera hudCamera;
    private Texture overlay;

    public PauseScreen(Main game, GameScreen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;

        // Stage para botones
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        // Cámara HUD para overlay
        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Textura semitransparente negra para oscurecer fondo
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.5f);
        pixmap.fill();
        overlay = new Texture(pixmap);
        pixmap.dispose();

        // UI de pausa
        Table table = new Table(skin);
        table.setFillParent(true);
        table.center();

        TextButton resumeBtn = new TextButton("Reanudar", skin);
        TextButton menuBtn   = new TextButton("Menu Principal", skin);
        resumeBtn.getLabel().setFontScale(3f);
        menuBtn.getLabel().setFontScale(3f);

        resumeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(previousScreen);
            }
        });
        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.add(resumeBtn).size(400, 100).pad(20).row();
        table.add(menuBtn)  .size(400, 100).pad(20);
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Render del juego en pausa (frame estático)
        previousScreen.render(0f);

        // Dibujar overlay semitransparente
        SpriteBatch batch = game.getBatch();
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(overlay, 0, 0, hudCamera.viewportWidth, hudCamera.viewportHeight);
        batch.end();

        // Dibujar interfaz de pausa encima
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        hudCamera.setToOrtho(false, width, height);
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
        stage.dispose();
        skin.dispose();
        overlay.dispose();
    }

}
