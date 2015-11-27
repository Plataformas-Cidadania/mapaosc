package gov.sgpr.fgv.osc.portalosc.configuration.shared.validate;

import java.text.DecimalFormat;

/**
 * @author victor Valida um CPF
 */
public class CpfValidator implements Validator<Long> {

	/* (non-Javadoc)
	 * @see gov.sgpr.fgv.osc.portalosc.user.shared.validate.Validator#validate(java.lang.Object)
	 */
	public boolean validate(Long value) {
		if (value <= 0)
			return false;
		DecimalFormat df = new DecimalFormat("00000000000");
		String cpf = df.format(value);
		if (cpf.length() != 11) 
			return false;

		String c = cpf.substring(0, 9);
		String dv = cpf.substring(9, 11);
		int d1 = 0;
		for (int i = 0; i < 9; i++) {
			d1 += Character.digit(c.charAt(i), 10) * (10 - i);
		}
		if (d1 == 0) {
			return false;
		}
		d1 = 11 - (d1 % 11);
		if (d1 > 9)
			d1 = 0;
		if (Character.digit(dv.charAt(0), 10) != d1) {
			return false;
		}
		d1 *= 2;
		for (int i = 0; i < 9; i++) {
			d1 += (Character.digit(c.charAt(i), 10)) * (11 - i);
		}
		d1 = 11 - (d1 % 11);
		if (d1 > 9)
			d1 = 0;
		if ((Character.digit(dv.charAt(1), 10)) != d1) {
			return false;
		}
		return true;
	}

}
