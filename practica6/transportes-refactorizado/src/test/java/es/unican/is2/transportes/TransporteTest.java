package es.unican.is2.transportes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests de regresion para la jerarquia de Transporte refactorizada.
 * Mantiene la misma cobertura que los tests originales.
 */
public class TransporteTest {

    @Test
    public void testTransporteMercancias() {
        TransporteMercancias sut = new TransporteMercancias(1, 1);
        assertEquals(1, sut.horas());
        assertEquals(1, sut.getTon());
        assertEquals(2.0, sut.extraSueldo());

        sut = new TransporteMercancias(10, 1000);
        assertEquals(10, sut.horas());
        assertEquals(1000, sut.getTon());
        assertEquals(2000.0, sut.extraSueldo());

        // Casos no validos
        assertThrows(IllegalArgumentException.class, () -> new TransporteMercancias(0, 1));
        assertThrows(IllegalArgumentException.class, () -> new TransporteMercancias(10, 0));
    }

    @Test
    public void testTransporteMercanciasPeligrosas() {
        TransporteMercanciasPeligrosas sut = new TransporteMercanciasPeligrosas(10, 1000);
        assertEquals(10, sut.horas());
        assertEquals(1000, sut.getTon());
        // 1000*2 + 50 = 2050
        assertEquals(2050.0, sut.extraSueldo());

        assertThrows(IllegalArgumentException.class, () -> new TransporteMercanciasPeligrosas(0, 1));
        assertThrows(IllegalArgumentException.class, () -> new TransporteMercanciasPeligrosas(10, 0));
    }

    @Test
    public void testTransportePersonas() {
        // No colectivo (<10 personas)
        TransportePersonas sut = new TransportePersonas(1, 1);
        assertEquals(1, sut.horas());
        assertEquals(1, sut.getPersonas());
        // 1 hora * 0.5 = 0.5
        assertEquals(0.5, sut.extraSueldo());

        // No colectivo limite (9 personas)
        sut = new TransportePersonas(10, 9);
        // 10 horas * 0.5 = 5.0
        assertEquals(5.0, sut.extraSueldo());

        // Colectivo exacto (10 personas)
        sut = new TransportePersonas(1, 10);
        // 1 hora * 1 = 1.0
        assertEquals(1.0, sut.extraSueldo());

        // Colectivo (20 personas)
        sut = new TransportePersonas(10, 20);
        // 10 horas * 1 = 10.0
        assertEquals(10.0, sut.extraSueldo());

        // Casos no validos
        assertThrows(IllegalArgumentException.class, () -> new TransportePersonas(0, 1));
        assertThrows(IllegalArgumentException.class, () -> new TransportePersonas(10, 0));
    }

}
