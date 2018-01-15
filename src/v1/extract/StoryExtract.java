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

import v1.util.StoryType;
import v1.util.v1Properties;

public class StoryExtract extends JFrame {

	public StoryExtract() throws V1Exception, MetaException, IOException {
		super("VersionOne Stories for Tableau");

		//Labels to provide feedback to user
		JLabel pageLabel0 = new JLabel("Attempting to connect to VersionOne.");
		JLabel pageLabelSuccess = new JLabel("StoryData.xls file created.");
		JLabel pageLabelFail = new JLabel("Unknown failure.");
		JLabel pageLabelFailAPI = new JLabel("Failure, unable to use VersionOne API.");
		JLabel pageLabelFailConnect = new JLabel("Failure, unable to connect to VersionOne.");
		JLabel pageLabelFailFile = new JLabel("Failure, unable to create StoryData.xls file.\n Please make sure file is not open.");

		//Create user interface window
		setSize(600, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel pane = new JPanel();
		BoxLayout box = new BoxLayout(pane, BoxLayout.Y_AXIS);
		pane.setLayout(box);
		pane.add(pageLabel0);
		add(pane);
		setVisible(true);


		System.out.println("VersionOne Stories for Tableau");

		//Connect to VersionOne
		System.out.println("Connecting to VersionOne...");
		v1Properties props = new v1Properties("\\\\mo3fp\\mydocs$\\boardllo\\V1Properties");
		V1Connector token_connector = V1Connector.withInstanceUrl(props.getURI())
				.withUserAgentHeader("VersionOne for Tableau", "0.1")
				.withAccessToken(props.getToken("PortfolioItemExtract"))
				.build();

		System.out.println("Creating Services...");
		IServices services = new Services(token_connector);

		System.out.println("Querying stories...");
		//Determine if fail or pass and display results to user
		int pass = 1;
		try {
			getstories(services); //This is where the data-pull from VersionOne occurs
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

	public static void getstories(IServices servit) throws APIException, MetaException, OidException, ConnectionException, IOException {
		// Return history of an asset
		Cell cell;
		Integer row_index = 0;
		Integer column_index = 0;
		String towrite;
		Calendar tempCal = Calendar.getInstance();
		Date day;

		StoryType storyAsset = new StoryType(servit);
		Query query = new Query(storyAsset.AssetType,true);

		//Query for the definition of VersionOne Epic (Portfolio Items) attributes

		storyAsset.AddAllAttributesToQuerySelection(query);

		FilterTerm scopeTerm = new FilterTerm(storyAsset.ScopeAttribute);
		scopeTerm.equal("Scope:2059");
		GroupFilterTerm groupFilter = new AndFilterTerm(scopeTerm);
		query.setFilter(groupFilter);

		//Limit the results to only the current values, removing this section will allow all versions to be returned
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -(0)); //If desired, this is where to change the requested date to pull
		query.setAsOf(c.getTime());

		//		query.getOrderBy().minorSort(rankAttribute, Order.Ascending); //Sort results by priority order as stored in VersionOne

		QueryResult result = servit.retrieve(query); //Query VersionOne for the data

		//Setup Excel
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet1 = workbook.createSheet("Portfolio Items");
		CellStyle cellStyleDate = workbook.createCellStyle();
		CellStyle cellStyleNumber = workbook.createCellStyle();
		cellStyleDate.setDataFormat((short) 0xe); // "m/d/yy" from http://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/BuiltinFormats.html
		cellStyleNumber.setDataFormat((short) 1); // "0" from http://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/BuiltinFormats.html
		Row topRow = sheet1.createRow(row_index);
		
		String attributeType;
		Integer valuesLength;
		
		for (Asset member : result.getAssets()) {

			row_index = row_index + 1;
			column_index = 0;
			Row row = sheet1.createRow(row_index);

			//for each attribute in storyAsset, print
			for (IAttributeDefinition attribute : query.getSelection()) {

				attributeType = attribute.getAttributeType().toString();
				topRow.createCell(column_index).setCellValue(attribute.getName());
				cell = row.createCell(column_index++);
				
				if(member.getAttribute(attribute).getValues()!=null)
				{
					valuesLength = member.getAttribute(attribute).getValues().length;
					if(valuesLength > 0) {
					switch(attributeType) {
					
					case "Text": 
						cell.setCellValue((String) member.getAttribute(attribute).getValues()[valuesLength - 1]);
						break;
						
					case "Relation": //don't print anything for Relation values
						break;
					
					case "Boolean": 
						cell.setCellValue((boolean) member.getAttribute(attribute).getValue());
						break;
					
					case "Rank": 
						cell.setCellValue(Long.parseLong((String) member.getAttribute(attribute).getValue()));
						break;
					
					case "Date": 
					    day = (Date) member.getAttribute(attribute).getValue();
					    if (day != null) {
							cell.setCellValue(day);
							cell.setCellStyle(cellStyleDate);
					    } else {
							cell.setCellValue("");	    	
					    }
						break;
					
					case "Numeric": 
						Double value = (Double)(member.getAttribute(attribute).getValue());
						if(value != null)
						{ cell.setCellValue(value);}
						else
						{cell.setCellValue(0);}
						break;
						
					default: break;
					}
					}
				}
			}
		}

		FileOutputStream output = new FileOutputStream("StoryData.xls");
		workbook.write(output);
		workbook.close();
		output.close();

	}

	public static String cleanString(String towrite){ 
		//Test
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

	public static void main(String[] args) throws MalformedURLException, V1Exception, MetaException, IOException {
		StoryExtract userWindow = new StoryExtract();

	}

}
