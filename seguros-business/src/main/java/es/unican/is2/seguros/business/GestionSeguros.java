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
        if (clientesDao.cliente(c.getDni()) != null) {
            return null;
        }
        return clientesDao.creaCliente(c);
    }

    @Override
    public Cliente bajaCliente(String dni) throws OperacionNoValida, DataAccessException {
        Cliente cliente = clientesDao.cliente(dni);
        if (cliente == null) {
            return null;
        }
        if (!cliente.getSeguros().isEmpty()) {
            throw new OperacionNoValida("El cliente tiene seguros asociados");
        }
        return clientesDao.eliminaCliente(dni);
    }

    @Override
    public Seguro nuevoSeguro(Seguro s, String dni) throws OperacionNoValida, DataAccessException {
        if (clientesDao.cliente(dni) == null) {
            return null;
        }
        if (segurosDao.seguroPorMatricula(s.getMatricula()) != null) {
            throw new OperacionNoValida("Ya existe un seguro para esa matricula");
        }
        return segurosDao.creaSeguro(s, dni);
    }

    @Override
    public Seguro bajaSeguro(String matricula, String dni) throws OperacionNoValida, DataAccessException {
        Cliente cliente = clientesDao.cliente(dni);
        if (cliente == null) {
            return null;
        }
        Seguro seguro = segurosDao.seguroPorMatricula(matricula);
        if (seguro == null) {
            return null;
        }
        if (!perteneceACliente(seguro, cliente)) {
            throw new OperacionNoValida("El seguro no pertenece al cliente indicado");
        }
        return segurosDao.eliminaSeguro(seguro.getId());
    }

    @Override
    public Seguro anhadeConductorAdicional(String matricula, String conductor) throws DataAccessException {
        Seguro seguro = segurosDao.seguroPorMatricula(matricula);
        if (seguro == null) {
            return null;
        }
        seguro.setConductorAdicional(conductor);
        return segurosDao.actualizaSeguro(seguro);
    }

    @Override
    public Cliente cliente(String dni) throws DataAccessException {
        return clientesDao.cliente(dni);
    }

    @Override
    public Seguro seguro(String matricula) throws DataAccessException {
        return segurosDao.seguroPorMatricula(matricula);
    }

    private boolean perteneceACliente(Seguro seguro, Cliente cliente) {
        for (Seguro seguroCliente : cliente.getSeguros()) {
            if (seguroCliente.getId() == seguro.getId()) {
                return true;
            }
        }
        return false;
    }
}
