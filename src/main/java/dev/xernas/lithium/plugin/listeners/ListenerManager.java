package dev.xernas.lithium.plugin.listeners;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {

    private final List<Listener> listeners = new ArrayList<>();

    public void registerListener(Listener listener) {
        listeners.add(listener);
    }

    public List<Listener> getListeners() {
        return listeners;
    }

}
