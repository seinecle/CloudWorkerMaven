///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package APICalls;
//
//import Model.EdgeTwitter;
//import Model.Job;
//import Model.NodesAndEdges;
//import Model.TwitterUser;
//import OAuth.Timer;
//import Persistence.MongoMorphia;
//import akka.actor.ActorRef;
//import akka.actor.Props;
//import akka.actor.UntypedActor;
//import com.google.code.morphia.Datastore;
//import com.google.code.morphia.query.Query;
//import com.google.code.morphia.query.UpdateOperations;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import twitter4j.Twitter;
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
//public class GetFriendsAndTheirRelations extends UntypedActor {
//
//    Set<Integer> setIdsInitialSeed;
//    Set<Integer> setFriends = new HashSet();
//    Set<Integer> setConnections;
//    Set<TwitterUser> twitterUsers = new HashSet();
//    Set<EdgeTwitter> edges = new HashSet();
//    EdgeTwitter edge;
//    Twitter twitter;
//    int totalCalls;
//    Timer timer;
//    Job job;
//    MongoMorphia mm;
//    Datastore dsJobs;
//    ActorRef parent;
//    List<ActorRef> actors;
//    int callsGetFriendsReturned = 0;
//    int callsGetUsersShowReturned = 0;
//    int callsGetFriends = 0;
//    int callsGetUsersShow = 0;
//
//    UpdateOperations<Job> ops;
//    Query<Job> updateQuery;
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
//        return "/friends/ids";
//    }
//    private boolean result;
//
//    @Override
//    public void onReceive(Object message) throws Exception {
//
//        if (message instanceof MsgLaunchGetFriendsAndTheirRelations) {
//            System.out.println("Get Friends and relations Actor. My path is " + context().self().path());
//            System.out.println("Get Friends and relations Actor. My name is " + context().self().path().name());
//
//            parent = getSender();
//            MsgLaunchGetFriendsAndTheirRelations msg = (MsgLaunchGetFriendsAndTheirRelations) message;
//            this.mm = msg.getMm();
//            this.dsJobs = mm.getDsJobs();
//
//            this.job = msg.getWFWjob();
//            this.setIdsInitialSeed = msg.getSetIdsInitialSeed();
//            this.totalCalls = setIdsInitialSeed.size();
//
//            //sending tasks to GETFRIENDS actor
//            int countIdSeeds = 0;
//            Set<Integer> setIdsForOneActor = new HashSet();
//            for (Integer id : setIdsInitialSeed) {
//                countIdSeeds++;
//                if (countIdSeeds % GetFriends.callsPerTimeWindow() == 0) {
//                    ActorRef actor = getContext().actorOf(Props.create(GetFriends.class));
//                    MsgSetTwitterIdsAndMongo msgForActor = new MsgSetTwitterIdsAndMongo(setIdsForOneActor, mm);
//                    actor.tell(msgForActor, getSelf());
//                    setIdsForOneActor = new HashSet();
//                    callsGetFriends++;
//                }
//                setIdsForOneActor.add(id);
//            }
//            if (!setIdsForOneActor.isEmpty()) {
//                ActorRef actor = getContext().actorOf(Props.create(GetFriends.class));
//                MsgSetTwitterIdsAndMongo msgForActor = new MsgSetTwitterIdsAndMongo(setIdsForOneActor, mm);
//                actor.tell(msgForActor, getSelf());
//                callsGetFriends++;
//            }
//
//            //sending tasks to GETUSERSHOW actor
//            countIdSeeds = 0;
//            setIdsForOneActor = new HashSet();
//
//            for (Integer id : setIdsInitialSeed) {
//                countIdSeeds++;
//                if (countIdSeeds % GetUsersShow.callsPerTimeWindow() == 0) {
//                    ActorRef actor = getContext().actorOf(Props.create(GetUsersShow.class));
//                    MsgLaunchGetUsersShow msgForActor = new MsgLaunchGetUsersShow(setIdsForOneActor, mm);
//                    actor.tell(msgForActor, getSelf());
//                    setIdsForOneActor = new HashSet();
//                    callsGetUsersShow++;
//                }
//                setIdsForOneActor.add(id);
//            }
//            if (!setIdsForOneActor.isEmpty()) {
//                ActorRef actor = getContext().actorOf(Props.create(GetUsersShow.class));
//                MsgLaunchGetUsersShow msgForActor = new MsgLaunchGetUsersShow(setIdsForOneActor, mm);
//                actor.tell(msgForActor, getSelf());
//                callsGetUsersShow++;
//            }
//        }
//
//        //this is a message sent by child workers to say they don't have tokens anymore.
//        //this "gives back the hand" back to this actor, allowing for other messages (like: msgs of interruption) to reach the kids.
//        if (message instanceof String) {
//            String msgString = (String) message;
//            if (msgString.equals("out of tokens")) {
//                getSender().tell("get more tokens", getSelf());
//            }
//        }
//
//        //this is a message sent by the parent, saying the job has been interrupted. All actors should stop.
//        if (message instanceof MsgInterrupt) {
//            String childrenPath = getContext().self().path() + "/*";
//            getContext().actorSelection(childrenPath).tell(message, getSelf());
//            getContext().stop(getSelf());
//        }
//
//        //this is a message sent by the "Get Friends" child actor to return their results
//        //towards the end, this part includes a condition: if all children have returned, then close the actor.
//        if (message instanceof MsgMapTwitterIds) {
//
//            callsGetFriendsReturned++;
//            result = updateProgress();
//
//            MsgMapTwitterIds msgIds = (MsgMapTwitterIds) message;
//            for (Integer key : msgIds.getUserAndItsFriends().keySet()) {
//                setFriends = new HashSet();
//                setFriends.addAll(msgIds.getUserAndItsFriends().get(key));
//
//                setConnections = new HashSet();
//                setConnections.addAll(setIdsInitialSeed);
//                setConnections.retainAll(setFriends);
//                for (long target : setConnections) {
//                    edge = new EdgeTwitter(key, target);
//                    if (edge != null) {
//                        edges.add(edge);
//                    }
//                }
//            }
//
//            if (callsGetFriendsReturned + callsGetUsersShowReturned == callsGetFriends + callsGetUsersShow) {
//                result = closeActor();
//            }
//        }
//        if (message instanceof MsgSetTwitterUsers) {
//
//            callsGetUsersShowReturned++;
//            result = updateProgress();
//
//            MsgSetTwitterUsers msg = (MsgSetTwitterUsers) message;
//            twitterUsers.addAll(msg.getTwitterUsers());
//
//            if (callsGetFriendsReturned + callsGetUsersShowReturned == callsGetFriends + callsGetUsersShow) {
//                result = closeActor();
//            }
//
//        } else {
//            unhandled(message);
//        }
//    }
//
//    private boolean closeActor() {
//
//        System.out.println("closing the GetFriends and Their Relations Actor");
//        updateQuery = dsJobs.createQuery(Job.class).field("jobId").equal(job.getJobId());
//
//        //**************************************
//        //setting nodes and edges in a NodesAndEdges object.
//        NodesAndEdges graph = new NodesAndEdges();
//        graph.setEdges(edges);
//        graph.setNodes(twitterUsers);
//        
//        //saving this NodesAndEdges object in the TwitterJob
//        ops = dsJobs.createUpdateOperations(Job.class).set("twitterJob.graph", graph);
//        dsJobs.update(updateQuery, ops);
//        //**************************************
//
//        //**************************************
//        //saving the end time of the job to the db
//        ops = dsJobs.createUpdateOperations(Job.class).set("end", System.currentTimeMillis());
//        dsJobs.update(updateQuery, ops);
//        //**************************************
//
//        //**************************************
//        //sending a message on Twitter to inform the job is done
//
//        //**************************************
//        MsgStop msgStop = new MsgStop();
//        parent.tell(msgStop, getSelf());
//        getContext().stop(getSelf());
//
//        return true;
//
//    }
//
//    private boolean updateProgress() {
//        Query<Job> query = dsJobs.createQuery(Job.class).field("jobId").equal(job.getJobId());
//        UpdateOperations<Job> update;
//
//        Integer progress = (Integer) (callsGetUsersShowReturned + callsGetFriendsReturned) * 100 / (callsGetUsersShow + callsGetFriends);
//        job.setProgress(progress);
//        update = dsJobs.createUpdateOperations(Job.class).set("progress", progress);
//        dsJobs.update(query, update, true);
//
//        return true;
//    }
//
//}
