package biz.mckinley;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ZimNoteTest extends BaseImporterTest {

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
		String attachmentFolder = getResourcesDir() + "/File_With_Attachments/";
		Set<String> expected = new HashSet<>();
		expected.add("--title='File With Attachments'");
		expected.add("--attachment='" + attachmentFolder + "Attachment_1.att'");
		expected.add("--attachment='" + attachmentFolder + "Attachment_2.att'");
		expected.add("--tag='TODO'");
		expected.add("--tag='Tagme'");
		expected.add("--notebook='somewhere'");

		
		assertThat(unit.getExportArgs("somewhere"), is(equalTo(expected)));
	}

}
