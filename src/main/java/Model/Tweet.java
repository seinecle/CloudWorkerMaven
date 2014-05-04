/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Tweet implements Serializable {

    private String source;
    private TwitterUser source_user;
    private String text;
    private String target;
    private String monthYear;
    private Date date;

    public Tweet() {
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target.replaceAll("\\@", "").trim();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }


    public TwitterUser getSource_user() {
        return this.source_user;
    }

    public void setSource_user(TwitterUser source_user) {
        this.source_user = source_user;
    }
}
