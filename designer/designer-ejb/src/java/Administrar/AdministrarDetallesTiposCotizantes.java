/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.DetallesTiposCotizantes;
import Entidades.TiposEntidades;
import InterfaceAdministrar.AdministrarDetallesTiposCotizantesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDetallesTiposCotizantesInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
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
public class AdministrarDetallesTiposCotizantes implements AdministrarDetallesTiposCotizantesInterface {

   private static Logger log = Logger.getLogger(AdministrarDetallesTiposCotizantes.class);

   @EJB
   PersistenciaDetallesTiposCotizantesInterface persistenciaDetallesTiposCotizantes;
   @EJB
   PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
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

   public List<DetallesTiposCotizantes> detallesTiposCotizantes(BigInteger secuenciaTipoCotizante) {
      try {
         return persistenciaDetallesTiposCotizantes.detallesTiposCotizantes(getEm(), secuenciaTipoCotizante);
      } catch (Exception e) {
         log.warn("error en detallesTiposCotizantes : " + e.toString());
         return null;
      }
   }

   @Override
   public void borrarDetalleTipoCotizante(List<DetallesTiposCotizantes> listaBorrar) {
      try {
         for (int t = 0; t < listaBorrar.size(); t++) {
            persistenciaDetallesTiposCotizantes.borrar(getEm(), listaBorrar.get(t));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearDetalleTipoCotizante(List<DetallesTiposCotizantes> listaCrear) {
      try {
         for (int t = 0; t < listaCrear.size(); t++) {
            persistenciaDetallesTiposCotizantes.borrar(getEm(), listaCrear.get(t));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarDetalleTipoCotizante(List<DetallesTiposCotizantes> listaDetallesTiposCotizantesModificar) {
      try {
         for (int i = 0; i < listaDetallesTiposCotizantesModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaDetallesTiposCotizantesModificar.get(i).getTipocotizante().getSecuencia() == null) {
               listaDetallesTiposCotizantesModificar.get(i).setTipocotizante(null);
            }
            if (listaDetallesTiposCotizantesModificar.get(i).getTipoentidad().getSecuencia() == null) {
               listaDetallesTiposCotizantesModificar.get(i).setTipoentidad(null);
            }
            if (listaDetallesTiposCotizantesModificar.get(i).getMinimosml() == null) {
               listaDetallesTiposCotizantesModificar.get(i).setMinimosml(null);
            }
            if (listaDetallesTiposCotizantesModificar.get(i).getMaximosml() == null) {
               listaDetallesTiposCotizantesModificar.get(i).setMaximosml(null);
            }
            persistenciaDetallesTiposCotizantes.editar(getEm(), listaDetallesTiposCotizantesModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposEntidades> lovTiposEntidades() {
      try {
         return persistenciaTiposEntidades.buscarTiposEntidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
