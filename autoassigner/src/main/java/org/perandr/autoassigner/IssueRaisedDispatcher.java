package org.perandr.autoassigner;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.database.DatabaseSession;
import org.sonar.api.database.model.MeasureData;
import org.sonar.api.database.model.MeasureModel;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.IssueHandler;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.notifications.Notification;
import org.sonar.api.notifications.NotificationDispatcher;
import org.sonar.api.platform.ComponentContainer;
import org.sonar.api.qualitymodel.Characteristic;
import org.sonar.api.qualitymodel.CharacteristicProperty;
import org.sonar.api.resources.JavaFile;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Resource;
import org.sonar.api.rule.RuleKey;

public class IssueRaisedDispatcher extends NotificationDispatcher implements
		IssueHandler {
	private static final Logger LOG = LoggerFactory
			.getLogger(IssueRaisedDispatcher.class);

	private ComponentContainer componentContainer;
	private DatabaseSession session;
	
	public IssueRaisedDispatcher(ComponentContainer componentContainer,
			DatabaseSession session) {
		this.componentContainer = componentContainer;
		this.session = session;
	}

	@Override
	public void dispatch(Notification notification,
			NotificationDispatcher.Context context) {

		LOG.info("==================================\r\n{}",
				notification.getType());
		LOG.info("DefaultMessage {}", notification.getDefaultMessage());
		LOG.info("toString : ", notification.toString());
	}

	public void onIssue(org.sonar.api.issue.IssueHandler.Context context) {
		if (context.isNew()) {
			Issue issue = context.issue();
			LOG.info("====================== ISSSSUUUUUEEEEEEEEEE",
					issue.message());
			LOG.info("Message {}", issue.message());
			LOG.info("assignee {}", issue.assignee());
			LOG.info("authorLogin {}", issue.authorLogin());
			LOG.info("creationDate {}", issue.creationDate());
			LOG.info("ruleKey {}", issue.ruleKey());
			LOG.info("rule Line {}", issue.line());
			LOG.info("actionPlanKey {}", issue.actionPlanKey());
			LOG.info("componentKey {}", issue.componentKey());
			LOG.info("severity{}", issue.severity());
			RuleKey ruleKey = issue.ruleKey();
			LOG.info("ruleKey.repository{}", ruleKey.repository());
			LOG.info("ruleKey.rule{}", ruleKey.rule());

			LOG.info("attributes");
			Map<String, String> attributes = issue.attributes();
			if (attributes != null && !attributes.isEmpty()) {
				for (String attribute : attributes.keySet()) {
					LOG.info("Key{}, Value{}", attribute,
							attributes.get(attribute));
				}
			} else {
				LOG.info("!! attributes are empty !!");
			}

//			if (projectFileSystem != null) {
//				LOG.info("===File");
//				File file = projectFileSystem.resolvePath(issue.componentKey());
//				if (file != null) {
//					Resource resource = projectFileSystem.toResource(file);
//					dumpResource(resource);
//				}
//			}

			JavaFile jf = new JavaFile(issue.componentKey());
			if (jf != null) {
				LOG.info("===JavaFile");
				//Metric measure = getSCMMesure(context, jf);
				dumpResource(jf);
			}

			Object component = componentContainer.getComponentByKey(issue
					.componentKey());
			if (component != null) {
				LOG.info("===Component");
				if (component instanceof Resource) {
					LOG.info("===Component == Resource");
					dumpResource((Resource) component);
				}
			}

		}
	}

	private Metric getSCMMesure(
			org.sonar.api.issue.IssueHandler.Context context, JavaFile jf) {
		// CoreMetrics.SCM_AUTHORS_BY_LINE
		List<Metric> metrics = session.getResults(Metric.class);

		//dumpMetric(metrics);

		List<MeasureModel> measures = session.getResults(MeasureModel.class);
		dumpMesures(measures);

		// return metrics.get(0);
		return null;
	}

	private void dumpMetric(List<Metric> metrics) {
		LOG.info("========== METRICS ===========");
		for (Metric metric : metrics) {
			LOG.info(String.format("Class [%s], Key %s, Name %s, Origin %s",
					metric.getClass(), metric.getKey(), metric.getName(),
					metric.getOrigin()));
			if (CoreMetrics.SCM_AUTHORS_BY_LINE.equals(metric.getKey())) {
				LOG.info("!!!!!!!!!! FOUND METRIC"
						+ CoreMetrics.SCM_AUTHORS_BY_LINE);
			}

		}
	}

	private void dumpMesures(List<MeasureModel> measures) {
		LOG.info("========== MEASURES ===========");
		for (MeasureModel measure : measures) {
			String str = "";
			StringBuilder sb = new StringBuilder(1024);
			MeasureData measureData = measure.getMeasureData();
			if (measureData != null) {
				str = String.format("Data.Test: [%s], Data [%s],",
						measureData.getText(), measureData.getData());
			}

			sb.append("Class: [");
			sb.append(measure.getClass());
			sb.append("], ");
			sb.append("AlertText: [");
			sb.append(measure.getAlertText());
			sb.append("], ");
			sb.append("Url: [");
			sb.append(measure.getUrl());
			sb.append("], ");
			sb.append("Description: [");
			sb.append(measure.getDescription());
			sb.append("], ");

			sb.append("TextValue: [");
			sb.append(measure.getTextValue());
			sb.append("], ");
			sb.append("Value: [");
			sb.append(measure.getValue());
			sb.append("], ");
			sb.append("MesureData: [");
			sb.append(str);
			sb.append("], ");

			//dump(measure.getCharacteristic(), sb);
			LOG.info(sb.toString());
		}
	}

	private void dump(Characteristic characteristic, StringBuilder sb) {
		// if (characteristic != null) {
		// sb.append("Characteristic: \r\n");
		// sb.append("Key: [");
		// sb.append(characteristic.getKey());
		// sb.append("], ");
		// sb.append("Description: [");
		// sb.append(characteristic.getDescription());
		// sb.append("], ");
		// sb.append("Name: [");
		// sb.append(characteristic.getName());
		// sb.append("], ");
		// sb.append("\r\n\t\tProperties: [");
		// for (CharacteristicProperty prop : characteristic.getProperties()) {
		// sb.append("[");
		// sb.append(prop.getKey());
		// sb.append(", ");
		// sb.append(prop.getTextValue());
		// sb.append("], ");
		// }
		// sb.append("], ");
		// List<Characteristic> children = characteristic.getChildren();
		// if (children != null && !children.isEmpty()) {
		// for (Characteristic child : characteristic.getChildren()) {
		// dump(child, sb);
		// }
		// }
		// }
	}

	public static void dumpResource(Resource resource) {
		LOG.info("--------------------------- RESOURCE -----------------------");
		LOG.info("ID {}", resource.getId());
		LOG.info("Key {}", resource.getKey());
		LOG.info("EffectiveKey {}", resource.getEffectiveKey());

		LOG.info("Name {}", resource.getName());
		LOG.info("LongName {}", resource.getLongName());
		LOG.info("Description {}", resource.getDescription());

		LOG.info("Qualifier {}", resource.getQualifier());
		LOG.info("Scope {}", resource.getScope());
		LOG.info("Parent {}", resource.getParent());
		LOG.info("Language {}", resource.getLanguage());

		LOG.info("------------------------------------------------------------");
	}

}
