package org.perandr.autoassigner.batch;

import java.util.Map;

import org.perandr.autoassigner.ExampleMetrics;
import org.perandr.autoassigner.AutoassignerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.Violation;

public class ExampleSensor implements Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(ExampleSensor.class);

  private Settings settings;

  /**
   * Use of IoC to get Settings
   */
  public ExampleSensor(Settings settings) {
    this.settings = settings;
    //dumpSettings(settings);
  }

  private void dumpSettings(Settings settings2) {
	Map<String, String> properties = settings.getProperties();
	for(String propKey : properties.keySet()){
		LOG.info("Key {}, Value {}", propKey,properties.get(propKey));
	}
}

public boolean shouldExecuteOnProject(Project project) {
    // This sensor is executed on any type of projects
    return true;
  }

  public void analyse(Project project, SensorContext sensorContext) {
    String value = settings.getString(AutoassignerPlugin.SUBJECT_TEMPLATE_PROPERTY);
    LOG.info(AutoassignerPlugin.SUBJECT_TEMPLATE_PROPERTY + "=" + value);
    LOG.info("============Date: {} ",project.getAnalysisDate());
    LOG.info("============IsLatest: {} ",project.isLatestAnalysis());
//    LOG.info("============Date: {} ",);
//    Violation violation = Violation.;
//	sensorContext.saveViolation(violation )
//    //    Measure measure = new Measure(ExampleMetrics.MESSAGE, value);
//    sensorContext.saveMeasure(measure);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
