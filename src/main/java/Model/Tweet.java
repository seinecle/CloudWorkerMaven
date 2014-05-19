/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.UUID;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

public class Tweet {

    @Id
    private ObjectId id;

    UUID jobId;
    long idTweet;
    String status;
    String lang;

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public long getIdTweet() {
        return idTweet;
    }

    public void setIdTweet(long idTweet) {
        this.idTweet = idTweet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    
    

}
