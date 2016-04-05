package biz.mckinley;

import org.junit.Ignore;
import org.junit.Test;

public class NixnoteImporterRunner {

	@Test
	@Ignore
	public void runIt() throws Exception {
		NixnoteImporter unit = new NixnoteImporter("/home/jezm/Dropbox/Zim/Notebooks/notes", "import", "1");
		unit.importNotes();
	}
	


}
