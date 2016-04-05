package biz.mckinley;

import org.junit.Test;

public class NixnoteImporterRunner {

	@Test
	public void runIt() throws Exception {
		NixnoteImporter unit = new NixnoteImporter("/home/jezm/Dropbox/Zim/Notebooks/notes", "testing");
		unit.importNotes();
	}
	


}
