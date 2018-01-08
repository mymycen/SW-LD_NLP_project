package initBackend;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class backendController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/nlp")
    public initBackend startAnalyze(@RequestParam String subject,@RequestParam String predicate,@RequestParam String onlyMatch) {
        initBackend a = new initBackend(subject,
                predicate);
        try{
            boolean userOption = Boolean.parseBoolean(onlyMatch);
            a.lookup(userOption);
        }catch (Exception e){
            e.printStackTrace();
        }


        return new initBackend(subject,
                predicate);
    }
}
