/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Tiposausentismos;
import InterfaceAdministrar.AdministrarTiposAusentismosInterface;
import InterfacePersistencia.PersistenciaTiposAusentismosInterface;
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
public class AdministrarTiposAusentismos implements AdministrarTiposAusentismosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposAusentismos.class);

   @EJB
   PersistenciaTiposAusentismosInterface persistenciaTiposAusentismos;
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
   public void modificarTiposAusentismos(List<Tiposausentismos> listaTiposAusentismos) {
      try {
         for (int i = 0; i < listaTiposAusentismos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposAusentismos.editar(getEm(), listaTiposAusentismos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposAusentismos(List<Tiposausentismos> listaTiposAusentismos) {
      try {
         for (int i = 0; i < listaTiposAusentismos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposAusentismos.borrar(getEm(), listaTiposAusentismos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposAusentismos(List<Tiposausentismos> listaTiposAusentismos) {
      try {
         for (int i = 0; i < listaTiposAusentismos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposAusentismos.crear(getEm(), listaTiposAusentismos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<Tiposausentismos> consultarTiposAusentismos() {
      try {
         return persistenciaTiposAusentismos.consultarTiposAusentismos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Tiposausentismos consultarTipoAusentismo(BigInteger secTiposAusentismos) {
      try {
         return persistenciaTiposAusentismos.consultarTipoAusentismo(getEm(), secTiposAusentismos);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarClasesAusentimosTipoAusentismo(BigInteger secTiposAusentismos) {
      try {
         return persistenciaTiposAusentismos.contarClasesAusentimosTipoAusentismo(getEm(), secTiposAusentismos);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposAusentismos contarClasesAusentimosTipoAusentismo ERROR : " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarSOAusentimosTipoAusentismo(BigInteger secTiposAusentismos) {
      try {
         return persistenciaTiposAusentismos.contarSOAusentimosTipoAusentismo(getEm(), secTiposAusentismos);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposAusentismos contarClasesAusentimosTipoAusentismo ERROR : " + e);
         return null;
      }
   }
}
