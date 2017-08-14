/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarMotivosPrestamosInterface;
import Entidades.MotivosPrestamos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosPrestamosInterface;
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
public class AdministrarMotivosPrestamos implements AdministrarMotivosPrestamosInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosPrestamos.class);

   @EJB
   PersistenciaMotivosPrestamosInterface persistenciaMotivosPrestamos;
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
   public void modificarMotivosPrestamos(List<MotivosPrestamos> listaMotivosPrestamos) {
      try {
         for (int i = 0; i < listaMotivosPrestamos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaMotivosPrestamos.editar(getEm(), listaMotivosPrestamos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarMotivosPrestamos(List<MotivosPrestamos> listaMotivosPrestamos) {
      try {
         for (int i = 0; i < listaMotivosPrestamos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaMotivosPrestamos.borrar(getEm(), listaMotivosPrestamos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearMotivosPrestamos(List<MotivosPrestamos> listaMotivosPrestamos) {
      try {
         for (int i = 0; i < listaMotivosPrestamos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaMotivosPrestamos.crear(getEm(), listaMotivosPrestamos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<MotivosPrestamos> mostrarMotivosPrestamos() {
      try {
         return persistenciaMotivosPrestamos.buscarMotivosPrestamos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public MotivosPrestamos mostrarMotivoPrestamo(BigInteger secMotivoPrestamo) {
      try {
         return persistenciaMotivosPrestamos.buscarMotivoPrestamo(getEm(), secMotivoPrestamo);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarEersPrestamosMotivoPrestamo(BigInteger secuenciaMotivosPrestamos) {
      try {
         return persistenciaMotivosPrestamos.contadorEersPrestamos(getEm(), secuenciaMotivosPrestamos);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARMOTIVOSPRESTAMOS VERIFICARDIASLABORALES ERROR :" + e);
         return null;
      }
   }
}
