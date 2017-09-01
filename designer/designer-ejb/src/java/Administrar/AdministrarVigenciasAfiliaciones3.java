package Administrar;

import Entidades.Empleados;
import Entidades.EstadosAfiliaciones;
import Entidades.Terceros;
import Entidades.TercerosSucursales;
import Entidades.TiposEntidades;
import Entidades.VigenciasAfiliaciones;
import InterfaceAdministrar.AdministrarVigenciasAfiliaciones3Interface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstadosAfiliacionesInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTercerosSucursalesInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaVigenciasAfiliacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarVigenciasAfiliaciones3 implements AdministrarVigenciasAfiliaciones3Interface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasAfiliaciones3.class);

   @EJB
   PersistenciaVigenciasAfiliacionesInterface persistenciaVigenciasAfilicaciones;
   @EJB
   PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   @EJB
   PersistenciaEstadosAfiliacionesInterface persistenciaEstadosAfiliaciones;
   @EJB
   PersistenciaTercerosSucursalesInterface persistenciaTercerosSucursales;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
   @EJB
   PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   //
//    List<VigenciasAfiliaciones> listVigenciasAfiliaciones;
//    List<Terceros> listTercetos;
//    List<EstadosAfiliaciones> listEstadosAfiliaciones;
//    List<TiposEntidades> listTiposEntidades;
//    List<TercerosSucursales> listTercerosSucursales;
//    Empleados empleado;
//   Date fechaContratacion;
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
   public void crearVigenciaAfiliacion(VigenciasAfiliaciones vigencia) {
      try {
         log.warn("AdministrarVigenciasAfiliaciones3.crearVigenciaAfiliacion");
         log.warn("Secuencia empleado: " + vigencia.getEmpleado());
         persistenciaVigenciasAfilicaciones.crear(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error crearVigenciaAfiliacion Admi : " + e.toString());
      }
   }

   @Override
   public void editarVigenciaAfiliacion(VigenciasAfiliaciones vigencia) {
      try {
         persistenciaVigenciasAfilicaciones.editar(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error editarVigenciaAfiliacion Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciaAfiliacion(VigenciasAfiliaciones vigencia) {
      try {
         persistenciaVigenciasAfilicaciones.borrar(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error borrarVigenciaAfiliacion Admi : " + e.toString());
      }
   }

   @Override
   public List<VigenciasAfiliaciones> listVigenciasAfiliacionesEmpleado(BigInteger secuencia) {
      try {
         return persistenciaVigenciasAfilicaciones.buscarVigenciasAfiliacionesEmpleado(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error listVigenciasAfiliacionesEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public VigenciasAfiliaciones vigenciaAfiliacionSecuencia(BigInteger secuencia) {
      try {
         return persistenciaVigenciasAfilicaciones.buscarVigenciasAfiliacionesSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error vigenciaAfiliacionSecuencia Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Terceros> listTerceros() {
      try {
         return persistenciaTerceros.buscarTerceros(getEm());
      } catch (Exception e) {
         log.warn("Error listTerceros Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<EstadosAfiliaciones> listEstadosAfiliaciones() {
      try {
         return persistenciaEstadosAfiliaciones.buscarEstadosAfiliaciones(getEm());
      } catch (Exception e) {
         log.warn("Error listEstadosAfiliaciones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposEntidades> listTiposEntidades() {
      try {
         return persistenciaTiposEntidades.buscarTiposEntidades(getEm());
      } catch (Exception e) {
         log.warn("Error listTiposEntidades Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TercerosSucursales> listTercerosSucursales() {
      try {
         return persistenciaTercerosSucursales.buscarTercerosSucursales(getEm());
      } catch (Exception e) {
         log.warn("Error listTercerosSucursales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Empleados obtenerEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleado(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error obtenerEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Long validacionTercerosSurcursalesNuevaVigencia(BigInteger secuencia, Date fechaInicial, BigInteger secuenciaTE, BigInteger secuenciaTer) {
      try {
         return persistenciaSolucionesNodos.validacionTercerosVigenciaAfiliacion(getEm(), secuencia, fechaInicial, secuenciaTE, secuenciaTer);
      } catch (Exception e) {
         log.warn("Error validacionTercerosSurcursales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date fechaContratacion(Empleados empleado) {
      try {
         return persistenciaVigenciasTiposContratos.fechaMaxContratacion(getEm(), empleado);
      } catch (Exception e) {
         log.warn("Error fechaContratacion Admi : " + e.toString());
         return null;
      }
   }
}
