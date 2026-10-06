package com.mycompany.test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = gp.screenWidth / 2;
        y = gp.screenHeight / 2;
        speed = 10;
        direction = "up";
    }

    public void getPlayerImage() {

        try {

            up1 = ImageIO.read(getClass().getResourceAsStream("/player/Scuttle_up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/Scuttle_up1.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/Scuttle_up1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/Scuttle_up1.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/Scuttle_up1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/Scuttle_up1.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/Scuttle_up1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/Scuttle_up1.png"));

            // change these for all 8 sprites
        } catch (IOException e) {

            System.getLogger(GamePanel.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);

        } catch (IllegalArgumentException e) {

            System.err.println("Could not find one or more image resources in /player/");

            e.printStackTrace();

        }
    }

    public void update() {
        if (keyH.upReleased == true) {
            direction = "up";
            y -= speed;
            keyH.upReleased = false;
        }

        if (keyH.downReleased == true) {
            direction = "down";
            y += speed;
            keyH.downReleased = false;
        }

        if (keyH.leftReleased == true) {
            direction = "left";
            x -= speed;
            keyH.leftReleased = false;
        }

        if (keyH.rightReleased == true) {
            direction = "right";
            x += speed;
            keyH.rightReleased = false;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = up1; // null

        switch (direction) {
            case "up":
                image = up1;
                break;
            case "down":
                image = down1;
                break;
            case "left":
                image = left1;
                break;
            case "right":
                image = right1;
                break;
        }
        
        // change these for all 8 sprites

        if (image != null) {
            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        }
    }
}
