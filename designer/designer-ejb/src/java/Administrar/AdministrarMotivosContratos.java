/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarMotivosContratosInterface;
import Entidades.MotivosContratos;
import InterfacePersistencia.PersistenciaMotivosContratosInterface;
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
public class AdministrarMotivosContratos implements AdministrarMotivosContratosInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosContratos.class);

   @EJB
   PersistenciaMotivosContratosInterface persistenciaMotivosContratos;
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
   public void modificarMotivosContratos(List<MotivosContratos> listaMotivosContratos) {
      try {
         for (int i = 0; i < listaMotivosContratos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaMotivosContratos.editar(getEm(), listaMotivosContratos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarMotivosContratos(List<MotivosContratos> listaMotivosContratos) {
      try {
         for (int i = 0; i < listaMotivosContratos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaMotivosContratos.borrar(getEm(), listaMotivosContratos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearMotivosContratos(List<MotivosContratos> listaMotivosContratos) {
      try {
         for (int i = 0; i < listaMotivosContratos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaMotivosContratos.crear(getEm(), listaMotivosContratos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<MotivosContratos> consultarMotivosContratos() {
      try {
         return persistenciaMotivosContratos.buscarMotivosContratos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public MotivosContratos consultarMotivoContrato(BigInteger secMotivosCambiosCargos) {
      try {
         return persistenciaMotivosContratos.buscarMotivoContrato(getEm(), secMotivosCambiosCargos);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarVigenciasTiposContratosMotivoContrato(BigInteger secuenciaMovitoCambioCargo) {
      try {
         return persistenciaMotivosContratos.verificarBorradoVigenciasTiposContratos(getEm(), secuenciaMovitoCambioCargo);
      } catch (Exception e) {
         log.error("ERROR AdministrarMotivosContratos contarVigenciasTiposContratosMotivoContrato ERROR :" + e);
         return null;
      }
   }
}
