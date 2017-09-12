/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EvalActividades;
import InterfaceAdministrar.AdministrarEvalActividadesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEvalActividadesInterface;
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
public class AdministrarEvalActividades implements AdministrarEvalActividadesInterface {

   private static Logger log = Logger.getLogger(AdministrarEvalActividades.class);

   @EJB
   PersistenciaEvalActividadesInterface persistenciaEvalActividades;
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

   public void modificarEvalActividades(List<EvalActividades> listaEvalActividades) {
      for (int i = 0; i < listaEvalActividades.size(); i++) {
         log.warn("Administrar Modificando...");
         persistenciaEvalActividades.editar(getEm(), listaEvalActividades.get(i));
      }
   }

   public void borrarEvalActividades(List<EvalActividades> listaEvalActividades) {
      try {
         for (int i = 0; i < listaEvalActividades.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaEvalActividades.borrar(getEm(), listaEvalActividades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarEvalActividades() ERROR: " + e);
      }
   }

   public void crearEvalActividades(List<EvalActividades> listaEvalActividades) {
      try {
         for (int i = 0; i < listaEvalActividades.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaEvalActividades.crear(getEm(), listaEvalActividades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearEvalActividades() ERROR: " + e);
      }
   }

   public List<EvalActividades> consultarEvalActividades() {
      try {
         return persistenciaEvalActividades.consultarEvalActividades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarEvalActividades() ERROR: " + e);
         return null;
      }
   }

   public EvalActividades consultarEvalActividad(BigInteger secEvalActividades) {
      try {
         return persistenciaEvalActividades.consultarEvalActividad(getEm(), secEvalActividades);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarEvalActividad() ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarCapBuzonesEvalActividad(BigInteger secEvalActividades) {
      try {
         return persistenciaEvalActividades.contarCapBuzonesEvalActividad(getEm(), secEvalActividades);
      } catch (Exception e) {
         log.error("ERROR AdministrarEvalActividades contarCapBuzonesEvalActividad ERROR : " + e);
         return null;
      }
   }

   public BigInteger contarCapNecesidadesEvalActividad(BigInteger secEvalActividades) {
      try {
         return persistenciaEvalActividades.contarCapNecesidadesEvalActividad(getEm(), secEvalActividades);
      } catch (Exception e) {
         log.error("ERROR AdministrarEvalActividades contarCapNecesidadesEvalActividad ERROR : " + e);
         return null;
      }
   }

   public BigInteger contarEvalPlanesDesarrollosEvalActividad(BigInteger secEvalActividades) {
      try {
         return persistenciaEvalActividades.contarEvalPlanesDesarrollosEvalActividad(getEm(), secEvalActividades);
      } catch (Exception e) {
         log.error("ERROR AdministrarEvalActividades contarEvalPlanesDesarrollosEvalActividad ERROR : " + e);
         return null;
      }
   }

}
