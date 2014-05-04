/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import APICalls.MsgInterrupt;
import APICalls.MsgLaunchCollectionMentionsTwitter;
import CollectionOfMentions.ControllerCollectionOfMentions;
import Model.Session;
import Singletons.SharedActorSystem;
import Singletons.SharedMongoMorphiaInstance;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.code.morphia.Datastore;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

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
public class Controller implements Serializable {

    boolean debug = false;
    boolean local = false;
    private ActorSystem system;

    public Controller() {
    }

    public void execute(String mention, String idGephi, String jobStartString, String terminate, String fromHourString, String fromDayString, String fromMonthString, String fromYearString, String forMinutesString, String forHoursString, String forDaysString, String nowString) {

        Integer fromHour = null;
        Integer fromDay = null;
        Integer fromMonth = null;
        Integer fromYear = null;
        if (SharedMongoMorphiaInstance.getMongoClient() == null) {
            SharedMongoMorphiaInstance.loadConfiguration();
        }
        if (SharedActorSystem.getSystem() == null) {
            SharedActorSystem.loadConfiguration();
        }

        if (mention != null && !mention.isEmpty()) {

            Integer forMinutes = Integer.valueOf(forMinutesString);
            Integer forHours = Integer.valueOf(forHoursString);
            Integer forDays = Integer.valueOf(forDaysString);
            if (!nowString.equals("true")) {
                fromHour = Integer.valueOf(fromHourString);
                fromDay = Integer.valueOf(fromDayString);
                fromMonth = Integer.valueOf(fromMonthString);
                fromYear = Integer.valueOf(fromYearString);
            }

            Datastore dsSessions = SharedMongoMorphiaInstance.getDsSessions();
            Session session = dsSessions.find(Session.class).field("idGephi").equal(idGephi).get();

            if (session == null) {
                return;
            }
            system = SharedActorSystem.getSystem();

            final ActorRef actorCollectionMentions = system.actorOf(Props.create(ControllerCollectionOfMentions.class), "controller" + String.valueOf(jobStartString));

            MsgLaunchCollectionMentionsTwitter msg = new MsgLaunchCollectionMentionsTwitter(idGephi, jobStartString, nowString, fromHour, fromDay, fromMonth, fromYear, mention, forMinutes, forHours, forDays);
            actorCollectionMentions.tell(msg, ActorRef.noSender());

        } else {
            if (terminate.equals("yes")) {
                system = SharedActorSystem.getSystem();
                String nameActorToInterrupt = "akka://systemJobs/user/controller" + terminate;
                MsgInterrupt msg = new MsgInterrupt();
                system.actorSelection(nameActorToInterrupt).tell(msg, ActorRef.noSender());
            }
        }
    }
}
