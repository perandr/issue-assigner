package org.perandr.autoassigner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.notifications.Notification;
import org.sonar.api.notifications.NotificationDispatcher;

public class IssueRaisedDispatcher extends NotificationDispatcher {
	private static final Logger LOG = LoggerFactory.getLogger(IssueRaisedDispatcher.class);
	
	@Override
	public void dispatch(Notification notification, Context context) {
		
		LOG.info("==================================\r\n{}",notification.getType());
	}

}
