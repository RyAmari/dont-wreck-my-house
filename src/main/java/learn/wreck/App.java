package learn.wreck;

import learn.wreck.data.ReservationFileRepository;
import learn.wreck.data.HostFileRepository;
import learn.wreck.data.GuestFileRepository;
import learn.wreck.domain.ReservationService;
import learn.wreck.domain.HostService;
import learn.wreck.domain.GuestService;
import learn.wreck.ui.Controller;
import learn.wreck.ui.ConsoleIO;
import learn.wreck.ui.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@PropertySource("classpath:data.properties")
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(App.class);
        Controller controller = context.getBean(Controller.class);
        controller.run();
    }
}
