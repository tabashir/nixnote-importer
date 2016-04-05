package biz.mckinley;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.VFS;

public class ZimNote {
	private final String filename;
	private final String bodyText;

	public ZimNote(String filename) throws InvalidNoteException {
		this.filename = filename;
		try {
			this.bodyText = FileUtils.readFileToString(new File(filename));
		} catch (IOException e) {
			throw new InvalidNoteException("cannot load note");
		}

	}

	public String getFilename() {
		return filename;
	}

	public String getBodyText() {
		return bodyText;
	}

	public Set<String> getTags() {
		return calculateTagsFromBody(bodyText);
	}

	public Set<String> calculateTagsFromBody(String body) {
		Set<String> tags = new HashSet<String>();
		Pattern pattern = Pattern.compile("\\B@([a-zA-Z_0-9]+)");
		Matcher matcher = pattern.matcher(body);
		while (matcher.find()) {
			tags.add(matcher.group(1));
		}
		return tags;
	}

	public Set<String> getAttachmentFilenames() {
		Set<String> attachments = new HashSet<String>();
		File attachmentFolder = getAttachmentFolderForFile(filename);

		if (attachmentFolder.isDirectory()) {
			try {
				FileSystemManager fsManager = VFS.getManager();
				FileObject fileObject = fsManager.resolveFile(attachmentFolder.getAbsolutePath());
				HashSet<FileObject> attachedFileObjects = new HashSet<FileObject>(Arrays.asList(fileObject.getChildren()));
				Set<FileObject> attachedFiles = filterNotes(attachedFileObjects);
				Set<FileObject> attachedNotes = findSubNotes(attachedFileObjects);
				Set<File> subNoteAttachmentFolderNames = new HashSet<>();
				for (FileObject subNote : attachedNotes) {
					subNoteAttachmentFolderNames.add(getAttachmentFolderForFile(getFileNameforEntry(subNote)));
				}

				for (FileObject entry : attachedFiles) {
					if (entry.getType().equals(FileType.FOLDER)) {
						if (!subNoteAttachmentFolderNames.contains(getAttachmentFolderForFile(getFileNameforEntry(entry)))) {
							FileObject[] subChildren = entry.getChildren();
							for (FileObject subChild : subChildren) {
								if (FileType.FILE.equals(subChild.getType())) {
									attachments.add(getFileNameforEntry(subChild));
								}
							}
						}

					} else {
						attachments.add(getFileNameforEntry(entry));
					}
				}

			} catch (FileSystemException e) {
				e.printStackTrace();
			}
		}

		return attachments;

	}

	private Set<FileObject> filterNotes(Set<FileObject> files) {
		Set<FileObject> nonNotes = new HashSet<FileObject>();
		for (FileObject fileEntry : files) {
			String fileEntryName = getFileNameforEntry(fileEntry);
			if (!fileEntryName.endsWith(".txt")) {
				nonNotes.add(fileEntry);
			}
		}
		return nonNotes;
	}

	private Set<FileObject> findSubNotes(Set<FileObject> files) {
		Set<FileObject> notes = new HashSet<FileObject>();
		for (FileObject fileEntry : files) {
			String fileEntryName = getFileNameforEntry(fileEntry);
			if (fileEntryName.endsWith(".txt")) {
				notes.add(fileEntry);
			}
		}
		return notes;
	}

	private String getFileNameforEntry(FileObject entry) {
		return StringUtils.removeStart(String.valueOf(entry.getName()), "file://");
	}

	private File getAttachmentFolderForFile(String filename) {
		return new File(StringUtils.substringBeforeLast(filename, ".txt"));
	}

	public String getTitle() {
		Pattern pattern = Pattern.compile("====== (.*) ======");
		Matcher matcher = pattern.matcher(bodyText);
		if (matcher.find()) {
			return (matcher.group(1));
		}
		return StringUtils.substringBefore(StringUtils.replace(StringUtils.substringAfterLast(filename, "/"), "_", " "), ".txt");
	}

	public Set<String> getExportArgs(String notebook) {
		Set<String> exportArgs = new HashSet<String>();
		exportArgs.add(makeArg("notebook", notebook));
		exportArgs.add(makeArg("title", getTitle()));
		exportArgs.addAll(makeArg("attachment", getAttachmentFilenames()));
		exportArgs.addAll(makeArg("tag", getTags()));
		return exportArgs;
	}

	public int exportToNixNote(String notebook, int accountId) {
		Executor executor = new DefaultExecutor();

		CommandLine cmdLine = new CommandLine("nixnote2");
		cmdLine.addArgument("addNote");
		cmdLine.addArgument("--accountId=" + String.valueOf(accountId));
		for (String cmdArg : getExportArgs(notebook)) {
			cmdLine.addArgument(cmdArg, false);
		}

		int result = 0;
		try {
			FileInputStream input;
			input = new FileInputStream(getFilename());
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			executor.setStreamHandler(new PumpStreamHandler(output, null, input));

			try {
				result = executor.execute(cmdLine);
			} catch (IOException e) {
				if (result != 0) {
					System.out.println("ERR Result: " + result);
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e1) {
		}

		return result;
	}

	private String makeArg(String name, String value) {
		return "--" + name + "=" + value;

	}

	private Set<String> makeArg(String name, Set<String> values) {
		Set<String> args = new HashSet<String>();
		for (String value : values) {
			args.add(makeArg(name, value));
		}
		return args;
	}

}
