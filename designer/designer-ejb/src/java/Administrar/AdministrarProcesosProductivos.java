/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.CentrosCostos;
import Entidades.ProcesosProductivos;
import InterfaceAdministrar.AdministrarProcesosProductivosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaProcesosProductivosInterface;
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
public class AdministrarProcesosProductivos implements AdministrarProcesosProductivosInterface {

   private static Logger log = Logger.getLogger(AdministrarProcesosProductivos.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaProcesosProductivos'.
    */
   @EJB
   PersistenciaProcesosProductivosInterface persistenciaProcesosProductivos;
   @EJB
   PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
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
   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------

   public void modificarProcesosProductivos(List<ProcesosProductivos> listaProcesosProductivos) {
      try {
         for (int i = 0; i < listaProcesosProductivos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaProcesosProductivos.editar(getEm(), listaProcesosProductivos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarProcesosProductivos() ERROR: " + e);
      }
   }

   public void borrarProcesosProductivos(List<ProcesosProductivos> listaProcesosProductivos) {
      try {
         for (int i = 0; i < listaProcesosProductivos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaProcesosProductivos.borrar(getEm(), listaProcesosProductivos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarProcesosProductivos() ERROR: " + e);
      }
   }

   public void crearProcesosProductivos(List<ProcesosProductivos> listaProcesosProductivos) {
      try {
         for (int i = 0; i < listaProcesosProductivos.size(); i++) {
            persistenciaProcesosProductivos.crear(getEm(), listaProcesosProductivos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearProcesosProductivos() ERROR: " + e);
      }
   }

   @Override
   public List<ProcesosProductivos> consultarProcesosProductivos() {
      try {
         return persistenciaProcesosProductivos.consultarProcesosProductivos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarProcesosProductivos() ERROR: " + e);
         return null;
      }
   }

   public List<CentrosCostos> consultarLOVCentrosCostos() {
      try {
         return persistenciaCentrosCostos.buscarCentrosCostos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLOVCentrosCostos() ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarCargosProcesoProductivo(BigInteger secuenciaSucursal) {
      try {
         return persistenciaProcesosProductivos.contarCargosProcesoProductivo(getEm(), secuenciaSucursal);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".contarCargosProcesoProductivo() ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarTarifasProductosProcesoProductivo(BigInteger secuenciaSucursal) {
      try {
         return persistenciaProcesosProductivos.contarTarifasProductosProcesoProductivo(getEm(), secuenciaSucursal);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".contarTarifasProductosProcesoProductivo() ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarUnidadesProducidasProcesoProductivo(BigInteger secuenciaSucursal) {
      try {
         return persistenciaProcesosProductivos.contarUnidadesProducidasProcesoProductivo(getEm(), secuenciaSucursal);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".contarUnidadesProducidasProcesoProductivo() ERROR: " + e);
         return null;
      }
   }
}
