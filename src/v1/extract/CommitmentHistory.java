package v1.extract;

//This creates a "Commitment Status" spreadsheet as of any date desired. Line 95 is where you change the as-of date.

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
import com.versionone.apiclient.interfaces.IAssetType;
import com.versionone.apiclient.interfaces.IAttributeDefinition;
import com.versionone.apiclient.interfaces.IServices;
import com.versionone.apiclient.services.QueryResult;
import com.versionone.apiclient.services.OrderBy.Order;

import v1.util.v1Properties;

public class CommitmentHistory  extends JFrame {
	
	public static void gethistory(IServices servit) throws APIException, MetaException, OidException, ConnectionException, IOException {
		// Return history of an asset
		Cell cell;
		Integer index = 0;
		String towrite;
	    Calendar tempCal = Calendar.getInstance();
	    Date day;

		IAssetType epicType = servit.getMeta().getAssetType("Epic");
		Query query = new Query(epicType, true);
		IAttributeDefinition idAttribute = epicType.getAttributeDefinition("Number");
		IAttributeDefinition typeAttribute = epicType.getAttributeDefinition("Category.Name");
		IAttributeDefinition changeDateAttribute = epicType.getAttributeDefinition("ChangeDate");
		IAttributeDefinition nameAttribute = epicType.getAttributeDefinition("Name");
		IAttributeDefinition beginDateAttribute = epicType.getAttributeDefinition("PlannedStart");
		IAttributeDefinition endDateAttribute = epicType.getAttributeDefinition("PlannedEnd");
		IAttributeDefinition commitAttribute = epicType.getAttributeDefinition("Custom_CommitmentAccuracy.Name");
		IAttributeDefinition teamAttribute = epicType.getAttributeDefinition("Custom_ITTeam.Name");
		IAttributeDefinition championAttribute = epicType.getAttributeDefinition("Custom_Champion.Name");
		IAttributeDefinition sourceAttribute = epicType.getAttributeDefinition("Source.Name");
		IAttributeDefinition statusAttribute = epicType.getAttributeDefinition("Status.Name");
		IAttributeDefinition stateAttribute = epicType.getAttributeDefinition("Inactive");
		query.getSelection().add(idAttribute);
		query.getSelection().add(changeDateAttribute);
		query.getSelection().add(typeAttribute);
		query.getSelection().add(nameAttribute);
		query.getSelection().add(beginDateAttribute);
		query.getSelection().add(endDateAttribute);
		query.getSelection().add(commitAttribute);
		query.getSelection().add(championAttribute);
		query.getSelection().add(teamAttribute);
		query.getSelection().add(sourceAttribute);
		query.getSelection().add(statusAttribute);
		query.getSelection().add(stateAttribute);
		FilterTerm idTerm = new FilterTerm(typeAttribute);
		idTerm.equal("Project Initiative");
		FilterTerm statusTerm = new FilterTerm(statusAttribute);
		statusTerm.notEqual("PROD");
		FilterTerm beginTerm = new FilterTerm(beginDateAttribute);
		beginTerm.exists();
		FilterTerm endTerm = new FilterTerm(endDateAttribute);
		endTerm.exists();
		FilterTerm commitTerm = new FilterTerm(commitAttribute);
		commitTerm.exists();
		FilterTerm sourceTerm = new FilterTerm(sourceAttribute);
		sourceTerm.exists();
		FilterTerm stateTerm = new FilterTerm(stateAttribute);
		stateTerm.notEqual(1);  // filter closed items
		GroupFilterTerm groupFilter = new AndFilterTerm(idTerm, statusTerm, beginTerm, endTerm, commitTerm, sourceTerm, stateTerm);
		query.setFilter(groupFilter);

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -(0)); //This is where to set the requested date to pull
		query.setAsOf(c.getTime());
		
		query.getOrderBy().minorSort(endDateAttribute, Order.Ascending);
		

		QueryResult result = servit.retrieve(query);
		
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet1 = workbook.createSheet("Project Initiatives");
	    CellStyle cellStyleDate = workbook.createCellStyle();
	    CellStyle cellStyleNumber = workbook.createCellStyle();
	    cellStyleDate.setDataFormat((short) 0xe); // "m/d/yy" from http://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/BuiltinFormats.html
	    cellStyleNumber.setDataFormat((short) 1); // "0" from http://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/BuiltinFormats.html

		Row row = sheet1.createRow(index);
		cell = row.createCell(0);	
		cell.setCellValue(c.getTime());	
		cell.setCellStyle(cellStyleDate);

		index = index + 1;
		row = sheet1.createRow(index);
		cell = row.createCell(0);	
		cell.setCellValue("Display ID");	
		cell = row.createCell(1);	
		cell.setCellValue("Name");	
		cell = row.createCell(2);	
		cell.setCellValue("Planned Begin");	
		cell = row.createCell(3);	
		cell.setCellValue("Planned End");	
		cell = row.createCell(4);	
		cell.setCellValue("Commitment Accuracy");	
		cell = row.createCell(5);	
		cell.setCellValue("Champion");	
		cell = row.createCell(6);	
		cell.setCellValue("Team");	
		cell = row.createCell(7);	
		cell.setCellValue("Source");	
		cell = row.createCell(8);	
		cell.setCellValue("Status");	

