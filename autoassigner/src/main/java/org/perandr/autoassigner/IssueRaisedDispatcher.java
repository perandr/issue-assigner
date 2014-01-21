package org.perandr.autoassigner;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.database.DatabaseSession;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.IssueHandler;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.notifications.Notification;
import org.sonar.api.notifications.NotificationDispatcher;
import org.sonar.api.platform.ComponentContainer;
import org.sonar.api.resources.JavaFile;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Resource;
import org.sonar.api.rule.RuleKey;

public class IssueRaisedDispatcher extends NotificationDispatcher implements IssueHandler{
	private static final Logger LOG = LoggerFactory.getLogger(IssueRaisedDispatcher.class);
	private ProjectFileSystem projectFileSystem;
	private ComponentContainer componentContainer;
	  private DatabaseSession session;
	
	public IssueRaisedDispatcher(ComponentContainer componentContainer,  DatabaseSession session) {
		this.componentContainer = componentContainer;
		this.session =session;
	}
	@Override
	public void dispatch(Notification notification, NotificationDispatcher.Context context) {
		
		LOG.info("==================================\r\n{}",notification.getType());
		LOG.info("DefaultMessage {}",notification.getDefaultMessage());
		LOG.info("toString : ", notification.toString());
	}

	public void onIssue(org.sonar.api.issue.IssueHandler.Context context) {
		if(context.isNew()){
			Issue issue = context.issue();
			LOG.info("====================== ISSSSUUUUUEEEEEEEEEE",issue.message());
			LOG.info("Message {}",issue.message());
			LOG.info("assignee {}",issue.assignee());
			LOG.info("authorLogin {}",issue.authorLogin());
			LOG.info("creationDate {}",issue.creationDate());
			LOG.info("ruleKey {}",issue.ruleKey());
			LOG.info("rule Line {}",issue.line());
			LOG.info("actionPlanKey {}",issue.actionPlanKey());
			LOG.info("componentKey {}",issue.componentKey());
			LOG.info("severity{}",issue.severity());
			RuleKey ruleKey = issue.ruleKey();
			LOG.info("ruleKey.repository{}",ruleKey.repository());
			LOG.info("ruleKey.rule{}",ruleKey.rule());
			
			LOG.info("attributes");
			Map<String, String> attributes = issue.attributes();
			for(String attribute : attributes.keySet()){
				LOG.info("Key{}, Value{}",attribute,attributes.get(attribute));
			}
			
			
			
			if(projectFileSystem!=null){
			LOG.info("===File");
			File file = projectFileSystem.resolvePath(issue.componentKey());
			if(file!=null){
				Resource resource = projectFileSystem.toResource(file);
				dumpResource(resource);
			}
			}
			
			LOG.info("===JavaFile");
			JavaFile jf = new JavaFile(issue.componentKey());
			if(jf!=null){
				  Metric measure = getSCMMesure(jf);
				dumpResource(jf);
				
			}
			
			LOG.info("===Component");
			Object component = componentContainer.getComponentByKey(issue.componentKey());
			if(component!=null){
			if(component instanceof Resource){
				dumpResource((Resource)component);
			}
			}
			
			
		}
	}
	
	private Metric getSCMMesure(JavaFile jf) {
		//CoreMetrics.SCM_AUTHORS_BY_LINE
		Object criterias;
		 List<Metric> metrics = session.getResults(Metric.class, criterias);
		return metrics.get(0);
	}
	public static void dumpResource(Resource resource) {
		LOG.info("--------------------------- RESOURCE -----------------------");
		LOG.info("ID {}",resource.getId());
		LOG.info("Key {}",resource.getKey());
		LOG.info("EffectiveKey {}",resource.getEffectiveKey());

		LOG.info("Name {}",resource.getName());
		LOG.info("LongName {}",resource.getLongName());
		LOG.info("Description {}",resource.getDescription());
		
		LOG.info("Qualifier {}",resource.getQualifier());
		LOG.info("Scope {}",resource.getScope());
		LOG.info("Parent {}",resource.getParent());
		LOG.info("Language {}",resource.getLanguage());
		
		LOG.info("------------------------------------------------------------");		
	}


}
