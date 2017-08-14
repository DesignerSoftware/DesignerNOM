/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposFamiliares;
import InterfacePersistencia.PersistenciaTiposFamiliaresInterface;
import InterfaceAdministrar.AdministrarTiposFamiliaresInterface;
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
public class AdministrarTiposFamiliares implements AdministrarTiposFamiliaresInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposFamiliares.class);

   @EJB
   PersistenciaTiposFamiliaresInterface persistenciaTiposFamiliares;
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
   public void modificarTiposFamiliares(List<TiposFamiliares> listTiposFamiliares) {
      try {
         for (int i = 0; i < listTiposFamiliares.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposFamiliares.editar(getEm(), listTiposFamiliares.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposFamiliares(List<TiposFamiliares> listTiposFamiliares) {
      try {
         for (int i = 0; i < listTiposFamiliares.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposFamiliares.borrar(getEm(), listTiposFamiliares.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposFamiliares(List<TiposFamiliares> listTiposFamiliares) {
      try {
         for (int i = 0; i < listTiposFamiliares.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposFamiliares.crear(getEm(), listTiposFamiliares.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposFamiliares> consultarTiposFamiliares() {
      try {
         return persistenciaTiposFamiliares.buscarTiposFamiliares(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposFamiliares consultarTipoExamen(BigInteger secTipoEmpresa) {
      try {
         return persistenciaTiposFamiliares.buscarTiposFamiliares(getEm(), secTipoEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarHvReferenciasTipoFamiliar(BigInteger secuenciaTiposFamiliares) {
      try {
         log.error("Secuencia Borrado Elementos" + secuenciaTiposFamiliares);
         return persistenciaTiposFamiliares.contadorHvReferencias(getEm(), secuenciaTiposFamiliares);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposFamiliares verificarBorradoElementos ERROR :" + e);
         return null;
      }
   }

}
