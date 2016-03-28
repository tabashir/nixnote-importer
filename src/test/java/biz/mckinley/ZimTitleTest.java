package biz.mckinley;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ZimTitleTest {

	private String getResourcesDir() {
	    return System.getProperty("user.dir") + "/src/test/resources";
	}
	
	@Test
	public void extractZimTitle() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/Baremetal_Script_Release.txt");
		assertThat(unit.getTitle(), is(equalTo("Baremetal Script Release")));
	}

	@Test
	public void extractTitleFallsBackToFilenameWithSpaces() throws Exception {
		ZimNote unit = new ZimNote(getResourcesDir() + "/File_With_No_Title.txt");
		assertThat(unit.getTitle(), is(equalTo("File With No Title")));
	}

	

}
