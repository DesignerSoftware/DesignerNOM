/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Bancos;
import Entidades.CuentasBancos;
import Entidades.Inforeportes;
import InterfaceAdministrar.AdministrarCuentasBancosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCuentasBancosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
@Local
public class AdministrarCuentasBancos implements AdministrarCuentasBancosInterface {

   private static Logger log = Logger.getLogger(AdministrarCuentasBancos.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaCuentasBancosInterface persistenciaCuentasBancos;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void modificarCuentaBanco(List<CuentasBancos> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaCuentasBancos.editar(em, listaModificar.get(i));
        }
    }

    @Override
    public void borrarCuentaBanco(List<CuentasBancos> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaCuentasBancos.borrar(em, listaBorrar.get(i));
        }
    }

    @Override
    public void crearCuentaBanco(List<CuentasBancos> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaCuentasBancos.crear(em, listaCrear.get(i));
        }
    }

    @Override
    public List<CuentasBancos> consultarCuentasBancos() {
        List<CuentasBancos> listaCuentas = persistenciaCuentasBancos.buscarCuentasBanco(em);
        return listaCuentas;
    }

    @Override
    public List<Bancos> consultarBancos() {
        List<Bancos> listaBancos = persistenciaCuentasBancos.buscarBancos(em);
        return listaBancos;
    }

    @Override
    public List<Inforeportes> consultarInfoReportes() {
        List<Inforeportes> listaReportes = persistenciaCuentasBancos.buscarReportes(em);
        return listaReportes;
    }
}
