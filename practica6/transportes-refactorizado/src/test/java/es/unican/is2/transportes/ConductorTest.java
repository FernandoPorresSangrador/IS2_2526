package es.unican.is2.transportes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests de regresion para Conductor refactorizado.
 * Mismos casos de prueba que el original, adaptados a la nueva jerarquia de Transporte.
 */
public class ConductorTest {

	private static Conductor sut;

	@Test
	public void testConstructor() {
		// Casos validos
		sut = new Conductor("123123123X", "Pepe", "Martinez", "Fernandez", "Avda. de los Castros s/n");
		assertEquals("123123123X", sut.dni());
		assertEquals("123123123X", sut.getDni());
		assertEquals("Pepe", sut.getNombre());
		assertEquals("Martinez", sut.getApellido1());
		assertEquals("Fernandez", sut.apellido2());
		assertEquals("Fernandez", sut.getApellido2());
		assertEquals("Avda. de los Castros s/n", sut.getDire());

		sut = new Conductor("123123123X", "Pepe", "Martinez", null, "Avda. de los Castros s/n");
		assertNull(sut.apellido2());

		// Casos no validos
		assertThrows(IllegalArgumentException.class, () -> new Conductor(null, "Pepe", "Martinez", "Fernandez", "Avda. de los Castros s/n"));
		assertThrows(IllegalArgumentException.class, () -> new Conductor("123123123X", null, "Martinez", "Fernandez", "Avda. de los Castros s/n"));
		assertThrows(IllegalArgumentException.class, () -> new Conductor("123123123X", "Pepe", null, "Fernandez", "Avda. de los Castros s/n"));
		assertThrows(IllegalArgumentException.class, () -> new Conductor("123123123X", "Pepe", "Martinez", "Fernandez", null));
	}

	@Test
	public void testSueldoYAnhadeTransporte() {
		sut = new Conductor("123123123X", "Pepe", "Martinez", "Fernandez", "Avda. de los Castros s/n");
		assertTrue(sut.sueldo() == 700);

		// Personas 1h 1per: 5*1 + 0.5*1 = 5.5 -> total 705.5
		sut.anhadeTransporte(new TransportePersonas(1, 1));
		assertEquals(705.5, sut.sueldo());

		// Personas 10h 9per: 5*10 + 0.5*10 = 55 -> total 760.5
		sut.anhadeTransporte(new TransportePersonas(10, 9));
		assertEquals(760.5, sut.sueldo());

		// Personas 1h 10per: 5*1 + 1*1 = 6 -> total 766.5
		sut.anhadeTransporte(new TransportePersonas(1, 10));
		assertEquals(766.5, sut.sueldo());

		// Personas 10h 20per: 5*10 + 1*10 = 60 -> total 826.5
		sut.anhadeTransporte(new TransportePersonas(10, 20));
		assertEquals(826.5, sut.sueldo());

		// Mercancias 1h 1ton: 5*1 + 2*1 = 7 -> total 833.5
		sut.anhadeTransporte(new TransporteMercancias(1, 1));
		assertEquals(833.5, sut.sueldo());

		// MercanciasPeligrosas 10h 100ton: 5*10 + 100*2+50 = 300 -> total 1133.5
		sut.anhadeTransporte(new TransporteMercanciasPeligrosas(10, 100));
		assertEquals(1133.5, sut.sueldo());
	}

}
