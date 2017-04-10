package v1.sample;

import java.net.MalformedURLException;
import java.io.FileOutputStream;
import javax.swing.JFrame;
import java.io.*;

import com.versionone.Oid;
import com.versionone.apiclient.*;
import com.versionone.apiclient.Asset;
import com.versionone.apiclient.Services;
import com.versionone.apiclient.V1Connector;
import com.versionone.apiclient.exceptions.APIException;
import com.versionone.apiclient.exceptions.ConnectionException;
import com.versionone.apiclient.exceptions.OidException;
import com.versionone.apiclient.exceptions.V1Exception;
import com.versionone.apiclient.filters.FilterTerm;
import com.versionone.apiclient.interfaces.IAssetType;
import com.versionone.apiclient.interfaces.IAttributeDefinition;
import com.versionone.apiclient.interfaces.IServices;
import com.versionone.apiclient.services.QueryResult;

import v1.util.v1Properties;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ntestapi  extends JFrame {
	
	static String OPENBRACKET = "{";
	static String CLOSEBRACKET = "}";
	static String OPENBRACE = "[";
	static String CLOSEBRACE = "]";
	
	public ntestapi() {
		super("ntestapi");
		setSize(350, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void makesheet(String xdata) {
		
		Integer index = 0;
		Integer indez, indezz;
		String towrite;
		Cell cell;
		String commitmentAccuracy;
	    Calendar cal = Calendar.getInstance();		
		Workbook workbook = new HSSFWorkbook();
			
		Sheet sheet1 = workbook.createSheet("Project Initiatives");
			
		String[] tokens = xdata.split("Epic:");

		for (String t : tokens) {
			Row row = sheet1.createRow(index);
			if (index > 0) {
				indez = t.indexOf("\"Name\":") + 9;
				indezz = t.indexOf("\"Number\":") - 10;
				towrite = t.substring(indez,indezz);
				cell = row.createCell(0);	
				cell.setCellValue(towrite);	
				indez = t.indexOf("\"Custom_CommitmentAccuracy\":") + 49;
				indezz = t.indexOf("\"Team\":") - 19;
				towrite = t.substring(indez,indezz);
				commitmentAccuracy = " ";
				if (towrite.equals("Custom_Commitment_Accuracy:28610")) commitmentAccuracy = "Day";
				if (towrite.equals("Custom_Commitment_Accuracy:28608")) commitmentAccuracy = "Quarter";
				if (towrite.equals("Custom_Commitment_Accuracy:28609")) commitmentAccuracy = "Month";
				if (towrite.equals("Custom_Commitment_Accuracy:28607")) commitmentAccuracy = "Year";
				if (towrite.equals("Null")) commitmentAccuracy = " ";
				cell = row.createCell(1);	
				cell.setCellValue(commitmentAccuracy);	
				indez = t.indexOf("\"PlannedEnd\":") + 15;
				indezz = t.indexOf("\"Custom_CommitmentAccuracy\":") - 10;
				towrite = t.substring(indez,indezz);
				Date date = null;
				boolean temptest = towrite.equals("ul");
				if (temptest) {
					towrite = " ";
				}
				else {

					try {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
						date = formatter.parse(towrite);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(date);


				    // You cannot use Date class to extract individual Date fields
				    cal.setTime(date);
				    int year = cal.get(Calendar.YEAR);
				    int month = cal.get(Calendar.MONTH);      // 0 to 11
				    int day = cal.get(Calendar.DAY_OF_MONTH);
				    int hour = cal.get(Calendar.HOUR_OF_DAY);
				    int minute = cal.get(Calendar.MINUTE);
				    int second = cal.get(Calendar.SECOND);
				   
				    System.out.printf("Now is %4d/%02d/%02d %02d:%02d:%02d\n",  // Pad with zero
				        year, month+1, day, hour, minute, second);
				    SimpleDateFormat formatter = new SimpleDateFormat("MMM");
				    System.out.println(formatter.format(date));


					cell = row.createCell(6);	
					cell.setCellValue(towrite);
					towrite = month + "";
					cell = row.createCell(7);	
					cell.setCellValue(towrite);	
				    towrite = formatter.format(date);					
					cell = row.createCell(8);	
					cell.setCellValue(towrite);	
				}
			    SimpleDateFormat formatter = new SimpleDateFormat("MMM");
				if (commitmentAccuracy.equals("Day"))
				{
				    formatter = new SimpleDateFormat("MMM-dd-yyyy");
					towrite = formatter.format(date);
				}
				if (commitmentAccuracy.equals("Month"))
				{
				    formatter = new SimpleDateFormat("MMM-yyyy");
					towrite = formatter.format(date);
				}
				if (commitmentAccuracy.equals("Quarter"))
				{
				    formatter = new SimpleDateFormat("yyyy");
					if (cal.get(Calendar.MONTH) < 3) {
						towrite = "Jan,Feb,Mar-" + formatter.format(date);
					}
					else if (cal.get(Calendar.MONTH) < 6) {
						towrite = "Apr,May,Jun-" + formatter.format(date);
					}
					else if (cal.get(Calendar.MONTH) < 9) {
						towrite = "Jul,Aug,Sep-" + formatter.format(date);
					}
					else {
						towrite = "Oct,Nov,Dec-" + formatter.format(date);
					}
// hold for changing from 4th Quarter to Sep-Oct-Nov				    
//				    SimpleDateFormat formatter2 = new SimpleDateFormat("MMM");
//				    cal.add(Calendar.MONTH, -1);
//					towrite = formatter2.format(cal) + " " + formatter2.format(date) + " " + formatter.format(date);
				}
				if (commitmentAccuracy.equals("Year"))
				{
				    formatter = new SimpleDateFormat("yyyy");
					towrite = formatter.format(date);
				}
				if (commitmentAccuracy.equals(" "))
				{
					towrite = " ";
				}
				cell = row.createCell(2);	
				cell.setCellValue(towrite);
				
				indez = t.indexOf("\"Team\":") + 28;
				indezz = t.indexOf("\"Custom_Champion\":") - 19;
				towrite = t.substring(indez,indezz);
				cell = row.createCell(3);	
				cell.setCellValue(towrite);	
				indez = t.indexOf("\"Custom_Champion\":") + 39;
				indezz = t.indexOf("\"Source\":") - 19;
				towrite = t.substring(indez,indezz);
				if (towrite.equals("Custom_Champion:40336")) towrite = "Brian Sullivan";
				if (towrite.equals("Custom_Champion:40337")) towrite = "Marty Wilson";
				if (towrite.equals("Custom_Champion:40338")) towrite = "Richard Blair";
				if (towrite.equals("Custom_Champion:40339")) towrite = "Amy Putnam";
				if (towrite.equals("Custom_Champion:40340")) towrite = "Rob McTier";
				if (towrite.equals("Custom_Champion:40341")) towrite = "Bonnie Felice";
				if (towrite.equals("Custom_Champion:40342")) towrite = "Bradley Frohman";
				if (towrite.equals("Null")) towrite = " ";
				cell = row.createCell(4);	
				cell.setCellValue(towrite);	
				indez = t.indexOf("\"Source\":") + 30;
				indezz = t.indexOf("\"Scope.Name\":") - 19;
				towrite = t.substring(indez,indezz);
				if (towrite.equals("StorySource:150")) towrite = "Technology";
				if (towrite.equals("StorySource:156")) towrite = "Departments";
				if (towrite.equals("StorySource:1614")) towrite = "OPTN";
				if (towrite.equals("StorySource:1615")) towrite = "Community";
				if (towrite.equals("Null")) towrite = " ";
				cell = row.createCell(5);	
				cell.setCellValue(towrite);	
			}
			System.out.println(t);
			System.out.println(index++);
		}
		
		try {
			FileOutputStream output = new FileOutputStream("Test1.xls");
			workbook.write(output);
			output.close();
		} catch(Exception e) {
			e.printStackTrace();
			}
	}

	public static void getsandboxprojects(IServices servit){
		System.out.println("getsandboxprojects");
		// Return Project Initiatives

 		String query = OPENBRACKET + "  \"from\": \"Epic\"," + 
				"  \"select\": [\"Name\",\"Number\",\"PlannedStart\",\"PlannedEnd\",\"Custom_CommitmentAccuracy\",\"Team\",\"Custom_Champion\",\"Source\",\"Scope.Name\",\"Inactive\",\"Status\"]" + "," +
				"  \"where\": " + OPENBRACKET + "\"Inactive\": \"False\"," 
				+ " \"Scope.Name\": \"Sandbox\"" 
						+ CLOSEBRACKET + CLOSEBRACKET;

		System.out.println(query);
		String result = servit.executePassThroughQuery(query);
//		System.out.println(result);
		makesheet(result);
		System.out.println("gotprojects");
		System.out.println(query);
	}
	
	public static void getprojects(IServices servit){
		System.out.println("getprojects");
		// Return Project Initiatives		

 		String query = OPENBRACKET + "  \"from\": \"Epic\"," + 
//				"  \"select\": [\"Name\",\"Number\",\"PlannedEnd\",\"Custom_CommitmentAccuracy\",\"Team\",\"Custom_Champion\",\"Source\",\"Scope.Name\"]" + "," +
				"  \"select\": [\"Name\",\"Number\",\"PlannedStart\",\"PlannedEnd\",\"Custom_CommitmentAccuracy\",\"Team\",\"Custom_Champion\",\"Source\",\"Scope.Name\",\"Inactive\",\"Status\"]" + "," +
				"  \"where\": " + OPENBRACKET + "\"Category\": \"EpicCategory:2171\"," + "\"Inactive\": \"False\"," 
//				+ "\"any\":" + OPENBRACE
//				+ "\"Status\": "  + "\"EpicStatus:23967\""  + "," //Approved
//				+ " OR "
//				+ "\"Status\": "  + "\"EpicStatus:5703\""  + ","  //Idea Concept
//				+ CLOSEBRACE
				//				+ "\"Status\": " + OPENBRACE + "\"EpicStatus:23967\"" + CLOSEBRACE + ","
						+ CLOSEBRACKET + CLOSEBRACKET;

		System.out.println(query);
		String result = servit.executePassThroughQuery(query);
//		System.out.println(result);
		makesheet(result);
		System.out.println("gotprojects");
		System.out.println(query);
	}

	public static void getallprojects(IServices servit){
		System.out.println("getprojects");
		// Return Project Initiatives
		String query = OPENBRACKET + "  \"from\": \"Epic\"," + 
				"  \"select\": [\"Name\",\"Number\",\"PlannedEnd\",\"Custom_CommitmentAccuracy\",\"Team\",\"Custom_Champion\",\"Source\",\"Scope.Name\"]" + "," +
				"  \"where\": " + OPENBRACKET + "\"Category\": \"EpicCategory:2171\","
						+ CLOSEBRACKET + CLOSEBRACKET;
		System.out.println(query);
		String result = servit.executePassThroughQuery(query);
//		System.out.println(result);
		makesheet(result);
		System.out.println("gotprojects");
		System.out.println(query);
	}
	
	public static void getteams(IServices servit){
		System.out.println("getteams");
		// Return Project Initiatives
		String query = OPENBRACKET + "  \"from\": \"Team\"," + 
//				"  \"select\": [\"Name\",\"Number\",\"Team\",\"Scope.Name\"]" + "," +
				"  \"select\": [\"Name\"]" + "," +
				"  \"where\": " + OPENBRACKET
						+ CLOSEBRACKET + CLOSEBRACKET;
		System.out.println(query);
		String result = servit.executePassThroughQuery(query);
		System.out.println(result);
//		makesheet(result);
		System.out.println("gotteams");
		System.out.println(query);
	}	
	
	
	public static void getteams2(IServices servit){
		System.out.println("getteams2");
		// Return filtered teams
		String query = OPENBRACKET + "  \"from\": \"Team\"," + 
//				"  \"select\": [\"Name\",\"Number\",\"Team\",\"Scope.Name\"]" + "," +
				"  \"select\": [\"Name\"]" + "," +
				"  \"filter\": " + OPENBRACKET + "\"Name\": \"Carna\"" 
						+ CLOSEBRACKET + CLOSEBRACKET;
		System.out.println(query);
		String result = servit.executePassThroughQuery(query);
		System.out.println(result);
//		makesheet(result);
		System.out.println("gotteams2");
		System.out.println(query);
	}
	
	public static void getspecificprojects(IServices servit){
		System.out.println("getprojects");
		// Return Project Initiatives in IT Portfolio
		String query = OPENBRACKET + "  \"from\": \"Epic\"," + 
				"  \"select\": [\"Name\",\"Number\",\"Scope.Name\",\"Category\"]" + "," +
				"  \"where\": " + OPENBRACKET + "\"Category\": \"EpicCategory:2171\","
						+ " \"Scope.Name\": \"IT Portfolio\"" + CLOSEBRACKET + CLOSEBRACKET;
		System.out.println(query);
		String result = servit.executePassThroughQuery(query);
		System.out.println(result);
		System.out.println("gotprojects");
		System.out.println(query);
	}

	public static void getepics(IServices servit) {
		// Return Epics from a particular backlog folder
		String query = OPENBRACKET + "  \"from\": \"Epic\"," + 
				"  \"select\": [\"Name\",\"Number\",\"Scope.Name\"]" + "," +
				"  \"where\": " + OPENBRACKET + "\"Scope.Name\": \"IT\"" + CLOSEBRACKET + CLOSEBRACKET;
		System.out.println(query);
		String result = servit.executePassThroughQuery(query);
		System.out.println(result);
		System.out.println();
		System.out.println(query);
	}
	
	public static void getstories(IServices servit) {
		// RETURN ALL STORIES from a particular backlog folder
		String query = "{" + "  \"from\": \"Story\"," + 
				"  \"select\": [\"Name\",\"Number\",\"Scope.Name\"]" + "," +
				"  \"where\": " + OPENBRACKET + "\"Scope.Name\": \"Intake\"" + CLOSEBRACKET + CLOSEBRACKET;
		System.out.println(query);		
		String result = servit.executePassThroughQuery(query);
		System.out.println(result);
		System.out.println(query);
	}
	
	public static void gettasks(IServices servit) throws ConnectionException, APIException, OidException {
		// Return all tasks with 6 hours remaining
		IAssetType taskType = servit.getMeta().getAssetType("Task");
		Query query = new Query(taskType);
		IAttributeDefinition nameAttribute = taskType.getAttributeDefinition("Name");
		IAttributeDefinition todoAttribute = taskType.getAttributeDefinition("ToDo");
		query.getSelection().add(nameAttribute);
		query.getSelection().add(todoAttribute);

		FilterTerm toDoTerm = new FilterTerm(todoAttribute);
		toDoTerm.equal(6);
		query.setFilter(toDoTerm);
		QueryResult result = servit.retrieve(query);

		for (Asset task : result.getAssets()) {
		    System.out.println(task.getOid().getToken());
		    System.out.println(task.getAttribute(nameAttribute).getValue());
		    System.out.println(task.getAttribute(todoAttribute).getValue());
		    System.out.println();
		}	
	}
	
	public static void gettasks2(IServices servit) throws ConnectionException, APIException, OidException {
		// Return all tasks with 6 hours remaining
		
		Workbook workbook = new HSSFWorkbook();
			
		Sheet sheet1 = workbook.createSheet();
		Sheet sheet2 = workbook.createSheet("Pancakes");
		
		IAssetType taskType = servit.getMeta().getAssetType("Task");
		Query query = new Query(taskType);
		IAttributeDefinition nameAttribute = taskType.getAttributeDefinition("Name");
		IAttributeDefinition todoAttribute = taskType.getAttributeDefinition("ToDo");
		query.getSelection().add(nameAttribute);
		query.getSelection().add(todoAttribute);

		FilterTerm toDoTerm = new FilterTerm(todoAttribute);
		toDoTerm.equal(6);
		query.setFilter(toDoTerm);
		QueryResult result = servit.retrieve(query);
		
		Integer index = 0;
		
		for (Asset task : result.getAssets()) {
			Row row = sheet1.createRow(index);
		    System.out.println(task.getOid().getToken());
			Cell cell = row.createCell(0);	
			cell.setCellValue(task.getOid().getToken());
		    System.out.println(task.getAttribute(nameAttribute).getValue());
			cell = row.createCell(1);	
			cell.setCellValue((String) task.getAttribute(nameAttribute).getValue());
			System.out.println(task.getAttribute(todoAttribute).getValue());
			cell = row.createCell(2);	
			cell.setCellValue((Double) task.getAttribute(todoAttribute).getValue());
			System.out.println();
			index++;
		}
		
		Row row = sheet2.createRow(0);
		//create first cell on created row
		Cell cell = row.createCell(0);
			
		try {
			FileOutputStream output = new FileOutputStream("Test1.xls");
			workbook.write(output);
			output.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public static void makestory(IServices servit) throws V1Exception {
		// CREATE A NEW STORY
		Oid projectId = servit.getOid("Scope:0");
		IAssetType storyType = servit.getMeta().getAssetType("Story");

		Asset newStory = servit.createNew(storyType, projectId);
		IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
		newStory.setAttributeValue(nameAttribute, "My Newer Story");
		servit.save(newStory);
		System.out.println(newStory.getOid().getToken());
		System.out.println(newStory.getAttribute(storyType.getAttributeDefinition("Scope")).getValue());
		System.out.println(newStory.getAttribute(nameAttribute).getValue());
	}
	
	public static void main(String[] args) throws MalformedURLException, V1Exception{
		v1Properties props = new v1Properties("M:\\V1Properties");
		ntestapi sal = new ntestapi();
		System.out.println("FirstVersion");
		V1Connector connector = V1Connector
			.withInstanceUrl(props.getURI())
			.withUserAgentHeader("testAPI", "1.0")
			.withAccessToken(props.getToken("TestAPI"))
			.build();

		System.out.println("SecondVersion");
		IServices services = new Services(connector);
		System.out.println("ThirdVersion");
		
//		getsandboxprojects(services);
		getprojects(services);
//		getallprojects(services);
//		getstories(services);
//		getepics(services);
//		gettasks2(services);
//		makestory(services);
//		getteams(services);
//		getteams2(services);
		System.out.println("FinalVersion");
	}

}