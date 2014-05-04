///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package APICalls;
//
//import OAuth.MyOwnTwitterFactory;
//import OAuth.Timer;
//import Persistence.MongoMorphia;
//import java.net.UnknownHostException;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.Callable;
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
//public class GetFriendsCallable implements Callable {
//
//    Set<Integer> setIdsInitialSeed;
//
//    Set<Integer> setFriends;
//    Twitter twitter;
//    int totalCalls;
//    Timer timer;
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
//    private MongoMorphia mm;
//
//    public GetFriendsCallable(Set<Integer> setIdsInitialSeed, MongoMorphia mm) {
//        this.setIdsInitialSeed = setIdsInitialSeed;
//        totalCalls = setIdsInitialSeed.size();
//        this.mm = mm;
//    }
//
//    @Override
//    public Set<Integer> call() throws UnknownHostException, InterruptedException {
//
//        MyOwnTwitterFactory factory = new MyOwnTwitterFactory();
//        twitter = factory.createOneTwitterInstance();
//
//        timer = new Timer(APICallPoints.GETFRIENDS, totalCalls, twitter, mm);
//        timer.start();
//
//        setFriends = new HashSet();
//
//        //looping through all the User ids provided
//        for (int id : setIdsInitialSeed) {
//            boolean done = false;
//            while (!done) {
//                try {
//                    twitter = timer.canICallPlease();
//                    //for each User Id, get the list of its followers;
//                    int[] ids = twitter.getFriendsIDs(id, -1).getIDs();
//                    setFriends.addAll(Arrays.asList(ArrayUtils.toObject(ids)));
//                    timer.thatsOneMoreCall();
//                    done = true;
//                } catch (TwitterException ex) {
//                    System.out.println("error: " + ex.getStatusCode());
//                    System.out.println("error: " + ex.getMessage());
//                    //429 is rate limits exceeded on the current token
//                    if (ex.getStatusCode() == 429) {
//                        timer.changeToken();
//                    }
//                    //401 is an error like "page not found". In practice, means the the user is protected.
//                    if (ex.getStatusCode() == 401) {
//                        done = true;
//                    }
//
//                }
//            }
//        }
//        timer.close();
//        return setFriends;
//
//    }
//
//}
