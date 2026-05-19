package es.unican.is2.transportes;

/**
 * Transporte de mercancias peligrosas.
 * Extra de sueldo: igual que mercancias (2€/ton) + 50€ fijo.
 * Hereda de TransporteMercancias (Pull Up de los campos comunes).
 *
 * CC constructor: 1 (llama a super, sin logica adicional)
 * CCog constructor: 0
 *
 * CC extraSueldo: 1 (secuencial)
 * CCog extraSueldo: 0
 */
public class TransporteMercanciasPeligrosas extends TransporteMercancias {

	private static final double EXTRA_PELIGROSAS = 50.0;

	/**
	 * CC: 1 | CCog: 0
	 */
	public TransporteMercanciasPeligrosas(double horas, int ton) {
		super(horas, ton);
	}

	/**
	 * Extra: 2€/ton + 50€ fijo
	 * CC: 1 (secuencial) | CCog: 0
	 */
	@Override
	public double extraSueldo() {
		return super.extraSueldo() + EXTRA_PELIGROSAS;
	}

}
