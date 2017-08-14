/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Tiposviajeros;
import InterfaceAdministrar.AdministrarTiposViajerosInterface;
import InterfacePersistencia.PersistenciaTiposViajerosInterface;
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
public class AdministrarTiposViajeros implements AdministrarTiposViajerosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposViajeros.class);

   @EJB
   PersistenciaTiposViajerosInterface persistenciaTiposViajeros;
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
   public void modificarTiposViajeros(List<Tiposviajeros> listaTiposViajeros) {
      try {
         for (int i = 0; i < listaTiposViajeros.size(); i++) {
            log.warn("Administrar Modificando...");
            log.warn("Nombre " + listaTiposViajeros.get(i).getNombre() + " Codigo " + listaTiposViajeros.get(i).getCodigo());
            persistenciaTiposViajeros.editar(getEm(), listaTiposViajeros.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposViajeros(List<Tiposviajeros> listaTiposViajeros) {
      try {
         for (int i = 0; i < listaTiposViajeros.size(); i++) {
            log.warn("Administrar Borrando...");
            log.warn("Nombre " + listaTiposViajeros.get(i).getNombre() + " Codigo " + listaTiposViajeros.get(i).getCodigo());
            persistenciaTiposViajeros.borrar(getEm(), listaTiposViajeros.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposViajeros(List<Tiposviajeros> listaTiposViajeros) {
      try {
         for (int i = 0; i < listaTiposViajeros.size(); i++) {
            log.warn("Administrar Creando...");
            log.warn("Nombre " + listaTiposViajeros.get(i).getNombre() + " Codigo " + listaTiposViajeros.get(i).getCodigo());
            persistenciaTiposViajeros.crear(getEm(), listaTiposViajeros.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<Tiposviajeros> consultarTiposViajeros() {
      try {
         return persistenciaTiposViajeros.consultarTiposViajeros(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Tiposviajeros consultarTipoViajero(BigInteger secTiposViajeros) {
      try {
         return persistenciaTiposViajeros.consultarSubCategoria(getEm(), secTiposViajeros);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarTiposLegalizaciones(BigInteger secTiposViajeros) {
      try {
         return persistenciaTiposViajeros.contarTiposLegalizacionesTipoViajero(getEm(), secTiposViajeros);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposViajeros contarEscalafones ERROR : " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarVigenciasViajeros(BigInteger secTiposViajeros) {
      try {
         return persistenciaTiposViajeros.contarVigenciasViajerosTipoViajero(getEm(), secTiposViajeros);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposViajeros contarEscalafones ERROR : " + e);
         return null;
      }
   }

}
