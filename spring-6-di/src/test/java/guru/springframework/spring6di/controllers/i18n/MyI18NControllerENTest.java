package guru.springframework.spring6di.controllers.i18n;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyI18NControllerENTest {

    @Autowired
    MyI18NController myi18NController;

    @Test
    void sayHello() {
        System.out.println(this.myi18NController.sayHello());
    }

}