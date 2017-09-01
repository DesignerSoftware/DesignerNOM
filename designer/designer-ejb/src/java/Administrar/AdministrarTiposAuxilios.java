/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposAuxiliosInterface;
import Entidades.TiposAuxilios;
import InterfacePersistencia.PersistenciaTiposAuxiliosInterface;
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
public class AdministrarTiposAuxilios implements AdministrarTiposAuxiliosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposAuxilios.class);

   @EJB
   PersistenciaTiposAuxiliosInterface persistenciaTiposAuxilios;
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
   public void modificarTiposAuxilios(List<TiposAuxilios> listaTiposAuxilios) {
      try {
         for (int i = 0; i < listaTiposAuxilios.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposAuxilios.editar(getEm(), listaTiposAuxilios.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposAuxilios(List<TiposAuxilios> listaTiposAuxilios) {
      try {
         for (int i = 0; i < listaTiposAuxilios.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposAuxilios.borrar(getEm(), listaTiposAuxilios.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposAuxilios(List<TiposAuxilios> listaTiposAuxilios) {
      try {
         for (int i = 0; i < listaTiposAuxilios.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposAuxilios.crear(getEm(), listaTiposAuxilios.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposAuxilios> consultarTiposAuxilios() {
      try {
         return persistenciaTiposAuxilios.buscarTiposAuxilios(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposAuxilios consultarTipoAuxilio(BigInteger secTiposAuxilios) {
      try {
         return persistenciaTiposAuxilios.buscarTipoAuxilio(getEm(), secTiposAuxilios);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarTablasAuxiliosTiposAuxilios(BigInteger secuenciaTiposAuxilios) {
      try {
         return persistenciaTiposAuxilios.contadorTablasAuxilios(getEm(), secuenciaTiposAuxilios);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARTIPOSAUXILIOS verificarTablasAuxilios ERROR :" + e);
         return null;
      }
   }
}
