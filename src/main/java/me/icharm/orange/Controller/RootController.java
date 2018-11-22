package me.icharm.orange.Controller;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 18073621
 * @version 1.0
 */
public class RootController {

    /**
     *  Allow XMLHttpRequest cross domain
     *
     * @param response HttpServletResponse
     */
    public void allowCrossDomain(HttpServletResponse response){
        // To allow cross origin request
        response.setHeader("Access-Control-Allow-Origin", "*");
        // Allowed request type
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        // Cache repose max 3600s
        response.setHeader("Access-Control-Max-Age", "3600");
        // Specify this XMLHttpRequest response
        // response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
    }
}
