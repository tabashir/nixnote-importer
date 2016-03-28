package biz.mckinley;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

public class ZimTagTest {

	private String getResourcesDir() {
	    return System.getProperty("user.dir") + "/src/test/resources";
	}

	@Test
	public void tagDetectionSingleLine() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/Baremetal_Script_Release.txt");
		StringBuilder bodyText = new StringBuilder();
		bodyText.append("@mytag");
		assertThat(unit.calculateTagsFromBody(bodyText.toString()), is(equalTo(Arrays.asList("mytag"))));
	}
	
	@Test
	public void tagDetectionSingleLineWithSpaceAfter() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/Baremetal_Script_Release.txt");
		StringBuilder bodyText = new StringBuilder();
		bodyText.append("@mytag ");
		assertThat(unit.calculateTagsFromBody(bodyText.toString()), is(equalTo(Arrays.asList("mytag"))));
	}
	
	@Test
	public void tagDetectionSingleLineWithMultipleTags() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/Baremetal_Script_Release.txt");
		StringBuilder bodyText = new StringBuilder();
		bodyText.append("@mytag @anothertag");
		assertThat(unit.calculateTagsFromBody(bodyText.toString()), is(equalTo(Arrays.asList("mytag", "anothertag"))));
	}
	
	@Test
	public void tagDetectionMultipleLines() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/Baremetal_Script_Release.txt");
		StringBuilder bodyText = new StringBuilder();
		bodyText.append("@mytag @anothertag");
		bodyText.append("\n");
		bodyText.append("this line does not have any tags");
		bodyText.append("\n");
		bodyText.append("@differentLine");

		assertThat(unit.calculateTagsFromBody(bodyText.toString()), is(equalTo(Arrays.asList("mytag", "anothertag", "differentLine"))));
	}
	
	@Test
	public void tagDetectionWillNotFalselyDetectEmailAddressAsTag() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/Baremetal_Script_Release.txt");
		StringBuilder bodyText = new StringBuilder();
		bodyText.append("jez@example");
		assertThat(unit.calculateTagsFromBody(bodyText.toString()).size(), is(equalTo(0)));
	}
	
	@Test
	public void getTagsCalculatesFromLoadedTextBody() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/Baremetal_Script_Release.txt");
		assertThat(unit.getTags(), is(equalTo(Arrays.asList("OnDemand", "Scrum_of_Scrums","TODO","Release", "jezmckinley"))));
	}

}
