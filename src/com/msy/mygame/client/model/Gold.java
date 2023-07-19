package com.msy.mygame.client.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 金币
 */
public class Gold extends Obstacle implements PaintElement{
    public Gold(int y) {
        super();
        this.setY(y);
        this.setWidth(20);
        this.setHeight(20);
        try {
            this.setImage(ImageIO.read(new File("Image/21.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void painElement(Graphics g) {
        g.drawImage(this.getImage(), this.getX(), this.getY(), this.getWidth(), this.getHeight(),null);
    }
}
