/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Motivosmvrs;
import Entidades.Mvrs;
import Entidades.OtrosCertificados;
import Entidades.TiposCertificados;
import InterfaceAdministrar.AdministrarEmplMvrsInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosMvrsInterface;
import InterfacePersistencia.PersistenciaMvrsInterface;
import InterfacePersistencia.PersistenciaOtrosCertificadosInterface;
import InterfacePersistencia.PersistenciaTiposCertificadosInterface;
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
public class AdministrarEmplMvrs implements AdministrarEmplMvrsInterface {

   private static Logger log = Logger.getLogger(AdministrarEmplMvrs.class);

   @EJB
   PersistenciaOtrosCertificadosInterface persistenciaOtrosCertificados;
   @EJB
   PersistenciaMvrsInterface persistenciaMvrs;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaMotivosMvrsInterface persistenciaMotivos;
   @EJB
   PersistenciaTiposCertificadosInterface persistenciaTiposCertificados;
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

   @Override
   public List<OtrosCertificados> listOtrosCertificadosEmpleado(BigInteger secuencia) {
      try {
         return persistenciaOtrosCertificados.buscarOtrosCertificadosEmpleado(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error listOtrosCertificadosEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Mvrs> listMvrsEmpleado(BigInteger secuenciaEmpl) {
      try {
         return persistenciaMvrs.buscarMvrsEmpleado(getEm(), secuenciaEmpl);
      } catch (Exception e) {
         log.warn("Error listMvrsEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearOtrosCertificados(List<OtrosCertificados> crearOtrosCertificados) {
      try {
         for (int i = 0; i < crearOtrosCertificados.size(); i++) {
            if (crearOtrosCertificados.get(i).getTipocertificado().getSecuencia() == null) {
               crearOtrosCertificados.get(i).setTipocertificado(null);
            }
            persistenciaOtrosCertificados.crear(getEm(), crearOtrosCertificados.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en crearOtrosCertificados Admi : " + e.toString());
      }
   }

   @Override
   public void modificarOtrosCertificados(List<OtrosCertificados> modificarOtrosCertificados) {
      try {
         for (int i = 0; i < modificarOtrosCertificados.size(); i++) {
            if (modificarOtrosCertificados.get(i).getTipocertificado().getSecuencia() == null) {
               modificarOtrosCertificados.get(i).setTipocertificado(null);
            }
            persistenciaOtrosCertificados.editar(getEm(), modificarOtrosCertificados.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en modificarOtrosCertificados Admi : " + e.toString());
      }
   }

   @Override
   public void borrarOtrosCertificados(List<OtrosCertificados> borrarOtrosCertificados) {
      try {
         for (int i = 0; i < borrarOtrosCertificados.size(); i++) {
            if (borrarOtrosCertificados.get(i).getTipocertificado().getSecuencia() == null) {
               borrarOtrosCertificados.get(i).setTipocertificado(null);
            }
            persistenciaOtrosCertificados.borrar(getEm(), borrarOtrosCertificados.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en borrarOtrosCertificados Admi : " + e.toString());
      }
   }

   @Override
   public void crearMvrs(List<Mvrs> crearMvrs) {
      try {
         for (int i = 0; i < crearMvrs.size(); i++) {
            if (crearMvrs.get(i).getMotivo().getSecuencia() == null) {
               crearMvrs.get(i).setMotivo(null);
            }
            persistenciaMvrs.crear(getEm(), crearMvrs.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en crearMvrs Admi : " + e.toString());
      }
   }

   @Override
   public void modificarMvrs(List<Mvrs> modificarMvrs) {
      try {
         for (int i = 0; i < modificarMvrs.size(); i++) {
            if (modificarMvrs.get(i).getMotivo().getSecuencia() == null) {
               modificarMvrs.get(i).setMotivo(null);
            }
            persistenciaMvrs.editar(getEm(), modificarMvrs.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en modificarMvrs Admi : " + e.toString());
      }
   }

   @Override
   public void borrarMvrs(List<Mvrs> borrarMvrs) {
      try {
         for (int i = 0; i < borrarMvrs.size(); i++) {
            if (borrarMvrs.get(i).getMotivo().getSecuencia() == null) {
               borrarMvrs.get(i).setMotivo(null);
            }
            persistenciaMvrs.borrar(getEm(), borrarMvrs.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en borrarMvrs Admi : " + e.toString());
      }
   }

   @Override
   public List<Motivosmvrs> listMotivos() {
      try {
         return persistenciaMotivos.buscarMotivosMvrs(getEm());
      } catch (Exception e) {
         log.warn("Error listMotivos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposCertificados> listTiposCertificados() {
      try {
         return persistenciaTiposCertificados.buscarTiposCertificados(getEm());
      } catch (Exception e) {
         log.warn("Error listTiposCertificados Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Empleados empleadoActual(BigInteger secuencia) {
      try {
         return persistenciaEmpleados.buscarEmpleado(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error empleadoActual Admi : " + e.toString());
         return null;
      }
   }
}
