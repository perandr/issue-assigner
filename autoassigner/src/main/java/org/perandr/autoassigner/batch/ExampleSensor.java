package org.perandr.autoassigner.batch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.perandr.autoassigner.ExampleMetrics;
import org.perandr.autoassigner.AutoassignerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.database.DatabaseSession;
import org.sonar.api.database.model.MeasureData;
import org.sonar.api.database.model.MeasureModel;
import org.sonar.api.database.model.User;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.IssueHandler;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.JavaFile;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.Violation;
import org.sonar.api.utils.KeyValueFormat;

public class ExampleSensor implements Sensor, IssueHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(ExampleSensor.class);

	public static final String SCM_AUTHORS_BY_LINE_KEY = "authors_by_line";
	private Settings settings;

	private DatabaseSession session;
	FileLinesContextFactory fileLinesContextFactory;

	/**
	 * Use of IoC to get Settings
	 */
	public ExampleSensor(Settings settings,
			FileLinesContextFactory fileLinesContextFactory,
			DatabaseSession session) {
		this.settings = settings;
		this.fileLinesContextFactory = fileLinesContextFactory;
		this.session = session;
		// dumpSettings(settings);
	}

	private void dumpSettings(Settings settings2) {
		Map<String, String> properties = settings.getProperties();
		for (String propKey : properties.keySet()) {
			LOG.info("Key {}, Value {}", propKey, properties.get(propKey));
		}
	}

	public boolean shouldExecuteOnProject(Project project) {
		// This sensor is executed on any type of projects
		return true;
	}

	public void analyse(Project project, SensorContext sensorContext) {
		String value = settings
				.getString(AutoassignerPlugin.SUBJECT_TEMPLATE_PROPERTY);
		LOG.info(AutoassignerPlugin.SUBJECT_TEMPLATE_PROPERTY + "=" + value);
		LOG.info("============Date: {} ", project.getAnalysisDate());
		LOG.info("============IsLatest: {} ", project.isLatestAnalysis());

		// LOG.info("============Date: {} ",);
		// Violation violation = Violation.;
		// sensorContext.saveViolation(violation )
		// // Measure measure = new Measure(ExampleMetrics.MESSAGE, value);
		// sensorContext.saveMeasure(measure);
	}

	public void onIssue(Context context) {
		if (context.isNew()) {
			Issue issue = context.issue();
			JavaFile jf = new JavaFile(issue.componentKey());
			if (jf != null) {
				LOG.info("ResourceID {}", jf.getId());
				Query query = session
						.getEntityManager()
						.createQuery(
								"select mm from MeasureModel as mm, "
										+ "\r\n Metric as m where m.key='authors_by_line' and m.id=mm.metricId");
				List<MeasureModel> result = query.getResultList();
				for (MeasureModel measure : result) {
					LOG.info("==========" + measure.getId()
							+ "==================== ");
					MeasureData data = measure.getMeasureData();
					Map<String, String> values = KeyValueFormat.parse(data
							.getText());
					dump(values);
					String author = values.get(String.valueOf(issue.line()));
					if (StringUtils.isNotBlank(author)) {
						User user = getUser(author);
						context.setAuthorLogin(user.getLogin());
						context.assign(user.getLogin());
						LOG.info("======================================== ");
						LOG.info("Author: " + author + "==================== ");
						LOG.info("======================================== ");
					}
				}

			}
		}
	}

	private User getUser(String author) {
		User user; 
		Query userQuery = session
				.getEntityManager()
				.createQuery(
						"select u from User as u where u.login =:login or u.email=:login");
		userQuery.setParameter("login", author);
		List<User> users = userQuery.getResultList();
		
		if(CollectionUtils.isEmpty(users)){
			user = createUser(author);
		}else{
			user = users.get(0);
		}
		return user;
	}

	private User createUser(String author) {
		User user = new User();
		user.setLogin(author);
		if(isEmail(author)){
			user.setEmail(author);
		}
		session
		.getEntityManager().persist(user);
		session
		.getEntityManager().flush();
		return user;
	}

	private boolean isEmail(String author) {		
		return author.contains("@");
	}

	private void dump(Map<String, String> values) {
		LOG.info("======================================== ");
		for (String key : values.keySet()) {
			LOG.info("Key {}, Value {}", key, values.get(key));
		}
		LOG.info("======================================== ");
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
