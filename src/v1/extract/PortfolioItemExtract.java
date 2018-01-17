package v1.extract;

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

import v1.util.DataCleaner;
import v1.util.v1Properties;

@SuppressWarnings("serial")
public class PortfolioItemExtract  extends JFrame {
	
	public static void gethistory(IServices servit) throws APIException, MetaException, OidException, ConnectionException, IOException {
		// Return history of an asset
		Cell cell;
		Integer row_index = 0;
		Integer column_index = 0;
		String towrite;
	    Calendar tempCal = Calendar.getInstance();
	    Date day;
	    
	    DataCleaner cleaner = new DataCleaner();

	    //Query for the definition of VersionOne Epic (Portfolio Items) attributes
		IAssetType epicType = servit.getMeta().getAssetType("Epic");
		Query query = new Query(epicType, true);
		IAttributeDefinition numberAttribute = epicType.getAttributeDefinition("Number");
		query.getSelection().add(numberAttribute);
		IAttributeDefinition typeAttribute = epicType.getAttributeDefinition("Category.Name");
		query.getSelection().add(typeAttribute);
		IAttributeDefinition changeDateAttribute = epicType.getAttributeDefinition("ChangeDate");
		query.getSelection().add(changeDateAttribute);
		IAttributeDefinition nameAttribute = epicType.getAttributeDefinition("Name");
		query.getSelection().add(nameAttribute);
		IAttributeDefinition beginDateAttribute = epicType.getAttributeDefinition("PlannedStart");
		query.getSelection().add(beginDateAttribute);
		IAttributeDefinition endDateAttribute = epicType.getAttributeDefinition("PlannedEnd");
		query.getSelection().add(endDateAttribute);
		IAttributeDefinition commitAttribute = epicType.getAttributeDefinition("Custom_CommitmentAccuracy.Name");
		query.getSelection().add(commitAttribute);
		IAttributeDefinition teamAttribute = epicType.getAttributeDefinition("Custom_ITTeam.Name");
		query.getSelection().add(teamAttribute);
		IAttributeDefinition championAttribute = epicType.getAttributeDefinition("Custom_Champion.Name");
		query.getSelection().add(championAttribute);
		IAttributeDefinition sourceAttribute = epicType.getAttributeDefinition("Source.Name");
		query.getSelection().add(sourceAttribute);
		IAttributeDefinition statusAttribute = epicType.getAttributeDefinition("Status.Name");
		query.getSelection().add(statusAttribute);
		IAttributeDefinition stateAttribute = epicType.getAttributeDefinition("Inactive");
		query.getSelection().add(stateAttribute);
		IAttributeDefinition scopeAttribute = epicType.getAttributeDefinition("Scope.ParentMeAndUp");
		query.getSelection().add(scopeAttribute);
		IAttributeDefinition scopeSimpleAttribute = epicType.getAttributeDefinition("Scope.Name");
		query.getSelection().add(scopeSimpleAttribute);
		IAttributeDefinition descriptionAttribute = epicType.getAttributeDefinition("Description");
		query.getSelection().add(descriptionAttribute);
		IAttributeDefinition referenceAttribute = epicType.getAttributeDefinition("Reference");
		query.getSelection().add(referenceAttribute);
		IAttributeDefinition priorityAttribute = epicType.getAttributeDefinition("Priority.Name");
		query.getSelection().add(priorityAttribute);
		IAttributeDefinition createDateAttribute = epicType.getAttributeDefinition("CreateDate");
		query.getSelection().add(createDateAttribute);
		IAttributeDefinition rankAttribute = epicType.getAttributeDefinition("Order");
		query.getSelection().add(rankAttribute);
		IAttributeDefinition swagAttribute = epicType.getAttributeDefinition("Swag");
		query.getSelection().add(swagAttribute);
		IAttributeDefinition pointsAttribute = epicType.getAttributeDefinition("SubsAndDown:PrimaryWorkitem.Estimate.@Sum");
		query.getSelection().add(pointsAttribute);
		IAttributeDefinition bodAttribute = epicType.getAttributeDefinition("Custom_AnticipatedBODDate2.Name");
		query.getSelection().add(bodAttribute);
		IAttributeDefinition ownerAttribute = epicType.getAttributeDefinition("Owners.Name");
		query.getSelection().add(ownerAttribute);
		IAttributeDefinition productAttribute = epicType.getAttributeDefinition("Custom_Product.Name");
		query.getSelection().add(productAttribute);
		IAttributeDefinition committeeAttribute = epicType.getAttributeDefinition("Custom_SponsoringCommittee.Name");
		query.getSelection().add(committeeAttribute);
		IAttributeDefinition themeAttribute = epicType.getAttributeDefinition("StrategicThemes.Name");
		query.getSelection().add(themeAttribute);
		IAttributeDefinition programAttribute = epicType.getAttributeDefinition("Custom_Program.Name");
		query.getSelection().add(programAttribute);
		IAttributeDefinition closedAttribute = epicType.getAttributeDefinition("ClosedDate");
		query.getSelection().add(closedAttribute);
		IAttributeDefinition approvedDateAttribute = epicType.getAttributeDefinition("Custom_ApprovedDate");
		query.getSelection().add(approvedDateAttribute);
// To add a new data field - step 1 of 2
//		IAttributeDefinition nameYourNewAttribute = epicType.getAttributeDefinition("Custom_ApprovedDate");
//		query.getSelection().add(nameYourNewAttribute);
		
		IAttributeDefinition bodITEstimateAttribute = epicType.getAttributeDefinition("Custom_BODITEstimate.Name");
		query.getSelection().add(bodITEstimateAttribute);
		IAttributeDefinition bodITHoursEstimateAttribute = epicType.getAttributeDefinition("Custom_BODHourEstimate");
		query.getSelection().add(bodITHoursEstimateAttribute);
		IAttributeDefinition chargeCodeAttribute = epicType.getAttributeDefinition("Custom_ChargeCode");
		query.getSelection().add(chargeCodeAttribute);
		IAttributeDefinition impactsDBAttribute = epicType.getAttributeDefinition("Custom_ImpactsDB.Name");
		query.getSelection().add(impactsDBAttribute);
		IAttributeDefinition impactsUIAttribute = epicType.getAttributeDefinition("Custom_ImpactsUI.Name");
		query.getSelection().add(impactsUIAttribute);
		IAttributeDefinition impactsOMBDataAttribute = epicType.getAttributeDefinition("Custom_ImpactsOMBDataCollection.Name");
		query.getSelection().add(impactsOMBDataAttribute);
		IAttributeDefinition impactsVendorsAttribute = epicType.getAttributeDefinition("Custom_ImpactsVendors.Name");
		query.getSelection().add(impactsVendorsAttribute);
		IAttributeDefinition pocITEstimateAttribute = epicType.getAttributeDefinition("Custom_POCITEstimate.Name");
		query.getSelection().add(pocITEstimateAttribute);
		IAttributeDefinition brdStatusAttribute = epicType.getAttributeDefinition("Custom_BRDStatus.Name");
		query.getSelection().add(brdStatusAttribute);
		IAttributeDefinition slaStartDateAttribute = epicType.getAttributeDefinition("Custom_SLAStartDate");
		query.getSelection().add(slaStartDateAttribute);
		IAttributeDefinition slaWaiverAttribute = epicType.getAttributeDefinition("Custom_SLAWaiver");
		query.getSelection().add(slaWaiverAttribute);
		IAttributeDefinition proposalIDAttribute = epicType.getAttributeDefinition("Custom_ProposalID");
		query.getSelection().add(proposalIDAttribute);
		IAttributeDefinition additionalEstimateAttribute = epicType.getAttributeDefinition("Custom_AdditionalEstimate");
		query.getSelection().add(additionalEstimateAttribute);
		IAttributeDefinition scrumMasterAttribute = epicType.getAttributeDefinition("Custom_ScrumMaster.Name");
		query.getSelection().add(scrumMasterAttribute);
		
		//Create a filter to limit the data pulled in the query (only Project Initiatives or Small Requests in folders IT and below)
		FilterTerm scopeTerm = new FilterTerm(scopeAttribute);
		scopeTerm.equal("Scope:2059"); // Filter project folder "IT" and below
		FilterTerm type1Term = new FilterTerm(typeAttribute);
		type1Term.notEqual("Increment");
		FilterTerm type2Term = new FilterTerm(typeAttribute);
		type2Term.notEqual("Epic");
		FilterTerm type3Term = new FilterTerm(typeAttribute);
		type3Term.notEqual("BetaPortal");
		FilterTerm type4Term = new FilterTerm(typeAttribute);
		type4Term.notEqual("DevMain");
		FilterTerm type5Term = new FilterTerm(typeAttribute);
		type5Term.notEqual("QA");
		FilterTerm type6Term = new FilterTerm(typeAttribute);
		type6Term.notEqual("Sub-Feature");
		FilterTerm type7Term = new FilterTerm(typeAttribute);
		type7Term.notEqual("TIP");
		FilterTerm type8Term = new FilterTerm(typeAttribute);
		type8Term.notEqual("");
		GroupFilterTerm groupFilter = new AndFilterTerm(scopeTerm, type1Term, type2Term, type3Term, type4Term, type5Term, type6Term, type7Term, type8Term);
		query.setFilter(groupFilter);

		//Limit the results to only the current values, removing this section will allow all versions to be returned
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -(0)); //If desired, this is where to change the requested date to pull
		query.setAsOf(c.getTime());
		
