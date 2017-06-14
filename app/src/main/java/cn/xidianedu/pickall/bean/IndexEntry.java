package cn.xidianedu.pickall.bean;

/**
 * Created by ShiningForever on 2017/5/13.
 */

public class IndexEntry implements Comparable<IndexEntry> {
    public String oid;
    public double index;

    public IndexEntry(String oid, double index) {
        this.index = index;
        this.oid = oid;
    }

    public IndexEntry() {
    }

    // 逆序
    @Override
    public int compareTo(IndexEntry o) {
        if ((index - o.index) < 0) {
            return 1;
        } else if ((index - o.index) > 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
