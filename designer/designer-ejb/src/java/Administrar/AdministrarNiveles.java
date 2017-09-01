/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarNivelesInterface;
import Entidades.Niveles;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaNivelesInterface;
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
public class AdministrarNiveles implements AdministrarNivelesInterface {

   private static Logger log = Logger.getLogger(AdministrarNiveles.class);

   @EJB
   PersistenciaNivelesInterface persistenciaNiveles;
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

   public void modificarNiveles(List<Niveles> listaNiveles) {
      try {
         for (int i = 0; i < listaNiveles.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaNiveles.editar(getEm(), listaNiveles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarNiveles(List<Niveles> listaNiveles) {
      try {
         for (int i = 0; i < listaNiveles.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaNiveles.borrar(getEm(), listaNiveles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearNiveles(List<Niveles> listaNiveles) {
      try {
         for (int i = 0; i < listaNiveles.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaNiveles.crear(getEm(), listaNiveles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<Niveles> consultarNiveles() {
      try {
         return persistenciaNiveles.consultarNiveles(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public Niveles consultarNivel(BigInteger secNiveles) {
      try {
         return persistenciaNiveles.consultarNivel(getEm(), secNiveles);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarEvalConvocatoriasNivel(BigInteger secNiveles) {
      try {
         return persistenciaNiveles.contarEvalConvocatoriasNivel(getEm(), secNiveles);
      } catch (Exception e) {
         log.error("ERROR AdministrarNiveles contarEvalConvocatoriasNivel ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarPlantasNivel(BigInteger secNiveles) {
      try {
         return persistenciaNiveles.contarPlantasNivel(getEm(), secNiveles);
      } catch (Exception e) {
         log.error("ERROR AdministrarNiveles contarPlantasNivel ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarPlantasPersonalesNivel(BigInteger secNiveles) {
      try {
         return persistenciaNiveles.contarPlantasPersonalesNivel(getEm(), secNiveles);
      } catch (Exception e) {
         log.error("ERROR AdministrarNiveles verificarBorradoVNE ERROR :" + e);
         return null;
      }
   }
}
