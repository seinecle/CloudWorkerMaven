///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package APICalls;
//
//import Model.TwitterUser;
//import OAuth.MyOwnTwitterFactory;
//import OAuth.Timer;
//import Persistence.MongoMorphia;
//import Utils.ConvertUsers;
//import akka.actor.ActorRef;
//import akka.actor.UntypedActor;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import org.apache.commons.lang3.ArrayUtils;
//import twitter4j.ResponseList;
//import twitter4j.Twitter;
//import twitter4j.TwitterException;
//import twitter4j.User;
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
//public class GetUsersShow extends UntypedActor {
//
//    Set<Integer> setIds;
//    List<String> listScreenNames;
//    Twitter twitter;
//    ResponseList<User> usersTwitter4J;
//    Set<User> users;
//    Timer timer;
//    int totalCalls;
//    private MongoMorphia mm;
//    ActorRef parent;
//    Set<TwitterUser> twitterUsers = new HashSet();
//
//    public static int callsPerTimeWindow() {
//        return 180;
//    }
//
//    public static int limitTimeWindow() {
//        return 15;
//    }
//
//    public static String apiEndPoint() {
//        return "/users/show/:id";
//    }
//
//    @Override
//    public void onReceive(Object message) throws Exception {
//        if (message instanceof MsgLaunchGetUsersShow) {
//
//            parent = getSender();
//
//            MsgLaunchGetUsersShow msg = (MsgLaunchGetUsersShow) message;
//            this.listScreenNames = msg.getListScreenNames();
//            this.mm = msg.getMm();
//            this.setIds = msg.getSetIds();
//
//            MyOwnTwitterFactory factory = new MyOwnTwitterFactory();
//            twitter = factory.createOneTwitterInstance();
//
//            timer = new Timer(APICallPoints.GETUSERSSHOW, totalCalls, twitter, mm);
//            timer.start();
//
//            call();
//        }
//
//        if (message instanceof MsgInterrupt) {
//            getContext().stop(getSelf());
//        }
//
//        if (message instanceof String) {
//            String msg = (String) message;
//            if (msg.equals("get more tokens")) {
//                timer.changeToken();
//                call();
//            }
//        }
//
//    }
//
//    public void call() throws UnknownHostException, InterruptedException {
//
//        users = new HashSet();
//
//        if (setIds != null) {
//            totalCalls = setIds.size();
//        } else {
//            totalCalls = listScreenNames.size();
//        }
//
//        // *********** case when we deal with ids (see below for screenNames)
//        if (setIds != null) {
//            List<Set<Integer>> sets = new ArrayList();
//            Set<Integer> setCurrent = new HashSet();
//            Set<Integer> set;
//            int counter = 0;
//            for (Integer id : setIds) {
//                if (counter == 100) {
//                    set = new HashSet();
//                    set.addAll(setCurrent);
//                    sets.add(set);
//                    counter = 0;
//                    setCurrent = new HashSet();
//                }
//                counter++;
//                setCurrent.add(id);
//            }
//            if (setCurrent.size() > 0) {
//                sets.add(setCurrent);
//            }
//
//            for (Set<Integer> setOne : sets) {
//                boolean done = false;
//                while (!done) {
//                    try {
//                        twitter = timer.canICallPlease();
//                        usersTwitter4J = twitter.lookupUsers(ArrayUtils.toPrimitive(setOne.toArray(new Integer[0])));
//                        timer.thatsOneMoreCall();
//                        if (usersTwitter4J != null) {
//                            users.addAll(usersTwitter4J);
//                            twitterUsers.addAll(new ConvertUsers().convertAll(users));
//                        }
//                        done = true;
//                        setIds.removeAll(setOne);
//                    } catch (TwitterException ex) {
//                        if (ex.getStatusCode() >= 500) {
//                            Thread.sleep((ex.getRetryAfter() + 1) * 1000);
//                        } else {
//                            System.out.println("error: " + ex.getStatusCode());
//                            System.out.println("error: " + ex.getMessage());
//                            //429 is rate limits exceeded on the current token
//                            if (ex.getStatusCode() == 429) {
//                                parent.tell("out of tokens", getSelf());
//                            }
//                            //401 is an error like "page not found". In practice, means the the user is protected.
//                            if (ex.getStatusCode() == 401) {
//                                done = true;
//                                setIds.removeAll(setOne);
//                            }
//
//                        }
//                    }
//                }
//            }
//
//        } // *********** case when we deal with screenNames (see above for ids)
//        else {
//            List<Set<String>> sets = new ArrayList();
//            Set<String> setCurrent = new HashSet();
//            Set<String> set;
//            int counter = 0;
//            for (String screenName : listScreenNames) {
//                if (counter == 100) {
//                    set = new HashSet();
//                    set.addAll(setCurrent);
//                    sets.add(set);
//                    counter = 0;
//                    setCurrent = new HashSet();
//                } else {
//                    counter++;
//                    setCurrent.add(screenName);
//                }
//            }
//            if (setCurrent.size() > 0) {
//                sets.add(setCurrent);
//            }
//
//            for (Set<String> setOne : sets) {
//                boolean done = false;
//                while (!done) {
//                    try {
//                        twitter = timer.canICallPlease();
//                        usersTwitter4J = twitter.lookupUsers(setOne.toArray(new String[0]));
//                        timer.thatsOneMoreCall();
//                        if (usersTwitter4J != null) {
//                            users.addAll(usersTwitter4J);
//                            twitterUsers.addAll(new ConvertUsers().convertAll(users));
//                        }
//                        done = true;
//                        setIds.removeAll(setOne);
//
//                    } catch (TwitterException ex) {
//                        if (ex.getStatusCode() >= 503) {
//                            Thread.sleep((ex.getRetryAfter() + 1) * 1000);
//                        } else {
//                            System.out.println("error: " + ex.getStatusCode());
//                            System.out.println("error: " + ex.getMessage());
//                            if (ex.getStatusCode() == 429) {
//                                parent.tell("out of tokens", getSelf());
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        timer.close();
//        MsgSetTwitterUsers msgSetUsers = new MsgSetTwitterUsers(twitterUsers);
//        parent.tell(msgSetUsers, getSelf());
//
//    }
//
//}
