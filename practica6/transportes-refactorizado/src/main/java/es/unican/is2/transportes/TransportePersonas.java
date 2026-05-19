package es.unican.is2.transportes;

/**
 * Transporte de personas.
 * Extra de sueldo: 0.5€/hora si < 10 personas, 1€/hora si >= 10 personas.
 *
 * CC constructor: 1 + 1(if horas<=0, heredado) + 1(if personas<=0) = ya en super; aqui: 1 + 1(if) = 2
 * CCog constructor: +1(if) = 1
 *
 * CC extraSueldo: 1 + 1(if) = 2
 * CCog extraSueldo: +1(if) = 1
 */
public class TransportePersonas extends Transporte {

	private static final int UMBRAL_COLECTIVO = 10;
	private int personas;

	/**
	 * CC: 1 + 1(if personas<=0) = 2
	 * CCog: +1(if) = 1
	 */
	public TransportePersonas(double horas, int personas) {
		super(horas);
		// CC: +1 if
		if (personas <= 0) {
			throw new IllegalArgumentException();
		}
		this.personas = personas;
	}

	// CC: 1 | CCog: 0
	public int getPersonas() {
		return personas;
	}

	/**
	 * Extra: 0.5€/hora si no colectivo (<10), 1€/hora si colectivo (>=10)
	 * CC: 1 + 1(if) = 2
	 * CCog: +1(if) = 1
	 */
	@Override
	public double extraSueldo() {
		// CC: +1 if
		if (personas < UMBRAL_COLECTIVO)
			return horas * 0.5;
		else
			return horas;
	}

}
