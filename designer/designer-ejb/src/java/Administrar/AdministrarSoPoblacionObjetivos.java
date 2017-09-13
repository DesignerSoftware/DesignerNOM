/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.SoPoblacionObjetivos;
import InterfaceAdministrar.AdministrarSoPoblacionObjetivosInterface;
import InterfacePersistencia.PersistenciaSoPoblacionObjetivosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarSoPoblacionObjetivos implements AdministrarSoPoblacionObjetivosInterface {

   private static Logger log = Logger.getLogger(AdministrarSoPoblacionObjetivos.class);
   @EJB
   PersistenciaSoPoblacionObjetivosInterface persistenciaSoPoblacionObjetivos;
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

   public void modificarSoPoblacionObjetivos(List<SoPoblacionObjetivos> listSoPoblacionObjetivos) {
      try {
         for (int i = 0; i < listSoPoblacionObjetivos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaSoPoblacionObjetivos.editar(getEm(), listSoPoblacionObjetivos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarSoPoblacionObjetivos() ERROR: " + e);
      }
   }

   public void borrarSoPoblacionObjetivos(List<SoPoblacionObjetivos> listSoPoblacionObjetivos) {
      try {
         for (int i = 0; i < listSoPoblacionObjetivos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaSoPoblacionObjetivos.borrar(getEm(), listSoPoblacionObjetivos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarSoPoblacionObjetivos() ERROR: " + e);
      }
   }

   public void crearSoPoblacionObjetivos(List<SoPoblacionObjetivos> listSoPoblacionObjetivos) {
      try {
         for (int i = 0; i < listSoPoblacionObjetivos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaSoPoblacionObjetivos.crear(getEm(), listSoPoblacionObjetivos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearSoPoblacionObjetivos() ERROR: " + e);
      }
   }

   @Override
   public List<SoPoblacionObjetivos> consultarSoPoblacionObjetivos() {
      try {
         return persistenciaSoPoblacionObjetivos.consultarSoPoblacionObjetivos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarSoPoblacionObjetivos() ERROR: " + e);
         return null;
      }
   }

   public SoPoblacionObjetivos consultarEvalCompetencia(BigInteger secSoPoblacionObjetivo) {
      try {
         return persistenciaSoPoblacionObjetivos.buscarSoPoblacionObjetivo(getEm(), secSoPoblacionObjetivo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarEvalCompetencia() ERROR: " + e);
         return null;
      }
   }

}
