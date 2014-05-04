///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package OAuth;
//
//import APICalls.APICallPoints;
//import APICalls.GetFollowersAndTheirRelations;
//import APICalls.GetFriendsAndTheirRelations;
//import APICalls.GetUsersShow;
//import Persistence.MongoMorphia;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//import twitter4j.Twitter;
//import twitter4j.TwitterException;
//import twitter4j.auth.AccessToken;
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
//public class Timer {
//
//    private int callsMadeInCurrentWindow;
//    private int callsMade;
//    private int limitTimeWindow;
//    private int limitCallsPerTimeWindow;
//    private final int callsToMake;
//    private final APICallPoints nameCallPoint;
//    private List<AccessToken> accessTokens;
//    private AccessToken accessToken;
//    private String apiMethodString;
//    private Twitter twitter;
//    private MongoMorphia mm;
//
//    public Timer(APICallPoints nameCallPoint, int callsToMake, Twitter twitter, MongoMorphia mm) {
//        this.callsMade = 0;
//        this.callsToMake = callsToMake;
//        this.nameCallPoint = nameCallPoint;
//        this.twitter = twitter;
//        this.mm = mm;
//    }
//
//    public void start() throws UnknownHostException, InterruptedException {
//
//        accessTokens = new ArrayList();
//
//        switch (nameCallPoint) {
//            case GETFRIENDS:
//                limitTimeWindow = GetFriendsAndTheirRelations.limitTimeWindow();
//                limitCallsPerTimeWindow = GetFriendsAndTheirRelations.callsPerTimeWindow();
//                apiMethodString = GetFriendsAndTheirRelations.apiEndPoint();
//                break;
//
//            case GETFOLLOWERS:
//                limitTimeWindow = GetFollowersAndTheirRelations.limitTimeWindow();
//                limitCallsPerTimeWindow = GetFollowersAndTheirRelations.callsPerTimeWindow();
//                apiMethodString = GetFollowersAndTheirRelations.apiEndPoint();
//                break;
//
//            case GETUSERSSHOW:
//                limitTimeWindow = GetUsersShow.limitTimeWindow();
//                limitCallsPerTimeWindow = GetUsersShow.callsPerTimeWindow();
//                apiMethodString = GetUsersShow.apiEndPoint();
//                break;
//        }
//
//        GetFreshTokens freshTokens = new GetFreshTokens(mm);
//        accessTokens = freshTokens.getThem(System.currentTimeMillis(), nameCallPoint, limitTimeWindow);
//        accessToken = new AccessToken(accessTokens.get(0).getToken(), accessTokens.get(0).getTokenSecret());
//        twitter.setOAuthAccessToken(accessToken);
//
//    }
//
//    public void close() {
//
//        Set<AccessToken> setTokens = new HashSet();
//        setTokens.addAll(accessTokens);
//        TokenReleaser releaser = new TokenReleaser(mm);
//        releaser.letThemFly(setTokens);
//
//    }
//
//    public void thatsOneMoreCall() {
//        callsMade++;
//        callsMadeInCurrentWindow++;
//    }
//
//    public Twitter canICallPlease() throws InterruptedException {
//        boolean changeToken = false;
//        boolean done = false;
//
//        if (limitCallsPerTimeWindow - callsMadeInCurrentWindow < 3) {
//            while (!done) {
//                try {
//                    int callsRemainingInTimeWindow = twitter.getRateLimitStatus().get(apiMethodString).getRemaining();
//                    System.out.println("calls remaining in " + apiMethodString + ": " + callsRemainingInTimeWindow);
//                    if (callsRemainingInTimeWindow <= 0) {
//                        changeToken = true;
//                    }
//                    done = true;
//                } catch (TwitterException ex) {
//                    if (ex.getStatusCode() == 503) {
//                        Thread.sleep((ex.getRetryAfter() + 1) * 1000);
//                    }
//                }
//            }
//        }
//        if (changeToken) {
//            changeToken();
//        }
//        return twitter;
//
//    }
//
//    public Twitter changeToken() throws InterruptedException {
//        boolean done = false;
//        while (!done) {
//            try {
//                TokenReleaser releaseToken = new TokenReleaser(mm);
//                releaseToken.letItFly(twitter.getOAuthAccessToken());
//                AccessToken currentToken = twitter.getOAuthAccessToken();
//                Iterator<AccessToken> it = accessTokens.iterator();
//                while (it.hasNext()) {
//                    AccessToken accessTokenInIterator = it.next();
//                    if (accessTokenInIterator.getToken().equals(currentToken.getToken())) {
//                        it.remove();
//                    }
//                }
//                if (!accessTokens.isEmpty()) {
//                    accessToken = new AccessToken(accessTokens.get(0).getToken(), accessTokens.get(0).getTokenSecret());
//                    twitter.setOAuthAccessToken(accessToken);
//                    callsMadeInCurrentWindow = 0;
//                    System.out.println("new token set by taking one from the list!");
//                    System.out.println("token: " + accessToken.getToken());
//                    int callsRemainingInTimeWindow = twitter.getRateLimitStatus().get(apiMethodString).getRemaining();
//                    System.out.println("with this new token, calls remaining in " + apiMethodString + ": " + callsRemainingInTimeWindow);
//
//                } else {
//                    while (accessTokens.isEmpty()) {
//                        System.out.println("list of available tokens is empty, waiting 1 minute before trying to replenish it");
//                        Thread.sleep(60000);
//                        GetFreshTokens freshTokens = new GetFreshTokens(mm);
//                        accessTokens = freshTokens.getThem(System.currentTimeMillis(), nameCallPoint, limitTimeWindow);
//                    }
//                    System.out.println("list of available tokens replenished!");
//                    accessToken = new AccessToken(accessTokens.get(0).getToken(), accessTokens.get(0).getTokenSecret());
//                    twitter.setOAuthAccessToken(accessToken);
//                    System.out.println("new token set!");
//                    callsMadeInCurrentWindow = 0;
//                    System.out.println("token: " + accessToken.getToken());
//                    int callsRemainingInTimeWindow = twitter.getRateLimitStatus().get(apiMethodString).getRemaining();
//                    System.out.println("with this new token, calls remaining in " + apiMethodString + ": " + callsRemainingInTimeWindow);
//                }
//                done = true;
//            } catch (TwitterException ex) {
//                if (ex.getStatusCode() == 503) {
//                    Thread.sleep((ex.getRetryAfter() + 1) * 1000);
//                }
//            }
//        }
//        return twitter;
//
//    }
//
//    public static int thisJobWillLastInMinutesOneToken(int callsToMake, APICallPoints nameCall) {
//        Integer limitCallsPerTimeWindow = null;
//        Integer limitTimeWindow = null;
//
//        switch (nameCall) {
//            case GETFRIENDS:
//                limitTimeWindow = GetFriendsAndTheirRelations.limitTimeWindow();
//                limitCallsPerTimeWindow = GetFriendsAndTheirRelations.callsPerTimeWindow();
//                break;
//
//            case GETFOLLOWERS:
//                limitTimeWindow = GetFollowersAndTheirRelations.limitTimeWindow();
//                limitCallsPerTimeWindow = GetFollowersAndTheirRelations.callsPerTimeWindow();
//                break;
//
//            case GETUSERSSHOW:
//                limitTimeWindow = GetUsersShow.limitTimeWindow();
//                limitCallsPerTimeWindow = GetUsersShow.callsPerTimeWindow();
//                break;
//        }
//
//        int necessaryTimeWindows = (int) (callsToMake / limitCallsPerTimeWindow);
//        return necessaryTimeWindows * limitTimeWindow;
//    }
//
//}
