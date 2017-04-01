package v1.cycletime;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.versionone.Oid;
import com.versionone.apiclient.Asset;
import com.versionone.apiclient.Query;
import com.versionone.apiclient.Services;
import com.versionone.apiclient.V1Connector;
import com.versionone.apiclient.exceptions.APIException;
import com.versionone.apiclient.exceptions.ConnectionException;
import com.versionone.apiclient.exceptions.MetaException;
import com.versionone.apiclient.exceptions.OidException;
import com.versionone.apiclient.exceptions.V1Exception;
import com.versionone.apiclient.filters.AndFilterTerm;
import com.versionone.apiclient.filters.FilterTerm;
import com.versionone.apiclient.filters.GroupFilterTerm;
import com.versionone.apiclient.filters.OrFilterTerm;
import com.versionone.apiclient.interfaces.IAssetType;
import com.versionone.apiclient.interfaces.IAttributeDefinition;
import com.versionone.apiclient.interfaces.IServices;
import com.versionone.apiclient.services.QueryResult;
import com.versionone.apiclient.services.OrderBy.Order;

import v1.util.v1Properties;

public class IronWookieeStories extends JFrame {

	public IronWookieeStories() throws V1Exception, MetaException, IOException {
		super("VersionOne for Tableau");

		//Labels to provide feedback to user
		JLabel pageLabel0 = new JLabel("Attempting to connect to VersionOne.");
		JLabel pageLabelConnSuccess = new JLabel("Connected - Querying Database");
		JLabel pageLabelSuccess = new JLabel("Done - VersionOneStories.xls file created.");
		JLabel pageLabelFail = new JLabel("Unknown failure.");
		JLabel pageLabelFailAPI = new JLabel("Failure, unable to use VersionOne API.");
		JLabel pageLabelFailConnect = new JLabel("Failure, unable to connect to VersionOne.");
		JLabel pageLabelFailFile = new JLabel("Failure, unable to create VersionOneStories.xls file.\n Please make sure file is not open.");

		//Create user interface window
		setSize(600, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel pane = new JPanel();
		BoxLayout box = new BoxLayout(pane, BoxLayout.Y_AXIS);
		pane.setLayout(box);
		pane.add(pageLabel0);
		add(pane);
		setVisible(true);

		//Connect to VersionOne - need to get token from properties and login
		System.out.println("VersionOne Stories");
		V1Connector token_connector = null;
		v1Properties props = null;
		
		//Load V1 connection properties
		
		//create and load properties
		props = new v1Properties("M:\\V1Properties");
		
		//Connect using connection class and token
		token_connector = V1Connector.withInstanceUrl(props.getURI())
				.withUserAgentHeader("Iron Wookiee Stories", "1.0")
				.withAccessToken(props.getToken())
				.build();

		IServices services = new Services(token_connector);

		//Determine if fail or pass and display results to user
		int pass = 1;
		try {
//			gethistory(services); //This is where the data-pull from VersionOne occurs
			getdates(services); //This is where the data-pull from VersionOne occurs
		} catch (APIException e) {
			pane.add(pageLabelFailAPI);
			pass = 0;
		} catch (MetaException e) {
			pane.add(pageLabelFailConnect);
			pass = 0;
		} catch (OidException e) {
			pane.add(pageLabelFail);
			pass = 0;
		} catch (ConnectionException e) {
			pane.add(pageLabelFailConnect);
			pass = 0;
		} catch (IOException e) {
			pane.add(pageLabelFailFile);
			pass = 0;
		} 
		
		if (pass == 1) {
			pane.add(pageLabelSuccess);
		}

		setVisible(true);
		System.out.println("Finished");
	}

	public static String cleanString(String towrite){ 
		//Use to clear out formatting text in Title, Description
		String tempstring;
		tempstring = towrite.replaceAll("\\<.*?\\>", "");  //remove <***> formatting
		towrite = tempstring.replace("Â","");
		tempstring = towrite.replace("&nbsp;","");				
		towrite = tempstring.replace("&ndash;","-");
		tempstring = towrite.replace("&ldquo;","\"");				
		towrite = tempstring.replace("&rdquo;","\"");
		tempstring = towrite.replace("&gt;",">");				
		towrite = tempstring.replace("&lt;","<");
		tempstring = towrite.replace("&rsquo;","\'");				
		towrite = tempstring.replace("â€“","–");
		tempstring = towrite.replace("&amp;","&");				
		towrite = tempstring.replace("websitehttp","http");
		tempstring = towrite.replace("â€¢","•");				
		towrite = tempstring.replace("â€‹","");
		tempstring = towrite.replace("â€™","’");				
		towrite = tempstring.replace("â€¦","…");
		tempstring = towrite.replace("&hellip;","…");				
		towrite = tempstring.replace("&middot;","·");
		tempstring = towrite.replace("&mdash;","-");				
		towrite = tempstring.replace("&bull;","\n•");
		tempstring = towrite.replace("ï‚§","\n•");				
		towrite = tempstring.replace("ïƒ˜","\n•");
		tempstring = towrite;				
		return tempstring;
	}
	
	public static void gethistory(IServices servit) throws APIException, MetaException, OidException, ConnectionException, IOException {
		// Return history of an asset
		Cell cell;
		Integer row_index = 0;
		Integer column_index = 0;
		String towrite;
	    Calendar tempCal = Calendar.getInstance();
	    Date day;

	    //Query for the definition of VersionOne Epic (Portfolio Items) attributes
		IAssetType storyType = servit.getMeta().getAssetType("Story");
		Query query = new Query(storyType, true);
		IAttributeDefinition numberAttribute = storyType.getAttributeDefinition("Number");
		query.getSelection().add(numberAttribute);
		IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
		query.getSelection().add(nameAttribute);
		IAttributeDefinition statusnameAttribute = storyType.getAttributeDefinition("Status.Name");
		query.getSelection().add(statusnameAttribute);
		IAttributeDefinition scopeAttribute = storyType.getAttributeDefinition("Scope.ParentMeAndUp");
		query.getSelection().add(scopeAttribute);
		IAttributeDefinition dateAttribute = storyType.getAttributeDefinition("ChangeDate");
		query.getSelection().add(dateAttribute);


		//Create a filter to limit the data pulled in the query (only Project Initiatives or Small Requests in folders IT and below)
		FilterTerm scopeTerm = new FilterTerm(scopeAttribute);
		scopeTerm.equal("Scope:52541"); // Filter project folder "Iron Wookiee" and below
		GroupFilterTerm groupFilter = new AndFilterTerm(scopeTerm);
		query.setFilter(groupFilter);

		QueryResult result = servit.retrieve(query); //Query VersionOne for the data

		//Setup Excel
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet1 = workbook.createSheet("Stories");
	    CellStyle cellStyleDate = workbook.createCellStyle();
	    CellStyle cellStyleNumber = workbook.createCellStyle();
	    cellStyleDate.setDataFormat((short) 0xe); // "m/d/yy" from http://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/BuiltinFormats.html
	    cellStyleNumber.setDataFormat((short) 1); // "0" from http://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/BuiltinFormats.html

		Row topRow = sheet1.createRow(row_index);

		//Parse the items into spreadsheet rows
		for (Asset member : result.getAssets()) {
			System.out.println(member.getAttribute(numberAttribute).getValue());
			row_index = row_index + 1;
			column_index = 0;
			Row row = sheet1.createRow(row_index);

			topRow.createCell(column_index).setCellValue("Display ID");	
		    cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(numberAttribute).getValue());	

			topRow.createCell(column_index).setCellValue("Name");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(nameAttribute).getValue());	

			topRow.createCell(column_index).setCellValue("Status");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(statusnameAttribute).getValue());	
		
