package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Pantalla de menú principal con fondo de juego y botones ampliados.
 */
public class MainMenuScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private Texture buttonWideBg;
    private static final float PLAY_BUTTON_SCALE = 4.5f; // Escala para botones
    private static final float LETTER_SCALE = 2f; // Escala para letras
    private static final float LETTER_PAD = 15f; // Padding entre letras

    public MainMenuScreen(final Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        // Fondo de la pantalla
        backgroundTexture = new Texture(Gdx.files.internal("PNG/Background.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        // Cargar fondo del botón
        buttonWideBg = new Texture(Gdx.files.internal(
            "PNG/Buttens and Headers/ButtonWide_Beighe.png"));
        float btnWidth = buttonWideBg.getWidth() * PLAY_BUTTON_SCALE;
        float btnHeight = buttonWideBg.getHeight() * PLAY_BUTTON_SCALE;

        // Tabla raíz
        Table root = new Table();
        root.setFillParent(true);
        root.center();

        // Botón JUGAR
        Stack playStack = createLetterButton("PLAY", btnWidth, btnHeight);
        playStack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        // Botón SALIR
        Stack exitStack = createLetterButton("EXIT", btnWidth, btnHeight);
        exitStack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Añadir botones
        root.add(playStack).size(btnWidth, btnHeight).pad(200).row();
        root.add(exitStack).size(btnWidth, btnHeight).pad(20);
        stage.addActor(root);

        // Crear tabla para el high score
        Table highScoreTable = new Table();
        highScoreTable.center();

        // Obtener el high score
        int highScore = PreferencesManager.getHighScore();

        // Crear fuente para el high score
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("font/Schoolbell-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 60;
        param.color = Color.WHITE;
        param.borderWidth = 5;
        param.borderColor = Color.BLACK;
        BitmapFont highScoreFont = gen.generateFont(param);

        // Mostrar high score
        highScoreTable.add(new Label("High Score: " + highScore, new Label.LabelStyle(highScoreFont, Color.WHITE)));

        // Añadir la tabla de high score a la tabla principal
        root.add(highScoreTable).colspan(2).padBottom(50f).row();

        gen.dispose();
    }

    /**
     * Crea un Stack con fondo de botón y las letras del texto dado.
     */
    private Stack createLetterButton(String text, float width, float height) {
        Stack stack = new Stack();
        // Fondo
        Image bgImage = new Image(new TextureRegionDrawable(buttonWideBg));
        bgImage.setSize(width, height);
        stack.add(bgImage);
        // Letras centradas
        Table lettersTable = new Table();
        lettersTable.center();
        for (char c : text.toCharArray()) {
            Texture letterTex = new Texture(Gdx.files.internal(
                "PNG/Numbers, Letters and Icons/Letter_" + c + ".png"));
            Image letterImg = new Image(letterTex);
            letterImg.setOrigin(0, 0);
            letterImg.setScale(LETTER_SCALE);
            lettersTable.add(letterImg).pad(LETTER_PAD);
        }
        stack.add(lettersTable);
        return stack;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
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
        backgroundTexture.dispose();
        buttonWideBg.dispose();
    }
}
