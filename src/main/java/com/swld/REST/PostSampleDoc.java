package com.swld.REST;


import com.swld.Lookup.InitBackend;
import com.swld.SolrServer.Indexer;
import com.swld.SolrServer.Searcher;
import com.swld.SolrServer.Suggester;
import com.swld.SolrServer.Triple;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Path("/sample")
public class PostSampleDoc {


    @POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)    // JSON
    public String postSampleDoc(){
        System.out.println("posting sample");
        try {
            Indexer.indexData();
        } catch (IOException e) {
            e.printStackTrace();
            return "Unable to post";
        }
        return "posted";
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)    // JSON
    public List<Triple> getSampleDoc(@Context UriInfo ui){
        System.out.println("getting sample");
        List<Triple> val= Searcher.searchDoc( ui.getQueryParameters().getFirst("search"));
        if (val !=null){
            Iterator<Triple> it = val.iterator();
//            while (it.hasNext()){
//
//            }
        }
        return val;
    }

    @GET
    @Path("/suggest/{word}")
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")    // JSON
    public List<String> getSuggestion(@PathParam("word") String searchWord){
        System.out.println("getting sample");
        return Suggester.getSuggestions(searchWord);
    }

    @GET
    @Path("/nlp")
//    @RequestMapping(value = "/nlp",params = {"message","subject","predicate","onlyMatch"})
    public String startAnalyze(@QueryParam("subject") String subject,@QueryParam("predicate") String predicate,@QueryParam("onlyMatch") String onlyMatch,@QueryParam("message") String message) {
        InitBackend a = new InitBackend(subject,
                predicate,message);
        System.out.println("s:"+ subject + "predicate p"+ predicate);
        try{
            boolean userOption = Boolean.parseBoolean(onlyMatch);
            String result = a.lookup(userOption);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }


        return "An error occur while searching";
    }

//    @RequestMapping(value = "/nlp",params = {"message","subject","predicate"})
//    public String startAnalyze(@RequestParam String subject,@RequestParam String predicate,@RequestParam String message) {
//        InitBackend a = new InitBackend(subject,
//                predicate,message);
//        try{
//            String result = a.lookup();
//            return result;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//        return "An error occur while searching";
//    }

}
