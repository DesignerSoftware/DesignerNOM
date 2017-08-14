package Administrar;

import Entidades.Cuadrillas;
import Entidades.DetallesTurnosRotativos;
import Entidades.Empleados;
import Entidades.Turnosrotativos;
import InterfaceAdministrar.AdministrarATCuadrillaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCuadrillasInterface;
import InterfacePersistencia.PersistenciaDetallesTurnosRotativosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaTurnosRotativosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarATCuadrilla implements AdministrarATCuadrillaInterface {

   private static Logger log = Logger.getLogger(AdministrarATCuadrilla.class);

   @EJB
   PersistenciaCuadrillasInterface persistenciaCuadrillas;
   @EJB
   PersistenciaTurnosRotativosInterface persistenciaTurnosRotativos;
   @EJB
   PersistenciaDetallesTurnosRotativosInterface persistenciaDetallesTurnosRotativos;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;

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

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Cuadrillas> obtenerCuadrillas() {
      try {
         List<Cuadrillas> lista = persistenciaCuadrillas.buscarCuadrillas(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error obtenerCuadrillas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearCuadrillas(List<Cuadrillas> listaC) {
      try {
         for (int i = 0; i < listaC.size(); i++) {
            persistenciaCuadrillas.crear(getEm(), listaC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearCuadrillas Admi : " + e.toString());
      }
   }

   @Override
   public void editarCuadrillas(List<Cuadrillas> listaC) {
      try {
         for (int i = 0; i < listaC.size(); i++) {
            persistenciaCuadrillas.editar(getEm(), listaC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearCuadrillas Admi : " + e.toString());
      }
   }

   @Override
   public void borrarCuadrillas(List<Cuadrillas> listaC) {
      try {
         for (int i = 0; i < listaC.size(); i++) {
            persistenciaCuadrillas.borrar(getEm(), listaC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearCuadrillas Admi : " + e.toString());
      }
   }

   @Override
   public List<Turnosrotativos> obtenerTurnosRotativos(BigInteger secuencia) {
      try {
         List<Turnosrotativos> lista = persistenciaTurnosRotativos.buscarTurnosRotativosPorCuadrilla(getEm(), secuencia);
         return lista;
      } catch (Exception e) {
         log.warn("Error obtenerTurnosRotativos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearTurnosRotativos(List<Turnosrotativos> listaTR) {
      try {
         for (int i = 0; i < listaTR.size(); i++) {
            persistenciaTurnosRotativos.crear(getEm(), listaTR.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearTurnosRotativos Admi : " + e.toString());
      }
   }

   @Override
   public void editarTurnosRotativos(List<Turnosrotativos> listaTR) {
      try {
         for (int i = 0; i < listaTR.size(); i++) {
            persistenciaTurnosRotativos.editar(getEm(), listaTR.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarTurnosRotativos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarTurnosRotativos(List<Turnosrotativos> listaTR) {
      try {
         for (int i = 0; i < listaTR.size(); i++) {
            persistenciaTurnosRotativos.borrar(getEm(), listaTR.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarTurnosRotativos Admi : " + e.toString());
      }
   }

   @Override
   public List<DetallesTurnosRotativos> obtenerDetallesTurnosRotativos(BigInteger secuencia) {
      try {
         List<DetallesTurnosRotativos> lista = persistenciaDetallesTurnosRotativos.buscarDetallesTurnosRotativosPorTurnoRotativo(getEm(), secuencia);
         return lista;
      } catch (Exception e) {
         log.warn("Error obtenerDetallesTurnosRotativos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearDetallesTurnosRotativos(List<DetallesTurnosRotativos> listaDTR) {
      try {
         log.warn("Here");
         for (int i = 0; i < listaDTR.size(); i++) {
            log.warn("Here Again For : " + i);
            persistenciaDetallesTurnosRotativos.crear(getEm(), listaDTR.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearDetallesTurnosRotativos Admi : " + e.toString());
      }
   }

   @Override
   public void editarDetallesTurnosRotativos(List<DetallesTurnosRotativos> listaDTR) {
      try {
         for (int i = 0; i < listaDTR.size(); i++) {
            persistenciaDetallesTurnosRotativos.editar(getEm(), listaDTR.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarDetallesTurnosRotativos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarDetallesTurnosRotativos(List<DetallesTurnosRotativos> listaDTR) {
      try {
         for (int i = 0; i < listaDTR.size(); i++) {
            persistenciaDetallesTurnosRotativos.borrar(getEm(), listaDTR.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarDetallesTurnosRotativos Admi : " + e.toString());
      }
   }

   @Override
   public List<Empleados> lovEmpleados() {
      try {
         List<Empleados> lista = persistenciaEmpleados.buscarEmpleados(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error lovEmpleados Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void borrarProgramacionCompleta() {
      try {
         persistenciaCuadrillas.borrarProgramacionCompleta(getEm());
      } catch (Exception e) {
         log.warn("Error borrarProgramacionCompleta Admi : " + e.toString());
      }
   }

   //@Override
   public List<Empleados> consultarEmpleadosProcesoBuscarEmpleadosCuadrillas() {
      try {
         List<Empleados> lista = persistenciaEmpleados.consultarEmpleadosCuadrillas(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error consultarEmpleadosProcesoBuscarEmpleadosCuadrillas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Cuadrillas> obtenerInfoCuadrillasPorEmpleado(BigInteger secuencia) {
      try {
         List<Cuadrillas> lista = persistenciaCuadrillas.buscarCuadrillasParaEmpleado(getEm(), secuencia);
         return lista;
      } catch (Exception e) {
         log.warn("Error obtenerInfoCuadrillasPorEmpleado Admi : " + e.toString());
         return null;
      }
   }

}
