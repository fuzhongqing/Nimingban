package com.fuzho.nimingban.pojo;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by fuzho on 2016/9/20.
 */
public class SendMessage implements Serializable {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    private String email,name,title,content;
    private Bitmap img;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("a Message :");
        sb.append("{");

        sb.append("name");
        sb.append(":");
        sb.append(this.name);
        sb.append(",");

        sb.append("title");
        sb.append(":");
        sb.append(this.title);
        sb.append(",");

        sb.append("email");
        sb.append(":");
        sb.append(this.email);
        sb.append(",");

        sb.append("content");
        sb.append(":");
        sb.append(this.content);

        if (img != null) {
            sb.append(",");
            sb.append("bitmap");
            sb.append(":");
            sb.append(this.img.getHeight() + "*" + img.getWidth());
        }

        sb.append("}");
        return sb.toString();
    }
}
