package utot.utot.customobjects;

/**
 * Created by elysi on 1/10/2017.
 */

public class Instructions {
    private int img;
    private String text;

    public Instructions(int img, String text) {
        this.img = img;
        this.text = text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
