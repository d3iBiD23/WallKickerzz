package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {
    private Texture backgroundTexture;
    private Texture platformTexture;
    private Texture playerTexture;

    private Texture groundPlatformTexture; // Textura para el suelo
    private Texture floatingPlatformTexture; // Textura para plataformas flotantes
    private Texture springTexture;

    public AssetManager() {
        loadAssets();
    }

    private void loadAssets() {
        backgroundTexture = new Texture(Gdx.files.internal("PNG/Background.png"));
        groundPlatformTexture = new Texture(Gdx.files.internal("PNG/LandPiece_DarkGreen.png"));
        floatingPlatformTexture = new Texture(Gdx.files.internal("PNG/LandPiece_DarkGray.png"));
        playerTexture = new Texture(Gdx.files.internal("PNG/CharacterLeft_Jump.png"));
        springTexture = new Texture(Gdx.files.internal("PNG/Spring.png"));

    }

    public Texture getSpringTexture() {
        return springTexture;
    }

    public TextureRegion getSpringRegion() {
        return new TextureRegion(springTexture);
    }

    public void drawBackground(SpriteBatch batch, float bottomY) {
        batch.draw(backgroundTexture, 0, bottomY, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public TextureRegion getGroundPlatformTexture() {
        return new TextureRegion(groundPlatformTexture, 13, 0, 35, 50);
    }

    public TextureRegion getFloatingPlatformTexture() {
        // Asegúrate de que la textura se carga con sus dimensiones originales
        return new TextureRegion(floatingPlatformTexture);
    }

    public TextureRegion getPlayerTexture() {
        return new TextureRegion(playerTexture);
    }

    public void dispose() {
        backgroundTexture.dispose();
        groundPlatformTexture.dispose();
        floatingPlatformTexture.dispose();
        playerTexture.dispose();
        springTexture.dispose();
    }
}
