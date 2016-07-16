package jswf.framework;

import java.util.HashMap;

public interface RunnerInterface {

    public void run(ComponentInterface component, HashMap<String, Object> services) throws Exception;

}
