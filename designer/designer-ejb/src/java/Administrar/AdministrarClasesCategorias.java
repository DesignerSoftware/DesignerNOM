/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ClasesCategorias;
import InterfaceAdministrar.AdministrarClasesCategoriasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaClasesCategoriasInterface;
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
public class AdministrarClasesCategorias implements AdministrarClasesCategoriasInterface {

   private static Logger log = Logger.getLogger(AdministrarClasesCategorias.class);

   @EJB
   PersistenciaClasesCategoriasInterface persistenciaClasesCategorias;
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
   public void modificarClasesCategorias(List<ClasesCategorias> listaClasesCategorias) {
      try {
         for (int i = 0; i < listaClasesCategorias.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaClasesCategorias.editar(getEm(), listaClasesCategorias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarClasesCategorias(List<ClasesCategorias> listaClasesCategorias) {
      try {
         for (int i = 0; i < listaClasesCategorias.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaClasesCategorias.borrar(getEm(), listaClasesCategorias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearClasesCategorias(List<ClasesCategorias> listaClasesCategorias) {
      try {
         for (int i = 0; i < listaClasesCategorias.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaClasesCategorias.crear(getEm(), listaClasesCategorias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<ClasesCategorias> consultarClasesCategorias() {
      try {
         List<ClasesCategorias> listMotivosCambiosCargos;
         listMotivosCambiosCargos = persistenciaClasesCategorias.consultarClasesCategorias(getEm());
         return listMotivosCambiosCargos;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public ClasesCategorias consultarClaseCategoria(BigInteger secClasesCategorias) {
      try {
         ClasesCategorias subCategoria;
         subCategoria = persistenciaClasesCategorias.consultarClaseCategoria(getEm(), secClasesCategorias);
         return subCategoria;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarCategoriaClaseCategoria(BigInteger secClasesCategorias) {
      BigInteger contarCategoriaClaseCategoria = null;

      try {
         return contarCategoriaClaseCategoria = persistenciaClasesCategorias.contarCategoriasClaseCategoria(getEm(), secClasesCategorias);
      } catch (Exception e) {
         log.error("ERROR AdministrarClasesCategorias contarCategoriaClaseCategoria ERROR : " + e);
         return null;
      }
   }
}
