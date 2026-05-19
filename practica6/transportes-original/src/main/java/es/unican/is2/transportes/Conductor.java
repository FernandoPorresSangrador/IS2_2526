package es.unican.is2.transportes;

import java.util.ArrayList;

/**
 * Clase que representa a un conductor, con sus datos personales
 * y los transportes que ha realizado.
 */
public class Conductor {

	private ArrayList<Transporte> transportes = new ArrayList<Transporte>();
	private String dni;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String dire;

	/**
	 * CC: 1 (base) + 1 (||) + 1 (||) + 1 (||) + 1 (if con 4 condiciones - 3 adicionales) = 5
	 * Mas preciso: 1 + 1 (if) + 1 (|| adic.) + 1 (|| adic.) + 1 (|| adic.) = 5
	 *  CCog: +1 (if) +1 (una sola secuencia de ||) = 2
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

	// CC: 1 | CCog: 0
	public String apellido2() {
		return apellido2;
	}

	// CC: 1 | CCog: 0
	public String getDire() {
		return dire;
	}

	/**
	 * Calcula el sueldo del conductor.
	 * Sueldo base: 700€
	 * Extra por transporte: 5€/hora conducida
	 * Extra adicional segun categoria:
	 *   - Mercancias: 2€/tonelada
	 *   - MercanciasPeligrosas: 2€/tonelada + 50€ fijo
	 *   - Personas: 0.5€/hora (no colectivo, <10) o 1€/hora (colectivo, >=10)
	 *
	 * CC: 1 (base) + 1 (for) + 3 (cases del switch: Mercancias, MercanciasPeligrosas, Personas)
	 *   + 1 (if dentro de Personas) = 6
	 *   Nota: switch suma 1 por cada case (no por el switch en si)
	 *  CCog: for(+1) switch(+1+1 anid=+2) if(+1+2 anid=+3) else(+1 siempre) = 1+2+3+1 = 7
	 *   Detalle: for(+1) switch(+1+1=+2) if dentro de case Personas(+1+2=+3) => total 6
	 */
	public double sueldo() {
		double sueldoTransportes = 0;
		// CC: +1 for
		for (Transporte t : transportes) {
			double sueldoExtraTransporte = 0.0;
			// CC: +1 case Mercancias, +1 case MercanciasPeligrosas, +1 case Personas
			switch (t.categoria()) {
				case Mercancias:
					sueldoExtraTransporte = t.ton() * 2;
					break;
				case MercanciasPeligrosas:
					sueldoExtraTransporte = t.ton() * 2 + 50;
					break;
				case Personas:
					// CC: +1 if
					if (t.getPersonas() < 10)
						sueldoExtraTransporte = t.horas() * 0.5;
					else
						sueldoExtraTransporte = t.horas();
					break;
			}
			sueldoTransportes += t.horas() * 5 + sueldoExtraTransporte;
		}
		return 700 + sueldoTransportes;
	}

	// CC: 1 | CCog: 0
	public void anhadeTransporte(Transporte t) {
		transportes.add(t);
	}

}
