import java.util.ArrayList;
import java.util.List;

import es.unican.is2.adt.IConjuntoOrdenado;

/**
 * Clase que implementa el ADT ConjuntoOrdenado
 */
public class ConjuntoOrdenado<E extends Comparable<E>> implements IConjuntoOrdenado<E> {

    private List<E> lista = new ArrayList<E>();

    public E get(int indice) {
        return lista.get(indice);
    }

    public boolean add(E elemento) {
        int indice = 0;
        if (elemento == null)
            throw new NullPointerException();
        if (lista.size() != 0) {
            // BUG CORREGIDO 1: era < 0, debe ser > 0 para orden ascendente
            while (indice < lista.size() && elemento.compareTo(lista.get(indice)) > 0) {
                indice++;
            }
            // BUG CORREGIDO 2: devolver false si el elemento ya existe
            if (indice < lista.size() && elemento.compareTo(lista.get(indice)) == 0) {
                return false;
            }
        }
        lista.add(indice, elemento);
        return true;
    }

    public E remove(int indice) {
        E borrado = lista.remove(indice);
        return borrado;
    }

    public int size() {
        return lista.size();
    }

    public void clear() {
        lista.clear();
    }

}