package es.unican.is2.seguros.business;

import es.unican.is2.seguros.common.Cliente;
import es.unican.is2.seguros.common.DataAccessException;
import es.unican.is2.seguros.common.IClientesDAO;
import es.unican.is2.seguros.common.IGestionClientes;
import es.unican.is2.seguros.common.IGestionSeguros;
import es.unican.is2.seguros.common.IInfoSeguros;
import es.unican.is2.seguros.common.ISegurosDAO;
import es.unican.is2.seguros.common.OperacionNoValida;
import es.unican.is2.seguros.common.Seguro;

public class GestionSeguros implements IGestionClientes, IGestionSeguros, IInfoSeguros {

    private final IClientesDAO clientesDao;
    private final ISegurosDAO segurosDao;

    public GestionSeguros(IClientesDAO clientesDao, ISegurosDAO segurosDao) {
        this.clientesDao = clientesDao;
        this.segurosDao = segurosDao;
    }

    @Override
    public Cliente nuevoCliente(Cliente c) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Cliente bajaCliente(String dni) throws OperacionNoValida, DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Seguro nuevoSeguro(Seguro s, String dni) throws OperacionNoValida, DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Seguro bajaSeguro(String matricula, String dni) throws OperacionNoValida, DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Seguro anhadeConductorAdicional(String matricula, String conductor) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Cliente cliente(String dni) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Seguro seguro(String matricula) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
