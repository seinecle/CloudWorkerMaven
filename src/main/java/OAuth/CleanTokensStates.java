/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OAuth;

import Model.AccessTokenPlus;
import Persistence.MongoMorphia;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import java.util.HashSet;
import java.util.Set;
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
public class CleanTokensStates {


    public Integer releaseAllUnusedTokens(Datastore dsAccessToken,Datastore dsAccessTokenBusy) {


        UpdateOperations<AccessTokenPlus> updateOperation;
        Query<AccessTokenPlus> query;
        int countTokensReleased = 0;

        Set<AccessToken> allTokens = new HashSet();
        Set<AccessToken> allTokensBusy = new HashSet();

        allTokens.addAll(dsAccessToken.find(AccessTokenPlus.class).field("isAvailable").equal(false).asList());
        allTokensBusy.addAll(dsAccessTokenBusy.find(AccessTokenPlus.class).asList());

        for (AccessToken token : allTokens) {
            if (!allTokensBusy.contains(token)) {
                query = dsAccessToken.createQuery(AccessTokenPlus.class).field("token").equal(token.getToken());
                updateOperation = dsAccessToken.createUpdateOperations(AccessTokenPlus.class).set("isAvailable", true);
                dsAccessToken.update(query, updateOperation, true);
                countTokensReleased++;
            }
        }
        int countTokensAvailable = dsAccessToken.find(AccessTokenPlus.class).field("isAvailable").equal(true).asList().size();
        return countTokensAvailable;
    }
}
