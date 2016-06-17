package jswf.framework;

public interface ComponentInterface {

    public void setNext(ComponentInterface next);

    public ComponentInterface getNext();

    public void next(Environment environment);

    public void invoke(Environment environment);

}
