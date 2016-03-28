package biz.mckinley;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ZimNoteTest {

	private String getResourcesDir() {
	    return System.getProperty("user.dir") + "/src/test/resources";
	}

	@Test
	public void willThrowInvalidNoteExceptionIfFileDoesNotExist() {
		try {
			new ZimNote(getResourcesDir() + "/non_existent_file.txt");
		} catch (InvalidNoteException exception) {
			assertThat(exception.getMessage(), is("cannot load note"));
		}
	}
	
	@Test
	public void loadingGoodFile() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/Baremetal_Script_Release.txt");
		assertThat(unit.getBodyText(), containsString("Content-Type: text/x-zim-wiki"));
	}
	
	@Test
	public void getExportArgs() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/File_With_Attachments.txt");
		List<String> expected = new ArrayList<String>();
		String attachmentFolder = getResourcesDir() + "/File_With_Attachments/";
		expected.add("--title='File With Attachments'");
		expected.add("--attachment='" + attachmentFolder + "Attachment_1.att'");
		expected.add("--attachment='" + attachmentFolder + "Attachment_2.att'");
		expected.add("--tag='TODO'");
		expected.add("--tag='Tagme'");
		
		assertThat(unit.getExportArgs(), is(equalTo(expected)));
	}
		
//	String[] args1 = {"c:/Python27/python", "../feedvalidator/feedvalidator/src/demo.py" };
//	Runtime r = Runtime.getRuntime();
//	Process p = r.exec(args1);

}
