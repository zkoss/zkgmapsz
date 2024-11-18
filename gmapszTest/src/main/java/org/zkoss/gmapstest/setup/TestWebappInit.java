package org.zkoss.gmapstest.setup;

import org.zkoss.html.JavaScript;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.metainfo.LanguageDefinition;
import org.zkoss.zk.ui.util.WebAppInit;

public class TestWebappInit implements WebAppInit {
	@Override
	public void init(WebApp wapp) throws Exception {
		LanguageDefinition langdef = LanguageDefinition.lookup(null);
		String apiKey = System.getProperty("googleAPIkey");
		if(Strings.isBlank(apiKey)) {
			System.out.println("You must enter a google API key when running this project, using -DgoogleAPIkey=yourKeyHere");
		}
		langdef.addJavaScript(new JavaScript("zk.googleAPIkey='"+apiKey+"';"));
	}
}
