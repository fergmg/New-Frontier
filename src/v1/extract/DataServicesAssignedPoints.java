package v1.extract;

//This displays the story points assigned to individuals in the Data Services team

import java.net.MalformedURLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.versionone.apiclient.*;
import com.versionone.apiclient.Asset;
import com.versionone.apiclient.Services;
import com.versionone.apiclient.V1Connector;
import com.versionone.apiclient.exceptions.APIException;
import com.versionone.apiclient.exceptions.ConnectionException;
import com.versionone.apiclient.exceptions.MetaException;
import com.versionone.apiclient.exceptions.OidException;
import com.versionone.apiclient.exceptions.V1Exception;
import com.versionone.apiclient.filters.FilterTerm;
import com.versionone.apiclient.interfaces.IAssetType;
import com.versionone.apiclient.interfaces.IAttributeDefinition;
import com.versionone.apiclient.interfaces.IServices;
import com.versionone.apiclient.services.QueryResult;
import java.awt.event.*;


public class DataServicesAssignedPoints extends JFrame {
	
	static String OPENBRACKET = "{";
	static String CLOSEBRACKET = "}";
	static String OPENBRACE = "[";
	static String CLOSEBRACE = "]";
	Double jamie;
	Double mahesh;
	Double daniel;
	Double mike;
	Double landon;
	Double will;	
	
	public DataServicesAssignedPoints(String sprint) throws MalformedURLException, V1Exception{
		super("Data Services Sprint Points");
		jamie = 0.0;
		mahesh = 0.0;
		daniel = 0.0;
		mike = 0.0;
		landon = 0.0;
		will = 0.0;
		setSize(350, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		V1Connector connector = V1Connector
				.withInstanceUrl("https://www4.v1host.com/UNOS")
				.withUserAgentHeader("DataServices", "1.0")
				.withAccessToken("1.DXo8kY4P6M9tVFXMIuxT6JpU0eM=")
				.build();

		System.out.println("SecondVersion");
		IServices services = new Services(connector);
		System.out.println("ThirdVersion");
		dataServicesPlanning(services,sprint);

		JLabel pageLabel0 = new JLabel(sprint);
		JLabel pageLabel1 = new JLabel("   Jamie: "+Double.toString(jamie),JLabel.RIGHT);
		JLabel pageLabel2 = new JLabel("   Mahesh: "+Double.toString(mahesh),JLabel.RIGHT);
		JLabel pageLabel3 = new JLabel("   Daniel: "+Double.toString(daniel),JLabel.RIGHT);
		JLabel pageLabel4 = new JLabel("   Mike: "+Double.toString(mike),JLabel.RIGHT);
		JLabel pageLabel5 = new JLabel("   Will: "+Double.toString(will),JLabel.RIGHT);
		JLabel pageLabel6 = new JLabel("   Landon: "+Double.toString(landon),JLabel.RIGHT);
		JLabel pageLabel7 = new JLabel("   Total: "+Double.toString(jamie+mike+mahesh+daniel+landon+will),JLabel.RIGHT);
		JPanel pane = new JPanel();
		BoxLayout box = new BoxLayout(pane, BoxLayout.Y_AXIS);
		pane.setLayout(box);
		pane.add(pageLabel0);
		pane.add(pageLabel1);
		pane.add(pageLabel2);
		pane.add(pageLabel3);
		pane.add(pageLabel4);
		pane.add(pageLabel5);
		pane.add(pageLabel6);
		pane.add(pageLabel7);
		add(pane);
		System.out.format("Jamie %.1f%n", jamie);
        System.out.format("Mahesh %.1f%n", mahesh);
        System.out.format("Daniel %.1f%n", daniel);
        System.out.format("Landon %.1f%n", landon);
        System.out.format("Will %.1f%n", will);
        System.out.format("Mike %.1f%n", mike);
        System.out.format("Total %.1f%n", (jamie+mahesh+daniel+landon+will+mike));		
		setVisible(true);
		
	}
	
	public void dataServicesPlanning(IServices services,String sprint) throws ConnectionException, APIException, OidException, MetaException{

		IAssetType storyType = services.getMeta().getAssetType("Story");
		Query query = new Query(storyType);
		IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
		IAttributeDefinition estimateAttribute = storyType.getAttributeDefinition("Estimate");
		IAttributeDefinition sprintAttribute = storyType.getAttributeDefinition("Timebox.Name");
		IAttributeDefinition ownersAttribute = storyType.getAttributeDefinition("Owners.Name");
		query.getSelection().add(nameAttribute);
		query.getSelection().add(estimateAttribute);
		query.getSelection().add(sprintAttribute);
		query.getSelection().add(ownersAttribute);

		FilterTerm toDoTerm = new FilterTerm(sprintAttribute);
		toDoTerm.equal(sprint);
		query.setFilter(toDoTerm);

		QueryResult result = null;
		result = services.retrieve(query);
        
		for (Asset story : result.getAssets()) {
			Double points = 0.0;
			Double staff = 0.0;
			points = (Double) story.getAttribute(estimateAttribute).getValue();
			if (points==null){
				points = 0.0;
			}
			for (Object value : story.getAttribute(ownersAttribute).getValues()) {
				if (value.equals("Mahesh KC")){
					staff = staff + 1;
				}
				if (value.equals("Jamie Sutphin")){
					staff = staff + 1;
				}
				if (value.equals("Daniel Evans")){
					staff = staff + 1;
				}
				if (value.equals("Mike Styers")){
					staff = staff + 1;
				}
				if (value.equals("Will Westing")){
					staff = staff + 1;
				}
				if (value.equals("Landon Wendt")){
					staff = staff + 1;
				}
			}
			points = points / staff;
			for (Object value : story.getAttribute(ownersAttribute).getValues()) {
				if (value.equals("Mahesh KC")){
					mahesh = mahesh + points;
				}
				if (value.equals("Jamie Sutphin")){
					jamie = jamie + points;
				}
				if (value.equals("Daniel Evans")){
					daniel = daniel + points;
				}
				if (value.equals("Mike Styers")){
					mike = mike + points;
				}
				if (value.equals("Will Westing")){
					will = will + points;
				}
				if (value.equals("Landon Wendt")){
					landon = landon + points;
				}
			}
		}
	}
	 
	public static void main(String[] args) throws MalformedURLException, V1Exception{
		System.out.println("FirstVersion");

//		String sprint = "Sprint 24";
		DataServicesAssignedPoints sal = new DataServicesAssignedPoints("Sprint 29");
		DataServicesAssignedPoints sal1 = new DataServicesAssignedPoints("Sprint 28");
		DataServicesAssignedPoints sal2 = new DataServicesAssignedPoints("Sprint 27");
		System.out.println("FinalVersion");
	}

}