/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposReemplazos;
import InterfaceAdministrar.AdministrarTiposReemplazosInterface;
import InterfacePersistencia.PersistenciaTiposReemplazosInterface;
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
public class AdministrarTiposReemplazos implements AdministrarTiposReemplazosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposReemplazos.class);

   @EJB
   PersistenciaTiposReemplazosInterface persistenciaTiposReemplazos;
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
   public void modificarTiposReemplazos(List<TiposReemplazos> listaTiposReemplazos) {
      try {
         for (int i = 0; i < listaTiposReemplazos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposReemplazos.editar(getEm(), listaTiposReemplazos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposReemplazos(List<TiposReemplazos> listaTiposReemplazos) {
      try {
         for (int i = 0; i < listaTiposReemplazos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposReemplazos.borrar(getEm(), listaTiposReemplazos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposReemplazos(List<TiposReemplazos> listaTiposReemplazos) {
      try {
         for (int i = 0; i < listaTiposReemplazos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposReemplazos.crear(getEm(), listaTiposReemplazos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposReemplazos> consultarTiposReemplazos() {
      try {
         return persistenciaTiposReemplazos.buscarTiposReemplazos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposReemplazos consultarTipoReemplazo(BigInteger secMotivoDemanda) {
      try {
         return persistenciaTiposReemplazos.buscarTipoReemplazo(getEm(), secMotivoDemanda);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarEncargaturasTipoReemplazo(BigInteger secuenciaTiposReemplazos) {
      try {
         log.warn("Secuencia Vigencias Indicadores " + secuenciaTiposReemplazos);
         return persistenciaTiposReemplazos.contadorEncargaturas(getEm(), secuenciaTiposReemplazos);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARTIPOSREEMPLAZOS VERIFICARBORRADOENCARGATURAS ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarProgramacionesTiemposTipoReemplazo(BigInteger secuenciaTiposReemplazos) {
      try {
         log.warn("Secuencia Vigencias Indicadores " + secuenciaTiposReemplazos);
         return persistenciaTiposReemplazos.contadorProgramacionesTiempos(getEm(), secuenciaTiposReemplazos);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARTIPOSREEMPLAZOS VERIFICARBORRADOPROGRAMACIONESTIEMPOS ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarReemplazosTipoReemplazo(BigInteger secuenciaTiposReemplazos) {
      try {
         log.warn("Secuencia Vigencias Indicadores " + secuenciaTiposReemplazos);
         return persistenciaTiposReemplazos.contadorReemplazos(getEm(), secuenciaTiposReemplazos);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARTIPOSREEMPLAZOS VERIFICARBORRADOREEMPLAZOS ERROR :" + e);
         return null;
      }
   }

   @Override
   public List<TiposReemplazos> consultarLOVTiposReemplazos() {
      try {
         return persistenciaTiposReemplazos.buscarTiposReemplazos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
