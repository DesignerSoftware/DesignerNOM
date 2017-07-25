/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Bancos;
import Entidades.Ciudades;
import Entidades.Empresas;
import Entidades.EmpresasBancos;
import InterfaceAdministrar.AdministrarEmpresasBancosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaBancosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaEmpresasBancosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarEmpresasBancos implements AdministrarEmpresasBancosInterface {

   private static Logger log = Logger.getLogger(AdministrarEmpresasBancos.class);

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaEmpresasBancos'.
     */
    @EJB
    PersistenciaEmpresasBancosInterface persistenciaEmpresasBancos;
    @EJB
    PersistenciaBancosInterface persistenciaBancos;
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;
    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public void modificarEmpresasBancos(List<EmpresasBancos> listaEmpresasBancos) {
        for (int i = 0; i < listaEmpresasBancos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaEmpresasBancos.editar(em,listaEmpresasBancos.get(i));
        }
    }

    public void borrarEmpresasBancos(List<EmpresasBancos> listaEmpresasBancos) {
        for (int i = 0; i < listaEmpresasBancos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaEmpresasBancos.borrar(em,listaEmpresasBancos.get(i));
        }
    }

    public void crearEmpresasBancos(List<EmpresasBancos> listaEmpresasBancos) {
        for (int i = 0; i < listaEmpresasBancos.size(); i++) {
            persistenciaEmpresasBancos.crear(em,listaEmpresasBancos.get(i));
        }
    }

    public List<EmpresasBancos> consultarEmpresasBancos() {
        List<EmpresasBancos> listaEmpresasBancos;
        listaEmpresasBancos = persistenciaEmpresasBancos.consultarEmpresasBancos(em);
        return listaEmpresasBancos;
    }

    public EmpresasBancos consultarTipoIndicador(BigInteger secMotivoDemanda) {
        EmpresasBancos tiposIndicadores;
        tiposIndicadores = persistenciaEmpresasBancos.consultarFirmaReporte(em,secMotivoDemanda);
        return tiposIndicadores;
    }

    public List<Bancos> consultarLOVBancos() {
        List<Bancos> listLOVBancos;
        listLOVBancos = persistenciaBancos.buscarBancos(em);
        return listLOVBancos;
    }

    public List<Ciudades> consultarLOVCiudades() {
        List<Ciudades> listLOVCiudades;
        listLOVCiudades = persistenciaCiudades.consultarCiudades(em);
        return listLOVCiudades;
    }

    public List<Empresas> consultarLOVEmpresas() {
        List<Empresas> listLOVEmpresas;
        listLOVEmpresas = persistenciaEmpresas.consultarEmpresas(em);
        return listLOVEmpresas;
    }
}
