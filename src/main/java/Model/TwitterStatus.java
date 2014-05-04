/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class TwitterStatus {

    List<Long> contributors;
    Date createdAt;
    long currentUserRetweetId;
    double latitude;
    double longitude;
    List<Hashtag> hashtagEntities;
    long id;
    String inReplyToScreenName;
    long inReplyToUserId;
    long inReplyToStatusId;
    String source;
    String text;
    TwitterUser user;
    boolean isRetweet;

    List<MediaEntities> mediaEntities;

    Place place;

    public TwitterStatus() {
        this.contributors = new ArrayList();
        this.hashtagEntities = new ArrayList();
        this.mediaEntities = new ArrayList();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getCurrentUserRetweetId() {
        return currentUserRetweetId;
    }

    public void setCurrentUserRetweetId(long currentUserRetweetId) {
        this.currentUserRetweetId = currentUserRetweetId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    public void setInReplyToScreenName(String inReplyToScreenName) {
        this.inReplyToScreenName = inReplyToScreenName;
    }

    public long getInReplyToUserId() {
        return inReplyToUserId;
    }

    public void setInReplyToUserId(long inReplyToUserId) {
        this.inReplyToUserId = inReplyToUserId;
    }

    public long getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    public void setInReplyToStatusId(long inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TwitterUser getUser() {
        return user;
    }

    public void setUser(TwitterUser user) {
        this.user = user;
    }

    public boolean isIsRetweet() {
        return isRetweet;
    }

    public void setIsRetweet(boolean isRetweet) {
        this.isRetweet = isRetweet;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<Long> getContributors() {
        return contributors;
    }

    public void setContributors(List<Long> contributors) {
        this.contributors = contributors;
    }

    public List<Hashtag> getHashtagEntities() {
        return hashtagEntities;
    }

    public void setHashtagEntities(List<Hashtag> hashtagEntities) {
        this.hashtagEntities = hashtagEntities;
    }

    public List<MediaEntities> getMediaEntities() {
        return mediaEntities;
    }

    public void setMediaEntities(List<MediaEntities> mediaEntities) {
        this.mediaEntities = mediaEntities;
    }

    public static class Hashtag {

        int start;
        int end;
        String text;

        public Hashtag() {
        }

        public Hashtag(int start, int end, String text) {
            this.start = start;
            this.end = end;
            this.text = text;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class MediaEntities {

        long id;
        String url;
        String mediaUrl;
        String displayUrl;
        String type;
        int start;
        int end;

        public MediaEntities() {
        }

        public MediaEntities(long id, String url, String mediaUrl, String displayUrl, String type, int start, int end) {
            this.id = id;
            this.url = url;
            this.mediaUrl = mediaUrl;
            this.displayUrl = displayUrl;
            this.type = type;
            this.start = start;
            this.end = end;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMediaUrl() {
            return mediaUrl;
        }

        public void setMediaUrl(String mediaUrl) {
            this.mediaUrl = mediaUrl;
        }

        public String getDisplayUrl() {
            return displayUrl;
        }

        public void setDisplayUrl(String displayUrl) {
            this.displayUrl = displayUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }

    public static class Place {

        String id;
        String url;
        String country;
        String countryCode;
        String fullName;
        String streetAddress;
        String name;
        String placeType;

        public Place() {
        }

        public Place(String id, String url, String country, String countryCode, String fullName, String streetAddress, String name, String placeType) {
            this.id = id;
            this.url = url;
            this.country = country;
            this.countryCode = countryCode;
            this.fullName = fullName;
            this.streetAddress = streetAddress;
            this.name = name;
            this.placeType = placeType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getStreetAddress() {
            return streetAddress;
        }

        public void setStreetAddress(String streetAddress) {
            this.streetAddress = streetAddress;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlaceType() {
            return placeType;
        }

        public void setPlaceType(String placeType) {
            this.placeType = placeType;
        }

    }

}
