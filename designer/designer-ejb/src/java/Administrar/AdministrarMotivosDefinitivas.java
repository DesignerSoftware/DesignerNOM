/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosDefinitivas;
import InterfaceAdministrar.AdministrarMotivosDefinitivasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosDefinitivasInterface;
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
public class AdministrarMotivosDefinitivas implements AdministrarMotivosDefinitivasInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosDefinitivas.class);

   @EJB
   PersistenciaMotivosDefinitivasInterface persistenciaMotivosDefinitivas;
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

   public void modificarMotivosDefinitivas(List<MotivosDefinitivas> listaMotivosDefinitivas) {
      try {
         for (int i = 0; i < listaMotivosDefinitivas.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaMotivosDefinitivas.editar(getEm(), listaMotivosDefinitivas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarMotivosDefinitivas() ERROR: " + e);
      }
   }

   public void borrarMotivosDefinitivas(List<MotivosDefinitivas> listaMotivosDefinitivas) {
      try {
         for (int i = 0; i < listaMotivosDefinitivas.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaMotivosDefinitivas.borrar(getEm(), listaMotivosDefinitivas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarMotivosDefinitivas() ERROR: " + e);
      }
   }

   public void crearMotivosDefinitivas(List<MotivosDefinitivas> listaMotivosDefinitivas) {
      try {
         for (int i = 0; i < listaMotivosDefinitivas.size(); i++) {
            log.warn("Administrar Crenando...");
            persistenciaMotivosDefinitivas.crear(getEm(), listaMotivosDefinitivas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearMotivosDefinitivas() ERROR: " + e);
      }
   }

   public List<MotivosDefinitivas> mostrarMotivosDefinitivas() {
      try {
         return persistenciaMotivosDefinitivas.buscarMotivosDefinitivas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".mostrarMotivosDefinitivas() ERROR: " + e);
         return null;
      }
   }

   public MotivosDefinitivas mostrarMotivoDefinitiva(BigInteger secMotivoPrestamo) {
      try {
         return persistenciaMotivosDefinitivas.buscarMotivoDefinitiva(getEm(), secMotivoPrestamo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".mostrarMotivoDefinitiva() ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarNovedadesSistemasMotivoDefinitiva(BigInteger secuenciaMotivosCesantias) {
      try {
         return persistenciaMotivosDefinitivas.contadorNovedadesSistema(getEm(), secuenciaMotivosCesantias);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARMOTIVOSDEFINITIVAS VERIFICARNOVEDADESSISTEMA ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarParametrosCambiosMasivosMotivoDefinitiva(BigInteger secuenciaMotivosCesantias) {
      try {
         return persistenciaMotivosDefinitivas.contadorParametrosCambiosMasivos(getEm(), secuenciaMotivosCesantias);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARMOTIVOSDEFINITIVAS VERIFICARPARAMETROSCAMBIOSMASIVOS ERROR :" + e);
         return null;
      }
   }

}
