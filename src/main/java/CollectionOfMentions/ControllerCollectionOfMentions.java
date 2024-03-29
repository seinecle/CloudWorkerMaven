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
import Model.JobInfo;
import Model.Session;
import Model.Tweet;
import OAuth.MyOwnTwitterFactory;
import Singletons.SharedMongoMorphiaInstance;
import akka.actor.UntypedActor;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.joda.time.DateTime;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
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
    private String app;
    private TwitterStream twitterStream;
    AccessToken accessToken;
    int numberOfMinutes;
    int numberOfHours;
    int numberOfDays;
    private String now;

    Datastore dsJobsInfo;
    Datastore dsSessions;
    Datastore dsTweets;

    UpdateOperations<JobInfo> opsJobInfo;
    Query<JobInfo> updateQueryJobInfo;

    int sizeBatch = 25;
    long timeLastStatus = 0L;
    long timeSinceLastStatus = 0L;
    DateTime startDateTime;
    long stopTime;
    boolean accept = true;
    private String idGephi;
    private long jobStart;
    int progress;
    Long progressLong;
    List<Long> statusesIds;
    String jobId;
    int nbTweets = 0;
    Tweet tweet;
    UUID jobUUID;
    String ck;
    String cks;
    String at;
    String ats;

    public ControllerCollectionOfMentions() {
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof MsgLaunchCollectionMentionsTwitter) {

            MsgLaunchCollectionMentionsTwitter msg = (MsgLaunchCollectionMentionsTwitter) message;
            this.ck = msg.getCk();
            this.cks = msg.getCks();
            this.at = msg.getAt();
            this.ats = msg.getAts();
            this.jobId = msg.getJobId();
            this.idGephi = msg.getIdGephi();
            this.jobStart = Long.decode(msg.getJobStart());
            this.app = msg.getApp();
            this.mention = msg.getMention();
            this.now = msg.isNow();
            this.numberOfMinutes = msg.getForMinutes();
            this.numberOfHours = msg.getForHours();
            System.out.println("hours controllercollectionmenttions: " + this.numberOfHours);

            this.numberOfDays = msg.getForDays();

            this.dsJobsInfo = SharedMongoMorphiaInstance.getDsJobsInfos();
            this.dsSessions = SharedMongoMorphiaInstance.getDsSessions();
            this.dsTweets = SharedMongoMorphiaInstance.getDsTweets();

            jobUUID = UUID.fromString(jobId);

            Session session = dsSessions.find(Session.class).field("idGephi").equal(idGephi).get();
            String currentUser = session.getUser();
            Datastore dsAccessToken = SharedMongoMorphiaInstance.getDsAccessToken();
            accessToken = dsAccessToken.find(AccessTokenPlus.class).field("screen_name").equal(currentUser).get();

            MyOwnTwitterFactory factory = new MyOwnTwitterFactory();
//            twitterStream = factory.createOneTwitterStreamInstance(accessToken);
            twitterStream = factory.createOneTwitterStreamInstanceFromApp(ck,cks,at,ats);

            updateQueryJobInfo = dsJobsInfo.createQuery(JobInfo.class).field("idGephi").equal(this.idGephi).field("start").equal(jobStart);

            run();

            //Send msg to central server about completion.
            SenderMsgToCentralServer sender = new SenderMsgToCentralServer();
            sender.streamIsTerminatedOK(idGephi, String.valueOf(jobStart), app);

            //stop the current actor
            getContext().stop(getSelf());

        }

        if (message instanceof MsgInterrupt) {
//            if (!twitterStatuses.isEmpty()) {
//                dsTweets.save(twitterStatuses);
//                opsJob = dsJobs.createUpdateOperations(Job.class).addAll("statuses", statusesIds, true);
//                dsJobs.update(updateQueryJob, opsJob);
//            }
//            //updating progress a last time;
//            progressLong = (Long) ((System.currentTimeMillis() - startDateTime.getMillis()) * 90 / (stopTime - startDateTime.getMillis()));
//            System.out.println("progress before closing: " + progressLong);
//            System.out.println("(progress is put back to 99% to allow for Excel file creation, after which it will be set to 100%");
//
//            progress = progressLong.intValue();
//            if (progress > 90) {
//                progress = 90;
//            }
//            opsJobInfo = dsJobsInfo.createUpdateOperations(JobInfo.class).set("progress", progress);
//            dsJobsInfo.update(updateQueryJobInfo, opsJobInfo);
//
//            opsJobInfo = dsJobsInfo.createUpdateOperations(JobInfo.class).set("nbTweets", nbTweets);
//            dsJobsInfo.update(updateQueryJobInfo, opsJobInfo);
//
//            //**************************************
//            //recording the time when the job ended
//            opsJobInfo = dsJobsInfo.createUpdateOperations(JobInfo.class).set("end", System.currentTimeMillis());
//            dsJobsInfo.update(updateQueryJobInfo, opsJobInfo);
//            //**************************************

            try {
                twitterStream.shutdown();
                System.out.println("shutdown of twitter stream was successful");
            } catch (Exception e) {
                System.out.println("exception when shutdown of twitter stream from stop message");
                System.out.println("error: " + e.getMessage());
            }
            //Send msg to central server about completion.
            SenderMsgToCentralServer sender = new SenderMsgToCentralServer();
            sender.streamIsTerminatedOK(idGephi, String.valueOf(jobStart), app);

            getContext().stop(getSelf());

        }
    }

    public void run() {

        startDateTime = new DateTime();

        //checks on dates to make sure it's not abobe 7 days
        if (numberOfMinutes < 0) {
            numberOfMinutes = 0;
        }
        if (numberOfMinutes > 59) {
            numberOfMinutes = 59;
        }
        if (numberOfHours > 24) {
            numberOfHours = 24;
        }
        if (numberOfHours < 0) {
            numberOfHours = 0;
        }
        if (numberOfDays > 7) {
            numberOfDays = 7;
        }
        if (numberOfDays < 0) {
            numberOfDays = 0;
        }

        stopTime = startDateTime.getMillis() + numberOfMinutes * 60000 + numberOfHours * 3600000 + numberOfDays * 3600000 * 24;
        if (stopTime - startDateTime.getMillis() > 3600000 * 24 * 7) {
            stopTime = startDateTime.getMillis() + 3600000 * 24 * 7;
        }

        //registers actual start time in the status field
        opsJobInfo = dsJobsInfo.createUpdateOperations(JobInfo.class).set("status", String.valueOf(startDateTime.getMillis()));
        dsJobsInfo.update(updateQueryJobInfo, opsJobInfo, false, WriteConcern.UNACKNOWLEDGED);

         //registers actual end time in the end field
        opsJobInfo = dsJobsInfo.createUpdateOperations(JobInfo.class).set("end", String.valueOf(stopTime));
        dsJobsInfo.update(updateQueryJobInfo, opsJobInfo, false, WriteConcern.UNACKNOWLEDGED);

        final Object lock = new Object();

        StatusListener listener;
        listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                nbTweets++;

                if (System.currentTimeMillis() > stopTime) {

                    //updating the job a last time;
                    //**************************************
                    //saving statuses to the db.
//                    if (!twitterStatuses.isEmpty()) {
//                        opsJob = dsJobs.createUpdateOperations(Job.class).addAll("statuses", statusesIds, true);
//                        dsJobs.update(updateQueryJob, opsJob);
//
//                        dsTweets.save(twitterStatuses);
//                    }
                    // 91 is the code for twitter stream has stopped collecting.
                    progress = 91;

                    //recording the progress, nbTweets and end time of the job
                    opsJobInfo = dsJobsInfo.createUpdateOperations(JobInfo.class).set("progress", progress).set("nbTweets", nbTweets).set("end", System.currentTimeMillis());
                    dsJobsInfo.update(updateQueryJobInfo, opsJobInfo);

                    synchronized (lock) {
                        lock.notify();
                    }

                } else {

                    tweet = new Tweet();
                    tweet.setStatus(TwitterObjectFactory.getRawJSON(status));
                    tweet.setIdTweet(nbTweets);
                    tweet.setJobId(jobUUID);
////                    System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
//                    twitterStatus = convertStatus.convertOneToTwitterStatus(status);
//                    twitterStatus.setJobId(jobUUID);
//                    twitterStatuses.add(twitterStatus);
//                    
//                    
//
//                    statusesIds.add(status.getId());
//                    timeSinceLastStatus = System.currentTimeMillis() - timeLastStatus;
//
//                    //**************************************
//                    //adjusting the frequency of saves to DB, function of number of statuses received per second
//                    if (timeSinceLastStatus < 200) {
//                        sizeBatch = 100;
//                    } else {
//                        sizeBatch = 25;
//                    }
//                    timeLastStatus = System.currentTimeMillis();
//                    progressLong = (Long) ((System.currentTimeMillis() - startDateTime.getMillis()) * 98 / (stopTime - startDateTime.getMillis()));

//                    if (statusesIds.size() > sizeBatch || progressLong.intValue() > progress) {
                    //**************************************
                    //saving statuses to the db.
                    try {
                        dsTweets.save(tweet, WriteConcern.UNACKNOWLEDGED);
                        opsJobInfo = dsJobsInfo.createUpdateOperations(JobInfo.class).set("nbTweets", nbTweets);
                        dsJobsInfo.update(updateQueryJobInfo, opsJobInfo, false, WriteConcern.UNACKNOWLEDGED);
                    } catch (MongoException m) {
                        System.out.println("saving of statuses to the db failed");
                    }
//                        twitterStatuses = new ArrayList();
//
//                        //**************************************
//                        //updating list of status ids of the job.
//                        opsJob = dsJobs.createUpdateOperations(Job.class).addAll("statuses", statusesIds, true);
//                        dsJobs.update(updateQueryJob, opsJob);
//                        statusesIds = new ArrayList();
//
//                        //updating progress.
//                        System.out.println("progress: " + progressLong);
//                        progress = progressLong.intValue();
//                        opsJobInfo = dsJobsInfo.createUpdateOperations(JobInfo.class).set("progress", progress).set("nbTweets", nbTweets);
//                        dsJobsInfo.update(updateQueryJobInfo, opsJobInfo);

                    //**************************************
//                    }
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
        String[] mentions = mention.split(",");
        fq.track(mentions);

//        twitterStream.filter(new FilterQuery(0, users, keywords));
        twitterStream.filter(fq);

        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
        }
        try {
            twitterStream.shutdown();
        } catch (Exception e) {
            System.out.println("exception when shutdown of twitter stream");
            System.out.println("error: " + e.getMessage());
        }
        System.out.println("shutdown of twitter stream was successful");

    }
}
