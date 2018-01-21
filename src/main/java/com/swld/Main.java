package com.swld;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {


    // Base URI the Grizzly HTTP server will listen on
    public static String BASE_URI = "http://localhost:5050/api/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.underdog.jersey.grizzly package
        final ResourceConfig rc = new ResourceConfig().packages("com.swld").register(new CORSFilter());

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void readArgs(String[] args){
        try {
            if (args.length == 1) {
                int port = Integer.parseInt(args[0]);
                BASE_URI = "http://localhost:" + port + "/de/";
            } else if (args.length == 2) {
                String base = args[0];
                int port = Integer.parseInt(args[1]);
                BASE_URI = "http://" + base + ":" + port + "/de/";
            }
        } catch(NumberFormatException e){
            e.printStackTrace();
        }
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        readArgs(args);
        final HttpServer server = startServer();
        System.out.println("Started server "+BASE_URI);
        System.out.println("hit enter to stop");
        System.in.read();
        server.stop();
    }
}


