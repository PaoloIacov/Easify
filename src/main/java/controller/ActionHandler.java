package controller;

@FunctionalInterface
public interface ActionHandler {
    boolean handleAction(String action);
}

