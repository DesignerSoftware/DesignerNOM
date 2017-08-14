/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ClavesSap;
import InterfaceAdministrar.AdministrarClavesSapInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaClavesSapInterface;
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
public class AdministrarClavesSap implements AdministrarClavesSapInterface {

   private static Logger log = Logger.getLogger(AdministrarClavesSap.class);

   @EJB
   PersistenciaClavesSapInterface persistenciaClavesSap;
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

   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   public void modificarClavesSap(List<ClavesSap> listaClavesSap) {
      try {
         for (int i = 0; i < listaClavesSap.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaClavesSap.editar(getEm(), listaClavesSap.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarClavesSap(List<ClavesSap> listaClavesSap) {
      try {
         for (int i = 0; i < listaClavesSap.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaClavesSap.borrar(getEm(), listaClavesSap.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearClavesSap(List<ClavesSap> listaClavesSap) {
      try {
         for (int i = 0; i < listaClavesSap.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaClavesSap.crear(getEm(), listaClavesSap.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<ClavesSap> consultarClavesSap() {
      try {
         List<ClavesSap> listMotivosCambiosCargos;
         listMotivosCambiosCargos = persistenciaClavesSap.consultarClavesSap(getEm());
         return listMotivosCambiosCargos;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<ClavesSap> consultarLOVClavesSap() {
      try {
         List<ClavesSap> listMotivosCambiosCargos;
         listMotivosCambiosCargos = persistenciaClavesSap.consultarClavesSap(getEm());
         return listMotivosCambiosCargos;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarClavesContablesCreditoClaveSap(BigInteger secuencia) {
      try {
         BigInteger retorno = persistenciaClavesSap.contarClavesContablesCreditoClaveSap(getEm(), secuencia);
         return retorno;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarClavesContablesDebitoClaveSap(BigInteger secuencia) {
      try {
         BigInteger retorno = persistenciaClavesSap.contarClavesContablesDebitoClaveSap(getEm(), secuencia);
         return retorno;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
