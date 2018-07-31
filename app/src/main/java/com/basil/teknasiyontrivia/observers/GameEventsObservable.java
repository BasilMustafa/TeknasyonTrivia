package com.basil.teknasiyontrivia.observers;

/**
 * Created by Basil on 7/30/2018.
 */

public interface GameEventsObservable {
    void registerObserver(GameEventsObserver observer,String eventType);
}
