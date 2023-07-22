package com.msy.mygame.client.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class OtherPerson implements PaintElement{
    String Id = "";

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    private Socket socket;

    private final int OTHERWIDTH = 80;//人物的宽
    private final int OTHERHEIGHT = 80;//人物的高
    private int otherX;

    public int getOtherX() {
        return otherX;
    }

    public void setOtherX(int otherX) {
        this.otherX = otherX;
    }

    public int getOtherY() {
        return otherY;
    }

    public void setOtherY(int otherY) {
        this.otherY = otherY;
    }

    private int otherY;
    private Image image;//人物当前显示图片
    private ArrayList<Image> images = new ArrayList<>();//人物的所有图片

//    private static final OtherPerson otherPerson = new OtherPerson();

    public OtherPerson(Socket socket, String Id) {
        index = 0;
        loadImages();
        this.socket = socket;
        this.Id = Id;
    }

//    public static OtherPerson getOtherPerson() {
//        return otherPerson;
//    }

    //加载其他玩家所有图片
    private void loadImages() {
        for (int i = 0; i < 9; i++) {
            try {
                images.add(ImageIO.read(new File("Image/" + (i+1) + ".png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //人物一直向前移动
    private int index;
    public void step() {
        image = images.get(index ++  % images.size());//切换人物图片
    }

    public void paintElement(Graphics g) {
        g.drawImage(image, otherX, otherY, OTHERWIDTH, OTHERHEIGHT,null);
    }
}
