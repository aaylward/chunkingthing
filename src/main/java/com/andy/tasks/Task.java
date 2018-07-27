package com.andy.tasks;

import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class Task {
  private final ReplaySubject<String> taskObserver;
  private final CompletableFuture<Void> task;

  public Task(ReplaySubject<String> taskObserver, ExecutorService executorService) {
    this.taskObserver = taskObserver;
    this.task = CompletableFuture.runAsync(this::doStuff, executorService);
  }

  public boolean isRunning() {
    return !task.isDone();
  }

  public Observable<String> getObservableReplay() {
    return taskObserver;
  }

  private void doStuff() {
    int i = 0;
    while (i++ < 150) {
      taskObserver.onNext(UUID.randomUUID().toString());

      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
    }

    taskObserver.onNext("all done");
    taskObserver.onComplete();
  }
}
