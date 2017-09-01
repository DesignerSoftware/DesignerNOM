/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposTrabajadores;
import Entidades.VigenciasDiasTT;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasDiasTTInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposTrabajadores implements AdministrarTiposTrabajadoresInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposTrabajadores.class);

   @EJB
   PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
   @EJB
   PersistenciaVigenciasDiasTTInterface persistenciaVigenciasDiasTT;
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
      log.warn("AdministrarTiposTrabajadores: entro en obtenerConexion");
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public void crearTT(TiposTrabajadores tiposTrabajadores) {
      try {
         if (tiposTrabajadores.getTipocotizante() != null) {
            if (tiposTrabajadores.getTipocotizante().getSecuencia() == null) {
               tiposTrabajadores.setTipocotizante(null);
            }
         }
         persistenciaTiposTrabajadores.crear(getEm(), tiposTrabajadores);
      } catch (Exception e) {
         log.error("AdministrarTiposTrabajadores.crearTT ERROR: " + e);
      }
   }

   @Override
   public void editarTT(TiposTrabajadores tiposTrabajadores) {
      try {
         if (tiposTrabajadores.getTipocotizante() != null) {
            if (tiposTrabajadores.getTipocotizante().getSecuencia() == null) {
               tiposTrabajadores.setTipocotizante(null);
            }
         }
         persistenciaTiposTrabajadores.editar(getEm(), tiposTrabajadores);
      } catch (Exception e) {
         log.error("AdministrarTiposTrabajadores.editarTT ERROR: " + e);
      }
   }

   @Override
   public void borrarTT(TiposTrabajadores tiposTrabajadores) {
      try {
         if (tiposTrabajadores.getTipocotizante() != null) {
            if (tiposTrabajadores.getTipocotizante().getSecuencia() == null) {
               tiposTrabajadores.setTipocotizante(null);
            }
         }
         persistenciaTiposTrabajadores.borrar(getEm(), tiposTrabajadores);
      } catch (Exception e) {
         log.error("AdministrarTiposTrabajadores.borrarTT ERROR: " + e);
      }
   }

   @Override
   public List<TiposTrabajadores> buscarTiposTrabajadoresTT() {
      log.warn("entro en buscarTiposTrabajadoresTT()");
      try {
         return persistenciaTiposTrabajadores.buscarTiposTrabajadores(getEm());
      } catch (Exception e) {
         log.error("AdministrarTiposTrabajadores.buscarTiposTrabajadoresTT ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposTrabajadores buscarTipoTrabajadorSecuencia(BigInteger secuencia) {
      try {
         return persistenciaTiposTrabajadores.buscarTipoTrabajadorSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.error("AdministrarTiposTrabajadores.buscarTipoTrabajadorSecuencia ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposTrabajadores buscarTipoTrabajadorCodigoTiposhort(short codigo) {
      try {
         return persistenciaTiposTrabajadores.buscarTipoTrabajadorCodigoTiposhort(getEm(), codigo);
      } catch (Exception e) {
         log.error("AdministrarTiposTrabajadores.buscarTipoTrabajadorCodigoTiposhort ERROR: " + e);
         return null;
      }
   }

   // Vigencias Dias Tipos Trabajadores: //
   @Override
   public void crearVD(VigenciasDiasTT vigenciasDiasTT) {
      try {
         persistenciaVigenciasDiasTT.crear(getEm(), vigenciasDiasTT);
      } catch (Exception e) {
         log.error("AdministrarTiposTrabajadores.crearVD ERROR: " + e);
      }
   }

   @Override
   public void editarVD(VigenciasDiasTT vigenciasDiasTT) {
      try {
         persistenciaVigenciasDiasTT.editar(getEm(), vigenciasDiasTT);
      } catch (Exception e) {
         log.error("AdministrarTiposTrabajadores.editarVD ERROR: " + e);
      }
   }

   @Override
   public void borrarVD(VigenciasDiasTT vigenciasDiasTT) {
      persistenciaVigenciasDiasTT.borrar(getEm(), vigenciasDiasTT);
      try {
      } catch (Exception e) {
         log.error("AdministrarTiposTrabajadores.borrarVD ERROR: " + e);
      }
   }

   @Override
   public List<VigenciasDiasTT> consultarDiasPorTipoT(BigInteger secuenciaTT) {
      try {
         return persistenciaVigenciasDiasTT.consultarDiasPorTT(getEm(), secuenciaTT);
      } catch (Exception e) {
         log.error("AdministrarTiposTrabajadores.consultarDiasPorTipoT ERROR: " + e);
         return null;
      }
   }

   @Override
   public String clonarTT(String nuevoNombre, Short nuevoCodigo, Short codClonado) {
      try {
         return persistenciaTiposTrabajadores.clonarTipoT(getEm(), nuevoNombre, nuevoCodigo, codClonado);
      } catch (Exception e) {
         log.error("AdministrarTiposTrabajadores.clonarTT ERROR : " + e);
         return ("ERROR AdministrarTiposTrabajadores.clonarTT");
      }
   }

   public boolean hayRegistrosSecundarios(BigInteger secuencia) {
      try {
         return persistenciaTiposTrabajadores.hayRegistrosSecundarios(getEm(), secuencia);
      } catch (Exception e) {
         log.error("AdministrarTiposTrabajadores.hayRegistrosSecundarios ERROR : " + e);
         return false;
      }
   }

}
