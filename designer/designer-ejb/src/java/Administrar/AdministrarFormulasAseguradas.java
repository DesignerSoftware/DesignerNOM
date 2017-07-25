/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Formulas;
import Entidades.FormulasAseguradas;
import Entidades.Periodicidades;
import Entidades.Procesos;
import InterfaceAdministrar.AdministrarFormulasAseguradasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFormulasAseguradasInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
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
public class AdministrarFormulasAseguradas implements AdministrarFormulasAseguradasInterface {

   private static Logger log = Logger.getLogger(AdministrarFormulasAseguradas.class);

    //-------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaFormulasAseguradas'.
     */
    @EJB
    PersistenciaFormulasAseguradasInterface persistenciaFormulasAseguradas;
    @EJB
    PersistenciaFormulasInterface persistenciaFormulas;
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
    @EJB
    PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;

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

    public void modificarFormulasAseguradas(List<FormulasAseguradas> listaFormulasAseguradas) {
        for (int i = 0; i < listaFormulasAseguradas.size(); i++) {
            log.warn("Administrar Modificando...");
            if (listaFormulasAseguradas.get(i).getPeriodicidad().getSecuencia() == null) {
                listaFormulasAseguradas.get(i).setPeriodicidad(null);
            }
            persistenciaFormulasAseguradas.editar(em, listaFormulasAseguradas.get(i));
        }
    }

    public void borrarFormulasAseguradas(List<FormulasAseguradas> listaFormulasAseguradas) {
        for (int i = 0; i < listaFormulasAseguradas.size(); i++) {
            log.warn("Administrar Borrando...");
            if (listaFormulasAseguradas.get(i).getPeriodicidad().getSecuencia() == null) {
                listaFormulasAseguradas.get(i).setPeriodicidad(null);
            }
            persistenciaFormulasAseguradas.borrar(em, listaFormulasAseguradas.get(i));
        }
    }

    public void crearFormulasAseguradas(List<FormulasAseguradas> listaFormulasAseguradas) {
        for (int i = 0; i < listaFormulasAseguradas.size(); i++) {
            if (listaFormulasAseguradas.get(i).getPeriodicidad().getSecuencia() == null) {
                listaFormulasAseguradas.get(i).setPeriodicidad(null);
            }
            persistenciaFormulasAseguradas.crear(em, listaFormulasAseguradas.get(i));
        }
    }

    @Override
    public List<FormulasAseguradas> consultarFormulasAseguradas() {
        List<FormulasAseguradas> listaFormulasAseguradas;
        listaFormulasAseguradas = persistenciaFormulasAseguradas.consultarFormulasAseguradas(em);
        return listaFormulasAseguradas;
    }

    @Override
    public List<Formulas> consultarLOVFormulas() {
        List<Formulas> listLOVFormulas;
        listLOVFormulas = persistenciaFormulas.buscarFormulas(em);
        return listLOVFormulas;
    }

    public List<Procesos> consultarLOVProcesos() {
        List<Procesos> listLOVFormulas;
        listLOVFormulas = persistenciaProcesos.buscarProcesos(em);
        return listLOVFormulas;
    }

    public List<Periodicidades> consultarLOVPPeriodicidades() {
        List<Periodicidades> listLOVFormulas;
        listLOVFormulas = persistenciaPeriodicidades.consultarPeriodicidades(em);
        return listLOVFormulas;
    }

}
