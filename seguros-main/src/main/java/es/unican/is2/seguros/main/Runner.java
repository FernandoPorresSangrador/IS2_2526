
package es.unican.is2.seguros.main;

import es.unican.is2.seguros.business.GestionSeguros;
import es.unican.is2.seguros.common.IClientesDAO;
import es.unican.is2.seguros.common.ISegurosDAO;
import es.unican.is2.seguros.daoh2.ClientesDAO;
import es.unican.is2.seguros.daoh2.SegurosDAO;
import es.unican.is2.seguros.gui.VistaAgente;

public class Runner {

	public static void main(String[] args) {
		IClientesDAO daoClientes = new ClientesDAO();
		ISegurosDAO daoSeguros = new SegurosDAO();
		GestionSeguros negocio = new GestionSeguros(daoClientes, daoSeguros);
		VistaAgente vista = new VistaAgente(negocio, negocio, negocio);
		vista.setVisible(true);
	}

}
