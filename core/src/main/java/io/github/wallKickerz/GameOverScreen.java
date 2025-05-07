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
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Texture backgroundTexture;
    private Texture buttonWideBg;
    private static final float BUTTON_SCALE = 5.5f;
    private static final float LETTER_SCALE = 2f;
    private static final float LETTER_PAD = 15f;
    private static final float LINE_SPACING = 30f;

    public GameOverScreen(final Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        // Configuración de fondo
        backgroundTexture = new Texture(Gdx.files.internal("PNG/Background.png"));
        buttonWideBg = new Texture(Gdx.files.internal("PNG/Buttens and Headers/ButtonWide_Beighe.png"));

        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        // Tabla principal
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // Configuración de botones
        float btnW = buttonWideBg.getWidth() * BUTTON_SCALE;
        float btnH = buttonWideBg.getHeight() * BUTTON_SCALE;

        // Tabla para los scores
        Table scoreTable = new Table();

        // Fuentes
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("font/Schoolbell-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // ===== TÍTULO GAME OVER =====
        FreeTypeFontGenerator.FreeTypeFontParameter titleParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParam.size = 180;
        titleParam.color = Color.WHITE;
        titleParam.borderWidth = 12; // borde más grueso
        titleParam.borderColor = Color.BLACK;
        BitmapFont titleFont = gen.generateFont(titleParam);
        Label titleLabel = new Label("GAME OVER", new Label.LabelStyle(titleFont, Color.WHITE));
        scoreTable.add(titleLabel).padBottom(160f).row();

        // Scores
        int finalScore = game.getCurrentScore();
        int highScore = game.getHighScore();

        // ===== HIGH SCORE =====
        FreeTypeFontGenerator.FreeTypeFontParameter highScoreParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        highScoreParam.size = 100;
        highScoreParam.color = Color.WHITE;
        highScoreParam.borderWidth = 8f; // borde más fino
        highScoreParam.borderColor = Color.BLACK;
        BitmapFont highScoreFont = gen.generateFont(highScoreParam);
        Label highScoreLabel = new Label("Highest Score: " + highScore, new Label.LabelStyle(highScoreFont, Color.WHITE));
        scoreTable.add(highScoreLabel).padBottom(90f).row();


        // High score
        param.size = 100;
        scoreTable.add(highScoreLabel).padBottom(90f).row();;

        // ===== SCORE ACTUAL =====
        FreeTypeFontGenerator.FreeTypeFontParameter scoreParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        scoreParam.size = 90;
        scoreParam.color = Color.WHITE;
        scoreParam.borderWidth = 8f; // aún más fino si deseas
        scoreParam.borderColor = Color.BLACK;
        BitmapFont scoreFont = gen.generateFont(scoreParam);
        Label scoreLabel = new Label("Your Score: " + finalScore, new Label.LabelStyle(scoreFont, Color.WHITE));
        scoreTable.add(scoreLabel).padBottom(90f);


        // Añadir scores a la tabla principal
        mainTable.add(scoreTable).colspan(1).row();

        // Botones
        Stack retryBtn = createLetterButton("TRY_AGAIN", btnW, btnH);
        retryBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        Stack menuBtn = createLetterButton("BACK_TO_MENU", btnW, btnH);
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        // Tabla para botones
        Table buttonsTable = new Table();
        buttonsTable.add(retryBtn).size(btnW, btnH).pad(20).row();
        buttonsTable.add(menuBtn).size(btnW, btnH).pad(20);

        mainTable.add(buttonsTable).center();

        Gdx.input.setInputProcessor(stage);
        gen.dispose();
    }

    private Stack createLetterButton(String text, float width, float height) {
        Stack stack = new Stack();
        Image bg = new Image(new TextureRegionDrawable(buttonWideBg));
        bg.setSize(width, height);
        stack.add(bg);

        Table mainTable = new Table();
        mainTable.center();

        String[] lines = text.split("_");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Table lineTable = new Table();
            lineTable.center();

            for (char c : line.toCharArray()) {
                Texture lt = new Texture(Gdx.files.internal(
                    "PNG/Numbers, Letters and Icons/Letter_" + c + ".png"));
                Image img = new Image(lt);
                img.setOrigin(0, 0);
                img.setScale(LETTER_SCALE);
                lineTable.add(img).pad(LETTER_PAD);
            }

            if (i < lines.length - 1) {
                mainTable.add(lineTable).padBottom(LINE_SPACING).row();
            } else {
                mainTable.add(lineTable).row();
            }
        }

        stack.add(mainTable);
        return stack;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        buttonWideBg.dispose();
    }
}