		query.getOrderBy().minorSort(rankAttribute, Order.Ascending); //Sort results by priority order as stored in VersionOne

		QueryResult result = servit.retrieve(query); //Query VersionOne for the data
//		System.out.println(query.toString());
//		System.out.println(query);
//		System.out.println(result.toString());

		//Setup Excel
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet1 = workbook.createSheet("Portfolio Items");
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

			//Display ID - numberAttribute
			topRow.createCell(column_index).setCellValue("Display ID");	
		    cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(numberAttribute).getValue());	
			towrite = (String) member.getAttribute(nameAttribute).getValue();	

			//Name - nameAttribute
			topRow.createCell(column_index).setCellValue("Name");	
		    cell = row.createCell(column_index++);
			towrite = (String) member.getAttribute(nameAttribute).getValue();
			String tempstring = towrite;
			if (towrite != null) {
				tempstring = cleaner.cleanString(towrite);
			}
			
			//Planned Begin - beginDateAttribute
			topRow.createCell(column_index).setCellValue("Planned Begin");	
			cell.setCellValue(tempstring);	
		    cell = row.createCell(column_index++);
		    day = (Date) member.getAttribute(beginDateAttribute).getValue();
		    if (day != null) {
				cell.setCellValue(day);
				cell.setCellStyle(cellStyleDate);
		    } else {
				cell.setCellValue("");	    	
		    }

		    //Planned End - endDateAttribute
			topRow.createCell(column_index).setCellValue("Planned End");	
		    cell = row.createCell(column_index++);
		    day = (Date) member.getAttribute(endDateAttribute).getValue();
		    if (day != null) {
		    	tempCal.setTime(day);
		    	tempCal.add(Calendar.DATE, -1);   //VersionOne stores Planned End as the day after what is shows in the UI
				cell.setCellValue(tempCal);
				cell.setCellStyle(cellStyleDate);
		    } else {
				cell.setCellValue("");	    	
		    }

		    //Commitment Accuracy - commitAttribute
			topRow.createCell(column_index).setCellValue("Commitment Accuracy");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(commitAttribute).getValue());	

			//Champion - championAttribute
			topRow.createCell(column_index).setCellValue("Champion");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(championAttribute).getValue());	

			//Team - teamAttribute
			topRow.createCell(column_index).setCellValue("Team");	
			cell = row.createCell(column_index++);
			towrite = "";
		    for (Object value : member.getAttribute(teamAttribute).getValues()) {
				towrite = towrite + (String) value + ", ";
		    }
		    if (towrite.length() > 0){
		    	towrite = towrite.substring(0, towrite.length()-2);
		    }
			cell.setCellValue(towrite);	

			//Source - sourceAttribute
			topRow.createCell(column_index).setCellValue("Source");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(sourceAttribute).getValue());	

			//Status - statusAttribute
			topRow.createCell(column_index).setCellValue("Status");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(statusAttribute).getValue());	

			//StateActive - stateAttribute
			topRow.createCell(column_index).setCellValue("StateActive");	
			cell = row.createCell(column_index++);
			cell.setCellValue(!(boolean) member.getAttribute(stateAttribute).getValue());	// True is closed, so flip to True is open

			//Type - typeAttribute
			topRow.createCell(column_index).setCellValue("Type");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(typeAttribute).getValue());	

			//Description - descriptionAttribute
			topRow.createCell(column_index).setCellValue("Description");	
			cell = row.createCell(column_index++);
			towrite = (String) member.getAttribute(descriptionAttribute).getValue();
			tempstring = "";
			if (towrite != null) {
				tempstring = cleaner.cleanString(towrite);
			}
			cell.setCellValue(tempstring);	

			//Reference - referenceAttribute
			topRow.createCell(column_index).setCellValue("Reference");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(referenceAttribute).getValue());	

			//Priority - priorityAttribute
			topRow.createCell(column_index).setCellValue("Priority");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(priorityAttribute).getValue());	

			//Create Date - createDateAttribute
			topRow.createCell(column_index).setCellValue("Create Date");	
			cell = row.createCell(column_index++);
		    day = (Date) member.getAttribute(createDateAttribute).getValue();
		    if (day != null) {
				cell.setCellValue(day);
				cell.setCellStyle(cellStyleDate);
		    } else {
				cell.setCellValue("");	    	
		    }

		    //Order - rankAttribute
			topRow.createCell(column_index).setCellValue("Order");	
			cell = row.createCell(column_index++);
			cell.setCellValue(Long.parseLong((String) member.getAttribute(rankAttribute).getValue()));
			cell.setCellStyle(cellStyleNumber);

			//ID - getToken()
			topRow.createCell(column_index).setCellValue("ID");	
			cell = row.createCell(column_index++);
			towrite = member.getOid().getToken();
			tempstring = towrite.replaceAll("Epic:", "");  //remove Epic:
			cell.setCellValue(tempstring);

			//Swag - swagAttribute
			topRow.createCell(column_index).setCellValue("Swag");	
			cell = row.createCell(column_index++);
			Double swag = 0.0; 
			swag = (Double) (member.getAttribute(swagAttribute).getValue());
			if (swag != null)
				cell.setCellValue(swag);
			else
				cell.setCellValue(0);
			cell.setCellStyle(cellStyleNumber);

			//Points - pointsAttribute
			topRow.createCell(column_index).setCellValue("Points");	
			cell = row.createCell(column_index++);
			Double points = 0.0; 
			points = (Double) (member.getAttribute(pointsAttribute).getValue());
			if (points != null)
				cell.setCellValue(points);
			else
				cell.setCellValue(0);
			cell.setCellStyle(cellStyleNumber);

			//BOD - bodAttribute
			topRow.createCell(column_index).setCellValue("BOD");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(bodAttribute).getValue());	

			//Owner - ownerAttribute
			topRow.createCell(column_index).setCellValue("Owner");	
			cell = row.createCell(column_index++);
			towrite = "";
		    for (Object value : member.getAttribute(ownerAttribute).getValues()) {
				towrite = towrite + (String) value + ", ";
		    }
		    if (towrite.length() > 0){
		    	towrite = towrite.substring(0, towrite.length()-2);
		    }
			cell.setCellValue(towrite);	

			//Product - productAttribute
			topRow.createCell(column_index).setCellValue("Product");	
			cell = row.createCell(column_index++);
			towrite = "";
		    for (Object value : member.getAttribute(productAttribute).getValues()) {
				towrite = towrite + (String) value + ", ";
		    }
		    if (towrite.length() > 0){
		    	towrite = towrite.substring(0, towrite.length()-2);
		    }
			cell.setCellValue(towrite);	

			//Committee - committeeAttribute
			topRow.createCell(column_index).setCellValue("Committee");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(committeeAttribute).getValue());	

			//Theme - themeAttribute
			topRow.createCell(column_index).setCellValue("Theme");	
			cell = row.createCell(column_index++);
			towrite = "";
		    for (Object value : member.getAttribute(themeAttribute).getValues()) {
				towrite = towrite + (String) value + ", ";
		    }
		    if (towrite.length() > 0){
		    	towrite = towrite.substring(0, towrite.length()-2);
		    }
			cell.setCellValue(towrite);	

			//Program - programAttribute
			topRow.createCell(column_index).setCellValue("Program");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(programAttribute).getValue());	

			//Folder - scopeSimpleAttribute
			topRow.createCell(column_index).setCellValue("Folder");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(scopeSimpleAttribute).getValue());

			//Closed Date - closedAttribute
			topRow.createCell(column_index).setCellValue("Closed Date");	
			cell = row.createCell(column_index++);
		    day = (Date) member.getAttribute(closedAttribute).getValue();
		    if (day != null) {
				cell.setCellValue(day);
				cell.setCellStyle(cellStyleDate);
		    } else {
				cell.setCellValue("");	    	
		    }

		    //Approved Date - approvedDateAttribute
			topRow.createCell(column_index).setCellValue("Approved Date");	
			cell = row.createCell(column_index++);
		    day = (Date) member.getAttribute(approvedDateAttribute).getValue();
		    if (day != null) {
				cell.setCellValue(day);
				cell.setCellStyle(cellStyleDate);
		    } else {
				cell.setCellValue("");	    	
		    }

