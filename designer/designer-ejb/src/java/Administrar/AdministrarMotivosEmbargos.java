/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarMotivosEmbargosInterface;
import Entidades.MotivosEmbargos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosEmbargosInterface;
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
public class AdministrarMotivosEmbargos implements AdministrarMotivosEmbargosInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosEmbargos.class);

   @EJB
   PersistenciaMotivosEmbargosInterface persistenciaMotivosEmbargos;
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

   public void modificarMotivosEmbargos(List<MotivosEmbargos> listaMotivosEmbargos) {
      try {
         for (int i = 0; i < listaMotivosEmbargos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaMotivosEmbargos.editar(getEm(), listaMotivosEmbargos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarMotivosEmbargos(List<MotivosEmbargos> listaMotivosEmbargos) {
      try {
         for (int i = 0; i < listaMotivosEmbargos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaMotivosEmbargos.borrar(getEm(), listaMotivosEmbargos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearMotivosEmbargos(List<MotivosEmbargos> listaMotivosEmbargos) {
      try {
         for (int i = 0; i < listaMotivosEmbargos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaMotivosEmbargos.crear(getEm(), listaMotivosEmbargos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<MotivosEmbargos> mostrarMotivosEmbargos() {
      try {
         return persistenciaMotivosEmbargos.buscarMotivosEmbargos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public MotivosEmbargos mostrarMotivoEmbargo(BigInteger secMotivoPrestamo) {
      try {
         return persistenciaMotivosEmbargos.buscarMotivoEmbargo(getEm(), secMotivoPrestamo);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarEersPrestamosMotivoEmbargo(BigInteger secuenciaTiposDias) {
      try {
         return persistenciaMotivosEmbargos.contadorEersPrestamos(getEm(), secuenciaTiposDias);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARMOTIVOSEMBARGOS VERIFICAREERSPRESTAMOS ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarEmbargosMotivoEmbargo(BigInteger secuenciaTiposDias) {
      try {
         return persistenciaMotivosEmbargos.contadorEmbargos(getEm(), secuenciaTiposDias);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARMOTIVOSEMBARGOS VERIFICAREMBARGOS ERROR :" + e);
         return null;
      }
   }
}
