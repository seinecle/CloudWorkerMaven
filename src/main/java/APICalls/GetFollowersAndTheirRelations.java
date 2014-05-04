///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package APICalls;
//
//import Model.EdgeTwitter;
//import Model.Job;
//import OAuth.MyOwnTwitterFactory;
//import OAuth.Timer;
//import Persistence.MongoMorphia;
//import akka.actor.ActorRef;
//import akka.actor.UntypedActor;
//import com.google.code.morphia.query.Query;
//import com.google.code.morphia.query.UpdateOperations;
//import java.net.UnknownHostException;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.apache.commons.lang3.ArrayUtils;
//import twitter4j.Twitter;
//import twitter4j.TwitterException;
//
///*
// Copyright 2008-2013 Clement Levallois
// Authors : Clement Levallois <clementlevallois@gmail.com>
// Website : http://www.clementlevallois.net
//
//
// DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
//
// Copyright 2013 Clement Levallois. All rights reserved.
//
// The contents of this file are subject to the terms of either the GNU
// General Public License Version 3 only ("GPL") or the Common
// Development and Distribution License("CDDL") (collectively, the
// "License"). You may not use this file except in compliance with the
// License. You can obtain a copy of the License at
// http://gephi.org/about/legal/license-notice/
// or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
// specific language governing permissions and limitations under the
// License.  When distributing the software, include this License Header
// Notice in each file and include the License files at
// /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
// License Header, with the fields enclosed by brackets [] replaced by
// your own identifying information:
// "Portions Copyrighted [year] [name of copyright owner]"
//
// If you wish your version of this file to be governed by only the CDDL
// or only the GPL Version 3, indicate your decision by adding
// "[Contributor] elects to include this software in this distribution
// under the [CDDL or GPL Version 3] license." If you do not indicate a
// single choice of license, a recipient has the option to distribute
// your version of this file under either the CDDL, the GPL Version 3 or
// to extend the choice of license to its licensees as provided above.
// However, if you add GPL Version 3 code and therefore, elected the GPL
// Version 3 license, then the option applies only if the new code is
// made subject to such option by the copyright holder.
//
// Contributor(s): Clement Levallois
//
// */
//public class GetFollowersAndTheirRelations extends UntypedActor {
//
//    Set<Integer> setIdsInitialSeed;
//
//    Set<Integer> setFollowersCurrent;
//    Set<Integer> setConnections;
//    Set<EdgeTwitter> edges;
//    EdgeTwitter edge;
//    Twitter twitter;
//    Timer timer;
//    int totalCalls;
//    MongoMorphia mm;
//    Job job;
//    private ActorRef caller;
//
//    public static int callsPerTimeWindow() {
//        return 15;
//    }
//
//    public static int limitTimeWindow() {
//        return 15;
//    }
//
//    public static String apiEndPoint() {
//        return "/followers/ids";
//    }
//
//    @Override
//    public void onReceive(Object message) throws Exception {
//
//        if (message instanceof MsgLaunchGetFollowersAndTheirRelations) {
//            
//                        caller = getSender();
//            MsgLaunchGetFollowersAndTheirRelations msg = (MsgLaunchGetFollowersAndTheirRelations) message;
//            this.mm = msg.getMm();
//            this.job = msg.getWFWjob();
//            this.setIdsInitialSeed = msg.getSetIdsInitialSeed();
//            this.totalCalls = setIdsInitialSeed.size();
//            call();
//        }
//    }
//
//    public void call() throws UnknownHostException, InterruptedException {
//
//        MyOwnTwitterFactory factory = new MyOwnTwitterFactory();
//        twitter = factory.createOneTwitterInstance();
//
//        timer = new Timer(APICallPoints.GETFOLLOWERS, totalCalls, twitter, mm);
//        timer.start();
//
//        //now finds the connections inside this groups of followers, friends or both
//        //note that we use a method that counts the calls and puts a timer when the API rate limit is reached
//        //in this case, this is 15 calls to the getFollowers() API method per 15 minutes
//        //we also tell the user early on how long this is gonna last.
//        edges = new HashSet();
//
//        int count = setIdsInitialSeed.size();
//
//        UpdateOperations<Job> ops;
//        Query<Job> updateQuery;
//
//        //looping through all the User ids provided
//        for (int id : setIdsInitialSeed) {
//            boolean done = false;
//            while (!done) {
//                try {
//                    twitter = timer.canICallPlease();
//                    setFollowersCurrent = new HashSet();
//                    int[] ids = twitter.getFollowersIDs(id, -1).getIDs();
//                    setFollowersCurrent.addAll(Arrays.asList(ArrayUtils.toObject(ids)));
//                    timer.thatsOneMoreCall();
//                    setConnections = new HashSet();
//                    setConnections.addAll(setIdsInitialSeed);
//                    setConnections.retainAll(setFollowersCurrent);
//                    for (long target : setConnections) {
//                        edge = new EdgeTwitter(id, target);
//                        edges.add(edge);
//                        
//                        //**************************************
//                        //saving to the db. Quite a heavy op since the whole set of edges is written - an increment wd be better, wd necessitate arrays.
//                        ops = mm.getDsJobs().createUpdateOperations(Job.class).set("twitterJob.edges", edges);
//                        updateQuery = mm.getDsJobs().createQuery(Job.class).field("ownerIdGephi").equal(job.getOwnerIdGephi()).field("start").equal(job.getStart());
//                        mm.getDsJobs().update(updateQuery, ops);
//                        //**************************************
//                        
//                    }
//                    done = true;
//                } catch (TwitterException ex) {
//                    Logger.getLogger(GetFollowersAndTheirRelations.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//        timer.close();
//        caller.tell("over", getSelf());
//        job.setEnd(System.currentTimeMillis());
//
//    }
//
//}