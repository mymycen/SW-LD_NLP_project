package com.swld.SolrServer;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.List;

public class Searcher {

    public static List<Triple> searchDoc(String str){
        HttpSolrClient solr=SolrServer.getSolrServer();
        SolrQuery query = new SolrQuery();
        query.setQuery(str);
        QueryResponse response = null;
        try {
            response = solr.query(query);
            List<Triple> items = response.getBeans(Triple.class);
            System.out.println(((Triple)items.get(0)).getP());
            return items;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
