/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosCesantias;
import InterfaceAdministrar.AdministrarMotivosCesantiasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosCesantiasInterface;
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
public class AdministrarMotivosCesantias implements AdministrarMotivosCesantiasInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosCesantias.class);

   @EJB
   PersistenciaMotivosCesantiasInterface persistenciaMotivosCensantias;
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
   public void modificarMotivosCesantias(List<MotivosCesantias> listaMotivosCesantias) {
      try {
         for (int i = 0; i < listaMotivosCesantias.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaMotivosCensantias.editar(getEm(), listaMotivosCesantias.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarMotivosCesantias(List<MotivosCesantias> listaMotivosCesantias) {
      try {
         for (int i = 0; i < listaMotivosCesantias.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaMotivosCensantias.borrar(getEm(), listaMotivosCesantias.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearMotivosCesantias(List<MotivosCesantias> listaMotivosCesantias) {
      try {
         for (int i = 0; i < listaMotivosCesantias.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaMotivosCensantias.crear(getEm(), listaMotivosCesantias.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<MotivosCesantias> consultarMotivosCesantias() {
      try {
         return persistenciaMotivosCensantias.buscarMotivosCesantias(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public MotivosCesantias consultarMotivoCesantia(BigInteger secMotivoPrestamo) {
      try {
         return persistenciaMotivosCensantias.buscarMotivoCensantia(getEm(), secMotivoPrestamo);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarNovedadesSistemasMotivoCesantia(BigInteger secuenciaMotivosCesantias) {
      try {
         return persistenciaMotivosCensantias.contadorNovedadesSistema(getEm(), secuenciaMotivosCesantias);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARMOTIVOSCESANTIAS verificarNovedadesSistema ERROR :" + e);
         return null;
      }
   }
}
