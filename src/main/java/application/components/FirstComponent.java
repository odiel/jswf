package application.components;

import framework.AbstractComponent;
import framework.Environment;

public class FirstComponent extends AbstractComponent {

    public void invoke(Environment environment) {
        next(environment);
    }

}
