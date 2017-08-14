/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposCentrosCostosInterface;
import Entidades.GruposTiposCC;
import Entidades.TiposCentrosCostos;
import InterfacePersistencia.PersistenciaGruposTiposCCInterface;
import InterfacePersistencia.PersistenciaTiposCentrosCostosInterface;
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
public class AdministrarTiposCentrosCostos implements AdministrarTiposCentrosCostosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposCentrosCostos.class);

   @EJB
   PersistenciaTiposCentrosCostosInterface persistenciaTiposCentrosCostos;
   @EJB
   PersistenciaGruposTiposCCInterface persistenciaGruposTiposCC;
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
   public void modificarTipoCentrosCostos(List<TiposCentrosCostos> listaTiposCentrosCostos) {
      try {
         for (int i = 0; i < listaTiposCentrosCostos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposCentrosCostos.editar(getEm(), listaTiposCentrosCostos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposCentrosCostos(List<TiposCentrosCostos> listaTiposCentrosCostos) {
      try {
         for (int i = 0; i < listaTiposCentrosCostos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposCentrosCostos.borrar(getEm(), listaTiposCentrosCostos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposCentrosCostos(List<TiposCentrosCostos> listaTiposCentrosCostos) {
      try {
         for (int i = 0; i < listaTiposCentrosCostos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposCentrosCostos.crear(getEm(), listaTiposCentrosCostos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposCentrosCostos> consultarTiposCentrosCostos() {
      try {
         return persistenciaTiposCentrosCostos.buscarTiposCentrosCostos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposCentrosCostos consultarTipoCentroCosto(BigInteger secTipoCentrosCostos) {
      try {
         return persistenciaTiposCentrosCostos.buscarTipoCentrosCostos(getEm(), secTipoCentrosCostos);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<GruposTiposCC> consultarLOVGruposTiposCentrosCostos() {
      try {
         return persistenciaGruposTiposCC.buscarGruposTiposCC(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarCentrosCostosTipoCentroCosto(BigInteger secuenciaTipoEntidad) {
      try {
         return persistenciaTiposCentrosCostos.verificarBorradoCentrosCostos(getEm(), secuenciaTipoEntidad);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposCentrosCostos verificarBorradoCC ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarVigenciasCuentasTipoCentroCosto(BigInteger secuenciaTipoEntidad) {
      try {
         return persistenciaTiposCentrosCostos.verificarBorradoVigenciasCuentas(getEm(), secuenciaTipoEntidad);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposCentrosCostos verificarBorradoVC ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarRiesgosProfesionalesTipoCentroCosto(BigInteger secuenciaTipoEntidad) {
      try {
         return persistenciaTiposCentrosCostos.verificarBorradoRiesgosProfesionales(getEm(), secuenciaTipoEntidad);
      } catch (Exception e) {
         log.error("ERROR AdministrarTipoEntidad verificarBorrado ERROR :" + e);
         return null;
      }
   }
}
