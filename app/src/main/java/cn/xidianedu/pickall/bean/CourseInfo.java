package cn.xidianedu.pickall.bean;

import java.util.List;

/**
 * Created by ShiningForever on 2016/8/13.
 */
public class CourseInfo {

    private List<Course> course_tomorrow;
    private List<Course> course_today;

    public List<Course> getCourse_tomorrow() {
        return course_tomorrow;
    }

    public void setCourse_tomorrow(List<Course> course_tomorrow) {
        this.course_tomorrow = course_tomorrow;
    }

    public List<Course> getCourse_today() {
        return course_today;
    }

    public void setCourse_today(List<Course> course_today) {
        this.course_today = course_today;
    }
}