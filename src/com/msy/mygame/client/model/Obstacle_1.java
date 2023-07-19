package com.msy.mygame.client.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 鱼叉障碍物
 */
public class Obstacle_1 extends Obstacle implements PaintElement{
    public Obstacle_1() {
        super();
        this.setY(0);
        this.setWidth(30);
        this.setHeight(500);
        try {
            this.setImage(ImageIO.read(new File("Image/11.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void painElement(Graphics g) {
        g.drawImage(this.getImage(), this.getX(), this.getY(), this.getWidth(), this.getHeight(),null);
    }
}
