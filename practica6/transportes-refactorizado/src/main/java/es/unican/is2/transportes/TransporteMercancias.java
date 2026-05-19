package es.unican.is2.transportes;

/**
 * Transporte de mercancias.
 * Extra de sueldo: 2€/tonelada.
 *
 * CC constructor: 1 + 1(if ton<=0) = 2
 * CCog constructor: +1(if) = 1
 *
 * CC extraSueldo: 1 (secuencial)
 * CCog extraSueldo: 0
 */
public class TransporteMercancias extends Transporte {

	protected int ton;

	/**
	 * CC: 1 + 1(if) = 2
	 * CCog: +1(if) = 1
	 */
	public TransporteMercancias(double horas, int ton) {
		super(horas);
		// CC: +1 if
		if (ton <= 0) {
			throw new IllegalArgumentException();
		}
		this.ton = ton;
	}

	// CC: 1 | CCog: 0
	public int getTon() {
		return ton;
	}

	/**
	 * Extra: 2€/tonelada
	 * CC: 1 (secuencial) | CCog: 0
	 */
	@Override
	public double extraSueldo() {
		return ton * 2;
	}

}
