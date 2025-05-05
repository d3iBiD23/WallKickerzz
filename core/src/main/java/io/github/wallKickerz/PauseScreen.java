package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class PauseScreen implements Screen {
    private final Main game;
    private final GameScreen previousScreen;
    private Stage stage;

    private Texture buttonWideBg;
    private static final float BUTTON_SCALE = 4.5f;
    private static final float LETTER_SCALE = 2f;
    private static final float LETTER_PAD   = 15f;

    public PauseScreen(Main game, GameScreen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // 1) Carga la textura del fondo de botón
        buttonWideBg = new Texture(Gdx.files.internal(
            "PNG/Buttens and Headers/ButtonWide_Beighe.png"
        ));

        float btnW = buttonWideBg.getWidth()  * BUTTON_SCALE;
        float btnH = buttonWideBg.getHeight() * BUTTON_SCALE;

        // 2) Monta la UI usando createLetterButton
        Table root = new Table();
        root.setFillParent(true);
        root.center();

        // Botón “Reanudar”
        Stack resumeBtn = createLetterButton("RESUME", btnW, btnH);
        resumeBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(previousScreen);
            }
        });

        // Botón “Menu Principal” (sin espacio: usaremos guión bajo para salto)
        Stack menuBtn = createLetterButton("BACK_TO_MENU", btnW, btnH);
        menuBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        root.add(resumeBtn).size(btnW, btnH).pad(20).row();
        root.add(menuBtn)  .size(btnW, btnH).pad(20);

        stage.addActor(root);
    }

    /**
     * Copia exacta de MainMenuScreen.createLetterButton(...)
     * Crea un Stack con fondo y letras en fila (salto de línea con '_')
     */
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
                // Si no es la última línea, añadimos padding inferior
                mainTable.add(lineTable).padBottom(30f).row(); // <<--- aquí pones el padding que quieras
            } else {
                // Última línea, sin padding extra
                mainTable.add(lineTable).row();
            }
        }

        stack.add(mainTable);
        return stack;
    }


    @Override
    public void render(float delta) {
        previousScreen.render(0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w,h,true); }
    @Override public void show()   {}
    @Override public void hide()   {}
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void dispose(){
        stage.dispose();
        buttonWideBg.dispose();
    }
}
