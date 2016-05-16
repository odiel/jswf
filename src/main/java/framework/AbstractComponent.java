package framework;

abstract public class AbstractComponent implements ComponentInterface {

    protected ComponentInterface nextComponent;

    public void setNext(ComponentInterface next) {
        nextComponent = next;
    }

    public ComponentInterface getNext() {
        return nextComponent;
    }

    public void next(Environment environment) {
        if (nextComponent != null) {
            nextComponent.invoke(environment);
        }
    }

}
