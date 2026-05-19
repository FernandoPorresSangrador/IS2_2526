package es.unican.is2.transportes;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona los conductores de la empresa de transportes.
 *
 * REFACTORIZACION 4: Rename Class
 *   gestionTransportes -> GestionTransportes (convencion Java de nombres de clases)
 *
 * CC: buscaConductor(3) + anhadeConductor(2) + conductores(1) = 6
 * WMC = 6, WMCn = 6/3 = 2.0
 */
public class GestionTransportes {

	private ArrayList<Conductor> cs = new ArrayList<Conductor>();

	/**
	 * CC: 1 + 1(for) + 1(if) = 3
	 * CCog: +1(for) +2(if anidado) = 3
	 */
	public Conductor buscaConductor(String DNI) {
		for (Conductor c : cs)
			if (c.getDni().equals(DNI))
				return c;
		return null;
	}

	/**
	 * CC: 1 + 1(if) = 2
	 * CCog: +1(if) = 1
	 */
	public boolean anhadeConductor(String dni, String nombre, String apellido1, String apellido2, String direccion) {
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
