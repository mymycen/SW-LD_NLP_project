package initBackend;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

public class initBackend {
    private final String subject;
    private final String predicate;

    public initBackend(String subject, String predicate) {
        this.subject = subject;
        this.predicate = predicate;

    }
    public String lookup(){

        final String uri = "http://lookup.dbpedia.org/api/search/KeywordSearch?QueryString="+subject;
        JSONObject jsonObj;
    RestTemplate restTemplate = new RestTemplate();
    String result = restTemplate.getForObject(uri, String.class);
        try {

            jsonObj = new JSONObject(result);
            JSONObject jsonObj2 = new JSONObject(jsonObj.get("results"));
           // JSONObject jsonObj3 = new JSONObject(jsonObj2.get("classes"));
            System.out.println(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    return result;
    }



    public String getSubject() {
        return subject;
    }

    public String getPredicate() {
        return predicate;
    }
}
