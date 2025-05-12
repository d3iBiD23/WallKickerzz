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
    private Texture brokenFloatingPlatformTexture;
    private Texture fragilePlatformTexture;

    private Texture playerLeftStandingTexture;
    private Texture playerLeftJumpTexture;
    private Texture playerRightStandingTexture;
    private Texture playerRightJumpTexture;

    public AssetManager() {
        loadAssets();
    }

    private void loadAssets() {
        backgroundTexture = new Texture(Gdx.files.internal("PNG/Background.png"));
        groundPlatformTexture = new Texture(Gdx.files.internal("PNG/LandPiece_DarkGreen.png"));
        floatingPlatformTexture = new Texture(Gdx.files.internal("PNG/LandPiece_DarkGray.png"));
        playerTexture = new Texture(Gdx.files.internal("PNG/CharacterLeft_Jump.png"));
        springTexture = new Texture(Gdx.files.internal("PNG/Spring.png"));
        fragilePlatformTexture = new Texture(Gdx.files.internal("PNG/LandPiece_DarkMulticolored.png"));
        brokenFloatingPlatformTexture = new Texture(Gdx.files.internal("PNG/BrokenLandPiece_Multicolored.png"));
        playerLeftStandingTexture = new Texture(Gdx.files.internal("PNG/CharacterLeft_Standing.png"));
        playerLeftJumpTexture = new Texture(Gdx.files.internal("PNG/CharacterLeft_Jump.png"));
        playerRightStandingTexture = new Texture(Gdx.files.internal("PNG/CharacterRight_Standing.png"));
        playerRightJumpTexture = new Texture(Gdx.files.internal("PNG/CharacterRight_Jump.png"));
    }

    public Texture getSpringTexture() {
        return springTexture;
    }

    public TextureRegion getSpringRegion() {
        return new TextureRegion(springTexture);
    }

    public TextureRegion getPlayerLeftStandingRegion() {
        return new TextureRegion(playerLeftStandingTexture);
    }

    public TextureRegion getPlayerLeftJumpRegion() {
        return new TextureRegion(playerLeftJumpTexture);
    }

    public TextureRegion getPlayerRightStandingRegion() {
        return new TextureRegion(playerRightStandingTexture);
    }

    public TextureRegion getPlayerRightJumpRegion() {
        return new TextureRegion(playerRightJumpTexture);
    }

    public void drawBackground(SpriteBatch batch, float bottomY) {
        batch.draw(backgroundTexture, 0, bottomY, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public TextureRegion getGroundPlatformTexture() {
        return new TextureRegion(groundPlatformTexture, 13, 0, 35, 50);
    }

    public TextureRegion getBrokenFloatingPlatformTexture() {
        return new TextureRegion(brokenFloatingPlatformTexture);
    }

    public TextureRegion getFragilePlatformTexture() {
        return new TextureRegion(fragilePlatformTexture);
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
        brokenFloatingPlatformTexture.dispose();
        fragilePlatformTexture.dispose();
        playerLeftStandingTexture.dispose();
        playerLeftJumpTexture.dispose();
        playerRightStandingTexture.dispose();
        playerRightJumpTexture.dispose();
    }
}
