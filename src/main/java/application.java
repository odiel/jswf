import framework.Framework;
import framework.components.FirstComponent;
import framework.components.LogRequestComponent;
import framework.components.RouteHandlerComponent;
import framework.components.SecondComponent;

public class application {

    public static void main(String args[]) {
        Framework framework = new Framework();
        framework
                .addComponent(new LogRequestComponent())
                .addComponent(new RouteHandlerComponent())
                .addComponent(new FirstComponent())
                .addComponent(new SecondComponent())
        ;

        try {
            framework.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
