package org.po;

/**
 * description:
 * author:xiangyang
 */
public class BilibiliPo {

    private  String name;
    private  String url;
    private  String  id;
    private  String time;

    public BilibiliPo(String name, String url, String id, String time) {
        this.name = name;
        this.url = url;
        this.id = id;
        this.time = time;
    }

    public BilibiliPo() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "BilibiliPo{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", id=" + id +
                ", time='" + time + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
