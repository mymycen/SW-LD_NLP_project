package com.swld.SolrServer;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

public class SolrServer {
    private final static String SOLR_SERVER_URI="http://localhost:8983/solr/search";
    private static HttpSolrClient solr;
    public static HttpSolrClient getSolrServer(){
        if (solr==null) {
            solr = new HttpSolrClient.Builder(SOLR_SERVER_URI).build();
            solr.setParser(new XMLResponseParser());
        }
        return solr;
    }
}
