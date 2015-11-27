package gov.sgpr.fgv.osc.portalosc.configuration.shared.exception;

/**
 * A wrapper exception type indicating that the remote validation end threw an exception
 * over the wire.
 */
public class ValidationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7892640312849653958L;

	public ValidationException() {
	}

	public ValidationException(String msg) {
		super(msg);
	}

	public ValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}
}