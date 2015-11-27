package gov.sgpr.fgv.osc.portalosc.configuration.shared.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements Validator<String> {
	private Pattern pattern;
	private Matcher matcher;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	/* (non-Javadoc)
	 * @see gov.sgpr.fgv.osc.portalosc.user.shared.validate.Validator#validate(java.lang.Object)
	 */
	public boolean validate(final String value) {
		matcher = pattern.matcher(value);
		return matcher.matches();
	}

}
