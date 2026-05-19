package es.unican.is2.transportes;

import java.util.LinkedList;
import java.util.List;
import fundamentos.*;

/**
 * Gestion de una empresa de transportes.
 * REFACTORIZACION 5: Extract Method (mostrarMejorConductor, procesarNuevoTransporte)
 * Se extraen bloques del switch a metodos separados para reducir la longitud de main().
 */
public class GestionTransportesGUI {

	private static final int ANHADE_CONDUCTOR = 0;
	private static final int ANHADE_TRANSPORTE = 1;
	private static final int SUELDO_CONDUCTOR = 2;
	private static final int MEJOR_CONDUCTOR = 3;

	/**
	 * Programa principal basado en menu.
	 * CC: 1 + 1(while) + 4(cases) + 1(if anhadeConductor) + 1(if c!=null sueldo) = 8
	 * CCog: while(+1) switch(+2) if(!gt.anhadeConductor)(+2) if(c!=null sueldo)(+2) = 7
	 */
	public static void main(String[] args) {
		GestionTransportes gt = new GestionTransportes();
		Menu menu = new Menu("Transportes");
		menu.insertaOpcion("Anhade conductor", ANHADE_CONDUCTOR);
		menu.insertaOpcion("Anhade transporte", ANHADE_TRANSPORTE);
		menu.insertaOpcion("Sueldo conductor", SUELDO_CONDUCTOR);
		menu.insertaOpcion("Mejor conductor", MEJOR_CONDUCTOR);

		while (true) {
			int opcion = menu.leeOpcion();

			switch (opcion) {
			case ANHADE_CONDUCTOR:
				procesarNuevoConductor(gt);
				break;
			case ANHADE_TRANSPORTE:
				procesarNuevoTransporte(gt);
				break;
			case SUELDO_CONDUCTOR:
				mostrarSueldoConductor(gt);
				break;
			case MEJOR_CONDUCTOR:
				mostrarMejorConductor(gt);
				break;
			}
		}
	}

	/**
	 * REFACTORIZACION 5 (Extract Method): logica de anadir conductor extraida de main.
	 * CC: 1 + 1(if) = 2
	 * CCog: +1(if) = 1
	 */
	private static void procesarNuevoConductor(GestionTransportes gt) {
		Lectura lect = new Lectura("Datos Conductor");
		lect.creaEntrada("DNI", "");
		lect.creaEntrada("Nombre", "");
		lect.creaEntrada("Apellido1", "");
		lect.creaEntrada("Apellido2", "");
		lect.creaEntrada("Direccion", "");
		lect.esperaYCierra();
		String dni = lect.leeString("DNI");
		String nombre = lect.leeString("Nombre");
		String apellido1 = lect.leeString("Apellido1");
		String apellido2 = lect.leeString("Apellido2");
		String direccion = lect.leeString("Direccion");
		if (!gt.anhadeConductor(dni, nombre, apellido1, apellido2, direccion))
			mensaje("ERROR", "Ya existe un conductor con DNI " + dni);
	}

	/**
	 * REFACTORIZACION 5 (Extract Method): logica de anadir transporte extraida de main.
	 * CC: 1 + 1(if c!=null) + 3(cases P/M/MP) = 5
	 * CCog: +1(if) +2(switch anid.) = 3
	 */
	private static void procesarNuevoTransporte(GestionTransportes gt) {
		Lectura lect = new Lectura("Nuevo transporte");
		lect.creaEntrada("DNI", "");
		lect.creaEntrada("Tipo Transporte: P | M | MP", "");
		lect.creaEntrada("Horas", 0);
		lect.creaEntrada("Personas", 0);
		lect.creaEntrada("Toneladas", 0);
		lect.esperaYCierra();
		String dni = lect.leeString("DNI");
		String tipo = lect.leeString("Tipo Transporte: P | M | MP");
		int horas = lect.leeInt("Horas");
		int personas = lect.leeInt("Personas");
		int toneladas = lect.leeInt("Toneladas");

		Conductor c = gt.buscaConductor(dni);
		if (c != null) {
			Transporte t = null;
			switch (tipo) {
				case "P":
					t = new TransportePersonas(horas, personas);
					break;
				case "M":
					t = new TransporteMercancias(horas, toneladas);
					break;
				case "MP":
					t = new TransporteMercanciasPeligrosas(horas, toneladas);
					break;
			}
			if (t != null)
				c.anhadeTransporte(t);
		} else {
			mensaje("ERROR", "No existe un conductor con DNI " + dni);
		}
	}

	/**
	 * CC: 1 + 1(if c!=null) = 2 | CCog: +1(if) = 1
	 */
	private static void mostrarSueldoConductor(GestionTransportes gt) {
		Lectura lect = new Lectura("Sueldo Conductor");
		lect.creaEntrada("DNI", "");
		lect.esperaYCierra();
		String dni = lect.leeString("DNI");
		Conductor c = gt.buscaConductor(dni);
		if (c != null) {
			mensaje("Sueldo", "El sueldo del conductor es: " + c.sueldo());
		} else {
			mensaje("ERROR", "No existe un conductor con DNI " + dni);
		}
	}

	/**
	 * REFACTORIZACION 5 (Extract Method): logica de mejor conductor extraida de main.
	 * CC: 1 + 1(for) + 1(if >) + 1(else if ==) + 1(if size==0) + 1(for msj) = 6
	 * CCog: for(+1) if(+2) else if(+1) if size(+1) for(+1) = 6
	 */
	private static void mostrarMejorConductor(GestionTransportes gt) {
		List<Conductor> resultado = new LinkedList<Conductor>();
		double maxSueldo = 0.0;
		for (Conductor conductor : gt.conductores()) {
			if (conductor.sueldo() > maxSueldo) {
				maxSueldo = conductor.sueldo();
				resultado.clear();
				resultado.add(conductor);
			} else if (conductor.sueldo() == maxSueldo) {
				resultado.add(conductor);
			}
		}
		String msj = "";
		if (resultado.size() == 0) {
			msj = "No hay conductores";
		} else {
			for (Conductor conductor : resultado) {
				msj += conductor.getNombre() + " " + conductor.getApellido1() + "\n";
			}
		}
		mensaje("MEJOR CONDUCTOR", msj);
	}

	// CC: 1 | CCog: 0
	private static void mensaje(String titulo, String txt) {
		Mensaje msj = new Mensaje(titulo);
		msj.escribe(txt);
	}

}
