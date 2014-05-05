/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CollectionOfMentions;

import APICalls.MsgInterrupt;
import APICalls.MsgLaunchCollectionMentionsTwitter;
import Control.SenderMsgToCentralServer;
import Model.AccessTokenPlus;
import Model.Job;
import Model.JobInfo;
import Model.Session;
import Model.TwitterStatus;
import OAuth.MyOwnTwitterFactory;
import Singletons.SharedMongoMorphiaInstance;
import Utils.ConvertStatus;
import akka.actor.UntypedActor;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.auth.AccessToken;

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
public class ControllerCollectionOfMentions extends UntypedActor {

    private String mention;
    private List<Status> statuses;
    private TwitterStream twitterStream;
    AccessToken accessToken;
    int numberOfMinutes;
    int numberOfHours;
    int numberOfDays;
    private Integer fromHour;
    private Integer fromDay;
    private Integer fromMonth;
    private Integer fromYear;
    private String now;
    private Job job;

    Datastore dsJobs;
    Datastore dsJobsInfo;
    Datastore dsSessions;

    UpdateOperations<JobInfo> opsJobInfo;
    Query<JobInfo> updateQueryJobInfo;
    UpdateOperations<Job> opsJob;
    Query<Job> updateQueryJob;

    int sizeBatch = 25;
    Long timeLastStatus = 0L;
    Long timeSinceLastStatus = 0L;
    DateTime startDateTime;
    Long stopTime;
    boolean accept = true;
    private String idGephi;
    private Long jobStart;

    public ControllerCollectionOfMentions() {
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof MsgLaunchCollectionMentionsTwitter) {

            MsgLaunchCollectionMentionsTwitter msg = (MsgLaunchCollectionMentionsTwitter) message;

            this.idGephi = msg.getIdGephi();
            this.jobStart = Long.decode(msg.getJobStart());

            this.mention = msg.getMention();
            this.now = msg.isNow();
            this.fromHour = msg.getFromHour();
            this.fromDay = msg.getFromDay();
            this.fromMonth = msg.getFromMonth();
            this.fromYear = msg.getFromYear();
            this.numberOfMinutes = msg.getForMinutes();
            this.numberOfHours = msg.getForHours();
            this.numberOfDays = msg.getForDays();

            this.dsJobs = SharedMongoMorphiaInstance.getDsJobs();
            this.dsJobsInfo = SharedMongoMorphiaInstance.getDsJobsInfos();
            this.dsSessions = SharedMongoMorphiaInstance.getDsSessions();

            Session session = dsSessions.find(Session.class).field("idGephi").equal(idGephi).get();
            String currentUser = session.getUser();
            Datastore dsAccessToken = SharedMongoMorphiaInstance.getDsAccessToken();
            accessToken = dsAccessToken.find(AccessTokenPlus.class).field("screen_name").equal(currentUser).get();

            MyOwnTwitterFactory factory = new MyOwnTwitterFactory();
            twitterStream = factory.createOneTwitterStreamInstance(accessToken);

            updateQueryJobInfo = dsJobsInfo.createQuery(JobInfo.class).field("idGephi").equal(this.idGephi).field("start").equal(jobStart);
            updateQueryJob = dsJobs.createQuery(Job.class).field("idGephi").equal(this.idGephi).field("start").equal(jobStart);

            run();
            System.out.println("run has returned!");

            //Send msg to central server about completion.
            SenderMsgToCentralServer sender = new SenderMsgToCentralServer();
            sender.streamIsTerminatedOK();
            
            
            //stop the current actor
            getContext().stop(getSelf());

        }

