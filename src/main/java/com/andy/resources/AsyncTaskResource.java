package com.andy.resources;

import com.andy.tasks.TaskManager;
import io.reactivex.Observable;
import org.glassfish.jersey.server.ChunkedOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CountDownLatch;

@Path("tasks")
@Produces(MediaType.TEXT_PLAIN)
public class AsyncTaskResource {
  private static final Logger LOG = LoggerFactory.getLogger(AsyncTaskResource.class);

  private final TaskManager manager;

  @Inject
  public AsyncTaskResource(TaskManager manager) {
    this.manager = manager;
  }

  @POST
  public ChunkedOutput<String> newTask() {
    return getChunkedOutput(manager.ensureTaskRunning());
  }

  @GET
  public ChunkedOutput<String> existingTask() {
    return getChunkedOutput(manager.getObservable());
  }

  @GET
  @Path("str")
  public void streamingOutput(@Context HttpServletResponse response) {
    writeObservable(response);
  }

  private void writeObservable(HttpServletResponse response) {
    final CountDownLatch latch = new CountDownLatch(1);

    manager.getObservable().subscribe(
        (next) -> {
          response.getOutputStream().write((next + System.lineSeparator()).getBytes());
          response.getOutputStream().flush();
        },
        (t) -> LOG.error("error {}", t),
        latch::countDown);

    try {
      latch.await();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  private ChunkedOutput<String> getChunkedOutput(Observable<String> observable) {
    final ChunkedOutput<String> output = new ChunkedOutput<>(String.class);

    observable.subscribe(
        (next) -> output.write(next + System.lineSeparator()),
        (t) -> LOG.error("error {}", t),
        () -> closeOutput(output));

    return output;
  }

  private void closeOutput(ChunkedOutput<String> output) {
    try {
      output.close();
    } catch (Exception e) {
      LOG.error("error closing stream");
    }
  }
}
