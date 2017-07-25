/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Administrar;

import Entidades.RetencionesMinimas;
import Entidades.VigenciasRetencionesMinimas;
import InterfaceAdministrar.AdministrarRetencionesMinimasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaRetencionesMinimasInterface;
import InterfacePersistencia.PersistenciaVigenciasRetencionesMinimasInterface;
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
public class AdministrarRetencionesMinimas implements AdministrarRetencionesMinimasInterface {

   private static Logger log = Logger.getLogger(AdministrarRetencionesMinimas.class);

    @EJB
    PersistenciaRetencionesMinimasInterface persistenciaRetencionesMinimas;
    @EJB
    PersistenciaVigenciasRetencionesMinimasInterface persistenciaVigenciasRetencionesMinimas;
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
    
//VIGENCIAS RETENCIONES
    @Override
    public void borrarVigenciaRetencion(VigenciasRetencionesMinimas vretenciones) {
        persistenciaVigenciasRetencionesMinimas.borrar(em, vretenciones);
    }

    @Override
    public void crearVigenciaRetencion(VigenciasRetencionesMinimas vretenciones) {
        persistenciaVigenciasRetencionesMinimas.crear(em, vretenciones);
    }

    @Override
    public void modificarVigenciaRetencion(List<VigenciasRetencionesMinimas> listaVigenciasRetencionesModificar) {
        for (int i = 0; i < listaVigenciasRetencionesModificar.size(); i++) {
            log.warn("Modificando...");
            persistenciaVigenciasRetencionesMinimas.editar(em, listaVigenciasRetencionesModificar.get(i));
        }
    }

    @Override
    public List<VigenciasRetencionesMinimas> consultarVigenciasRetenciones() {
        try {
            List<VigenciasRetencionesMinimas> actual = persistenciaVigenciasRetencionesMinimas.buscarVigenciasRetencionesMinimas(em);
            return actual;
        } catch (Exception e) {
            log.warn("Error consultarVigenciasRetenciones: " + e.toString());
            return null;
        }
    }

//RETENCIONES
    @Override
    public void borrarRetencion(RetencionesMinimas retenciones) {
        persistenciaRetencionesMinimas.borrar(em, retenciones);
    }

    @Override
    public void crearRetencion(RetencionesMinimas retenciones) {
        persistenciaRetencionesMinimas.crear(em, retenciones);
    }

    @Override
    public void modificarRetencion(List<RetencionesMinimas> listaRetencionesModificar) {
        for (int i = 0; i < listaRetencionesModificar.size(); i++) {
            log.warn("Modificando...");
            
            persistenciaRetencionesMinimas.editar(em,listaRetencionesModificar.get(i));
        }
    }

    @Override
    public List<RetencionesMinimas> consultarRetenciones(BigInteger secRetencion) {
        try {
            List<RetencionesMinimas> actual = persistenciaRetencionesMinimas.buscarRetencionesMinimasVig(em, secRetencion);
            return actual;
        } catch (Exception e) {
            log.warn("Error consultarRetenciones Admi : " + e.toString());
            return null;
        }
    }
}
