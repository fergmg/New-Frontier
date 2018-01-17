package v1.util;

public class DataCleaner {

	public String cleanString(String data){ 
		//Test
		//Use to clear out formatting text in Title, Description
		String tempstring;
		tempstring = data.replaceAll("\\<.*?\\>", "");  //remove <***> formatting
		data = tempstring.replace("Â","");
		tempstring = data.replace("&nbsp;","");				
		data = tempstring.replace("&ndash;","-");
		tempstring = data.replace("&ldquo;","\"");				
		data = tempstring.replace("&rdquo;","\"");
		tempstring = data.replace("&gt;",">");				
		data = tempstring.replace("&lt;","<");
		tempstring = data.replace("&rsquo;","\'");				
		data = tempstring.replace("â€“","–");
		tempstring = data.replace("&amp;","&");				
		data = tempstring.replace("websitehttp","http");
		tempstring = data.replace("â€¢","•");				
		data = tempstring.replace("â€‹","");
		tempstring = data.replace("â€™","’");				
		data = tempstring.replace("â€¦","…");
		tempstring = data.replace("&hellip;","…");				
		data = tempstring.replace("&middot;","·");
		tempstring = data.replace("&mdash;","-");				
		data = tempstring.replace("&bull;","\n•");
		tempstring = data.replace("ï‚§","\n•");				
		data = tempstring.replace("ïƒ˜","\n•");
		tempstring = data;				
		return tempstring;
	}
}
