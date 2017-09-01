/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Eventos;
import Entidades.VigenciasEventos;
import InterfaceAdministrar.AdministrarEmplVigenciaEventoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEventosInterface;
import InterfacePersistencia.PersistenciaVigenciasEventosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarEmplVigenciaEvento implements AdministrarEmplVigenciaEventoInterface {

   private static Logger log = Logger.getLogger(AdministrarEmplVigenciaEvento.class);

   @EJB
   PersistenciaEventosInterface persistenciaEventos;
   @EJB
   PersistenciaVigenciasEventosInterface persistenciaVigenciasEventos;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em; private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) { if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         } else {
            this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<VigenciasEventos> listVigenciasEventosEmpleado(BigInteger secuencia) {
      try {
         List<VigenciasEventos> vigencias = persistenciaVigenciasEventos.vigenciasEventosSecuenciaEmpleado(getEm(), secuencia);
         return vigencias;
      } catch (Exception e) {
         log.warn("Error listVigenciasEventosEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasEventos(List<VigenciasEventos> listaV) {
      try {
         for (int i = 0; i < listaV.size(); i++) {
            if (listaV.get(i).getEvento().getSecuencia() == null) {
               listaV.get(i).setEvento(null);
            }
            persistenciaVigenciasEventos.crear(getEm(), listaV.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearVigenciasEventos Admi : " + e.toString());
      }
   }

   @Override
   public void editarVigenciasEventos(List<VigenciasEventos> listaV) {
      try {
         for (int i = 0; i < listaV.size(); i++) {
            if (listaV.get(i).getEvento().getSecuencia() == null) {
               listaV.get(i).setEvento(null);
            }
            persistenciaVigenciasEventos.editar(getEm(), listaV.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarVigenciasEventos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasEventos(List<VigenciasEventos> listaV) {
      try {
         for (int i = 0; i < listaV.size(); i++) {
            if (listaV.get(i).getEvento().getSecuencia() == null) {
               listaV.get(i).setEvento(null);
            }
            persistenciaVigenciasEventos.borrar(getEm(), listaV.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarVigenciasEventos Admi : " + e.toString());
      }
   }

   @Override
   public List<Eventos> listEventos() {
      try {
         return persistenciaEventos.buscarEventos(getEm());
      } catch (Exception e) {
         log.warn("Error listEventos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Empleados empleadoActual(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleado(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error empleadoActual Admi : " + e.toString());
         return null;
      }
   }

}
