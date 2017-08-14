/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposChequeos;
import InterfaceAdministrar.AdministrarTiposChequeosInterface;
import InterfacePersistencia.PersistenciaTiposChequeosInterface;
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
public class AdministrarTiposChequeos implements AdministrarTiposChequeosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposChequeos.class);

   @EJB
   PersistenciaTiposChequeosInterface persistenciaTiposChequeos;
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
   public void modificarTiposChequeos(List<TiposChequeos> listaTiposChequeos) {
      try {
         for (int i = 0; i < listaTiposChequeos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposChequeos.editar(getEm(), listaTiposChequeos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposChequeos(List<TiposChequeos> listaTiposChequeos) {
      try {
         for (int i = 0; i < listaTiposChequeos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposChequeos.borrar(getEm(), listaTiposChequeos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposChequeos(List<TiposChequeos> listaTiposChequeos) {
      try {
         for (int i = 0; i < listaTiposChequeos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposChequeos.crear(getEm(), listaTiposChequeos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposChequeos> consultarTiposChequeos() {
      try {
         return persistenciaTiposChequeos.buscarTiposChequeos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public TiposChequeos consultarTipoChequeo(BigInteger secTipoEmpresa) {
      try {
         return persistenciaTiposChequeos.buscarTipoChequeo(getEm(), secTipoEmpresa);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarChequeosMedicosTipoChequeo(BigInteger secuenciaJuzgados) {
      try {
         log.warn("Administrar SecuenciaBorrar " + secuenciaJuzgados);
         return persistenciaTiposChequeos.contadorChequeosMedicos(getEm(), secuenciaJuzgados);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARTIPOSCHEQUEOS VERIFICARCHEQUEOSMEDICOS ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarTiposExamenesCargosTipoChequeo(BigInteger secuenciaJuzgados) {
      try {
         log.warn("Administrar SecuenciaBorrar " + secuenciaJuzgados);
         return persistenciaTiposChequeos.contadorTiposExamenesCargos(getEm(), secuenciaJuzgados);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARTIPOSCHEQUEOS VERIFICARTIPOSEXAMENESCARGOS ERROR :" + e);
         return null;
      }
   }
}
