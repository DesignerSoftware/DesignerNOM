/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Grupostiposentidades;
import Entidades.TiposEntidades;
import InterfaceAdministrar.AdministrarTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaGruposTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaVigenciasAfiliacionesInterface;
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
public class AdministrarTiposEntidades implements AdministrarTiposEntidadesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposEntidades.class);

   @EJB
   PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
   @EJB
   PersistenciaGruposTiposEntidadesInterface persistenciaGruposTiposEntidades;
   @EJB
   PersistenciaVigenciasAfiliacionesInterface persistenciaVigenciasAfiliaciones;
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
   public void modificarTipoEntidad(List<TiposEntidades> listTiposEntidadesModificadas) {
      try {
         for (int i = 0; i < listTiposEntidadesModificadas.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposEntidades.editar(getEm(), listTiposEntidadesModificadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTipoEntidad(List<TiposEntidades> listTiposEntidadesModificadas) {
      try {
         for (int i = 0; i < listTiposEntidadesModificadas.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposEntidades.borrar(getEm(), listTiposEntidadesModificadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTipoEntidad(List<TiposEntidades> listTiposEntidadesModificadas) {
      try {
         for (int i = 0; i < listTiposEntidadesModificadas.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposEntidades.crear(getEm(), listTiposEntidadesModificadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposEntidades> consultarTiposEntidades() {
      try {
         return persistenciaTiposEntidades.buscarTiposEntidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposEntidades consultarTipoEntidad(BigInteger secTipoEntidad) {
      try {
         return persistenciaTiposEntidades.buscarTiposEntidadesSecuencia(getEm(), secTipoEntidad);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Grupostiposentidades> consultarLOVGruposTiposEntidades() {
      try {
         return persistenciaGruposTiposEntidades.consultarGruposTiposEntidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarVigenciasAfiliacionesTipoEntidad(BigInteger secuenciaTipoEntidad) {
      try {
         return persistenciaTiposEntidades.verificarBorrado(getEm(), secuenciaTipoEntidad);
      } catch (Exception e) {
         log.error("ERROR AdministrarTipoEntidad verificarBorrado ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarFormulasContratosEntidadesTipoEntidad(BigInteger secuenciaTipoEntidad) {
      try {
         return persistenciaTiposEntidades.verificarBorradoFCE(getEm(), secuenciaTipoEntidad);
      } catch (Exception e) {
         log.error("ERROR AdministrarTipoEntidad verificarBorradoFCE ERROR :" + e);
         return null;
      }
   }
}