// To add a new data field - step 2 of 2
//			topRow.createCell(column_index).setCellValue("NameTheColumnInExcel");	
//			cell = row.createCell(column_index++);
//			cell.setCellValue((String) member.getAttribute(nameYourNewAttribute).getValue());
		    
     		topRow.createCell(column_index).setCellValue("BOD IT Estimate");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(bodITEstimateAttribute).getValue());
			
			topRow.createCell(column_index).setCellValue("BOD IT Hours Estimate");	
			cell = row.createCell(column_index++);
			Double itHoursEstimate = (Double) member.getAttribute(bodITHoursEstimateAttribute).getValue();
			if (itHoursEstimate != null)
				cell.setCellValue(itHoursEstimate);
			else
				cell.setCellValue(0);
			cell.setCellStyle(cellStyleNumber);

			topRow.createCell(column_index).setCellValue("Charge Code");	
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(chargeCodeAttribute).getValue());
			
			
			//New
			topRow.createCell(column_index).setCellValue("Impacts DB");
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(impactsDBAttribute).getValue());
			
			topRow.createCell(column_index).setCellValue("Impacts UI");
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(impactsUIAttribute).getValue());
			
			topRow.createCell(column_index).setCellValue("Impacts OMB Data Collection");
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(impactsOMBDataAttribute).getValue());
			
			topRow.createCell(column_index).setCellValue("Impacts Vendors");
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(impactsVendorsAttribute).getValue());
			
			
		    //SLA Start Date - slaStartDateAttribute
			topRow.createCell(column_index).setCellValue("SLA Start Date");	
			cell = row.createCell(column_index++);
		    day = (Date) member.getAttribute(slaStartDateAttribute).getValue();
		    if (day != null) {
				cell.setCellValue(day);
				cell.setCellStyle(cellStyleDate);
		    } else {
				cell.setCellValue("");	    	
		    }
		    
		    //SLA Waiver - slaWaiverAttribute
			topRow.createCell(column_index).setCellValue("SLA Waiver");
			cell = row.createCell(column_index++);
			Object waiverObject = member.getAttribute(slaWaiverAttribute).getValue();
			if (waiverObject == null) {
				cell.setCellValue("False");
			}
			else{
				cell.setCellValue("True");
			}
			
			//Additional Estimate - addtionalEstimateAttribute
			topRow.createCell(column_index).setCellValue("Additional Estimate");
			cell = row.createCell(column_index++);
			if(member.getAttribute(additionalEstimateAttribute).getValue() != null)
			{
				cell.setCellValue((String) member.getAttribute(additionalEstimateAttribute).getValue().toString());
			}
			
			//Proposal ID - proposalIDAttribute
			topRow.createCell(column_index).setCellValue("Proposal ID");
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(proposalIDAttribute).getValue());

			
			//Scrum Master - scrumMasterAttribute
			topRow.createCell(column_index).setCellValue("Scrum Master");	
			cell = row.createCell(column_index++);
			towrite = "";
		    for (Object value : member.getAttribute(scrumMasterAttribute).getValues()) {
				towrite = towrite + (String) value + ", ";
		    }
		    if (towrite.length() > 0){
		    	towrite = towrite.substring(0, towrite.length()-2);
		    }
			cell.setCellValue(towrite);	
			
			//BRD Status - brdStatusAttribute
			topRow.createCell(column_index).setCellValue("BRD Status");
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(brdStatusAttribute).getValue());
			
			//POC IT Estimate - pocITEstimateAttribute
			topRow.createCell(column_index).setCellValue("POC IT Estimate");
			cell = row.createCell(column_index++);
			cell.setCellValue((String) member.getAttribute(pocITEstimateAttribute).getValue());

		}
	
