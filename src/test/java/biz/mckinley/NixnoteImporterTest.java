package biz.mckinley;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class NixnoteImporterTest extends BaseImporterTest {

	@Test
	public void importerGathersTextFiles() throws Exception {
		NixnoteImporter unit = new NixnoteImporter(getResourcesDir(), "dontcare", "42");
		Set<String> expected = new HashSet<>();
		expected.add(getResourcesDir() + "/Baremetal_Script_Release.txt");
		expected.add(getResourcesDir() + "/File_With_Attachments.txt");
		expected.add(getResourcesDir() + "/File_With_Attachments_and_Nested_Subfolder/Attachment_5.txt");
		expected.add(getResourcesDir() + "/File_With_Attachments_and_Nested_Subfolder.txt");
		expected.add(getResourcesDir() + "/File_With_Attachments_and_Subfolder/Attachment_5.txt");
		expected.add(getResourcesDir() + "/File_With_Attachments_and_Subfolder.txt");
		expected.add(getResourcesDir() + "/File_With_Attachments_and_SubNote/subnote.txt");
		expected.add(getResourcesDir() + "/File_With_Attachments_and_SubNote.txt");
		expected.add(getResourcesDir() + "/File_With_No_Attachments.txt");
		expected.add(getResourcesDir() + "/File_With_No_Title.txt");

		Set<String> actual = new HashSet<>();
		for (ZimNote note : unit.getNotes()) {
			actual.add(note.getFilename());
		}

		assertThat(expected, is(equalTo(actual)));

	}
	
	@Test
	public void importerNeedsBaseFolder() throws Exception {
		try {
			new NixnoteImporter(null, "dontcare", "42");
			fail("should have thrown runtime exception");
		} catch (Exception exc) {
			assertThat(exc.getMessage(), is(equalTo("You need to specify the Zim notebook base folder")));
		}

	}
	
	@Test
	public void importerNeedsNotebook() throws Exception {
		try {
			new NixnoteImporter(getResourcesDir(), null, "42");
			fail("should have thrown runtime exception");
		} catch (Exception exc) {
			assertThat(exc.getMessage(), is(equalTo("You need to specify the Evernote notebook to import to")));
		}

	}

}
