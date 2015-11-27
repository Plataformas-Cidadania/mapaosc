/**
 * 
 */
package gov.sgpr.fgv.osc.portalosc.configuration.shared.validate;

/**
 * @author victor
 * 
 * Interface utilizada por validadores.
 *
 */
public interface Validator<E> {
	
	/**
	 * @param value Elemento a ser Validado a ser 
	 * @return True se for valido.
	 */
	public boolean validate(final E value);

}
