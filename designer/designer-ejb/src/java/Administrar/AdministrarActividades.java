/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Actividades;
import InterfaceAdministrar.AdministrarActividadesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActividadesInterface;
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
public class AdministrarActividades implements AdministrarActividadesInterface {

   private static Logger log = Logger.getLogger(AdministrarActividades.class);
    

    @EJB
    PersistenciaActividadesInterface persistenciaActividades;
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

    public void modificarActividades(List<Actividades> listaActividades) {
        for (int i = 0; i < listaActividades.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaActividades.editar(em,listaActividades.get(i));
        }
    }

    public void borrarActividades(List<Actividades> listaActividades) {
        for (int i = 0; i < listaActividades.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaActividades.borrar(em,listaActividades.get(i));
        }
    }

    public void crearActividades(List<Actividades> listaActividades) {
        for (int i = 0; i < listaActividades.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaActividades.crear(em,listaActividades.get(i));
        }
    }

    public List<Actividades> consultarActividades() {
        List<Actividades> listActividades;
        listActividades = persistenciaActividades.buscarActividades(em);
        return listActividades;
    }

    public BigInteger contarBienNecesidadesActividad(BigInteger secuenciaActividades) {
        BigInteger contarParametrosInformesActividad = null;
        try {
            return contarParametrosInformesActividad = persistenciaActividades.contarBienNecesidadesActividad(em,secuenciaActividades);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARACTIVIDADES contarBienNecesidadesActividad ERROR :" + e);
            return null;
        }
    }
    public BigInteger contarParametrosInformesActividad(BigInteger secuenciaActividades) {
        BigInteger contarParametrosInformesActividad = null;
        try {
            return contarParametrosInformesActividad = persistenciaActividades.contarParametrosInformesActividad(em,secuenciaActividades);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARACTIVIDADES contarParametrosInformesActividad ERROR :" + e);
            return null;
        }
    }
}
