/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Cargos;
import Entidades.Empresas;
import Entidades.FirmasReportes;
import Entidades.Personas;
import InterfaceAdministrar.AdministrarFirmasReportesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaFirmasReportesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
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
public class AdministrarFirmasReportes implements AdministrarFirmasReportesInterface {

   private static Logger log = Logger.getLogger(AdministrarFirmasReportes.class);

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaFirmasReportes'.
     */
    @EJB
    PersistenciaFirmasReportesInterface persistenciaFirmasReportes;
    @EJB
    PersistenciaCargosInterface persistenciaCargos;
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
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

    public void modificarFirmasReportes(List<FirmasReportes> listaFirmasReportes) {
        for (int i = 0; i < listaFirmasReportes.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaFirmasReportes.editar(em,listaFirmasReportes.get(i));
        }
    }

    public void borrarFirmasReportes(List<FirmasReportes> listaFirmasReportes) {
        for (int i = 0; i < listaFirmasReportes.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaFirmasReportes.borrar(em,listaFirmasReportes.get(i));
        }
    }

    public void crearFirmasReportes(List<FirmasReportes> listaFirmasReportes) {
        for (int i = 0; i < listaFirmasReportes.size(); i++) {
            log.warn("Administrar Creando...");
            log.warn("--------------DUPLICAR------------------------");
            log.warn("CODIGO : " + listaFirmasReportes.get(i).getCodigo());
            log.warn("NOMBRE: " + listaFirmasReportes.get(i).getDescripcion());
            log.warn("EMPRESA: " + listaFirmasReportes.get(i).getEmpresa().getNombre());
            log.warn("SUBTITULO : " + listaFirmasReportes.get(i).getSubtitulofirma());
            log.warn("PERSONA : " + listaFirmasReportes.get(i).getPersonafirma().getNombre());
            log.warn("CARGO : " + listaFirmasReportes.get(i).getCargofirma().getNombre());
            log.warn("--------------DUPLICAR------------------------");
            persistenciaFirmasReportes.crear(em,listaFirmasReportes.get(i));
        }
    }

    @Override
    public List<FirmasReportes> consultarFirmasReportes() {
        List<FirmasReportes> listaFirmasReportes;
        listaFirmasReportes = persistenciaFirmasReportes.consultarFirmasReportes(em);
        return listaFirmasReportes;
    }

    public FirmasReportes consultarTipoIndicador(BigInteger secMotivoDemanda) {
        FirmasReportes tiposIndicadores;
        tiposIndicadores = persistenciaFirmasReportes.consultarFirmaReporte(em,secMotivoDemanda);
        return tiposIndicadores;
    }

    public List<Cargos> consultarLOVCargos() {
        List<Cargos> listLOVCargos;
        listLOVCargos = persistenciaCargos.consultarCargos(em);
        return listLOVCargos;
    }

    public List<Personas> consultarLOVPersonas() {
        List<Personas> listLOVPersonas;
        listLOVPersonas = persistenciaPersonas.consultarPersonas(em);
        return listLOVPersonas;
    }

    public List<Empresas> consultarLOVEmpresas() {
        List<Empresas> listLOVEmpresas;
        listLOVEmpresas = persistenciaEmpresas.consultarEmpresas(em);
        return listLOVEmpresas;
    }

}
