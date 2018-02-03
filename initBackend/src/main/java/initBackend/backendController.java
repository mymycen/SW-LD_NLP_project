package initBackend;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CharacterEncodingFilter;

@RestController
public class backendController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/nlp",params = {"message","subject","predicate","onlyMatch"})
    public String startAnalyze(@RequestParam String subject,@RequestParam String predicate,@RequestParam String onlyMatch,@RequestParam String message) {
        initBackend a = new initBackend(subject,
                predicate,message);
        try{
            boolean userOption = Boolean.parseBoolean(onlyMatch);
            String result = a.lookup(userOption);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }


        return "An error occur while searching";
    }

    @RequestMapping(value = "/nlp",params = {"message"})
    public String startAnalyze(@RequestParam String message) {
        String subject="";
        String predicate="";
        initBackend a = new initBackend(subject,
                predicate,message);
        try{
            String result = a.lookup();
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }


        return "An error occur while searching";
    }
    @RequestMapping(value = "/nlp",params = {"message","subject","predicate"})
    public String startAnalyze(@RequestParam String subject,@RequestParam String predicate,@RequestParam String message) {
        initBackend a = new initBackend(subject,
                predicate,message);
        try{
            String result = a.lookup();
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }


        return "An error occur while searching";
    }
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilter.setEncoding("UTF-8");
        registrationBean.setFilter(characterEncodingFilter);
        return registrationBean;
    }
}
