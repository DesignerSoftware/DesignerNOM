/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.SubCategorias;
import InterfaceAdministrar.AdministrarSubCategoriasInterface;
import InterfacePersistencia.PersistenciaSubCategoriasInterface;
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
public class AdministrarSubCategorias implements AdministrarSubCategoriasInterface {

   private static Logger log = Logger.getLogger(AdministrarSubCategorias.class);

   @EJB
   PersistenciaSubCategoriasInterface persistenciaSubCategorias;
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
   public void modificarSubCategorias(List<SubCategorias> listaSubCategorias) {
      try {
         for (int i = 0; i < listaSubCategorias.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaSubCategorias.editar(getEm(), listaSubCategorias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarSubCategorias(List<SubCategorias> listaSubCategorias) {
      try {
         for (int i = 0; i < listaSubCategorias.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaSubCategorias.borrar(getEm(), listaSubCategorias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearSubCategorias(List<SubCategorias> listaSubCategorias) {
      try {
         for (int i = 0; i < listaSubCategorias.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaSubCategorias.crear(getEm(), listaSubCategorias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<SubCategorias> consultarSubCategorias() {
      try {
         return persistenciaSubCategorias.consultarSubCategorias(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public SubCategorias consultarSubCategoria(BigInteger secSubCategorias) {
      try {
         return persistenciaSubCategorias.consultarSubCategoria(getEm(), secSubCategorias);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarEscalafones(BigInteger secSubCategorias) {
      try {
         return persistenciaSubCategorias.contarEscalafones(getEm(), secSubCategorias);
      } catch (Exception e) {
         log.error("ERROR AdministrarSubCategorias contarEscalafones ERROR : " + e);
         return null;
      }
   }
}
