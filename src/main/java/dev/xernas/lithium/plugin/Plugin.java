package dev.xernas.lithium.plugin;

public interface Plugin {

    void onEnable();

    void onDisable();

    default Priority getPriority() {
        return Priority.NORMAL;
    };

}
