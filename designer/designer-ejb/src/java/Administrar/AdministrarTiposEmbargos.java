/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposEmbargosInterface;
import Entidades.TiposEmbargos;
import InterfacePersistencia.PersistenciaTiposEmbargosInterface;
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
public class AdministrarTiposEmbargos implements AdministrarTiposEmbargosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposEmbargos.class);

   @EJB
   PersistenciaTiposEmbargosInterface persistenciaTiposEmbargos;
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
   public void modificarTiposPrestamos(List<TiposEmbargos> listaTiposEmbargos) {
      try {
         for (int i = 0; i < listaTiposEmbargos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposEmbargos.editar(getEm(), listaTiposEmbargos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposPrestamos(List<TiposEmbargos> listaTiposEmbargos) {
      try {
         for (int i = 0; i < listaTiposEmbargos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposEmbargos.borrar(getEm(), listaTiposEmbargos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposPrestamos(List<TiposEmbargos> listaTiposEmbargos) {
      try {
         for (int i = 0; i < listaTiposEmbargos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposEmbargos.crear(getEm(), listaTiposEmbargos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposEmbargos> consultarTiposPrestamos() {
      try {
         return persistenciaTiposEmbargos.buscarTiposEmbargos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposEmbargos consultarTipoPrestamo(BigInteger secMotivoPrestamo) {
      try {
         return persistenciaTiposEmbargos.buscarTipoEmbargo(getEm(), secMotivoPrestamo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarDiasLaboralesTipoEmbargo(BigInteger secuenciaTiposDias) {
      try {
         return persistenciaTiposEmbargos.contadorEerPrestamos(getEm(), secuenciaTiposDias);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARTIPOSEMBARGOS VERIFICARDIASLABORALES ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarExtrasRecargosTipoEmbargo(BigInteger secuenciaTiposDias) {
      try {
         return persistenciaTiposEmbargos.contadorFormasDtos(getEm(), secuenciaTiposDias);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARTIPOSEMBARGOS VERIFICAREXTRASRECARGOS ERROR :" + e);
         return null;
      }
   }
}
