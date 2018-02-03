package com.swld.SolrServer;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SuggesterResponse;

import java.util.List;
import java.util.Map;

public class Suggester {

    public static List<String> getSuggestions(String searchWord){
        SolrQuery query = new SolrQuery();
        HttpSolrClient solr=SolrServer.getSolrServer();
        query.setRequestHandler("/suggester");
        query.set("suggest", "true");
        query.set("suggest.build", "true");
        query.set("suggest.dictionary", "mySuggester");
        query.set("suggest.q", searchWord);
        QueryResponse response = null;
        try {
            response = solr.query(query);
            SuggesterResponse suggesterResponse = response.getSuggesterResponse();
            Map<String, List<String>> suggestedTerms = suggesterResponse.getSuggestedTerms();
            List<String> suggestions = suggestedTerms.get("mySuggester");
            return suggestions;
        }catch (Exception e){
            System.out.println("suggester failed");
            e.printStackTrace();
        }
        return null;
    }
}
