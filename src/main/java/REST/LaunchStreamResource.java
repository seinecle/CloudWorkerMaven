/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package REST;

import APICalls.MsgLaunchCollectionMentionsTwitter;
import CollectionOfMentions.ControllerCollectionOfMentions;
import Control.Controller;
import Singletons.SharedActorSystem;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author C. Levallois
 */
@Path("/launchStream")
public class LaunchStreamResource {

    @Context
    private UriInfo context;


    /**
     * Creates a new instance of LaunchStreamResource
     */
    public LaunchStreamResource() {
    }

    /**
     * Retrieves representation of an instance of REST.LaunchStreamResource
     *
     * @return an instance of java.lang.String
     */
    @Path("/get")
    @GET
    @Produces("text/plain")
    public String getText(@QueryParam("mention") String mention, @QueryParam("idGephi") String idGephi, @QueryParam("jobStart") String jobStart, @QueryParam("terminate") String terminate, @QueryParam("fromHourString") String fromHourString, @QueryParam("fromDayString") String fromDayString, @QueryParam("fromMonthString") String fromMonthString, @QueryParam("fromYearString") String fromYearString, @QueryParam("forMinutesString") String forMinutesString, @QueryParam("forHoursString") String forHoursString, @QueryParam("forDaysString") String forDaysString, @QueryParam("nowString") String nowString) {
        System.out.println("mention: " + mention);

        Controller controller = new Controller();
        controller.execute(mention, idGephi, jobStart, terminate, fromHourString, fromDayString, fromMonthString, fromYearString, forMinutesString, forHoursString, forDaysString, nowString);
        return "success";
    }

    /**
     * PUT method for updating or creating an instance of LaunchStreamResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/plain")
    public void putText(String content) {
    }
}
