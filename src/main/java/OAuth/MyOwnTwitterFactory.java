/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OAuth;

import Control.Admin;
import Private.APIkeys;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

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
public class MyOwnTwitterFactory {

    public Twitter createOneTwitterInstance() {
        Twitter twitter;
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(APIkeys.getTwitterAPIKey());
        builder.setOAuthConsumerSecret(APIkeys.getTwitterAPISecret());
        builder.setOAuthAccessToken("31805620-QQy8TFFDKRxWyOUVnY08UcxT5bzrFhRWUa0A3lEW3");
        builder.setOAuthAccessTokenSecret("iJuCkdgrfIpGn5odyF2evMSvAsovreeEV6cZU5ihVVI7j");
        Configuration configuration = builder.build();
        TwitterFactory factory = new TwitterFactory(configuration);
        twitter = factory.getInstance();

        return twitter;

    }

    public TwitterStream createOneTwitterStreamInstance(AccessToken accessToken) {
        TwitterStream twitterStream;
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(APIkeys.getTwitterAPIKey());
        builder.setOAuthConsumerSecret(APIkeys.getTwitterAPISecret());
        System.out.println("AT: " + accessToken.getToken());
        System.out.println("ATS: " + accessToken.getTokenSecret());
        builder.setOAuthAccessToken(accessToken.getToken());
        builder.setOAuthAccessTokenSecret(accessToken.getTokenSecret());
        builder.setJSONStoreEnabled(true);
//        builder.setOAuthAccessToken("31805620-QQy8TFFDKRxWyOUVnY08UcxT5bzrFhRWUa0A3lEW3");
//        builder.setOAuthAccessTokenSecret("iJuCkdgrfIpGn5odyF2evMSvAsovreeEV6cZU5ihVVI7j");
        Configuration configuration = builder.build();
        TwitterStreamFactory factory = new TwitterStreamFactory(configuration);
        twitterStream = factory.getInstance();

        return twitterStream;
    }

    public TwitterStream createOneTwitterStreamInstanceFromApp(String APIKey, String APIKeySecret, String accessToken, String accessTokenSecret) {
        TwitterStream twitterStream;
        ConfigurationBuilder builder = new ConfigurationBuilder();
        if (!Admin.isTest()) {
            builder.setOAuthConsumerKey(APIKey);
            builder.setOAuthConsumerSecret(APIKeySecret);
            builder.setOAuthAccessToken(accessToken);
            builder.setOAuthAccessTokenSecret(accessTokenSecret);
            builder.setJSONStoreEnabled(true);
        } else {
            builder.setOAuthConsumerKey("KNjw1QTK1hJKx8LpK6X6rg");
            builder.setOAuthConsumerSecret("ikX9blowuh3FqFAkIg5LQi5voLOV413EWzPsDl77uU");
            builder.setOAuthAccessToken("31805620-QQy8TFFDKRxWyOUVnY08UcxT5bzrFhRWUa0A3lEW3");
            builder.setOAuthAccessTokenSecret("iJuCkdgrfIpGn5odyF2evMSvAsovreeEV6cZU5ihVVI7j");
            builder.setJSONStoreEnabled(true);
        }
        Configuration configuration = builder.build();
        TwitterStreamFactory factory = new TwitterStreamFactory(configuration);
        twitterStream = factory.getInstance();

        return twitterStream;
    }

}
