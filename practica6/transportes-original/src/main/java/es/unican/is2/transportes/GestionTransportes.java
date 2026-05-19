package es.unican.is2.transportes;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona los conductores de la empresa de transportes.
 * CC total: buscaConductor(2) + anhadeConductor(2) + conductores(1) = 5
 * WMC = 5, WMCn = 5/3 = 1.67
 */
public class GestionTransportes {

	private ArrayList<Conductor> cs = new ArrayList<Conductor>();

	/**
	 * CC: 1 (base) + 1 (for) + 1 (if) = 3
	 * CCog: +1 (for) +1 (if, nivel 1, +1 anid) = 1+2 = 3
	 */
	public Conductor buscaConductor(String DNI) {
		// CC: +1 for
		for (Conductor c : cs)
			// CC: +1 if
			if (c.dni().equals(DNI))
				return c;
		return null;
	}

	/**
	 * CC: 1 (base) + 1 (if) = 2
	 * CCog: +1 (if) = 1
	 */
	public boolean anhadeConductor(String dni, String nombre, String apellido1, String apellido2, String direccion) {
		// CC: +1 if
		if (buscaConductor(dni) != null)
			return false;
		cs.add(new Conductor(dni, nombre, apellido1, apellido2, direccion));
		return true;
	}

	// CC: 1 | CCog: 0
	public List<Conductor> conductores() {
		return cs;
	}

}
