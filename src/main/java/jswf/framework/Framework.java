package jswf.framework;

import jswf.framework.exceptions.FirstComponentNotProvidedException;
import jswf.framework.exceptions.RunnerNotProvidedException;

public class Framework {

    protected AbstractComponent firstComponent;

    protected AbstractComponent currentComponent;

    protected RunnerInterface runner;

    public Framework(){}

    public RunnerInterface getRunner() {
        return runner;
    }

    public Framework setRunner(RunnerInterface runner) {
        this.runner = runner;

        return this;
    }

    public Framework addComponent(AbstractComponent component) {
        if (firstComponent == null) {
            firstComponent = component;
            currentComponent = component;
        } else {
            currentComponent.setNext(component);
            currentComponent = component;
        }

        return this;
    }

    public ComponentInterface getComponentPipeLine() {
        return firstComponent;
    }

    public void run() throws Exception {
        if (runner == null) {
            throw new RunnerNotProvidedException("A Http instance must be provided in order to start the execution.");
        }

        if (firstComponent == null) {
            throw new FirstComponentNotProvidedException("At least one component must be provided in order to start the execution.");
        }

        runner.run(firstComponent, new Environment());
    }

}
