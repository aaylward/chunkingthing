package com.andy;

import com.hubspot.dropwizard.guicier.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ExampleApp extends Application<ExampleConfig> {
  public static void main(String... args) throws Exception {
    new ExampleApp().run(args);
  }

  @Override
  public void initialize(Bootstrap<ExampleConfig> bootstrap) {
    GuiceBundle<ExampleConfig> guiceBundle = GuiceBundle.defaultBuilder(ExampleConfig.class)
        .modules(new ExampleModule())
        .build();

    bootstrap.addBundle(guiceBundle);
  }

  @Override
  public void run(ExampleConfig configuration, Environment environment) throws Exception {
  }
}
