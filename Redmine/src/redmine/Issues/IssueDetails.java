package redmine.Issues;


import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueFactory;
import com.taskadapter.redmineapi.bean.Tracker;
import com.taskadapter.redmineapi.bean.User;

import java.util.List;



import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;





import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
@Path("/preview")
public class IssueDetails {
	
	private static final Logger logger =
	        Logger.getLogger(IssueDetails.class.getName());
	 String uri = "http://10.4.6.10/redmine/";
	    String login = "admin";
	    String pass = "admin";
	    Integer queryId = null; 
	    

		  @Path("{f}")
		  @GET
		  @Produces("application/json")
		
		public Response  ticketsid(@PathParam("f")int f) throws JSONException, RedmineException {

		   
		    RedmineManager mgr = RedmineManagerFactory.createWithUserAuth(uri,login,pass);
		    IssueManager issueManager = mgr.getIssueManager();
		    JSONObject jsonObject = new JSONObject();
		   
			    Issue issue = issueManager.getIssueById(f);
			    User usr = issue.getAssignee();
		   	    Tracker trcr = issue.getTracker();
		   	    
		    
		    	 
		   
			   String temp ="Id=" + Integer .toString(  issue.getId()) + "," + "Subject=" + issue.getSubject() +  "," + "Priority=" + issue.getPriorityText() + "," + "Assignee:" + usr.getFullName() + "," + "Status:" + issue.getStatusName()+","+"Tracker:"+trcr.getName();
			    jsonObject.put("issuesss", temp);
			    
				logger.info("info"+issue);
				logger.info("info"+temp);
				String result = "" + jsonObject;
			return Response.status(200).entity(result).build(); 
			 

	}


}
