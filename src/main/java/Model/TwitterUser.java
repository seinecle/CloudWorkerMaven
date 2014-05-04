/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import com.google.code.morphia.annotations.Id;
import java.util.Date;
import org.bson.types.ObjectId;

/**
 *
 * @author C. Levallois
 */
public class TwitterUser {

    @Id
    private ObjectId id;
    private String screen_name;
    private String screen_name_ignorecase;
    private String name;
    private String location;
    private String description;
    private int followers_count;
    private int friends_count;
    private String profile_image_url;
    private String profile_banner_url;
    private String url;
    private Date created_at;
    private String lang;
    private long idTwitter;

    public TwitterUser() {
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getScreen_name_ignorecase() {
        return screen_name.toLowerCase();
    }

    public void setScreen_name_ignorecase(String screen_name_ignorecase) {
        this.screen_name_ignorecase = screen_name_ignorecase;
    }

    public String getName() {
        if (name == null) {
            return "no name detected";
        } else {
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        if (location == null) {
            return "no location available";
        } else {
            return location;
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        if (description == null){
            return "no description available";
        }
        else{
        return description;}
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public long getIdTwitter() {
        return idTwitter;
    }

    public void setIdTwitter(long idTwitter) {
        this.idTwitter = idTwitter;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public String getProfile_banner_url() {
        return profile_banner_url;
    }

    public void setProfile_banner_url(String profile_banner_url) {
        this.profile_banner_url = profile_banner_url;
    }

    
    

    @Override
    public String toString() {
        return screen_name + "(" + location + ")";

    }
}
