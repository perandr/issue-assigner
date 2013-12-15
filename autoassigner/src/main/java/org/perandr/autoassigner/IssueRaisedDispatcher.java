package org.perandr.autoassigner;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.IssueHandler;
import org.sonar.api.notifications.Notification;
import org.sonar.api.notifications.NotificationDispatcher;
import org.sonar.api.rule.RuleKey;

public class IssueRaisedDispatcher extends NotificationDispatcher implements IssueHandler{
	private static final Logger LOG = LoggerFactory.getLogger(IssueRaisedDispatcher.class);
	
	@Override
	public void dispatch(Notification notification, NotificationDispatcher.Context context) {
		
		LOG.info("==================================\r\n{}",notification.getType());
		
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
			LOG.info("actionPlanKey{}",issue.actionPlanKey());
			LOG.info("componentKey{}",issue.componentKey());
			LOG.info("severity{}",issue.severity());
			
			RuleKey ruleKey = issue.ruleKey();
			LOG.info("ruleKey.repository{}",ruleKey.repository());
			LOG.info("ruleKey.rule{}",ruleKey.rule());
			LOG.info("ruleKey.rule{}",ruleKey.rule());
			
			LOG.info("attributes");
			
			Map<String, String> attributes = issue.attributes();
			for(String attribute : attributes.keySet()){
				LOG.info("Key{}, Value{}",attribute,attributes.get(attribute));
			}
		}
	}


}
