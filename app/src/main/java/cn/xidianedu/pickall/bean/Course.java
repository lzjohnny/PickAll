package cn.xidianedu.pickall.bean;

/**
 * 每天5节课，上午两节下午两节，晚上一节（选修）
 * Created by ShiningForever on 2016/8/5.
 */
public class Course {
    private String num;
    private String name;
    private String location;
    private String time;
    private String teacher;

//    public Course(String num, String location, String name, String teacher, String time) {
//        this.location = location;
//        this.name = name;
//        this.num = num;
//        this.teacher = teacher;
//        this.time = time;
//    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
