package com.msy.mygame.client.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 导弹
 */
public class Missile extends Obstacle implements PaintElement{
    public Missile(int y) {
        super();
        type = "Missile";
        this.setY(y);
        this.setWidth(100);
        this.setHeight(50);
        try {
            this.setImage(ImageIO.read(new File("Image/daodan.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void paintElement(Graphics g) {
        g.drawImage(this.getImage(), this.getX(), this.getY(), this.getWidth(), this.getHeight(),null);
    }
}
