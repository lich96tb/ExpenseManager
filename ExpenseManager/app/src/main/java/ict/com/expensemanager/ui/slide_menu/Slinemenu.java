package ict.com.expensemanager.ui.slide_menu;

/**
 * Created by lich96tb on 1/17/2018.
 */

public class Slinemenu {
    private int img;
    private String name;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Slinemenu() {
    }

    public Slinemenu(int img, String name) {
        this.img = img;
        this.name = name;
    }
}
