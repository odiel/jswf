package framework;

import framework.exceptions.FirstComponentNotProvidedException;
import framework.exceptions.RunnerNotProvidedException;

public class Framework {

    protected AbstractComponent firstComponent;

    protected AbstractComponent currentComponent;

    protected RunnerInterface runner;

    public RunnerInterface getRunner() {
        return runner;
    }

    public Framework setRunner(RunnerInterface runner) {
        this.runner = runner;

        return this;
    }

    public Framework(){}

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

    public void run() throws Exception {
        if (runner == null) {
            throw new RunnerNotProvidedException("A Runner instance must be provided in order to start the execution.");
        }

        if (firstComponent == null) {
            throw new FirstComponentNotProvidedException("At least one component must be provided in order to start the execution.");
        }

        runner.run(firstComponent, new Environment());
    }

}
