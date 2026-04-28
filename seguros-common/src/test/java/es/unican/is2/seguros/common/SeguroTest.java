package es.unican.is2.seguros.common;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de Seguro.precio()
 *
 * Método analizado: precio()
 *   - Retorna 0 si fechaInicio es null o está en el futuro
 *   - Precio base según cobertura: TERCEROS=400, TERCEROS_LUNAS=600, TODO_RIESGO=1000
 *   - Recargo potencia: >=90 y <=110 → +5%; >110 → +20%; <90 → sin recargo
 *   - Descuento del 20% si el seguro lleva menos de 1 año en vigor
 *
 * Técnicas: Partición Equivalente (PE) + Análisis de Valores Límite (AVL)
 *
 * | ID   | Clase de Equivalencia                          | Técnica | fechaInicio                   | cobertura      | potencia | Resultado esperado |
 * |------|------------------------------------------------|---------|-------------------------------|----------------|----------|--------------------|
 * | SP01 | fechaInicio null                               | PE      | null                          | TERCEROS       | 50       | 0.0                |
 * | SP02 | fechaInicio mañana (futuro, límite superior)   | AVL     | now+1                         | TERCEROS       | 50       | 0.0                |
 * | SP03 | fechaInicio hoy (límite inferior válido)        | AVL     | now                           | TERCEROS       | 50       | 320.0              |
 * | SP04 | último día con descuento (now-1año+1día)        | AVL     | now-1year+1day                | TERCEROS       | 50       | 320.0              |
 * | SP05 | primer día sin descuento (now-1año exacto)     | AVL     | now-1year                     | TERCEROS       | 50       | 400.0              |
 * | SP06 | seguro con más de 1 año (sin descuento)        | PE      | now-2years                    | TERCEROS       | 50       | 400.0              |
 * | SP07 | cobertura TERCEROS_LUNAS                       | PE      | now-2years                    | TERCEROS_LUNAS | 50       | 600.0              |
 * | SP08 | cobertura TODO_RIESGO                          | PE      | now-2years                    | TODO_RIESGO    | 50       | 1000.0             |
 * | SP09 | potencia=89 (límite inferior sin recargo)      | AVL     | now-2years                    | TERCEROS       | 89       | 400.0              |
 * | SP10 | potencia=90 (límite inferior recargo 5%)       | AVL     | now-2years                    | TERCEROS       | 90       | 420.0              |
 * | SP11 | potencia=110 (límite superior recargo 5%)      | AVL     | now-2years                    | TERCEROS       | 110      | 420.0              |
 * | SP12 | potencia=111 (límite inferior recargo 20%)     | AVL     | now-2years                    | TERCEROS       | 111      | 480.0              |
 * | SP13 | potencia alta (recargo 20%, PE)                | PE      | now-2years                    | TERCEROS       | 200      | 480.0              |
 */
class SeguroTest {

    private Seguro sut;

    @BeforeEach
    void setUp() {
        sut = new Seguro();
        sut.setCobertura(Cobertura.TERCEROS);
        sut.setPotencia(50);
    }

    // ── SP01: fechaInicio null ─────────────────────────────────────────────

    @Test
    void testSP01PrecioFechaInicioNull() {
        sut.setFechaInicio(null);
        assertEquals(0.0, sut.precio());
    }

    // ── SP02-SP03: límite entre futuro (→0) y presente (→precio) ──────────

    @Test
    void testSP02PrecioFechaInicioManana() {
        sut.setFechaInicio(LocalDate.now().plusDays(1));
        assertEquals(0.0, sut.precio());
    }

    @Test
    void testSP03PrecioFechaInicioHoy() {
        // Hoy es válido y lleva menos de 1 año → aplica descuento 20%
        sut.setFechaInicio(LocalDate.now());
        assertEquals(320.0, sut.precio()); // 400 * 0.8
    }

    // ── SP04-SP05: límite del año de descuento ─────────────────────────────

    @Test
    void testSP04PrecioUltimoDiaConDescuento() {
        // Un día antes del año exacto → todavía aplica descuento
        sut.setFechaInicio(LocalDate.now().minusYears(1).plusDays(1));
        assertEquals(320.0, sut.precio()); // 400 * 0.8
    }

    @Test
    void testSP05PrecioPrimerDiaSinDescuento() {
        // Exactamente 1 año → ya no aplica descuento
        sut.setFechaInicio(LocalDate.now().minusYears(1));
        assertEquals(400.0, sut.precio());
    }

    // ── SP06: sin descuento (PE, >1 año) ──────────────────────────────────

    @Test
    void testSP06PrecioMasDeUnAno() {
        sut.setFechaInicio(LocalDate.now().minusYears(2));
        assertEquals(400.0, sut.precio());
    }

    // ── SP07-SP08: cobertura (PE) ──────────────────────────────────────────

    @Test
    void testSP07PrecioCoberturaTercerosLunas() {
        sut.setFechaInicio(LocalDate.now().minusYears(2));
        sut.setCobertura(Cobertura.TERCEROS_LUNAS);
        assertEquals(600.0, sut.precio());
    }

    @Test
    void testSP08PrecioCoberturaTodoRiesgo() {
        sut.setFechaInicio(LocalDate.now().minusYears(2));
        sut.setCobertura(Cobertura.TODO_RIESGO);
        assertEquals(1000.0, sut.precio());
    }

    // ── SP09-SP13: recargo por potencia (AVL) ─────────────────────────────

    @Test
    void testSP09PrecioPotencia89SinRecargo() {
        sut.setFechaInicio(LocalDate.now().minusYears(2));
        sut.setPotencia(89);
        assertEquals(400.0, sut.precio());
    }

    @Test
    void testSP10PrecioPotencia90Recargo5pct() {
        sut.setFechaInicio(LocalDate.now().minusYears(2));
        sut.setPotencia(90);
        assertEquals(420.0, sut.precio()); // 400 + 400*0.05
    }

    @Test
    void testSP11PrecioPotencia110Recargo5pct() {
        sut.setFechaInicio(LocalDate.now().minusYears(2));
        sut.setPotencia(110);
        assertEquals(420.0, sut.precio()); // 400 + 400*0.05
    }

    @Test
    void testSP12PrecioPotencia111Recargo20pct() {
        sut.setFechaInicio(LocalDate.now().minusYears(2));
        sut.setPotencia(111);
        assertEquals(480.0, sut.precio()); // 400 + 400*0.20
    }

    @Test
    void testSP13PrecioPotenciaAltaRecargo20pct() {
        sut.setFechaInicio(LocalDate.now().minusYears(2));
        sut.setPotencia(200);
        assertEquals(480.0, sut.precio()); // 400 + 400*0.20
    }
}
