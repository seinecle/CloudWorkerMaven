/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Singletons;

import Control.Admin;
import Model.AccessTokenPlus;
import Model.Job;
import Model.JobInfo;
import Model.Session;
import Model.TwitterStatus;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

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
//@Singleton
//@Startup
// initialize at deployment time instead of first invocation
public class SharedMongoMorphiaInstance {

    static MongoClient mongoClient;
    static Morphia morphia;
    static Datastore dsAccessToken;
    static Datastore dsJobs;
    static Datastore dsJobsInfos;
    static Datastore dsSessions;
    static Datastore dsTweets;

//    @PostConstruct
    public static void loadConfiguration() {
        try {
            MongoCredential credential = MongoCredential.createMongoCRCredential("admin", "admin", "$$PASdefumee1984".toCharArray());
            ServerAddress server = new ServerAddress(Admin.ipMongo(), 27017);
            mongoClient = new MongoClient(server, Arrays.asList(credential));
            morphia = new Morphia();
            dsAccessToken = morphia.createDatastore(mongoClient, "AccessToken");
            dsJobs = morphia.createDatastore(mongoClient, "Job");
            dsJobsInfos = morphia.createDatastore(mongoClient, "JobInfo");
            dsSessions = morphia.createDatastore(mongoClient, "Session");
            dsTweets = morphia.createDatastore(mongoClient, "TwitterStatus");

            morphia.map(AccessTokenPlus.class);
            morphia.map(Job.class);
            morphia.map(JobInfo.class);
            morphia.map(Session.class);
            morphia.map(TwitterStatus.class);

        } catch (UnknownHostException ex) {
            Logger.getLogger(SharedMongoMorphiaInstance.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Datastore getDsAccessToken() {
        return dsAccessToken;
    }

    public static Datastore getDsJobs() {
        return dsJobs;
    }

    public static Datastore getDsSessions() {
        return dsSessions;
    }

    public static Datastore getDsJobsInfos() {
        return dsJobsInfos;
    }

    public static MongoClient getMongoClient() {
        return mongoClient;
    }

    public static Datastore getDsTweets() {
        return dsTweets;
    }

}
