/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package REST;

import Control.Controller;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author C. Levallois
 */
@Path("LaunchTweetStream")
@RequestScoped
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
    public String getText(@QueryParam("app") String app, @QueryParam("mention") String mention, @QueryParam("idGephi") String idGephi, @QueryParam("jobStart") String jobStart, @QueryParam("terminate") String terminate, @QueryParam("fromHourString") String fromHourString, @QueryParam("fromDayString") String fromDayString, @QueryParam("fromMonthString") String fromMonthString, @QueryParam("fromYearString") String fromYearString, @QueryParam("forMinutesString") String forMinutesString, @QueryParam("forHoursString") String forHoursString, @QueryParam("forDaysString") String forDaysString, @QueryParam("nowString") String nowString) {

        System.out.println("mention: " + mention);
        System.out.println("app: " + app);

        Controller controller = new Controller(app, mention, idGephi, jobStart, terminate, fromHourString, fromDayString, fromMonthString, fromYearString, forMinutesString, forHoursString, forDaysString, nowString);
        Thread myThread = new Thread(controller);
        myThread.start();

        return "success";
    }
}
