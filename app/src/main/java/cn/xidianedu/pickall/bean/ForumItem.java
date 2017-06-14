package cn.xidianedu.pickall.bean;

/**
 * Created by ShiningForever on 2016/8/30.
 */
public class ForumItem {
    private String image;
    private String title;
    private String tag;
    private String name;
    private String time;
    private String num;

    public ForumItem(String image, String title, String tag, String name, String time, String num) {
        this.image = image;
        this.name = name;
        this.num = num;
        this.tag = tag;
        this.time = time;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
