import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de ConjuntoOrdenado.
 * Caja negra: particion equivalente + AVL.
 * Caja blanca: cobertura de sentencias y decision/condicion.
 */
class ConjuntoOrdenadoTest {

    private ConjuntoOrdenado<Integer> sut;

    @BeforeEach
    void setUp() {
        sut = new ConjuntoOrdenado<>();
    }

    // =========================================================================
    // Método: size()
    // =========================================================================

    // CO01 - size sobre conjunto vacio
    @Test
    void testSizeConjuntoVacio() {
        assertEquals(0, sut.size());
    }

    // CO02 - size sobre conjunto con un elemento
    @Test
    void testSizeUnElemento() {
        sut.add(5);
        assertEquals(1, sut.size());
    }

    // CO03 - size sobre conjunto con varios elementos
    @Test
    void testSizeVariosElementos() {
        sut.add(3);
        sut.add(1);
        sut.add(2);
        assertEquals(3, sut.size());
    }

    // =========================================================================
    // Método: add(E elemento)
    // =========================================================================

    // CO04 - add elemento nulo lanza NullPointerException
    @Test
    void testAddNullLanzaExcepcion() {
        assertThrows(NullPointerException.class, () -> sut.add(null));
    }

    // CO05 - add primer elemento en conjunto vacio, devuelve true
    @Test
    void testAddPrimerElemento() {
        assertTrue(sut.add(5));
        assertEquals(1, sut.size());
    }

    // CO06 - add elemento menor que el existente (va al principio)
    @Test
    void testAddElementoMenorAlPrincipio() {
        sut.add(5);
        assertTrue(sut.add(2));
        assertEquals(2, sut.get(0));
        assertEquals(5, sut.get(1));
    }

    // CO07 - add elemento mayor que el existente (va al final)
    @Test
    void testAddElementoMayorAlFinal() {
        sut.add(5);
        assertTrue(sut.add(8));
        assertEquals(5, sut.get(0));
        assertEquals(8, sut.get(1));
    }

    // CO08 - add elemento en medio, orden correcto
    @Test
    void testAddElementoEnMedio() {
        sut.add(1);
        sut.add(5);
        sut.add(3);
        assertEquals(1, sut.get(0));
        assertEquals(3, sut.get(1));
        assertEquals(5, sut.get(2));
    }

    // CO09 - add elemento duplicado devuelve false y no lo inserta
    @Test
    void testAddDuplicadoDevuelveFalse() {
        sut.add(5);
        assertFalse(sut.add(5));
        assertEquals(1, sut.size());
    }

    // CO10 - add varios elementos en orden descendente, resultado ascendente
    @Test
    void testAddOrdenAscendenteResultante() {
        sut.add(10);
        sut.add(3);
        sut.add(7);
        sut.add(1);
        assertEquals(1,  sut.get(0));
        assertEquals(3,  sut.get(1));
        assertEquals(7,  sut.get(2));
        assertEquals(10, sut.get(3));
    }

    // =========================================================================
    // Método: get(int index)
    // =========================================================================

    // CO11 - get indice valido (primero)
    @Test
    void testGetPrimerElemento() {
        sut.add(3);
        sut.add(1);
        assertEquals(1, sut.get(0));
    }

    // CO12 - get indice valido (ultimo)
    @Test
    void testGetUltimoElemento() {
        sut.add(1);
        sut.add(3);
        assertEquals(3, sut.get(1));
    }

    // CO13 - get indice negativo lanza IndexOutOfBoundsException (AVL)
    @Test
    void testGetIndiceNegativo() {
        sut.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> sut.get(-1));
    }

    // CO14 - get indice igual a size (limite superior invalido, AVL)
    @Test
    void testGetIndiceFueraDeLimite() {
        sut.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> sut.get(1));
    }

    // CO15 - get sobre conjunto vacio lanza IndexOutOfBoundsException
    @Test
    void testGetConjuntoVacio() {
        assertThrows(IndexOutOfBoundsException.class, () -> sut.get(0));
    }

    // =========================================================================
    // Método: remove(int index)
    // =========================================================================

    // CO16 - remove indice valido devuelve elemento eliminado
    @Test
    void testRemoveIndiceValido() {
        sut.add(1);
        sut.add(3);
        assertEquals(1, sut.remove(0));
        assertEquals(1, sut.size());
    }

    // CO17 - remove unico elemento, conjunto queda vacio
    @Test
    void testRemoveUnicoElemento() {
        sut.add(5);
        sut.remove(0);
        assertEquals(0, sut.size());
    }

    // CO18 - remove indice negativo lanza IndexOutOfBoundsException (AVL)
    @Test
    void testRemoveIndiceNegativo() {
        sut.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> sut.remove(-1));
    }

    // CO19 - remove indice igual a size (limite superior invalido, AVL)
    @Test
    void testRemoveIndiceFueraDeLimite() {
        sut.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> sut.remove(1));
    }

    // CO20 - remove sobre conjunto vacio lanza IndexOutOfBoundsException
    @Test
    void testRemoveConjuntoVacio() {
        assertThrows(IndexOutOfBoundsException.class, () -> sut.remove(0));
    }

    // =========================================================================
    // Método: clear()
    // =========================================================================

    // CO21 - clear sobre conjunto con elementos, queda vacio
    @Test
    void testClearConElementos() {
        sut.add(1);
        sut.add(2);
        sut.clear();
        assertEquals(0, sut.size());
    }

    // CO22 - clear sobre conjunto vacio, no lanza excepcion
    @Test
    void testClearConjuntoVacio() {
        assertDoesNotThrow(() -> sut.clear());
        assertEquals(0, sut.size());
    }

    // =========================================================================
    // Casos adicionales de caja blanca
    // =========================================================================

    // CO23 - add duplicado al principio (cubre rama == 0 con indice=0)
    @Test
    void testAddDuplicadoPrimerElemento() {
        sut.add(1);
        sut.add(3);
        assertFalse(sut.add(1));
        assertEquals(2, sut.size());
    }

    // CO24 - add duplicado al final (cubre rama == 0 con indice=size-1)
    @Test
    void testAddDuplicadoUltimoElemento() {
        sut.add(1);
        sut.add(3);
        assertFalse(sut.add(3));
        assertEquals(2, sut.size());
    }
}