// This section will resize the width of columns for easier human reading
//
//		for (int i=0; i<column_index; i++){
//			sheet1.autoSizeColumn(i);
//		}

		FileOutputStream output = new FileOutputStream("VersionOneData.xls");
		workbook.write(output);
		workbook.close();
		output.close();
	}
		 
	public PortfolioItemExtract() throws V1Exception, MetaException, IOException {
		super("VersionOne for Tableau");

		//Labels to provide feedback to user
		JLabel pageLabel0 = new JLabel("Attempting to connect to VersionOne.");
		JLabel pageLabelSuccess = new JLabel("VersionOneData.xls file created.");
		JLabel pageLabelFail = new JLabel("Unknown failure.");
		JLabel pageLabelFailAPI = new JLabel("Failure, unable to use VersionOne API.");
		JLabel pageLabelFailConnect = new JLabel("Failure, unable to connect to VersionOne.");
		JLabel pageLabelFailFile = new JLabel("Failure, unable to create VersionOneData.xls file.\n Please make sure file is not open.");

		//Create user interface window
		setSize(600, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel pane = new JPanel();
		BoxLayout box = new BoxLayout(pane, BoxLayout.Y_AXIS);
		pane.setLayout(box);
		pane.add(pageLabel0);
		add(pane);
		setVisible(true);

		//Connect to VersionOne
		System.out.println("VersionOne for Tableau");
		V1Connector token_connector = null;
		v1Properties props = null;
		
		//Load V1 connection properties
		//props = new v1Properties("M:\\V1Properties");
		props = new v1Properties("\\\\mo3fp\\mydocs$\\boardllo\\V1Properties");
		//Connect using connection class and token
		token_connector = V1Connector.withInstanceUrl(props.getURI())
				.withUserAgentHeader("VersionOne for Tableau", "0.1")
				.withAccessToken(props.getToken("PortfolioItemExtract"))
				.build();
		
		//1.ew88DA8d3ExmKr1cEuLbsyFsNRs=
		
		IServices services = new Services(token_connector);

		//Determine if fail or pass and display results to user
		int pass = 1;
		try {
			gethistory(services); //This is where the data-pull from VersionOne occurs
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

	public static void main(String[] args) throws MalformedURLException, V1Exception, MetaException, IOException {
		@SuppressWarnings("unused")
		PortfolioItemExtract userWindow = new PortfolioItemExtract();
	}

}