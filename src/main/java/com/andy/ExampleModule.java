package com.andy;

import com.andy.resources.AsyncTaskResource;
import com.andy.tasks.TaskManager;
import com.google.inject.Binder;
import com.hubspot.dropwizard.guicier.DropwizardAwareModule;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerFactory;

public class ExampleModule extends DropwizardAwareModule<ExampleConfig> {
  @Override
  public void configure(Binder binder) {
    binder.bind(DocumentBuilderFactory.class).toInstance(DocumentBuilderFactory.newInstance());
    binder.bind(SAXParserFactory.class).toInstance(SAXParserFactory.newInstance());
    binder.bind(TransformerFactory.class).toInstance(TransformerFactory.newInstance());
    binder.bind(AsyncTaskResource.class);
    binder.bind(TaskManager.class).asEagerSingleton();
  }
}
