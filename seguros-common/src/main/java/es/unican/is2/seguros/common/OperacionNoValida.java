
package es.unican.is2.seguros.common;


@SuppressWarnings("serial")
public class OperacionNoValida extends RuntimeException {

	public OperacionNoValida(String string) {
		super(string);
	}

}
