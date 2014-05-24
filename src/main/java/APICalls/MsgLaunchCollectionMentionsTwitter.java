/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APICalls;


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
public class MsgLaunchCollectionMentionsTwitter {

    String ck;
    String cks;
    String at;
    String ats;
    String jobId;
    String app;
    String idGephi;
    String jobStart;
    String now;
    String mention;
    Integer fromHour;
    Integer fromDay;
    Integer fromMonth;
    Integer fromYear;
    Integer forMinutes;
    Integer forHours;
    Integer forDays;

    public MsgLaunchCollectionMentionsTwitter(String ck, String cks, String at, String ats, String jobId, String app, String idGephi, String jobStart, String now, Integer fromHour, Integer fromDay, Integer fromMonth, Integer fromYear, String mention, Integer forMinutes, Integer forHours, Integer forDays) {
        this.ck = ck;
        this.cks = cks;
        this.at = at;
        this.ats = ats;
        this.jobId = jobId;
        this.app = app;
        this.idGephi = idGephi;
        this.jobStart = jobStart;
        this.now = now;
        this.mention = mention;
        this.fromHour = fromHour;
        this.fromDay = fromDay;
        this.fromMonth = fromMonth;
        this.fromYear = fromYear;
        this.forMinutes = forMinutes;
        this.forHours = forHours;
        this.forDays = forDays;
    }

    public String getCk() {
        return ck;
    }

    public String getCks() {
        return cks;
    }

    public String getAt() {
        return at;
    }

    public String getAts() {
        return ats;
    }

    
    
    public String getIdGephi() {
        return idGephi;
    }

    public String getJobStart() {
        return jobStart;
    }

    public String isNow() {
        return now;
    }

    public Integer getFromHour() {
        return fromHour;
    }

    public Integer getFromDay() {
        return fromDay;
    }

    public Integer getFromMonth() {
        return fromMonth;
    }

    public Integer getFromYear() {
        return fromYear;
    }

    public Integer getForMinutes() {
        return forMinutes;
    }

    public Integer getForDays() {
        return forDays;
    }

    public Integer getForHours() {
        return forHours;
    }

    public String getMention() {
        return mention;
    }

    public String getApp() {
        return app;
    }

    public String getJobId() {
        return jobId;
    }

}
