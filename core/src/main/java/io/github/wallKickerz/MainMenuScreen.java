package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Pantalla de menú principal con opciones para iniciar el juego o salir.
 */
public class MainMenuScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;

    public MainMenuScreen(final Main game) {
        this.game = game;
        // Crear escenario y skin
        stage = new Stage(new ScreenViewport());
        skin  = new Skin(Gdx.files.internal("uiskin.json")); // Asegúrate de incluir este archivo en assets

        // Tabla para organizar UI
        Table table = new Table(skin);
        table.setFillParent(true);

        // Título del juego
        Label title = new Label("WallKickerz", skin);
        title.setFontScale(2f);

        // Botones
        TextButton play = new TextButton("Jugar", skin);
        TextButton exit = new TextButton("Salir", skin);

        // Listener para Jugar
        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
        // Listener para Salir
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        // Añadir elementos a la tabla
        table.add(title).padBottom(40).row();
        table.add(play).size(200, 50).pad(10).row();
        table.add(exit ).size(200, 50).pad(10);

        // Agregar tabla al escenario
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        // No usado
    }

    @Override
    public void render(float delta) {
        // Limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Actualizar y dibujar UI
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // No usado
    }

    @Override
    public void resume() {
        // No usado
    }

    @Override
    public void hide() {
        // No usado
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
