/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Novedades;
import Entidades.Periodicidades;
import Entidades.Terceros;
import InterfaceAdministrar.AdministrarEmplNovedadInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaNovedadesInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
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
public class AdministrarEmplNovedad implements AdministrarEmplNovedadInterface {

   private static Logger log = Logger.getLogger(AdministrarEmplNovedad.class);

    @EJB
    PersistenciaNovedadesInterface persistenciaNovedades;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaConceptosInterface persistenciaConceptos;
    @EJB
    PersistenciaPeriodicidadesInterface persistenciaPeriodicidad;
    @EJB
    PersistenciaTercerosInterface persistenciaTercero;
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

    @Override
    public List<Novedades> listNovedadesEmpleado(BigInteger secuenciaE) {
        try {
            List<Novedades> listNovedades = persistenciaNovedades.todasNovedadesEmpleado(em, secuenciaE);
            return listNovedades;
        } catch (Exception e) {
            log.warn("Error listNovedadesEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Empleados actualEmpleado(BigInteger secuencia) {
        try {
            Empleados empl = persistenciaEmpleado.buscarEmpleadoSecuencia(em, secuencia);
            return empl;
        } catch (Exception e) {
            log.warn("Error actualEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Conceptos> lovConceptos() {
        try {
            List<Conceptos> lovConceptos = persistenciaConceptos.buscarConceptos(em);
            return lovConceptos;
        } catch (Exception e) {
            log.warn("Error en lovConceptos:" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Periodicidades> lovPeriodicidades() {
        try {
            List<Periodicidades> lovPeriodicidades = persistenciaPeriodicidad.consultarPeriodicidades(em);
            return lovPeriodicidades;
        } catch (Exception e) {
            log.warn("Error en lovPeriodicidades:" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Terceros> lovTerceros() {
        try {
            List<Terceros> lovTerceros = persistenciaTercero.buscarTerceros(em);
            return lovTerceros;
        } catch (Exception e) {
            log.warn("Error en lovTerceros:" + e.getMessage());
            return null;
        }
    }

    @Override
    public void editarNovedad(List<Novedades> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            if(listaModificar.get(i).getTercero().getSecuencia() == null){
                listaModificar.get(i).setTercero(null);
            }
            
            if(listaModificar.get(i).getPeriodicidad().getSecuencia() == null){
                listaModificar.get(i).setPeriodicidad(null);
            }
            
            if(listaModificar.get(i).getConcepto() == null){
                listaModificar.get(i).setConcepto(new Conceptos());
            }
            persistenciaNovedades.editar(em, listaModificar.get(i));
        }
    }

    @Override
    public void borrarNovedad(List<Novedades> listaBorrar) {
        try{
            for (int i = 0; i < listaBorrar.size(); i++) {
                if (listaBorrar.get(i).getTercero().getSecuencia() == null) {
                    listaBorrar.get(i).setTercero(null);
                }

                if (listaBorrar.get(i).getPeriodicidad().getSecuencia() == null) {
                    listaBorrar.get(i).setPeriodicidad(null);
                }

                if (listaBorrar.get(i).getConcepto() == null) {
                    listaBorrar.get(i).setConcepto(new Conceptos());
                }
                persistenciaNovedades.borrar(em, listaBorrar.get(i));
            }
        }catch(Exception e){
            log.warn("error en BorrarNovedad" + e.getMessage() );
        }
    }

}
