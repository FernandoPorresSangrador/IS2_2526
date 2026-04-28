package es.unican.is2.seguros.common;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de Cliente.totalSeguros()
 *
 * Método analizado: totalSeguros()
 *   - Suma el precio() de todos los seguros del cliente
 *   - Si minusvalia=true aplica descuento del 25% sobre el total
 *
 * Técnicas: Partición Equivalente (PE)
 *
 * | ID   | Clase de Equivalencia                        | seguros                           | minusvalia | Resultado esperado |
 * |------|----------------------------------------------|-----------------------------------|------------|--------------------|
 * | CT01 | Lista vacía, sin minusvalía                  | []                                | false      | 0.0                |
 * | CT02 | Lista vacía, con minusvalía                  | []                                | true       | 0.0                |
 * | CT03 | Un seguro, sin minusvalía                    | [TERCEROS, p=50, 2 años]          | false      | 400.0              |
 * | CT04 | Un seguro, con minusvalía                    | [TERCEROS, p=50, 2 años]          | true       | 300.0              |
 * | CT05 | Varios seguros, sin minusvalía               | [TERCEROS + TODO_RIESGO, p=50]    | false      | 1400.0             |
 * | CT06 | Varios seguros, con minusvalía               | [TERCEROS + TODO_RIESGO, p=50]    | true       | 1050.0             |
 */
class ClienteTest {

    private Cliente sut;

    @BeforeEach
    void setUp() {
        sut = new Cliente();
    }

    // Helper: seguro sin descuento ni recargo → precio = precioBase
    private Seguro seguro(Cobertura cobertura) {
        Seguro s = new Seguro();
        s.setCobertura(cobertura);
        s.setPotencia(50);
        s.setFechaInicio(LocalDate.now().minusYears(2));
        return s;
    }

    // ── CT01-CT02: lista vacía ─────────────────────────────────────────────

    @Test
    void testCT01TotalListaVaciaSinMinusvalia() {
        sut.setSeguros(Collections.emptyList());
        sut.setMinusvalia(false);
        assertEquals(0.0, sut.totalSeguros());
    }

    @Test
    void testCT02TotalListaVaciaConMinusvalia() {
        sut.setSeguros(Collections.emptyList());
        sut.setMinusvalia(true);
        assertEquals(0.0, sut.totalSeguros());
    }

    // ── CT03-CT04: un seguro ───────────────────────────────────────────────

    @Test
    void testCT03TotalUnSeguroSinMinusvalia() {
        sut.setSeguros(Arrays.asList(seguro(Cobertura.TERCEROS)));
        sut.setMinusvalia(false);
        assertEquals(400.0, sut.totalSeguros());
    }

    @Test
    void testCT04TotalUnSeguroConMinusvalia() {
        sut.setSeguros(Arrays.asList(seguro(Cobertura.TERCEROS)));
        sut.setMinusvalia(true);
        assertEquals(300.0, sut.totalSeguros()); // 400 * 0.75
    }

    // ── CT05-CT06: varios seguros ──────────────────────────────────────────

    @Test
    void testCT05TotalVariosSegurosSinMinusvalia() {
        sut.setSeguros(Arrays.asList(
            seguro(Cobertura.TERCEROS),    // 400
            seguro(Cobertura.TODO_RIESGO)  // 1000
        ));
        sut.setMinusvalia(false);
        assertEquals(1400.0, sut.totalSeguros());
    }

    @Test
    void testCT06TotalVariosSegurosConMinusvalia() {
        sut.setSeguros(Arrays.asList(
            seguro(Cobertura.TERCEROS),    // 400
            seguro(Cobertura.TODO_RIESGO)  // 1000
        ));
        sut.setMinusvalia(true);
        assertEquals(1050.0, sut.totalSeguros()); // 1400 * 0.75
    }
}
