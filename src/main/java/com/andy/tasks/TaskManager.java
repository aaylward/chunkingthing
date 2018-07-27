package com.andy.tasks;

import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class TaskManager {
  private final ExecutorService executorService = Executors.newSingleThreadExecutor();
  private final AtomicReference<Task> taskProcessorAtomicReference = new AtomicReference<>();

  @Inject
  public TaskManager() {
    taskProcessorAtomicReference.set(new Task(ReplaySubject.create(), executorService));
  }

  public Observable<String> ensureTaskRunning() {
    if (!taskProcessorAtomicReference.get().isRunning()) {
      taskProcessorAtomicReference.set(new Task(ReplaySubject.create(), executorService));
    }
    return taskProcessorAtomicReference.get().getObservableReplay();
  }

  public Observable<String> getObservable() {
    return taskProcessorAtomicReference.get().getObservableReplay();
  }
}
