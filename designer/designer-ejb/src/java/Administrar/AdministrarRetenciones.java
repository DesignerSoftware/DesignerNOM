/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Retenciones;
import Entidades.VigenciasRetenciones;
import InterfaceAdministrar.AdministrarRetencionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaRetencionesInterface;
import InterfacePersistencia.PersistenciaVigenciasRetencionesInterface;
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
public class AdministrarRetenciones implements AdministrarRetencionesInterface {

   private static Logger log = Logger.getLogger(AdministrarRetenciones.class);

    @EJB
    PersistenciaRetencionesInterface persistenciaRetenciones;
    @EJB
    PersistenciaVigenciasRetencionesInterface persistenciaVigenciasRetenciones;
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
    public void borrarVigenciaRetencion(VigenciasRetenciones vretenciones) {
        persistenciaVigenciasRetenciones.borrar(em, vretenciones);
    }

    @Override
    public void crearVigenciaRetencion(VigenciasRetenciones vretenciones) {
        persistenciaVigenciasRetenciones.crear(em, vretenciones);
    }

    @Override
    public void modificarVigenciaRetencion(List<VigenciasRetenciones> listaVigenciasRetencionesModificar) {
        for (int i = 0; i < listaVigenciasRetencionesModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaVigenciasRetencionesModificar.get(i).getUvt() == null) {
                listaVigenciasRetencionesModificar.get(i).setUvt(null);
            }
            persistenciaVigenciasRetenciones.editar(em, listaVigenciasRetencionesModificar.get(i));
        }
    }

    @Override
    public List<VigenciasRetenciones> consultarVigenciasRetenciones() {
        try {
            List<VigenciasRetenciones> actual = persistenciaVigenciasRetenciones.buscarVigenciasRetenciones(em);
            return actual;
        } catch (Exception e) {
            log.warn("Error consultarVigenciasRetenciones: " + e.toString());
            return null;
        }
    }

//RETENCIONES
    @Override
    public void borrarRetencion(Retenciones retenciones) {
        persistenciaRetenciones.borrar(em, retenciones);
    }

    @Override
    public void crearRetencion(Retenciones retenciones) {
        persistenciaRetenciones.crear(em, retenciones);
    }

    @Override
    public void modificarRetencion(List<Retenciones> listaRetencionesModificar) {
        for (int i = 0; i < listaRetencionesModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaRetencionesModificar.get(i).getAdicionauvt()== null) {
                listaRetencionesModificar.get(i).setAdicionauvt(null);
            }
            persistenciaRetenciones.editar(em, listaRetencionesModificar.get(i));
        }
    }

    @Override
    public List<Retenciones> consultarRetenciones(BigInteger secRetencion) {
        try {
            List<Retenciones> actual = persistenciaRetenciones.buscarRetencionesVig(em, secRetencion);
            return actual;
        } catch (Exception e) {
            log.warn("Error conceptoActual Admi : " + e.toString());
            return null;
        }
    }

}
