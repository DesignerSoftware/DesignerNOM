/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Eventos;
import InterfaceAdministrar.AdministrarEventosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEventosInterface;
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
public class AdministrarEventos implements AdministrarEventosInterface {

   private static Logger log = Logger.getLogger(AdministrarEventos.class);

    @EJB
    PersistenciaEventosInterface persistenciaEventos;

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
    public void modificarEventos(List<Eventos> listaEventos) {
        for (int i = 0; i < listaEventos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaEventos.editar(em,listaEventos.get(i));
        }
    }

    @Override
    public void borrarEventos(List<Eventos> listaEventos) {
        for (int i = 0; i < listaEventos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaEventos.borrar(em,listaEventos.get(i));
        }
    }

    @Override
    public void crearEventos(List<Eventos> listaEventos) {
        for (int i = 0; i < listaEventos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaEventos.crear(em,listaEventos.get(i));
        }
    }

    @Override
    public List<Eventos> consultarEventos() {
        List<Eventos> listEventos;
        listEventos = persistenciaEventos.buscarEventos(em);
        return listEventos;
    }

    @Override
    public Eventos consultarEvento(BigInteger secDeportes) {
        Eventos eventos;
        eventos = persistenciaEventos.buscarEvento(em,secDeportes);
        return eventos;
    }

    @Override
    public BigInteger verificarVigenciasEventos(BigInteger secuenciaEventos) {
        BigInteger verificadorVigenciasEventos = null;
        try {
            log.error("Secuencia VigenciasEventos " + secuenciaEventos);
            verificadorVigenciasEventos = persistenciaEventos.contadorVigenciasEventos(em,secuenciaEventos);
        } catch (Exception e) {
            log.error("ERROR AdministrarEventos VigenciasEstadoCiviles ERROR :" + e);
        } finally {
            return verificadorVigenciasEventos;
        }
    }

}
