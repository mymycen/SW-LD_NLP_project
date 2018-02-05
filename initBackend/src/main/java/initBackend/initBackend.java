package initBackend;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.base.Sys;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.rules.RuleMatch;
import org.springframework.web.client.RestTemplate;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerType;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.data.list.PointerTargetTree;
import net.sf.extjwnl.data.relationship.AsymmetricRelationship;
import net.sf.extjwnl.data.relationship.Relationship;
import net.sf.extjwnl.data.relationship.RelationshipFinder;
import net.sf.extjwnl.data.relationship.RelationshipList;
import net.sf.extjwnl.dictionary.Dictionary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.core.DatasetImpl;

import javax.xml.crypto.dsig.keyinfo.KeyValue;


public class initBackend {
    private  String subject;
    private  String predicate;
    private  String sentence;

    public initBackend(String subject, String predicate,String sentence) {
        this.subject = subject;
        this.sentence = sentence;
        this.predicate = predicate;

    }
    public String lookup(){
        ArrayList responseResult  = new ArrayList();
        RestTemplate restTemplate = new RestTemplate();
        try {

            String uriSolr = "http://localhost:5050/search/get?search=" + sentence;
            String resultSolr = "{ \"results\":" + restTemplate.getForObject(uriSolr, String.class) + "}";

            //System.out.println(resultSolr);
            JsonParser jsonParser = new JsonParser();
            JsonObject o = jsonParser.parse(resultSolr).getAsJsonObject();
            for (int indexSolr=0;indexSolr<o.get("results").getAsJsonArray().size();indexSolr++){
                JsonObject oNext  = jsonParser.parse(o.get("results").getAsJsonArray().get(indexSolr).toString()).getAsJsonObject();
                // System.out.println(oNext.get("o"));
                if(oNext.size()>0&&(responseResult.size()<21)){
                    responseResult.add(oNext);
                }

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        String json = new Gson().toJson(responseResult);
        return json;
    }
    public String lookup(boolean onlyMatch){
        String newSubj=null;
        final String uri = "http://lookup.dbpedia.org/api/search/PrefixSearch?QueryString="+subject;
        String returnObj = null;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        try {

            JSONObject jsonObj = new JSONObject(result);
            JSONArray jsonObj2 = jsonObj.getJSONArray("results");

            JSONObject jsonObj3 = new JSONObject(jsonObj2.getString(0));

            returnObj = jsonObj3.get("uri").toString();
            //System.out.println(returnObj+"  ____________");
            String[] splited = returnObj.split("/");
            for(int l=0;l<splited.length;l++){
                if(splited[l].equals("resource")){
                    try{
                        newSubj=splited[l+1];
                    }catch(Exception e){
                        e.printStackTrace();
                        newSubj=subject;
                    }
                }
            }


            ArrayList responsePred = getPredicate(newSubj,predicate);
            String resultQuery = null;
            if (responsePred.size() != 0) {
                int predSize = responsePred.size();
                if (predSize > 4){
                    predSize -= 4;
                }
                for (int i = responsePred.size() - 1; i >= predSize; i--) {
                    if (responsePred.get(i).equals(subject) || responsePred.get(i).equals("abstraction") || responsePred.get(i).equals("abstract entity")
                            || responsePred.get(i).equals("attribute") || responsePred.get(i).equals("physical property") || responsePred.get(i).equals("property")
                            || responsePred.get(i).equals("entity") || responsePred.get(i).equals("physical entity")) {
                        responsePred.remove(i);
                    }
                    if (resultQuery == "" || resultQuery == null) {
                        try {
                            resultQuery = query(returnObj, responsePred.get(i).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else break;
                }
            }
            if(resultQuery!=null && onlyMatch==true){
                System.out.println(resultQuery+"  _______result_____");
                ArrayList<HashMap> list = new ArrayList<>();
                HashMap<String, String> responseResult = new HashMap<>();
                responseResult.put("s", resultQuery);
                list.add(responseResult);
                return new Gson().toJson(list);}
            else {

                ArrayList responseResult  = new ArrayList();
                for(int i =responsePred.size()-1;i>=0;i--){


                        try {
                            String tempForIndexing = sentence.replaceAll(subject, newSubj);
                            tempForIndexing = tempForIndexing.replaceAll(predicate, responsePred.get(i).toString());

                            String uriSolr = "http://localhost:5050/search/get?search=" + tempForIndexing;
                            String resultSolr = "{ \"results\":" + restTemplate.getForObject(uriSolr, String.class) + "}";
                            JsonParser jsonParser = new JsonParser();
                            JsonObject o = jsonParser.parse(resultSolr).getAsJsonObject();
                            for (int indexSolr=0;indexSolr<o.get("results").getAsJsonArray().size();indexSolr++){
                                JsonObject oNext  = jsonParser.parse(o.get("results").getAsJsonArray().get(indexSolr).toString()).getAsJsonObject();
                                //System.out.println(oNext+" ((((((((((((");
                                if(oNext.size()>0&&(responseResult.size()<21)){
                                    responseResult.add(oNext);
                                }

                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
//                        JsonObject jo = (JsonObject)jsonParser.parse(resultSolr);
//                        for(int aa=0;aa<jo.getAsJsonArray().size();aa++){
//                            System.out.println(jo.getAsJsonArray().get(aa));
//                        }


                }

                String json = new Gson().toJson(responseResult);
                //***********Call index backend********
                //use subject from global
                //predicate will be list from possible dictionary

                return json;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnObj;
    }
    // System.out.println(returnObj+" helloooooooo");
    // System.out.println(newSubj+" helloooooooo");
//                JLanguageTool langTool = null;
//                try {
//                    langTool = new JLanguageTool(Language.AMERICAN_ENGLISH);
//                    langTool.activateDefaultPatternRules();
//                    List<RuleMatch> matches = langTool.check(predicate);
//                    int k=0;
//
//                    for (RuleMatch match : matches) {
//
//                               if(k==1){
//                                   System.out.println("======+!!!!!!!++++");
//                                   System.out.println(predicate);
//                                    String temp = predicate;
//                                   predicate = match.getSuggestedReplacements().toString().substring(1,match.getSuggestedReplacements().toString().length()-1);
//                                   String[] testSpilt = predicate.split(",");
//
//                                   predicate = testSpilt[0];
//                                   sentence.replaceAll(temp,predicate);
//                                   System.out.println("======+!!!!!!!++++");
//                                   System.out.println(predicate);
//                                   System.out.println(sentence);
//                               }
//
//
//                                k++;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


    public String query(String lookupRes,String pred){
        String query = "select ?property ?hasValue where {<"+lookupRes+"> ?property ?hasValue} ";
        String response=null;
        try {
            Query queryR = QueryFactory.create(query);
            ResultSet results = executeQuery(queryR);
            System.out.println(results.nextSolution());
            for ( ; results.hasNext() ; ) {
                QuerySolution soln = results.nextSolution() ;
                System.out.println(soln);
                if(soln.toString().contains("http://dbpedia.org/property/"+pred+">")||soln.toString().contains("http://dbpedia.org/ontology/"+pred+">")){

                    String temp = soln.toString().substring(soln.toString().lastIndexOf("= ") + 1);
                    String[] res = temp.split("\\)");
                    response = res[0];
                    System.out.println(response);
                    return response;
                }else{
                    response=null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }



    public String getSubject() {
        return subject;
    }

    public ArrayList getPredicate(String subj,String pred) {
        Dictionary dictionary = null;
        ArrayList<IndexWord> pos = new ArrayList<IndexWord>();
        ArrayList<String> responsePred = new ArrayList<String>();
        IndexWord isubj;
        try {
            dictionary = Dictionary.getDefaultResourceInstance();

        } catch (JWNLException e) {
            e.printStackTrace();
        }
        try {
            IndexWord noun=dictionary.getIndexWord(POS.NOUN, pred);
            pos.add(noun);
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        try {
            IndexWord verb=dictionary.getIndexWord(POS.VERB, pred);
            pos.add(verb);
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        try {
            IndexWord abverb=dictionary.getIndexWord(POS.ADVERB, pred);
            pos.add(abverb);
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        try {
            IndexWord adj=dictionary.getIndexWord(POS.ADJECTIVE, pred);
            pos.add(adj);
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        try {
            // System.out.println(pos.size()+" nnnnnnnnnnnnn");
            for(int i =0;i<pos.size();i++) {
                try {
                    isubj=dictionary.getIndexWord(POS.NOUN, subj);

                    ArrayList temp =  demonstrateAsymmetricRelationshipOperation(isubj,pos.get(i));
                    if(temp!=null) {

                        for (int k = 0; k < temp.size(); k++) {

                            responsePred.add((String)temp.get(k));
                        }

                    }
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

            }
        } catch (JWNLException e) {

            e.printStackTrace();
        }
        for(int a =0;a<responsePred.size();a++){
            System.out.println(responsePred.get(a)+" predicate index : "+ a);
        }
        if(responsePred.size()<1){
            responsePred.add(predicate);
        }


        return responsePred;
    }

    public ResultSet executeQuery(Query queryString) throws Exception {

        try  {
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", queryString);
            // Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000");

            // Execute.
            //ResultSet rs = qexec.execSelect();
            // System.out.println("testtttt");
            // ResultSetFormatter.out(System.out, rs, queryString);
            return qexec.execSelect();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        QueryExecution exec =  QueryExecutionFactory.create(QueryFactory.create(queryString), new
//                DatasetImpl(ModelFactory.createDefaultModel()));
        return null;

    }
    private ArrayList demonstrateAsymmetricRelationshipOperation(IndexWord start, IndexWord end) throws JWNLException, CloneNotSupportedException {
        // Try to find a relationship between the first sense of <var>start</var> and the first sense of <var>end</var>
        ArrayList<String> predicateList = new ArrayList<String>();

        try {
            RelationshipList list = RelationshipFinder.findRelationships(start.getSenses().get(0), end.getSenses().get(0), PointerType.HYPERNYM);
            System.out.println("Hypernym relationship between \"" + start.getLemma() + "\" and \"" + end.getLemma() + "\":");

            for (Object aList : list) {

                ((Relationship) aList).getNodeList().print();
                String[] temp = ((Relationship) aList).getNodeList().toString().split("(?<= --) ");

                for (int i = 0; i < temp.length; i++) {

                    if (temp[i].contains("Words:")) {
                        String first = temp[i].substring(temp[i].lastIndexOf(": ") + 2);
                        String second = first.substring(0, first.lastIndexOf(" --"));
                        // System.out.println(second+" ??????");
                        String[] res = second.split(", ");
                        for (int j = 0; j < res.length; j++) {
                            if (!predicateList.contains(res[j])) {
                                predicateList.add(res[j]);

                            }

                        }
                    }
                }
            }
        }catch(NullPointerException e){
            e.printStackTrace();
            return null;
        }
        return predicateList;
        //  System.out.println("Common Parent Index: " + ((AsymmetricRelationship) list.get(0)).getCommonParentIndex());
        //  System.out.println("Depth: " + list.get(0).getDepth());
    }

}
