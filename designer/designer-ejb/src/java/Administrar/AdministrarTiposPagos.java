/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Tipospagos;
import InterfaceAdministrar.AdministrarTiposPagosInterface;
import InterfacePersistencia.PersistenciaTiposPagosInterface;
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
public class AdministrarTiposPagos implements AdministrarTiposPagosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposPagos.class);

   @EJB
   PersistenciaTiposPagosInterface persistenciaTiposPagos;
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

   public void modificarTiposPagos(List<Tipospagos> listaTiposPagos) {
      try {
         for (int i = 0; i < listaTiposPagos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposPagos.editar(getEm(), listaTiposPagos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarTiposPagos() ERROR: " + e);
      }
   }

   public void borrarTiposPagos(List<Tipospagos> listaTiposPagos) {
      try {
         for (int i = 0; i < listaTiposPagos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposPagos.borrar(getEm(), listaTiposPagos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarTiposPagos() ERROR: " + e);
      }
   }

   public void crearTiposPagos(List<Tipospagos> listaTiposPagos) {
      try {
         for (int i = 0; i < listaTiposPagos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposPagos.crear(getEm(), listaTiposPagos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearTiposPagos() ERROR: " + e);
      }
   }

   @Override
   public List<Tipospagos> consultarTiposPagos() {
      try {
         return persistenciaTiposPagos.consultarTiposPagos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTiposPagos() ERROR: " + e);
         return null;
      }
   }

   public Tipospagos consultarTipoPago(BigInteger secTiposPagos) {
      try {
         return persistenciaTiposPagos.consultarTipoPago(getEm(), secTiposPagos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTipoPago() ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarProcesosTipoPago(BigInteger secTiposPagos) {
      try {
         return persistenciaTiposPagos.contarProcesosTipoPago(getEm(), secTiposPagos);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposPagos contarProcesosTipoPago ERROR : " + e);
         return null;
      }
   }
}
