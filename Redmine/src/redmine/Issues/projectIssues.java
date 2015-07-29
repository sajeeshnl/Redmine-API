package redmine.Issues;


import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.UserManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueFactory;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Tracker;
import com.taskadapter.redmineapi.bean.User;
import com.taskadapter.redmineapi.ProjectManager;

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
@Path("/project/{name}")
public class projectIssues {
	
	private static final Logger logger =
	        Logger.getLogger(projectIssues.class.getName());
	 String uri = "http://10.4.6.10/redmine/";
	    String login = "admin";
	    String pass = "admin";
	    String projectKey = null;
	    Integer queryId = null; 
	    
	    @GET
		  @Produces("application/json")
	    public Response tickets(@PathParam("name")String name) throws JSONException, RedmineException  {
	    	logger.info("entering fn");
	    	 String temp = "";
	    	 String just = "";
	    	  RedmineManager mgr = RedmineManagerFactory.createWithUserAuth(uri,login,pass);
			    IssueManager issueManager = mgr.getIssueManager();
			    ProjectManager pmgr = mgr.getProjectManager();
			  
			  //  JSONObject[] objects = new JSONObject[10];
			    JSONObject jsonObject = new JSONObject();
				
			    List<Project> prjcts = pmgr.getProjects();
			    for (Project prjct : prjcts) {
			    	if(prjct.getName().equals(name)){
			    		 projectKey = prjct.getName().toLowerCase();
			    	}
			    }
			    
			    List<Issue> issues = issueManager.getIssues(projectKey, queryId);
			    for (Issue issue : issues) {
			    	
			    	
			 
				    User usr = issue.getAssignee();
			   	    Tracker trcr = issue.getTracker();
			 		
			    	  
				    temp = temp + issue +","+  "Priority:" + issue.getPriorityText() + ","+"Tracker:"+trcr.getName()+","+"Status:"+issue.getStatusName()+ "\n";
			    	
                  
			    }
			    jsonObject.put("allissues",temp);
				
				String result = "" + jsonObject;
				return Response.status(200).entity(result).build(); 
			    		
			  
			    
			  
	    }

}
