package guru.springframework.spring6di.controllers.i18n;

import guru.springframework.spring6di.services.GreetingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class MyI18NController {

    private final GreetingService greetingService;

    public MyI18NController(@Qualifier("@i18NService") GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public String sayHello() {
        return this.greetingService.sayGreeting();
    }

}
