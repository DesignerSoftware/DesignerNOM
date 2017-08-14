package Administrar;

import Entidades.Empleados;
import Entidades.MotivosCambiosSueldos;
import Entidades.Terceros;
import Entidades.TercerosSucursales;
import Entidades.TiposEntidades;
import Entidades.TiposSueldos;
import Entidades.VigenciasAfiliaciones;
import Entidades.VigenciasSueldos;
import InterfaceAdministrar.AdministrarVigenciasSueldosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosCambiosSueldosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTercerosSucursalesInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaTiposSueldosInterface;
import InterfacePersistencia.PersistenciaVigenciasAfiliacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasSueldosInterface;
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
 * @author AndresPineda
 */
@Stateful
public class AdministrarVigenciasSueldos implements AdministrarVigenciasSueldosInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasSueldos.class);

   @EJB
   PersistenciaVigenciasSueldosInterface persistenciaVigenciasSueldos;
   @EJB
   PersistenciaVigenciasAfiliacionesInterface persistenciaVigenciasAfiliaciones;
   @EJB
   PersistenciaTiposSueldosInterface persistenciaTiposSueldos;
   @EJB
   PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
   @EJB
   PersistenciaMotivosCambiosSueldosInterface persistenciaMotivosCambiosSueldos;
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaTercerosSucursalesInterface persistenciaTercerosSucursales;
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
   public List<VigenciasSueldos> VigenciasSueldosEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasSueldos.buscarVigenciasSueldosEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error en Administrar Vigencias Sueldos (VigenciasSueldosEmpleado)");
         return null;
      }
   }

   @Override
   public List<VigenciasSueldos> VigenciasSueldosActualesEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasSueldos.buscarVigenciasSueldosEmpleadoRecientes(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error en Administrar Vigencias Sueldos (VigenciasSueldosActualesEmpleado)");
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
   public void modificarVS(List<VigenciasSueldos> listVSModificadas) {
      try {
         for (int i = 0; i < listVSModificadas.size(); i++) {
            log.warn("Modificando...");
            if (listVSModificadas.get(i).getMotivocambiosueldo().getSecuencia() == null) {
               listVSModificadas.get(i).setMotivocambiosueldo(null);
            }
            if (listVSModificadas.get(i).getTiposueldo().getSecuencia() == null) {
               listVSModificadas.get(i).setTiposueldo(null);
            }
            persistenciaVigenciasSueldos.editar(getEm(), listVSModificadas.get(i));
         }
      } catch (Exception e) {
         log.warn("Error modificarVS AdmiVigenciasSueldos");
      }
   }

   @Override
   public void borrarVS(VigenciasSueldos vigenciasSueldos) {
      try {
         persistenciaVigenciasSueldos.borrar(getEm(), vigenciasSueldos);
      } catch (Exception e) {
         log.warn("Error borrarVS AdmiVigenciasSueldos");
      }
   }

   @Override
   public void crearVS(VigenciasSueldos vigenciasSueldos) {
      try {
         persistenciaVigenciasSueldos.crear(getEm(), vigenciasSueldos);
      } catch (Exception e) {
         log.warn("Error crearVS AdmiVigenciasSueldos");
      }
   }

   @Override
   public Empleados buscarEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleados.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error buscarEmpleado AdmiVigenciasSueldos");
         return null;
      }
   }

   @Override
   public List<VigenciasAfiliaciones> VigenciasAfiliacionesVigencia(BigInteger secVigencia) {
      try {
         return persistenciaVigenciasAfiliaciones.buscarVigenciasAfiliacionesVigenciaSecuencia(getEm(), secVigencia);
      } catch (Exception e) {
         log.warn("Error en Administrar Vigencias Afiliaciones (VigenciasSueldosEmpleado)");
         return null;
      }
   }

   @Override
   public void modificarVA(List<VigenciasAfiliaciones> listVAModificadas) {
      try {
         for (int i = 0; i < listVAModificadas.size(); i++) {
            log.warn("Modificando...");
            if (listVAModificadas.get(i).getTipoentidad().getSecuencia() == null) {
               listVAModificadas.get(i).setTipoentidad(null);
            }
            if (listVAModificadas.get(i).getTercerosucursal().getTercero() == null) {
               listVAModificadas.get(i).getTercerosucursal().setTercero(null);
            }
            persistenciaVigenciasAfiliaciones.editar(getEm(), listVAModificadas.get(i));
         }
      } catch (Exception e) {
         log.warn("Error modificarVA AdmiVigenciasSueldos");
      }
   }

   @Override
   public void borrarVA(VigenciasAfiliaciones vigenciasAfiliaciones) {
      try {
         persistenciaVigenciasAfiliaciones.borrar(getEm(), vigenciasAfiliaciones);
      } catch (Exception e) {
         log.warn("Error borrarVA AdmiVigenciasSueldos");
      }
   }

   @Override
   public void crearVA(VigenciasAfiliaciones vigenciasAfiliaciones) {
      try {
         persistenciaVigenciasAfiliaciones.crear(getEm(), vigenciasAfiliaciones);
      } catch (Exception e) {
         log.warn("Error crearVA AdmiVigenciasSueldos");
      }
   }

   @Override
   public List<TiposSueldos> tiposSueldos() {
      try {
         return persistenciaTiposSueldos.buscarTiposSueldos(getEm());
      } catch (Exception e) {
         log.warn("Error tiposSueldos AdmiVigenciasSueldos");
         return null;
      }
   }

   @Override
   public List<MotivosCambiosSueldos> motivosCambiosSueldos() {
      try {
         return persistenciaMotivosCambiosSueldos.buscarMotivosCambiosSueldos(getEm());
      } catch (Exception e) {
         log.warn("Error motivosCambiosSueldos AdmiVigenciasSueldos");
         return null;
      }
   }

   @Override
   public List<TiposEntidades> tiposEntidades() {
      try {
         return persistenciaTiposEntidades.buscarTiposEntidades(getEm());
      } catch (Exception e) {
         log.warn("Error tiposEntidades AdmiVigenciasSueldos");
         return null;
      }
   }

   @Override
   public List<Terceros> terceros() {
      try {
         return persistenciaTerceros.buscarTerceros(getEm());
      } catch (Exception e) {
         log.warn("Error terceros AdmiVigenciasSueldos");
         return null;
      }
   }

   @Override
   public List<TercerosSucursales> tercerosSucursales(BigInteger secuencia) {
      try {
         return persistenciaTercerosSucursales.buscarTercerosSucursalesPorTerceroSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error tercerosSucursales AdmiVigenciasSueldos");
         return null;
      }
   }

   @Override
   public void crearTerceroSurcursal(TercerosSucursales tercerosSucursales) {
      try {
         persistenciaTercerosSucursales.crear(getEm(), tercerosSucursales);
      } catch (Exception e) {
         log.warn("Error crearTerceroSucursal AdmiVigenciasSueldos");
      }
   }
}
