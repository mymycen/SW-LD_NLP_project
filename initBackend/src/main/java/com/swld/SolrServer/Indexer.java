package com.swld.SolrServer;


import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.*;
import java.util.List;



public class Indexer {
    private static boolean isIndexed = false; // TODO: dynamic index
    public static void addSampleDoc(String[] spo){
        HttpSolrClient solr = SolrServer.getSolrServer();
        SolrInputDocument document = new SolrInputDocument();
        document.addField("s", spo[1]);
        document.addField("p",spo[2]);
        document.addField("o", spo[3]);
        try {
            solr.add(document);
            solr.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isIndexed = true;
    }

public static String indexData() throws IOException {
        if(!isIndexed) {
            //TODO: do for all files in the resources
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream isr = classloader.getResourceAsStream("Berlin.ntriples");

            readFromInputStream(isr);
        }
            return null;

}

    private static void process(String line) {
        String[] spo = ("\t"+line).split("\\t");
        if (spo.length>=3){
            System.out.println(spo[1]==null?"snull":spo[1]);
            System.out.println(spo[2]==null?"pnull":spo[2]);
            System.out.println(spo[3]==null?"onull":spo[3]);
            addSampleDoc(spo);
        }
    }

    private static void readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                process(line);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("Berlin.ntriples");

        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int lineC=0;
            while ((line = br.readLine()) != null) {
                String[] spo = ("\t"+line).split("\\t");
                if (spo.length>=3){
                    System.out.println(spo[1]);
                    System.out.println(spo[2]);
                    System.out.println(spo[3]);
                }
                lineC++;
                if(lineC==10){
                    break;
                }
            }
        }
    }
}


