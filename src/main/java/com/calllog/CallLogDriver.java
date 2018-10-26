package com.calllog;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;


import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;

public class CallLogDriver {


    public static void main(String[] ar)
    {
        URI rui = UriBuilder.fromUri("http://0.0.0.0").port(2127).build();

        final HttpServer grizzlyServer = GrizzlyHttpServerFactory.createHttpServer(rui, create(),false);
        try {
            grizzlyServer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  static ResourceConfig create() {

        ResourceApi rs = new ResourceApi();
        final ResourceConfig resourceConfig = new ResourceConfig()
                .register(new CORSFilter())
                .register(MultiPartFeature.class)
                .register(rs);


        return resourceConfig;
    }

    @Provider
    public static class CORSFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext request,
                           ContainerResponseContext response) throws IOException {
            response.getHeaders().add("Access-Control-Allow-Origin", "*");
            response.getHeaders().add("Access-Control-Allow-Headers",
                    "origin, content-type, accept, authorization , access-control-allow-methods, access-control-allow-origin");
            response.getHeaders().add("Access-Control-Allow-Credentials", "true");
            response.getHeaders().add("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        }
    }

}
