/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Bancos;
import Entidades.CuentasBancos;
import Entidades.Inforeportes;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarCuentasBancosInterface {

    public void obtenerConexion(String idSesion);

    public void modificarCuentaBanco(List<CuentasBancos> listaModificar);

    public void borrarCuentaBanco(List<CuentasBancos> listaBorrar);

    public void crearCuentaBanco(List<CuentasBancos> listaCrear);

    public List<CuentasBancos> consultarCuentasBancos();

    public List<Bancos> consultarBancos();

    public List<Inforeportes> consultarInfoReportes();

}
