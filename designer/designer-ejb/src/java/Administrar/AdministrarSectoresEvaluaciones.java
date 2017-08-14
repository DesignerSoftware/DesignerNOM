/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.SectoresEvaluaciones;
import InterfaceAdministrar.AdministrarSectoresEvaluacionesInterface;
import InterfacePersistencia.PersistenciaSectoresEvaluacionesInterface;
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
public class AdministrarSectoresEvaluaciones implements AdministrarSectoresEvaluacionesInterface {

   private static Logger log = Logger.getLogger(AdministrarSectoresEvaluaciones.class);

   @EJB
   PersistenciaSectoresEvaluacionesInterface persistenciaSectoresEvaluaciones;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em;

   private EntityManager getEm() {
      try {
         if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public void modificarSectoresEvaluaciones(List<SectoresEvaluaciones> listaSectoresEvaluaciones) {
      try {
         for (int i = 0; i < listaSectoresEvaluaciones.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaSectoresEvaluaciones.editar(getEm(), listaSectoresEvaluaciones.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarSectoresEvaluaciones(List<SectoresEvaluaciones> listaSectoresEvaluaciones) {
      try {
         for (int i = 0; i < listaSectoresEvaluaciones.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaSectoresEvaluaciones.borrar(getEm(), listaSectoresEvaluaciones.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearSectoresEvaluaciones(List<SectoresEvaluaciones> listaSectoresEvaluaciones) {
      try {
         for (int i = 0; i < listaSectoresEvaluaciones.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaSectoresEvaluaciones.crear(getEm(), listaSectoresEvaluaciones.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<SectoresEvaluaciones> consultarSectoresEvaluaciones() {
      try {
         return persistenciaSectoresEvaluaciones.consultarSectoresEvaluaciones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public SectoresEvaluaciones consultarSectorEvaluacion(BigInteger secSectoresEvaluaciones) {
      try {
         return persistenciaSectoresEvaluaciones.consultarSectorEvaluacion(getEm(), secSectoresEvaluaciones);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
