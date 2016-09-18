package com.fuzho.nimingban.pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fuzho on 2016/8/30.
 */
public class Menu {
    private String name;
    private int sort;
    private String id;
    private String status;
    private String msg;
    private String fgroup;
    private String showName;
    private long interval;
    private String createdAt;
    private String updateAt;
    private ArrayList<Menu> sMenu = null;

    public Menu(JSONObject obj) throws JSONException {
        if (obj.has("id")) this.id = obj.getString("id");
        if (obj.has("name")) this.name = obj.getString("name");
        if (obj.has("sort")) this.sort = obj.getInt("sort");
        if (obj.has("status")) this.status = obj.getString("status");
        if (obj.has("showName")) this.showName = obj.getString("showName");
        if (obj.has("interval")) this.interval = obj.getLong("interval");
        if (obj.has("createdAt")) this.createdAt = obj.getString("createdAt");
        if (obj.has("updateAt")) this.updateAt = obj.getString("updateAt");

        if (obj.has("forums")) {
            sMenu = new ArrayList<>();
            JSONArray jarr = obj.getJSONArray("forums");
            for (int i = 0;i < jarr.length();++i) {
                sMenu.add(new Menu(jarr.getJSONObject(i)));
            }
        }
    }

    public int getSort() {
        return sort;
    }

    public String getFgroup() {
        return fgroup;
    }

    public void setFgroup(String fgroup) {
        this.fgroup = fgroup;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<Menu> getsMenu() {
        return sMenu;
    }

    public void setsMenu(ArrayList<Menu> sMenu) {
        this.sMenu = sMenu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
