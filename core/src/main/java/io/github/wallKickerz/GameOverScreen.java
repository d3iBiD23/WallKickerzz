package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    private static final float LINE_SPACING = 30f; // espacio entre líneas

    public GameOverScreen(final Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        backgroundTexture = new Texture(Gdx.files.internal("PNG/Background.png"));
        buttonWideBg = new Texture(Gdx.files.internal("PNG/Buttens and Headers/ButtonWide_Beighe.png"));

        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        float btnW = buttonWideBg.getWidth() * BUTTON_SCALE;
        float btnH = buttonWideBg.getHeight() * BUTTON_SCALE;

        // Botón "Intentar de nuevo" (en 2 líneas: "INTENTAR" + "DE NUEVO")
        Stack retryBtn = createLetterButton("INTENTAR_DE_NUEVO", btnW, btnH);
        retryBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        // Botón "Volver al inicio" (en 2 líneas también)
        Stack menuBtn = createLetterButton("VOLVER_AL_INICIO", btnW, btnH);
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        table.add(retryBtn).size(btnW, btnH).pad(50).row();
        table.add(menuBtn).size(btnW, btnH).pad(10);

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
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
                    "PNG/Numbers, Letters and Icons/Letter_" + c + ".png"
                ));
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

    @Override public void resize(int w,int h){ stage.getViewport().update(w,h,true); }
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
