package biz.mckinley;

public class InvalidNoteException extends Exception {
	
	private static final long serialVersionUID = 3124634343611632630L;

	public InvalidNoteException(String message) {
		super(message);
	}

}
