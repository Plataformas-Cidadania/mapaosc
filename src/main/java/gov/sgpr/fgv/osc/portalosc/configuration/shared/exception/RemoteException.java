package gov.sgpr.fgv.osc.portalosc.configuration.shared.exception;

/**
 * A wrapper exception type indicating that the remote end threw an exception
 * over the wire.
 */
public class RemoteException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7892640312849653958L;

	public RemoteException() {
	}

	public RemoteException(String msg) {
		super(msg);
	}

	public RemoteException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public RemoteException(Throwable cause) {
		super(cause);
	}
}