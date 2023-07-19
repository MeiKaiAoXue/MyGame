package com.msy.mygame.client.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 小螃蟹怪兽
 */
public class Monster_1 extends Obstacle implements PaintElement{
    public Monster_1() {
        super();
        this.setY(580);
        this.setWidth(50);
        this.setHeight(50);
        try {
            this.setImage(ImageIO.read(new File("image/a4.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void painElement(Graphics g) {
        g.drawImage(this.getImage(), this.getX(), this.getY(), this.getWidth(), this.getHeight(),null);
    }
}
