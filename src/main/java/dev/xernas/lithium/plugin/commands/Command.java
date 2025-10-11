package dev.xernas.lithium.plugin.commands;

public interface Command {

    String getName();
    String execute(String... args);

}
