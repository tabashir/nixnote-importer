package biz.mckinley;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.StringUtils;

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

	public List<String> getTags() {
		return calculateTagsFromBody(bodyText);
	}

	public List<String> calculateTagsFromBody(String body) {
		List<String> tags = new ArrayList<String>();
		Pattern pattern = Pattern.compile("\\B@([a-zA-Z_0-9]+)");
		Matcher matcher = pattern.matcher(body);
		while (matcher.find()) {
			tags.add(matcher.group(1));
		}
		return tags;
	}

	public List<String> getAttachmentFilenames() {
		File attachmentFolder = new File(StringUtils.substringBeforeLast(filename, ".txt"));
		List<String> filenames = new ArrayList<String>();
		if (attachmentFolder.isDirectory()) {
			IOFileFilter fileFilter = new IOFileFilter() {
				
				public boolean accept(File dir, String name) {
					return false;
				}
				
				public boolean accept(File file) {
					return true;
				}
				
			};
			Collection<File> attachments = FileUtils.listFiles(attachmentFolder, fileFilter, null);
			
			for (File file : attachments) {
				filenames.add(file.getAbsolutePath());
			}
		}
		return filenames;
	}

	public String getTitle() {
		Pattern pattern = Pattern.compile("====== (.*) ======");
		Matcher matcher = pattern.matcher(bodyText);
		if (matcher.find()) {
			return (matcher.group(1));
		}
		return StringUtils.substringBefore(StringUtils.replace(StringUtils.substringAfterLast(filename, "/"), "_", " "), ".txt");
	}

	public List<String> getExportArgs() {
		List<String> exportArgs = new ArrayList<String>();
		exportArgs.add(makeArg("title", getTitle()));
		exportArgs.addAll(makeArg("attachment", getAttachmentFilenames()));
		exportArgs.addAll(makeArg("tag", getTags()));
		return exportArgs;
	}

	private String makeArg(String name, String value) {
		return "--" + name + "='" + value + "'";
	}
	
	private List<String> makeArg(String name, List<String> values) {
		List<String> args = new ArrayList<String>();
		for (String value : values) {
			args.add(makeArg(name, value));
		}
		return args;
	}
	
	

}
