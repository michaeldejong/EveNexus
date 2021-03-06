package nl.minicom.evenexus.bugreport;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.BugReport;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.api.v2.schema.Issue;
import com.github.api.v2.schema.Issue.State;
import com.github.api.v2.services.GitHubServiceFactory;
import com.github.api.v2.services.IssueService;
import com.github.api.v2.services.auth.LoginTokenAuthentication;

/**
 * This class is responsible for reporting bugs to the GitHub bug repo.
 *
 * @author michael
 */
@Singleton
public class BugReporter {
	
	private static final Logger LOG = LoggerFactory.getLogger(BugReporter.class);
	
	private static final String REPORTER_USER = "bugreporter";
	private static final String REPORTER_TOKEN = "426b0b26a84a4e8b6f62211488102e8a";
	private static final String REPO_NAME = "EveNexus";
	private static final String REPO_OWNER = "michaeldejong";

	private final Database database;
	
	/**
	 * This constructs a new {@link BugReporter} object.
	 * 
	 * @param database
	 * 		A reference to the {@link Database}.
	 */
	@Inject
	BugReporter(Database database) {
		this.database = database;
	}
	
	/**
	 * This method creates a new GitHub issue.
	 * 
	 * @param title
	 * 		The title of the issue.
	 * 
	 * @param message
	 * 		The message of the issue.
	 * 
	 * @return
	 * 		The number of the issue which was just created.
	 * 
	 * @throws Exception
	 * 		If for some reason we could not create a new GitHub issue.
	 */
	public int createNewIssue(String title, String message) throws Exception {
		IssueService service = createService();
		createNewIssue(title, message, service);
		int issueNumber = getCreatedIssueNumber(service);
		persistIssue(issueNumber);
		return issueNumber;
	}

	private IssueService createService() {
		GitHubServiceFactory factory = GitHubServiceFactory.newInstance();
		IssueService service = factory.createIssueService();
		service.setAuthentication(new LoginTokenAuthentication(REPORTER_USER, REPORTER_TOKEN));
		return service;
	}
	
	private void createNewIssue(String title, String message, IssueService issueService) {
		LOG.info("Submitting new issue to the GitHub issue tracker...");
		issueService.createIssue(REPO_OWNER, REPO_NAME, title, message);
	}

	private int getCreatedIssueNumber(IssueService issueService) {
		List<Issue> issues = issueService.getIssues(REPO_OWNER, REPO_NAME, State.OPEN);
		return issues.get(issues.size() - 1).getNumber();
	}

	/**
	 * This method saves the issue number to the database.
	 * 
	 * @param issueNumber
	 * 		The saved issue number.
	 */
	@Transactional
	void persistIssue(int issueNumber) {
		Session session = database.getCurrentSession();
		BugReport report = new BugReport();
		report.setIssueNumber(issueNumber);
		session.save(report);
	}
	
}
