/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposCertificadosInterface;
import Entidades.TiposCertificados;
import InterfacePersistencia.PersistenciaTiposCertificadosInterface;
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
public class AdministrarTiposCertificados implements AdministrarTiposCertificadosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposCertificados.class);

   @EJB
   PersistenciaTiposCertificadosInterface persistenciaTiposCertificados;
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
   public void modificarTiposCertificados(List<TiposCertificados> listaTiposCertificados) {
      try {
         for (int i = 0; i < listaTiposCertificados.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposCertificados.editar(getEm(), listaTiposCertificados.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposCertificados(List<TiposCertificados> listaTiposCertificados) {
      try {
         for (int i = 0; i < listaTiposCertificados.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposCertificados.borrar(getEm(), listaTiposCertificados.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposCertificados(List<TiposCertificados> listaTiposCertificados) {
      try {
         for (int i = 0; i < listaTiposCertificados.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposCertificados.crear(getEm(), listaTiposCertificados.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposCertificados> consultarTiposCertificados() {
      try {
         return persistenciaTiposCertificados.buscarTiposCertificados(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposCertificados consultarTipoCertificado(BigInteger secTipoCertificado) {
      try {
         return persistenciaTiposCertificados.buscarTipoCertificado(getEm(), secTipoCertificado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
