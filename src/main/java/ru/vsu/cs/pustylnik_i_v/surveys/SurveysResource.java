package ru.vsu.cs.pustylnik_i_v.surveys;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/hello-world")
public class SurveysResource {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
}