/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposAccidentes;
import InterfaceAdministrar.AdministrarTiposAccidentesInterface;
import InterfacePersistencia.PersistenciaTiposAccidentesInterface;
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
public class AdministrarTiposAccidentes implements AdministrarTiposAccidentesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposAccidentes.class);

   @EJB
   PersistenciaTiposAccidentesInterface persistenciaTiposAccidentes;
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
   public void modificarTiposAccidentes(List<TiposAccidentes> listaTiposAccidentes) {
      try {
         for (int i = 0; i < listaTiposAccidentes.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposAccidentes.editar(getEm(), listaTiposAccidentes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposAccidentes(List<TiposAccidentes> listaTiposAccidentes) {
      try {
         for (int i = 0; i < listaTiposAccidentes.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposAccidentes.borrar(getEm(), listaTiposAccidentes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposAccidentes(List<TiposAccidentes> listaTiposAccidentes) {
      try {
         for (int i = 0; i < listaTiposAccidentes.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposAccidentes.crear(getEm(), listaTiposAccidentes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposAccidentes> consultarTiposAccidentes() {
      try {
         return persistenciaTiposAccidentes.buscarTiposAccidentes(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposAccidentes consultarTiposAccidentes(BigInteger secTiposAccidentes) {
      try {
         return persistenciaTiposAccidentes.buscarTipoAccidente(getEm(), secTiposAccidentes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarSoAccidentesMedicosTipoAccidente(BigInteger secuenciaTiposAccidentes) {
      try {
         return persistenciaTiposAccidentes.contadorSoAccidentesMedicos(getEm(), secuenciaTiposAccidentes);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARTIPOSACCIDENTES verificarSoAccidentesMedicos ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarAccidentesTipoAccidente(BigInteger secuenciaTiposAccidentes) {
      try {
         return persistenciaTiposAccidentes.contadorAccidentes(getEm(), secuenciaTiposAccidentes);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARTIPOSACCIDENTES verificarBorradoAccidentes ERROR :" + e);
         return null;
      }
   }
}
