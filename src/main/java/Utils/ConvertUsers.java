/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Model.TwitterUser;
import java.util.HashSet;
import java.util.Set;
import twitter4j.User;

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
public class ConvertUsers {

    public Set<TwitterUser> convertAll(Set<User> users) {

        Set<TwitterUser> twitterUsers = new HashSet();
        TwitterUser twitterUser;

        for (User user : users) {
            twitterUser = new TwitterUser();
            twitterUser.setCreated_at(user.getCreatedAt());
            twitterUser.setDescription(user.getDescription());
            twitterUser.setFollowers_count(user.getFollowersCount());
            twitterUser.setLang(user.getLang());
            twitterUser.setLocation(user.getLocation());
            twitterUser.setName(user.getName());
            twitterUser.setProfile_image_url(user.getProfileImageURL());
            twitterUser.setScreen_name(user.getScreenName());
            twitterUser.setIdTwitter(user.getId());
            twitterUser.setFriends_count(user.getFriendsCount());
            twitterUser.setProfile_banner_url(user.getProfileBannerURL());
            twitterUsers.add(twitterUser);
        }
        return twitterUsers;
    }

    public TwitterUser convertOne(User user) {

        TwitterUser twitterUser;

        twitterUser = new TwitterUser();
        twitterUser.setCreated_at(user.getCreatedAt());
        twitterUser.setDescription(user.getDescription());
        twitterUser.setFollowers_count(user.getFollowersCount());
        twitterUser.setLang(user.getLang());
        twitterUser.setLocation(user.getLocation());
        twitterUser.setName(user.getName());
        twitterUser.setProfile_image_url(user.getProfileImageURL());
        twitterUser.setScreen_name(user.getScreenName());
        twitterUser.setIdTwitter(user.getId());
        twitterUser.setFriends_count(user.getFriendsCount());
        twitterUser.setProfile_banner_url(user.getProfileBannerURL());

        return twitterUser;
    }

}
