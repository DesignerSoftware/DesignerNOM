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
import javax.persistence.EntityManagerFactory;
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
   public void modificarEventos(List<Eventos> listaEventos) {
      try {
         for (int i = 0; i < listaEventos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaEventos.editar(getEm(), listaEventos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarEventos(List<Eventos> listaEventos) {
      try {
         for (int i = 0; i < listaEventos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaEventos.borrar(getEm(), listaEventos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearEventos(List<Eventos> listaEventos) {
      try {
         for (int i = 0; i < listaEventos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaEventos.crear(getEm(), listaEventos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<Eventos> consultarEventos() {
      try {
         return persistenciaEventos.buscarEventos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Eventos consultarEvento(BigInteger secDeportes) {
      try {
         return persistenciaEventos.buscarEvento(getEm(), secDeportes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarVigenciasEventos(BigInteger secuenciaEventos) {
      try {
         log.error("Secuencia VigenciasEventos " + secuenciaEventos);
         return persistenciaEventos.contadorVigenciasEventos(getEm(), secuenciaEventos);
      } catch (Exception e) {
         log.error("ERROR AdministrarEventos VigenciasEstadoCiviles ERROR :" + e);
         return null;
      }
   }

}
