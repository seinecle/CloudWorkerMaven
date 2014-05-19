/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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
public class SenderMsgToCentralServer {

    public void streamIsTerminatedOK(String idGephi, String jobStart, String app) throws URISyntaxException, IOException {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(Admin.ipServerDispatch() + "webresources/CommunicationServers/TerminatedOK")
                .setParameter("jobStart", jobStart)
                .setParameter("idGephi", idGephi)
                .setParameter("app", app);
        URI uri = builder.build();
        System.out.println("uri: " + uri);
        HttpGet httpget = new HttpGet(uri);

        HttpResponse response;
        HttpEntity entity;
        int codeStatus = 0;
        int attempts = 0;
        boolean success = false;

        while (!success && attempts < 4) {
            attempts++;

            response = httpclient.execute(httpget);
            entity = response.getEntity();
            EntityUtils.consumeQuietly(entity);
            codeStatus = response.getStatusLine().getStatusCode();
            success = (codeStatus == 200);
        }
        if (!success) {
            System.out.println("server dispatcher could not be reached to tell about job termination - 3 failed attempts.");
        }else{
            System.out.println("message correctly sent to server dispatcherabout cloudbees job termination");
            
        }
    }

}
