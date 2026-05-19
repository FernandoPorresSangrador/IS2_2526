package es.unican.is2.transportes;

/**
 * Clase abstracta base para todos los tipos de transporte.
 * REFACTORIZACION 1: Replace Type Code with Subclasses
 * Se elimina el enum CategoriaTransporte y se crea una jerarquia de clases.
 * Esto elimina el switch en Conductor.sueldo() y aplica polimorfismo.
 *
 * REFACTORIZACION 2: Extract Method (extraExtraSueldo)
 * El calculo del extra de sueldo de cada tipo se mueve a cada subclase
 * mediante el metodo abstracto extraSueldo().
 *
 * CC constructor: 1 + 1(if) + 1(|| adic.) + 1(|| adic.) = 4
 * CCog constructor: +1(if) +1(||) +1(||) = 3
 */
public abstract class Transporte {

	protected double horas;

	public Transporte(double horas) {
		// CC: +1 if; +1 || adicional (horas<=0 es condicion simple = 1 total)
		if (horas <= 0) {
			throw new IllegalArgumentException();
		}
		this.horas = horas;
	}

	// CC: 1 | CCog: 0
	public double horas() {
		return horas;
	}

	/**
	 * Retorna el extra de sueldo especifico de cada tipo de transporte.
	 * CC: 1 (abstracto) | CCog: 0
	 */
	public abstract double extraSueldo();

}
