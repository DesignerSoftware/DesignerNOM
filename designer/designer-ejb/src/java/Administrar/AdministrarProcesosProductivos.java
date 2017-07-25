/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.CentrosCostos;
import Entidades.ProcesosProductivos;
import InterfaceAdministrar.AdministrarProcesosProductivosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaProcesosProductivosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarProcesosProductivos implements AdministrarProcesosProductivosInterface {

   private static Logger log = Logger.getLogger(AdministrarProcesosProductivos.class);

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaProcesosProductivos'.
     */
    @EJB
    PersistenciaProcesosProductivosInterface persistenciaProcesosProductivos;
    @EJB
    PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
        /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------

    public void modificarProcesosProductivos(List<ProcesosProductivos> listaProcesosProductivos) {
        for (int i = 0; i < listaProcesosProductivos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaProcesosProductivos.editar(em, listaProcesosProductivos.get(i));
        }
    }

    public void borrarProcesosProductivos(List<ProcesosProductivos> listaProcesosProductivos) {
        for (int i = 0; i < listaProcesosProductivos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaProcesosProductivos.borrar(em, listaProcesosProductivos.get(i));
        }
    }

    public void crearProcesosProductivos(List<ProcesosProductivos> listaProcesosProductivos) {
        for (int i = 0; i < listaProcesosProductivos.size(); i++) {
            persistenciaProcesosProductivos.crear(em, listaProcesosProductivos.get(i));
        }
    }

    @Override
    public List<ProcesosProductivos> consultarProcesosProductivos() {
        List<ProcesosProductivos> listaProcesosProductivos;
        listaProcesosProductivos = persistenciaProcesosProductivos.consultarProcesosProductivos(em);
        return listaProcesosProductivos;
    }

    public List<CentrosCostos> consultarLOVCentrosCostos() {
        List<CentrosCostos> listLOVCentrosCostos;
        listLOVCentrosCostos = persistenciaCentrosCostos.buscarCentrosCostos(em);
        return listLOVCentrosCostos;
    }

    public BigInteger contarCargosProcesoProductivo(BigInteger secuenciaSucursal) {
        BigInteger contarVigenciasFormasPagosSucursal;
        contarVigenciasFormasPagosSucursal = persistenciaProcesosProductivos.contarCargosProcesoProductivo(em, secuenciaSucursal);
        return contarVigenciasFormasPagosSucursal;
    }

    public BigInteger contarTarifasProductosProcesoProductivo(BigInteger secuenciaSucursal) {
        BigInteger contarVigenciasFormasPagosSucursal;
        contarVigenciasFormasPagosSucursal = persistenciaProcesosProductivos.contarTarifasProductosProcesoProductivo(em, secuenciaSucursal);
        return contarVigenciasFormasPagosSucursal;
    }

    public BigInteger contarUnidadesProducidasProcesoProductivo(BigInteger secuenciaSucursal) {
        BigInteger contarVigenciasFormasPagosSucursal;
        contarVigenciasFormasPagosSucursal = persistenciaProcesosProductivos.contarUnidadesProducidasProcesoProductivo(em, secuenciaSucursal);
        return contarVigenciasFormasPagosSucursal;
    }
}