		for (Asset member : result.getAssets()) {
			index = index + 1;
			row = sheet1.createRow(index);
			
		    System.out.println(member.getAttribute(idAttribute).getValue());
		    cell = row.createCell(0);
			cell.setCellValue((String) member.getAttribute(idAttribute).getValue());	
			towrite = (String) member.getAttribute(nameAttribute).getValue();	
			cell = row.createCell(1);	
			towrite = (String) member.getAttribute(nameAttribute).getValue();
			String tempstring = towrite;
			if (towrite != null) {
				tempstring = towrite.replaceAll("\\<.*?\\>", "");  //remove <***> formatting
				towrite = tempstring.replaceAll("Â","");
				tempstring = towrite.replace("&nbsp;","");				
				towrite = tempstring.replaceAll("&ndash;","-");
				tempstring = towrite.replace("&ldquo;","\"");				
				towrite = tempstring.replaceAll("&rdquo;","\"");
				tempstring = towrite.replace("&gt;",">");				
				towrite = tempstring.replaceAll("&lt;","<");
				tempstring = towrite.replace("&rsquo;","\'");				
				towrite = tempstring.replaceAll("â€“","–");
				tempstring = towrite.replace("&amp;","&");				
				towrite = tempstring.replaceAll("&middot;","·");
				tempstring = towrite.replace("&amp;","&");				
				towrite = tempstring.replaceAll("websitehttp","http");
				tempstring = towrite.replace("â€¢","•");				
				towrite = tempstring.replaceAll("â€‹","");
				tempstring = towrite.replace("â€™","’");				
				towrite = tempstring.replaceAll("â€¦","…");
				tempstring = towrite.replace("","");				
				//future expansion				towrite = tempstring.replaceAll("","");
				//future expansion				tempstring = towrite.replace("","");				
			}
			cell.setCellValue(tempstring);	
			cell = row.createCell(2);	
		    day = (Date) member.getAttribute(beginDateAttribute).getValue();
		    if (day != null) {
				cell.setCellValue(day);
				cell.setCellStyle(cellStyleDate);
		    } else {
				cell.setCellValue("");	    	
		    }
			cell = row.createCell(3);	
		    day = (Date) member.getAttribute(endDateAttribute).getValue();
		    if (day != null) {
		    	tempCal.setTime(day);
		    	tempCal.add(Calendar.DATE, -1);
				cell.setCellValue(tempCal);
				cell.setCellStyle(cellStyleDate);
		    } else {
				cell.setCellValue("");	    	
		    }
			cell = row.createCell(4);	
			cell.setCellValue((String) member.getAttribute(commitAttribute).getValue());	
			cell = row.createCell(5);	
			cell.setCellValue((String) member.getAttribute(championAttribute).getValue());	
			cell = row.createCell(6);	
			towrite = "";
		    for (Object value : member.getAttribute(teamAttribute).getValues()) {
				towrite = towrite + (String) value + " ";
		    }
			cell.setCellValue(towrite);	
			cell = row.createCell(7);	
			cell.setCellValue((String) member.getAttribute(sourceAttribute).getValue());	
			cell = row.createCell(8);	
			cell.setCellValue((String) member.getAttribute(statusAttribute).getValue());	
		}
		
		for (int i=0; i<13; i++){
			sheet1.autoSizeColumn(i);
		}

		FileOutputStream output = new FileOutputStream("Commitment Status.xls");
		workbook.write(output);
		workbook.close();
		output.close();
	}
		 
	public CommitmentHistory() throws V1Exception, MetaException, IOException {
		super("Commitment Status");
		setSize(600, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		JLabel pageLabel0 = new JLabel("Attempting to connect to VersionOne.");
		JLabel pageLabelSuccess = new JLabel("Commitment Status.xls file created.");
		JLabel pageLabelFail = new JLabel("Unknown failure.");
		JLabel pageLabelFailAPI = new JLabel("Failure, unable to connect to VersionOne.");
		JLabel pageLabelFailFile = new JLabel(
				"Failure, unable to create Commitment Status.xls file.\n Please make sure file is not open.");
		JPanel pane = new JPanel();
		BoxLayout box = new BoxLayout(pane, BoxLayout.Y_AXIS);
		pane.setLayout(box);
		pane.add(pageLabel0);
		add(pane);
		setVisible(true);

		System.out.println("Commitment Status");
		V1Connector token_connector = null;
		v1Properties props = null;
		
		//Load V1 connection properties
		props = new v1Properties("M:\\V1Properties");
		
		//Connect using connection class and token
		token_connector = V1Connector.withInstanceUrl(props.getURI())
				.withUserAgentHeader("Commitment Status", "1.0")
				.withAccessToken(props.getToken("CommitmentHistory"))
				.build();

		IServices services = new Services(token_connector);

		int pass = 1;
		try {
			gethistory(services);
		} catch (APIException e) {
			pane.add(pageLabelFailAPI);
			pass = 0;
		} catch (MetaException e) {
			pane.add(pageLabelFail);
			pass = 0;
		} catch (OidException e) {
			pane.add(pageLabelFail);
			pass = 0;
		} catch (ConnectionException e) {
			pane.add(pageLabelFail);
			pass = 0;
		} catch (IOException e) {
			pane.add(pageLabelFailFile);
			pass = 0;
		}

		if (pass == 1) {
			pane.add(pageLabelSuccess);
			add(pane);
		}

		setVisible(true);
		System.out.println("Finished");
	}

	public static void main(String[] args) throws MalformedURLException, V1Exception, MetaException, IOException {
		CommitmentHistory sal = new CommitmentHistory();
	}

}