        if (message instanceof MsgInterrupt) {
            getContext().stop(getSelf());
        }
    }

    public void stopStream() {
    }

    public void run() {

        if (!now.equals("true")) {
            startDateTime = new DateTime(fromYear, fromMonth, fromDay, fromHour, 0);
        } else {
            startDateTime = new DateTime();
        }

        stopTime = startDateTime.getMillis() + numberOfMinutes * 60000 + numberOfHours * 3600000 + numberOfDays * 3600000 * 24;

        statuses = new ArrayList();

        final Object lock = new Object();

        StatusListener listener;
        listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                if (System.currentTimeMillis() > stopTime || new DateTime().isBefore(startDateTime)) {

                    //updating the job a last time;
                    //**************************************
                    //saving statuses to the db.
                    List<TwitterStatus> twitterStatuses = new ConvertStatus().convertAllToTwitterStatus(statuses);
                    if (!twitterStatuses.isEmpty()) {
                        opsJob = dsJobs.createUpdateOperations(Job.class).addAll("statuses", twitterStatuses, true);
                        dsJobs.update(updateQueryJob, opsJob);
                    }
                    //updating progress a last time;
                    Long progressLong = (Long) ((System.currentTimeMillis() - startDateTime.getMillis()) * 100 / (stopTime - startDateTime.getMillis()));
                    System.out.println("progress before closing: " + progressLong);

                    Integer progress = progressLong.intValue();
                    if (progress < 100) {
                        opsJobInfo = dsJobsInfo.createUpdateOperations(JobInfo.class).set("progress", progress);
                        dsJobsInfo.update(updateQueryJobInfo, opsJobInfo);
                    }
                    //**************************************

                    //recording the time when the job ended
                    opsJobInfo = dsJobsInfo.createUpdateOperations(JobInfo.class).set("end", System.currentTimeMillis());
                    dsJobsInfo.update(updateQueryJobInfo, opsJobInfo);
                    //**************************************

                    synchronized (lock) {
                        lock.notify();
                    }
                    System.out.println("unlocked");

                } else {

                    System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                    statuses.add(status);
                    timeSinceLastStatus = System.currentTimeMillis() - timeLastStatus;

                    //**************************************
                    //adjusting the frequency of saves to DB, function of number of statuses received per second
                    if (timeSinceLastStatus < 200) {
                        sizeBatch = 100;
                    } else {
                        sizeBatch = 25;
                    }
                    timeLastStatus = System.currentTimeMillis();

                    if (statuses.size() > sizeBatch) {

                        //**************************************
                        //saving statuses to the db.
                        List<TwitterStatus> twitterStatuses = new ConvertStatus().convertAllToTwitterStatus(statuses);
                        opsJob = dsJobs.createUpdateOperations(Job.class).addAll("statuses", twitterStatuses, true);
                        dsJobs.update(updateQueryJob, opsJob);

                        statuses = new ArrayList();

                        //updating progress.
                        Long progressLong = (Long) ((System.currentTimeMillis() - startDateTime.getMillis()) * 100 / (stopTime - startDateTime.getMillis()));
                        System.out.println("progress: " + progressLong);
                        Integer progress = progressLong.intValue();
                        opsJobInfo = dsJobsInfo.createUpdateOperations(JobInfo.class).set("progress", progress);
                        dsJobsInfo.update(updateQueryJobInfo, opsJobInfo);
                        //**************************************
                    }
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onException(Exception ex) {
                System.out.println("Exception: " + ex);
            }

            @Override
            public void onStallWarning(StallWarning sw) {
                System.out.println("Got stall warning:" + sw.getMessage());

            }
        };
        twitterStream.addListener(listener);

        FilterQuery fq = new FilterQuery();
        String[] mentions = {mention};
        fq.track(mentions);

//        twitterStream.filter(new FilterQuery(0, users, keywords));
        twitterStream.filter(fq);

        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
        }
        System.out.println("returning statuses");
        try {
            twitterStream.shutdown();
        } catch (Exception e) {
            System.out.println("exception when shutdown of twitter stream");
            System.out.println("error: " + e.getMessage());
        }
        System.out.println("shutdown of twitter stream was successful");

    }
}
