package es.unican.is2.seguros.gui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JFrame;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import es.unican.is2.seguros.common.Cobertura;
import es.unican.is2.seguros.common.Cliente;
import es.unican.is2.seguros.common.DataAccessException;
import es.unican.is2.seguros.common.IInfoSeguros;
import es.unican.is2.seguros.common.Seguro;

/**
 * Pruebas de integración del caso de uso "Consulta Cliente" en VistaAgente.
 *
 * Se verifica el comportamiento de la GUI cuando el usuario introduce un DNI
 * y pulsa "Buscar". La capa de negocio se sustituye por un mock de IInfoSeguros.
 *
 * Nombres de componentes en VistaAgente:
 *   - txtDNICliente  : campo de texto para el DNI
 *   - btnBuscar      : botón de búsqueda
 *   - txtNombreCliente: campo de texto con el nombre del cliente
 *   - txtTotalCliente : campo de texto con el total a pagar
 *   - listSeguros    : lista de seguros del cliente
 *
 * | ID   | Descripción                        | DNI introducido | Resultado esperado              |
 * |------|------------------------------------|-----------------|---------------------------------|
 * | IT01 | Cliente con 3 seguros              | 11111111A       | nombre=Juan, 3 elementos lista  |
 * | IT02 | Cliente sin seguros                | 33333333A       | nombre=Luis, lista vacía        |
 * | IT03 | Cliente no existente (null)        | 99999999Z       | nombre="DNI No Válido"          |
 * | IT04 | DNI vacío (null del mock)          | ""              | nombre="DNI No Válido"          |
 * | IT05 | Error acceso BBDD (excepción)      | ERRORTEST       | nombre="Error en BBDD"          |
 */
public class VistaAgenteIT extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private IInfoSeguros mockInfo;

    @Override
    protected void onSetUp() {
        mockInfo = mock(IInfoSeguros.class);

        VistaAgente frame = GuiActionRunner.execute(() -> {
            VistaAgente f = new VistaAgente(null, null, mockInfo);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            return f;
        });

        window = new FrameFixture(robot(), frame);
        window.show();
    }

    @Override
    protected void onTearDown() {
        window.cleanUp();
    }

    // ── IT01: cliente con 3 seguros ────────────────────────────────────────

    @Test
    public void testIT01ConsultaClienteConSeguros() throws DataAccessException {
        when(mockInfo.cliente("11111111A")).thenReturn(clienteJuan());

        window.textBox("txtDNICliente").enterText("11111111A");
        window.button("btnBuscar").click();

        window.textBox("txtNombreCliente").requireText("Juan");
        window.list("listSeguros").requireItemCount(3);
    }

    // ── IT02: cliente sin seguros ──────────────────────────────────────────

    @Test
    public void testIT02ConsultaClienteSinSeguros() throws DataAccessException {
        Cliente luis = new Cliente();
        luis.setNombre("Luis");
        luis.setDni("33333333A");
        luis.setMinusvalia(true);
        luis.setSeguros(Collections.emptyList());
        when(mockInfo.cliente("33333333A")).thenReturn(luis);

        window.textBox("txtDNICliente").enterText("33333333A");
        window.button("btnBuscar").click();

        window.textBox("txtNombreCliente").requireText("Luis");
        window.list("listSeguros").requireItemCount(0);
        window.textBox("txtTotalCliente").requireText("0.0");
    }

    // ── IT03: cliente no existente ─────────────────────────────────────────

    @Test
    public void testIT03ConsultaClienteNoExistente() throws DataAccessException {
        when(mockInfo.cliente("99999999Z")).thenReturn(null);

        window.textBox("txtDNICliente").enterText("99999999Z");
        window.button("btnBuscar").click();

        window.textBox("txtNombreCliente").requireText("DNI No Válido");
    }

    // ── IT04: DNI vacío ────────────────────────────────────────────────────

    @Test
    public void testIT04ConsultaDNIVacio() throws DataAccessException {
        when(mockInfo.cliente("")).thenReturn(null);

        window.button("btnBuscar").click();

        window.textBox("txtNombreCliente").requireText("DNI No Válido");
    }

    // ── IT05: error de acceso a base de datos ──────────────────────────────

    @Test
    public void testIT05ErrorAccesoBBDD() throws DataAccessException {
        when(mockInfo.cliente("ERRORTEST")).thenThrow(new DataAccessException());

        window.textBox("txtDNICliente").enterText("ERRORTEST");
        window.button("btnBuscar").click();

        window.textBox("txtNombreCliente").requireText("Error en BBDD");
    }

    // ── Datos de prueba ────────────────────────────────────────────────────

    private Cliente clienteJuan() {
        Seguro s1 = new Seguro();
        s1.setMatricula("1111AAA");
        s1.setCobertura(Cobertura.TERCEROS);
        s1.setPotencia(15);
        s1.setFechaInicio(LocalDate.of(2002, 1, 15));

        Seguro s2 = new Seguro();
        s2.setMatricula("1111BBB");
        s2.setCobertura(Cobertura.TODO_RIESGO);
        s2.setPotencia(20);
        s2.setFechaInicio(LocalDate.of(2016, 5, 20));
        s2.setConductorAdicional("Pepe");

        Seguro s3 = new Seguro();
        s3.setMatricula("1111CCC");
        s3.setCobertura(Cobertura.TERCEROS);
        s3.setPotencia(100);
        s3.setFechaInicio(LocalDate.of(2022, 5, 21));

        Cliente juan = new Cliente();
        juan.setNombre("Juan");
        juan.setDni("11111111A");
        juan.setMinusvalia(false);
        juan.setSeguros(Arrays.asList(s1, s2, s3));
        return juan;
    }
}
