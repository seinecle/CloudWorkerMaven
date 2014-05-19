/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package REST;

import APICalls.MsgInterrupt;
import Control.Controller;
import Singletons.SharedActorSystem;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author C. Levallois
 */
@Path("LaunchTweetStream")
//@RequestScoped
public class LaunchTweetStreamResource {

    public LaunchTweetStreamResource() {
    }

    /**
     * Retrieves representation of an instance of REST.LaunchStreamResource
     *
     * @return an instance of java.lang.String
     */
    @Path("/get")
    @GET
    @Produces("text/plain")
    public String getText(@QueryParam("jobId") String jobId, @QueryParam("app") String app, @QueryParam("mention") String mention, @QueryParam("idGephi") String idGephi, @QueryParam("jobStart") String jobStart, @QueryParam("terminate") String terminate, @QueryParam("fromHourString") String fromHourString, @QueryParam("fromDayString") String fromDayString, @QueryParam("fromMonthString") String fromMonthString, @QueryParam("fromYearString") String fromYearString, @QueryParam("forMinutesString") String forMinutesString, @QueryParam("forHoursString") String forHoursString, @QueryParam("forDaysString") String forDaysString, @QueryParam("nowString") String nowString) {

        System.out.println("mention: " + mention);
        System.out.println("jobId: " + jobId);
        System.out.println("hours: " + forHoursString);

        Controller controller = new Controller(jobId, app, mention, idGephi, jobStart, terminate, fromHourString, fromDayString, fromMonthString, fromYearString, forMinutesString, forHoursString, forDaysString, nowString);
        Thread myThread = new Thread(controller);
        myThread.start();

        return "success";
    }

    /**
     * Retrieves representation of an instance of REST.LaunchStreamResource
     *
     * @return an instance of java.lang.String
     */
    @Path("/stop")
    @GET
    @Produces("text/plain")
    public String stopApp(@QueryParam("jobId") String jobId) {

        
        
        if (SharedActorSystem.getSystem() == null) {
            SharedActorSystem.loadConfiguration();
        }
        ActorSystem system;
        system = SharedActorSystem.getSystem();
        
        

        ActorSelection actorSelection = system.actorSelection("controller" + jobId);
        MsgInterrupt msgInterrupt = new MsgInterrupt();
        actorSelection.tell(msgInterrupt, ActorRef.noSender());

        return "success";
    }
}
