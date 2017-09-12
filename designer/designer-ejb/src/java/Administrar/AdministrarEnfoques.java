/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Enfoques;
import InterfaceAdministrar.AdministrarEnfoquesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEnfoquesInterface;
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
public class AdministrarEnfoques implements AdministrarEnfoquesInterface {

   private static Logger log = Logger.getLogger(AdministrarEnfoques.class);

   @EJB
   PersistenciaEnfoquesInterface PersistenciaEnfoques;
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

   public void modificarEnfoques(List<Enfoques> listEnfoques) {
      try {
         for (int i = 0; i < listEnfoques.size(); i++) {
            log.warn("Administrar Modificando...");
            PersistenciaEnfoques.editar(getEm(), listEnfoques.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarEnfoques() ERROR: " + e);
      }
   }

   public void borrarEnfoques(List<Enfoques> listEnfoques) {
      try {
         for (int i = 0; i < listEnfoques.size(); i++) {
            log.warn("Administrar Borrando...");
            PersistenciaEnfoques.borrar(getEm(), listEnfoques.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarEnfoques() ERROR: " + e);
      }
   }

   public void crearEnfoques(List<Enfoques> listEnfoques) {
      try {
         for (int i = 0; i < listEnfoques.size(); i++) {
            log.warn("Administrar Creando...");
            PersistenciaEnfoques.crear(getEm(), listEnfoques.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearEnfoques() ERROR: " + e);
      }
   }

   public List<Enfoques> consultarEnfoques() {
      try {
         return PersistenciaEnfoques.buscarEnfoques(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarEnfoques() ERROR: " + e);
         return null;
      }
   }

   public Enfoques consultarEnfoque(BigInteger secEnfoques) {
      try {
         return PersistenciaEnfoques.buscarEnfoque(getEm(), secEnfoques);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarEnfoque() ERROR: " + e);
         return null;
      }
   }

   public BigInteger verificarTiposDetalles(BigInteger secuenciaTiposAuxilios) {
      try {
         return PersistenciaEnfoques.contadorTiposDetalles(getEm(), secuenciaTiposAuxilios);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRAREVALPLANILLAS verificarTiposDetalles ERROR :" + e);
         return null;
      }
   }
}
