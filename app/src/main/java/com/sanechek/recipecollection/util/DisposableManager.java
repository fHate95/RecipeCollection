package com.sanechek.recipecollection.util;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.disposables.Disposable;

public final class DisposableManager implements LifecycleObserver {

    private final Set<Disposable> disposeOnPause = new HashSet<>();
    private final Set<Disposable> disposeOnStop = new HashSet<>();
    private final Set<Disposable> disposeOnDestroy = new HashSet<>();

    public DisposableManager(LifecycleOwner owner){
        owner.getLifecycle().addObserver(this);
    }

    public void disposeOnPause(Disposable d){
        disposeOnPause.add(d);
    }

    public void disposeOnStop(Disposable d){
        disposeOnStop.add(d);
    }

    public void disposeOnDestroy(Disposable d){
        disposeOnDestroy.add(d);
    }

    private static void disposeAndClear(Collection<Disposable> disposables){
        for(Disposable d : disposables){
            if(!d.isDisposed()){
                d.dispose();
            }
        }
        disposables.clear();
    }

    public void disposeAll(){
        onDestroy();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause(){
        disposeAndClear(disposeOnPause);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop(){
        onPause();
        disposeAndClear(disposeOnStop);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(){
        onStop();
        disposeAndClear(disposeOnDestroy);
    }
}

