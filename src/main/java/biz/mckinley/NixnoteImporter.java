package biz.mckinley;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Strings;

public class NixnoteImporter {

	private final Set<ZimNote> notes;
	private final String notebook;
	private final String accountId;

	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			System.out.println("Usage - specify both the notebook folder and nixnote notebook");
		} else {
			NixnoteImporter importer = new NixnoteImporter(args[0], args[1], args[2]);
			importer.importNotes();
		}
	}

	public void importNotes() throws FileNotFoundException {
		System.out.println("Starting....");

		for (ZimNote note : notes) {
			System.out.println("* " + note.getFilename());
			note.exportToNixNote(notebook, accountId);
		}
		System.out.println("Finished.");

	}

	public NixnoteImporter(String baseDir, String notebook, String accountId) {
		if (Strings.isNullOrEmpty(baseDir)) {
			throw new RuntimeException("You need to specify the Zim notebook base folder");
		}

		if (Strings.isNullOrEmpty(notebook)) {
			throw new RuntimeException("You need to specify the Evernote notebook to import to");
		}
		this.notebook = notebook;
		this.accountId = accountId;

		Set<ZimNote> zimNotes = new HashSet<>();
		Collection<File> textFiles = FileUtils.listFiles(new File(baseDir), new String[] { "txt" }, true);
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
