package framework.components;

import framework.AbstractComponent;
import framework.Environment;

public class SecondComponent extends AbstractComponent {

    public void invoke(Environment environment) {
        next(environment);
    }

}
