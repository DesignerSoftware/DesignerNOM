/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MetodosPagos;
import InterfaceAdministrar.AdministrarMetodosPagosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMetodosPagosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarMetodosPagos implements AdministrarMetodosPagosInterface {

   private static Logger log = Logger.getLogger(AdministrarMetodosPagos.class);

   @EJB
   PersistenciaMetodosPagosInterface persistenciaMetodosPagos;
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
   public void modificarMetodosPagos(List<MetodosPagos> listaMetodosPagos) {
      try {
         for (int i = 0; i < listaMetodosPagos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaMetodosPagos.editar(getEm(), listaMetodosPagos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarMetodosPagos(List<MetodosPagos> listaMetodosPagos) {
      try {
         for (int i = 0; i < listaMetodosPagos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaMetodosPagos.borrar(getEm(), listaMetodosPagos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearMetodosPagos(List<MetodosPagos> listaMetodosPagos) {
      try {
         for (int i = 0; i < listaMetodosPagos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaMetodosPagos.crear(getEm(), listaMetodosPagos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<MetodosPagos> consultarMetodosPagos() {
      try {
         return persistenciaMetodosPagos.buscarMetodosPagos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public MetodosPagos consultarMetodoPago(BigInteger secMetodosPagos) {
      try {
         return persistenciaMetodosPagos.buscarMetodosPagosEmpleado(getEm(), secMetodosPagos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarMetodosPagosVigenciasFormasPagos(BigInteger secuenciaMetodoPago) {
      try {
         return persistenciaMetodosPagos.contadorvigenciasformaspagos(getEm(), secuenciaMetodoPago);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARMETODOSPAGOS VERIFICARVIGENCIASFORMASPAGOS ERROR " + e);
         return null;
      }
   }
}
