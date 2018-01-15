package v1.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Attr;

import com.sun.scenario.animation.SplineInterpolator;
import com.versionone.apiclient.Query;
import com.versionone.apiclient.interfaces.IAssetType;
import com.versionone.apiclient.interfaces.IAttributeDefinition;
import com.versionone.apiclient.interfaces.IServices;

public class StoryType {
	
	public IAssetType AssetType;
	public IAttributeDefinition NumberAttribute;
	public IAttributeDefinition ScopeAttribute;
	public IAttributeDefinition ScopeHeiarchyNamesAttribute;
	public IAttributeDefinition ScopeNameAttribute;
	public IAttributeDefinition CategoryNameAttribute;
	public IAttributeDefinition EstimateAttribute;
	public IAttributeDefinition OriginalEstimateAttribute;
	public IAttributeDefinition ReferenceAttribute;
	public IAttributeDefinition SourceNameAttribute;
	public IAttributeDefinition StatusNameAttribute;
	public IAttributeDefinition IsClosedAttribute;
	public IAttributeDefinition SuperNameAttribute;
	public IAttributeDefinition TeamNameAttribute;
	public IAttributeDefinition TimeboxNameAttribute;
	public IAttributeDefinition CustomerNameAttribute; //I think this is Product Owner
	public IAttributeDefinition SplitFromNumberAttribute;
	public IAttributeDefinition SplitToNumberAttribute;
	public IAttributeDefinition IsDeletedAttribute;
	public IAttributeDefinition SuperAndUpNumbersAttribute;
	
	public List<IAttributeDefinition> Attributes;
	
	public StoryType(IServices services) {
		AssetType = services.getMeta().getAssetType("Story");
		Attributes = new ArrayList<IAttributeDefinition>();
		setAttributes(services);
	}
	
	private void setAttributes(IServices services) {

		NumberAttribute = createAttribute("Number");
		Attributes.add(NumberAttribute);
		
		ScopeAttribute = createAttribute("Scope.ParentMeAndUp");
		Attributes.add(ScopeAttribute);
		
		ScopeNameAttribute = createAttribute("Scope.Name");
		Attributes.add(ScopeNameAttribute);
		
		ScopeHeiarchyNamesAttribute = createAttribute("Scope.ParentMeAndUp.Name");
		Attributes.add(ScopeHeiarchyNamesAttribute);
		
		CategoryNameAttribute = createAttribute("Category.Name");
		Attributes.add(CategoryNameAttribute);
		
		EstimateAttribute = createAttribute("Estimate");
		Attributes.add(EstimateAttribute);
		
		ReferenceAttribute = createAttribute("Reference");
		Attributes.add(ReferenceAttribute);
		
		IsClosedAttribute = createAttribute("IsClosed");
		Attributes.add(IsClosedAttribute);
		
		SuperNameAttribute = createAttribute("Super.Number");
		Attributes.add(SuperNameAttribute);
		
		TeamNameAttribute = createAttribute("Team.Name");
		Attributes.add(TeamNameAttribute);
		
		OriginalEstimateAttribute = createAttribute("OriginalEstimate");
		Attributes.add(OriginalEstimateAttribute);
		
		SourceNameAttribute = createAttribute("Source.Name");
		Attributes.add(SourceNameAttribute);
		
		StatusNameAttribute = createAttribute("Status.Name");
		Attributes.add(StatusNameAttribute);
		
		TimeboxNameAttribute = createAttribute("Timebox.Name");
		Attributes.add(TimeboxNameAttribute);
		
		CustomerNameAttribute = createAttribute("Customer.Name");
		Attributes.add(CustomerNameAttribute);
		
		SplitFromNumberAttribute = createAttribute("SplitFrom.Number");
		Attributes.add(SplitFromNumberAttribute);
		
		SplitToNumberAttribute = createAttribute("SplitTo.Number");
		Attributes.add(SplitToNumberAttribute);
		
		IsDeletedAttribute = createAttribute("IsDeleted");
		Attributes.add(IsDeletedAttribute);
		
		SuperAndUpNumbersAttribute = createAttribute("SuperAndUp.Number");
		Attributes.add(SuperAndUpNumbersAttribute);

	}
	
	private IAttributeDefinition createAttribute(String attDefString) {
		return AssetType.getAttributeDefinition(attDefString);
	}
	
	public void AddAllAttributesToQuerySelection(Query query)
	{
		for (IAttributeDefinition attributeDef : Attributes) {
			query.getSelection().add(attributeDef);
		}
	}

}
