package biz.mckinley;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class NixnoteImporter {

	private final Set<ZimNote> notes;

	public NixnoteImporter(String zimDir) {
		Set<ZimNote> zimNotes = new HashSet<>();
		Collection<File> textFiles = FileUtils.listFiles(new File(zimDir), new String[] { "txt" } , true);
		for (File textFile : textFiles) {
			try {
				System.out.println(textFile.getAbsolutePath());
				zimNotes.add(new ZimNote(textFile.getAbsolutePath()));
			} catch (InvalidNoteException e) {
				e.printStackTrace();
			}
		}
		this.notes = zimNotes;
		
	}

	public Set<ZimNote> getNotes() {
		return notes;
	}

}
