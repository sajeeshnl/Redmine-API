package redmine.excel;

import com.taskadapter.redmineapi.MembershipManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.UserManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueFactory;
import com.taskadapter.redmineapi.bean.IssueStatusFactory;
import com.taskadapter.redmineapi.bean.Membership;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.bean.User;
import com.taskadapter.redmineapi.bean.Tracker;
import com.taskadapter.redmineapi.bean.TrackerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;






import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
@Path("/tickets/")

public class processExcel {
	
	private static final Logger logger =
		     Logger.getLogger(processExcel.class.getName());
		 String uri = "http://10.4.6.10/redmine/";
		    String login = "admin";
		    String pass = "admin";
		    String projectKey = "demo";
		    Integer queryId = null; 
		    int proid;
		    String temp;
		    int id;
		    @Path("{f}")
		    @PUT
		    
		    public void readExcel(@PathParam("f")String f) throws BiffException, IOException, RedmineException {
		    //	logger.info("fn entered");
		    	 RedmineManager mgr = RedmineManagerFactory.createWithUserAuth(uri,login,pass);
				    IssueManager issueManager = mgr.getIssueManager();
				    ProjectManager pmgr = mgr.getProjectManager();
				    UserManager userManager = mgr.getUserManager();
				    MembershipManager mbrshpmngr = mgr.getMembershipManager();
				  //  IssueStatusFactory  fctry = new  IssueStatusFactory ();
				String FilePath = "/opt/tomcat/" + f;
				FileInputStream fs = new FileInputStream(FilePath);
				Workbook wb = Workbook.getWorkbook(fs);
				List<String> listA = new ArrayList();
				// TO get the access to the sheet
				Sheet sh = wb.getSheet("Sheet1");

				// To get the number of rows present in sheet
				int totalNoOfRows = sh.getRows();

				// To get the number of columns present in sheet
				int totalNoOfCols = sh.getColumns();

				for (int row = 1; row < totalNoOfRows; row++) {

					for (int col = 0; col < totalNoOfCols; col++) {
					 
						
						listA.add(sh.getCell(col, row).getContents());
					    }
					
					if (listA.get(0).equals("create")) {
					logger.info("in create if");
						 List<Project> prjcts = pmgr.getProjects();
						    for (Project prjct : prjcts) {
						  
						    	   	if(prjct.getName().equals( (listA.get(1)))){
						    		 proid = prjct.getId();
						    	}
						    }
						   
					
						   logger.info("proid:"+proid);
						   		 List<Membership>  mbrs = mbrshpmngr.getMemberships(proid);
								    for(Membership mbr : mbrs) {
								    	User usr = mbr.getUser();
								    		    
								if	(((usr.getFullName()).equals(listA.get(5)))) {
									logger.info("equal");
								
										Issue issueToCreate = IssueFactory.create(proid,listA.get(3));
									   			Issue createdIssue = issueManager.createIssue( issueToCreate);
									   System.out.println("id:"+proid);
									   
									   if (listA.get(7) .equals("") != true ){
									   id = Integer.parseInt(listA.get(7));
								    	Tracker trac =  TrackerFactory.create(id,"sd");
								    	
										createdIssue.setTracker(trac);
									
									   }
									
									createdIssue.setAssignee(usr);
									issueManager.update(createdIssue);
								}
								else
								{}
								    }
						
						
						
											    
					}				
					
					if (listA.get(0).equals("delete")) {
	try{
						    
							
							 id = Integer.parseInt(listA.get(2));
				
							issueManager.deleteIssue(id);    
						}
						    catch(RedmineException e) {
						    	logger.info("exception caught");
						    }
					}
					
					
					if (listA.get(0).equals("update")) {
						logger.info("in update if");
						 try{
					
					
								 
							    	int id1 = Integer.parseInt(listA.get(4));
							    	
							    	 id= Integer.parseInt(listA.get(2));
							    	
							    Issue	 issue = issueManager.getIssueById(id);
							    		 id = issue.getStatusId();
							    		logger.info("id2:"+id);
							    		issue.setStatusId(id1);
							    		 id = issue.getStatusId();
							    		logger.info("id3:"+id);
							    		 issueManager.update(issue);
							 
				
							    }
							  
							    
							    catch (RedmineException e)
							    
							    {
							    	 logger.info("exception caught"+f);
							    }
						}
					
					if(listA.get(0).equals("set priority")) {
						logger.info("in set priority if");
						
						
						try {
						
					    	 id = Integer.parseInt(listA.get(2));
							Issue iss = issueManager.getIssueById(id );
						
					    	id = Integer.parseInt(listA.get(6));
					    	iss.setPriorityId(id);
							issueManager.update(iss);
							
						}
						  catch (RedmineException e)
					    
					    {
					    	 logger.info("exception caught"+f);
					    }
					
					
					
					}
					
					
					
					
					if(listA.get(0).equals("set tracker")) {
						logger.info("in set tracker if");
						
						
						try {
							
				
						
					     id = Integer.parseInt(listA.get(7));
					    	Tracker trac =  TrackerFactory.create(id,"sd");
				
					    	
					    	id = Integer.parseInt( listA.get(2));
							Issue iss = issueManager.getIssueById(id );
							iss.setTracker(trac);
							issueManager.update(iss);
							
							
						}
					
	 catch (RedmineException e)
					    
					    {
					    	 logger.info("exception caught"+f);
					    }
						
					}
				  listA.clear();
				//	logger.info("\n");
				System.out.println();
				}
		    }  


}
