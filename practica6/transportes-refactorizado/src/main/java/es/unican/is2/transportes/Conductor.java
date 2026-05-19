package es.unican.is2.transportes;

import java.util.ArrayList;

/**
 * Clase que representa a un conductor, con sus datos personales
 * y los transportes que ha realizado.
 *
 * REFACTORIZACION 3: Rename Method
 *   - apellido2() -> getApellido2() para uniformidad de getters
 *   - dni() -> getDni() (se mantienen ambos por compatibilidad con tests)
 *
 * REFACTORIZACION 1 (efecto): el metodo sueldo() ya no tiene switch,
 * usa polimorfismo llamando a t.extraSueldo() en cada Transporte.
 */
public class Conductor {

	private ArrayList<Transporte> transportes = new ArrayList<Transporte>();
	private String dni;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String dire;

	/**
	 * CC: 1 + 1(if) + 1(||) + 1(||) + 1(||) = 5
	 * CCog: +1(if) +1(||) +1(||) +1(||) = 4
	 */
	public Conductor(String dni, String nombre, String apellido1,
			String apellido2, String direccion) {
		// CC: +1 if; +3 por tres || adicionales
		if (dni == null || nombre == null || apellido1 == null || direccion == null) {
			throw new IllegalArgumentException();
		}
		this.dni = dni;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.dire = direccion;
	}

	// CC: 1 | CCog: 0
	public String dni() {
		return dni;
	}

	// CC: 1 | CCog: 0
	public String getDni() {
		return dni;
	}

	// CC: 1 | CCog: 0
	public String getNombre() {
		return nombre;
	}

	// CC: 1 | CCog: 0
	public String getApellido1() {
		return apellido1;
	}

	// CC: 1 | CCog: 0  (Rename: apellido2() -> getApellido2(), se mantiene apellido2() por tests)
	public String apellido2() {
		return apellido2;
	}

	// CC: 1 | CCog: 0
	public String getApellido2() {
		return apellido2;
	}

	// CC: 1 | CCog: 0
	public String getDire() {
		return dire;
	}

	/**
	 * Calcula el sueldo del conductor.
	 * Sueldo base: 700€
	 * Extra por transporte: 5€/hora + extraSueldo() especifico de cada tipo (polimorfismo).
	 *
	 * REFACTORIZACION 1 aplicada: se elimina el switch, cada Transporte
	 * sabe calcular su propio extra mediante extraSueldo().
	 *
	 * CC: 1 + 1(for) = 2  (el switch desaparece)
	 * CCog: +1(for) = 1
	 */
	public double sueldo() {
		double sueldoTransportes = 0;
		// CC: +1 for
		for (Transporte t : transportes) {
			// Polimorfismo: cada subclase implementa su propio extraSueldo()
			sueldoTransportes += t.horas() * 5 + t.extraSueldo();
		}
		return 700 + sueldoTransportes;
	}

	// CC: 1 | CCog: 0
	public void anhadeTransporte(Transporte t) {
		transportes.add(t);
	}

}
