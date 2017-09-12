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
import javax.persistence.EntityManagerFactory;
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

   public void modificarActividades(List<Actividades> listaActividades) {
      try {
         for (int i = 0; i < listaActividades.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaActividades.editar(getEm(), listaActividades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarActividades() ERROR: " + e);
      }
   }

   public void borrarActividades(List<Actividades> listaActividades) {
      try {
         for (int i = 0; i < listaActividades.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaActividades.borrar(getEm(), listaActividades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarActividades() ERROR: " + e);
      }
   }

   public void crearActividades(List<Actividades> listaActividades) {
      try {
         for (int i = 0; i < listaActividades.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaActividades.crear(getEm(), listaActividades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearActividades() ERROR: " + e);
      }
   }

   public List<Actividades> consultarActividades() {
      try {
         return persistenciaActividades.buscarActividades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarActividades() ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarBienNecesidadesActividad(BigInteger secuenciaActividades) {
      try {
         return persistenciaActividades.contarBienNecesidadesActividad(getEm(), secuenciaActividades);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARACTIVIDADES contarBienNecesidadesActividad ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarParametrosInformesActividad(BigInteger secuenciaActividades) {
      try {
         return persistenciaActividades.contarParametrosInformesActividad(getEm(), secuenciaActividades);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARACTIVIDADES contarParametrosInformesActividad ERROR :" + e);
         return null;
      }
   }
}
