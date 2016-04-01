package biz.mckinley;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ZimAttachmentsTest {

	private String getResourcesDir() {
	    return System.getProperty("user.dir") + "/src/test/resources";
	}

	@Test
	public void withNoExistingAttachments() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/File_With_No_Attachments.txt");
		Set<String> expected = new HashSet<String>();

		assertThat(unit.getAttachmentFilenames(), is(equalTo(expected)));
	}

	@Test
	public void findsAllFilesInFolderOfSameNameAsNote() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/File_With_Attachments.txt");
		Set<String> expected = new HashSet<String>();
		expected.add(getResourcesDir() + "/File_With_Attachments/Attachment_1.att");
		expected.add(getResourcesDir() + "/File_With_Attachments/Attachment_2.att");
		
		assertThat(unit.getAttachmentFilenames(), is(equalTo(expected)));
	}
	
	@Test
	public void findsAllNonNoteFilesInSubFolderOfAttachmentFolder() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/File_With_Attachments_and_Subfolder.txt");
		Set<String> expected = new HashSet<String>();
		expected.add(getResourcesDir() + "/File_With_Attachments_and_Subfolder/Attachment_6.att");
		expected.add(getResourcesDir() + "/File_With_Attachments_and_Subfolder/subfolder/Attachment_7.att");
		
		assertThat(unit.getAttachmentFilenames(), is(equalTo(expected)));
	}
	
	@Test
	public void excludesFilesInSubFolderWhereTheyAreAttachmentsOfASubNote() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/File_With_Attachments_and_SubNote.txt");
		Set<String> expected = new HashSet<String>();
		expected.add(getResourcesDir() + "/File_With_Attachments_and_SubNote/Attachment_3.att");
		expected.add(getResourcesDir() + "/File_With_Attachments_and_SubNote/not_subnote/Attachment_8.att");

		
		assertThat(unit.getAttachmentFilenames(), is(equalTo(expected)));
	}
	
	@Test
	public void willRecurseASingleLevelInAttachmentFolderIfNotASubnoteFolder() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/File_With_Attachments_and_Nested_Subfolder.txt");
		Set<String> expected = new HashSet<String>();
		expected.add(getResourcesDir() + "/File_With_Attachments_and_Nested_Subfolder/Attachment_6.att");
		expected.add(getResourcesDir() + "/File_With_Attachments_and_Nested_Subfolder/subfolder/Attachment_7.att");
		
		assertThat(unit.getAttachmentFilenames(), is(equalTo(expected)));
	}
	

}
