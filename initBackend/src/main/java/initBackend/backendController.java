package initBackend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class backendController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/nlp", params = {"message", "subject", "predicate", "onlyMatch"})
    public String startAnalyze(@RequestParam String subject, @RequestParam String predicate, @RequestParam String onlyMatch, @RequestParam String message) {
        initBackend a = new initBackend(subject,
                predicate, message);
        try {
            boolean userOption = Boolean.parseBoolean(onlyMatch);
            String result = a.lookup(userOption);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "An error occur while searching";
    }

    @RequestMapping(value = "/nlp", params = {"message", "subject", "predicate"})
    public String startAnalyze(@RequestParam String subject, @RequestParam String predicate, @RequestParam String message) {
        initBackend a = new initBackend(subject,
                predicate, message);
        try {
            String result = a.lookup();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "An error occur while searching";
    }
}
