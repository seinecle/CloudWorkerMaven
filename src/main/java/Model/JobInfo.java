/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.UUID;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

/*
 Copyright 2008-2013 Clement Levallois
 Authors : Clement Levallois <clementlevallois@gmail.com>
 Website : http://www.clementlevallois.net


 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2013 Clement Levallois. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s): Clement Levallois

 */
public class JobInfo {

    @Id
    private ObjectId id;
    private long start;
    private long end;
    private String ownerScreenName;
    private String description;
    private String status;
    private String idGephi;
    private UUID jobId;
    private Integer progress;
    private boolean twitterStream;
    private int nbTweets;

    public JobInfo() {
    }

    public void setIdGephi(String ownerIdGephi) {
        this.idGephi = ownerIdGephi;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getOwnerScreenName() {
        return ownerScreenName;
    }

    public void setOwnerScreenName(String ownerScreenName) {
        this.ownerScreenName = ownerScreenName;
    }

    public String getIdGephi() {
        return idGephi;
    }

    public UUID getJobId() {
        return jobId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public boolean isTwitterStream() {
        return twitterStream;
    }

    public void setTwitterStream(boolean twitterStream) {
        this.twitterStream = twitterStream;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getNbTweets() {
        return nbTweets;
    }

    public void setNbTweets(int nbTweets) {
        this.nbTweets = nbTweets;
    }
}
