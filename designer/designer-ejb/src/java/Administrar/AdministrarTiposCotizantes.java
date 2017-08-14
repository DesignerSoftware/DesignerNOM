/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposCotizantes;
import InterfaceAdministrar.AdministrarTiposCotizantesInterface;
import InterfacePersistencia.PersistenciaTiposCotizantesInterface;
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
public class AdministrarTiposCotizantes implements AdministrarTiposCotizantesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposCotizantes.class);

   @EJB
   PersistenciaTiposCotizantesInterface persistenciaTiposCotizantes;
   /**
    * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
    * conexión del usuario que está usando el aplicativo.
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
   public List<TiposCotizantes> tiposCotizantes() {
      try {
         return persistenciaTiposCotizantes.lovTiposCotizantes(getEm());
      } catch (Exception e) {
         log.warn("error en AdminsitrarTiposCotizantes.tiposCotizantes : " + e.toString());
         return null;
      }
   }

   @Override
   public void borrarTipoCotizante(List<TiposCotizantes> listBorrar) {
      try {
         for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaTiposCotizantes.borrar(getEm(), listBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTipoCotizante(List<TiposCotizantes> listCrear) {
      try {
         for (int i = 0; i < listCrear.size(); i++) {
            persistenciaTiposCotizantes.crear(getEm(), listCrear.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarTipoCotizante(List<TiposCotizantes> listEditar) {
      try {
         for (int i = 0; i < listEditar.size(); i++) {
            log.warn("Modificando...");
            persistenciaTiposCotizantes.editar(getEm(), listEditar.get(i));
         }
      } catch (Exception e) {
         log.warn("error en modificarTipoCotizante : " + e.toString());
      }
   }

   @Override
   public BigInteger clonarTipoCotizante(BigInteger codOrigen, BigInteger codDestino, String descripcion, BigInteger secClonado) {
      try {
         return persistenciaTiposCotizantes.clonarTipoCotizante(getEm(), codOrigen, codDestino, descripcion, secClonado);
      } catch (Exception e) {
         log.warn("error AdministrarTiposCotizantes.clonarTipoCotizante : " + e.toString());
         return null;
      }
   }
}
