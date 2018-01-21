package initBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class test {
    public static void main(String[] args) {
        SpringApplication.run(test.class, args);
        //initBackend a = new initBackend("USA","capl","What is the capl of USA?");
        //String x = a.lookup(false);
        //System.out.println(x+"  <<<");
    }
}