			topRow.createCell(column_index).setCellValue("Change Date");	
		    cell = row.createCell(column_index++);
		    day = (Date) member.getAttribute(dateAttribute).getValue();
		    if (day != null) {
				cell.setCellValue(day);
				cell.setCellStyle(cellStyleDate);
		    } else {
				cell.setCellValue("");	    	
		    }

		}

		for (int i=0; i<column_index; i++){
			sheet1.autoSizeColumn(i);
		}

		FileOutputStream output = new FileOutputStream("IronWookieeStories.xls");
		workbook.write(output);
		workbook.close();
		output.close();
	}

	public static void getdates(IServices servit) throws APIException, MetaException, OidException, ConnectionException, IOException {
		// Return date history of an asset
		Cell cell;
		Cell cellID;
		Cell cellName;
		Cell cellApproved;
		Cell cellInProgress;
		Cell cellTest;
		Cell cellDone;
		Integer row_index = 0;
		Integer column_index = 0;
		String towrite;
	    Calendar tempCal = Calendar.getInstance();
	    Date day;

	    //Query for the definition of VersionOne Epic (Portfolio Items) attributes
		IAssetType storyType = servit.getMeta().getAssetType("Story");
		Query query = new Query(storyType, true);
		IAttributeDefinition numberAttribute = storyType.getAttributeDefinition("Number");
		query.getSelection().add(numberAttribute);
		IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
		query.getSelection().add(nameAttribute);
		IAttributeDefinition statusnameAttribute = storyType.getAttributeDefinition("Status.Name");
		query.getSelection().add(statusnameAttribute);
		IAttributeDefinition scopeAttribute = storyType.getAttributeDefinition("Scope.ParentMeAndUp");
		query.getSelection().add(scopeAttribute);
		IAttributeDefinition dateAttribute = storyType.getAttributeDefinition("ChangeDate");
		query.getSelection().add(dateAttribute);
		IAttributeDefinition stateAttribute = storyType.getAttributeDefinition("AssetState");
//		query.getSelection().add(stateAttribute);


		//Create a filter to limit the data pulled in the query (only Project Initiatives or Small Requests in folders IT and below)
		FilterTerm scopeTerm = new FilterTerm(scopeAttribute);
		scopeTerm.equal("Scope:52541"); // Filter project folder "Iron Wookiee" and below
//		FilterTerm stateTerm = new FilterTerm(stateAttribute);
//		stateTerm.notEqual(64); //Filter out deleted stories
//		GroupFilterTerm groupFilter = new AndFilterTerm(scopeTerm, stateTerm);
		GroupFilterTerm groupFilter = new AndFilterTerm(scopeTerm);
		query.setFilter(groupFilter);

		QueryResult result = servit.retrieve(query); //Query VersionOne for the data

		//Setup Excel
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet1 = workbook.createSheet("Stories");
	    CellStyle cellStyleDate = workbook.createCellStyle();
	    CellStyle cellStyleNumber = workbook.createCellStyle();
	    cellStyleDate.setDataFormat((short) 0xe); // "m/d/yy" from http://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/BuiltinFormats.html
	    cellStyleNumber.setDataFormat((short) 1); // "0" from http://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/BuiltinFormats.html

		Row topRow = sheet1.createRow(row_index);
		topRow.createCell(column_index++).setCellValue("Display ID");	
		topRow.createCell(column_index++).setCellValue("Name");	
		topRow.createCell(column_index++).setCellValue("Approved");	
		topRow.createCell(column_index++).setCellValue("In Progress");	
		topRow.createCell(column_index++).setCellValue("Test");	
		topRow.createCell(column_index++).setCellValue("Done");
		topRow.createCell(column_index++).setCellValue("");
		topRow.createCell(column_index++).setCellValue("Cycle Time");
		

		//Parse the items into spreadsheet rows
		cellID = topRow.createCell(column_index++); //dummy cell to initialize for check below
		cellName = cellID; //dummy cell to initialize for check below
		cellApproved = cellID; //dummy cell to initialize for check below
		cellInProgress = cellID; //dummy cell to initialize for check below
		cellTest = cellID; //dummy cell to initialize for check below
		cellDone = cellID; //dummy cell to initialize for check below
		Object oldid = "hi";

		for (Asset member : result.getAssets()) {
			System.out.println(member.getAttribute(numberAttribute).getValue());

			// If first time with this ID, create row
			Object id = member.getAttribute(numberAttribute).getValue();
			if (id.toString().matches(oldid.toString())){
				
			}
			else{
				oldid = id;
		    	row_index = row_index + 1;
				Row row = sheet1.createRow(row_index);
			    cellID = row.createCell(0);
		    	cellID.setCellValue((String) member.getAttribute(numberAttribute).getValue());	
		    	cellName = row.createCell(1);
		    	cellName.setCellValue((String) member.getAttribute(nameAttribute).getValue());	
				cellApproved = row.createCell(2);
				cellApproved.setCellStyle(cellStyleDate);
				cellInProgress = row.createCell(3);
				cellInProgress.setCellStyle(cellStyleDate);
				cellTest = row.createCell(4);
				cellTest.setCellStyle(cellStyleDate);
				cellDone = row.createCell(5);
				cellDone.setCellStyle(cellStyleDate);
				cell = row.createCell(6);
				cell.setCellStyle(cellStyleDate);
			    cell = row.createCell(7);
		    	cell.setCellFormula("IF(F"+(row_index+1)+",F"+(row_index+1)+"-D"+(row_index+1)+")");
		    }

			// Put date in proper status column
			String status = ((String) member.getAttribute(statusnameAttribute).getValue());
			if (status != null){
				if (status.equals("Approved")){
					if (cellApproved.getDateCellValue() == null)
						cellApproved.setCellValue((Date) member.getAttribute(dateAttribute).getValue());
				}
				if (status.equals("In Progress")){
					if (cellInProgress.getDateCellValue() == null)
						cellInProgress.setCellValue((Date) member.getAttribute(dateAttribute).getValue());
				}
				if (status.equals("Test")){
					if (cellTest.getDateCellValue() == null)
						cellTest.setCellValue((Date) member.getAttribute(dateAttribute).getValue());
				}
				if (status.equals("Done")){
					if (cellDone.getDateCellValue() == null)
						cellDone.setCellValue((Date) member.getAttribute(dateAttribute).getValue());
				}
			}
		
		}
		
		//Post process data
		int rowStart = Math.min(15, sheet1.getFirstRowNum());
		rowStart++;
		int rowEnd = Math.max(1400, sheet1.getLastRowNum());
		rowEnd--;
		for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
		       Row r = sheet1.getRow(rowNum);
		       if (r == null) {
		          // This whole row is empty
		          // Handle it as needed
		          continue;
		       }

		       for (int cn = 5; cn > 1; cn--) {
		          Cell c = r.getCell(cn);
		          if (c == null)
		          {
		          }
		          else if (c.getDateCellValue() == null) {
		             Cell next = r.getCell(cn+1);
		             day = next.getDateCellValue();
		             if (day != null){
	            		 c.setCellValue(next.getDateCellValue());
		             }
		          } else {
		             // Do something useful with the cell's contents
		          }
		       }
		    }

		
		
		for (int i=0; i<7; i++){
			sheet1.autoSizeColumn(i);
		}

		FileOutputStream output = new FileOutputStream("IronWookieeStories.xls");
		workbook.write(output);
		workbook.close();
		output.close();
	}
	
	public static void main(String[] args) throws MalformedURLException, V1Exception, MetaException, IOException {
		IronWookieeStories userWindow = new IronWookieeStories();
	}

}
