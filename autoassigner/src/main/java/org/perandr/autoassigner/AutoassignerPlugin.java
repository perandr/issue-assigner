package org.perandr.autoassigner;

import org.perandr.autoassigner.batch.ExampleSensor;
import org.perandr.autoassigner.batch.RandomDecorator;
import org.perandr.autoassigner.ui.ExampleFooter;
import org.perandr.autoassigner.ui.ExampleRubyWidget;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * This class is the entry point for all extensions
 */
@Properties({
  @Property(
    key = AutoassignerPlugin.CC_COPY_PROPERTY,
    name = "Contact Copy",
    description = "List of emails in cc.",
    defaultValue = ""),
  @Property(
		    key = AutoassignerPlugin.SUBJECT_TEMPLATE_PROPERTY,
		    name = "Subject",
		    description = "Subject of Notification Message",
		    defaultValue = "[sonar][issue]")
  })
public final class AutoassignerPlugin extends SonarPlugin {

  public static final String CC_COPY_PROPERTY = "autoassigner.cc";
  public static final String SUBJECT_TEMPLATE_PROPERTY = "autoassigner.subject";

  // This is where you're going to declare all your Sonar extensions
  public List getExtensions() {
    return Arrays.asList(
        // Definitions
        //ExampleMetrics.class,
    		IssueRaisedDispatcher.class,
        // Batch
        ExampleSensor.class/*, RandomDecorator.class,

        // UI
        ExampleFooter.class, ExampleRubyWidget.class*/);
  }
}
