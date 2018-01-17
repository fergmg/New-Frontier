package v1.util;

public class DataCleaner {

	public String cleanString(String data){ 
		//Test
		//Use to clear out formatting text in Title, Description
		String tempstring;
		tempstring = data.replaceAll("\\<.*?\\>", "");  //remove <***> formatting
		data = tempstring.replace("�","");
		tempstring = data.replace("&nbsp;","");				
		data = tempstring.replace("&ndash;","-");
		tempstring = data.replace("&ldquo;","\"");				
		data = tempstring.replace("&rdquo;","\"");
		tempstring = data.replace("&gt;",">");				
		data = tempstring.replace("&lt;","<");
		tempstring = data.replace("&rsquo;","\'");				
		data = tempstring.replace("–","�");
		tempstring = data.replace("&amp;","&");				
		data = tempstring.replace("websitehttp","http");
		tempstring = data.replace("•","�");				
		data = tempstring.replace("​","");
		tempstring = data.replace("’","�");				
		data = tempstring.replace("…","�");
		tempstring = data.replace("&hellip;","�");				
		data = tempstring.replace("&middot;","�");
		tempstring = data.replace("&mdash;","-");				
		data = tempstring.replace("&bull;","\n�");
		tempstring = data.replace("","\n�");				
		data = tempstring.replace("","\n�");
		tempstring = data;				
		return tempstring;
	}
}
