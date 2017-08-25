package Administrar;

import Entidades.ActualUsuario;
import Entidades.Cuadrillas;
import Entidades.Empleados;
import Entidades.ParametrosTiempos;
import InterfaceAdministrar.AdministrarATParametroTiempoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaCuadrillasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaParametrosTiemposInterface;
import InterfacePersistencia.PersistenciaTurnosEmpleadosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
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
public class AdministrarATParametroTiempo implements AdministrarATParametroTiempoInterface {

   private static Logger log = Logger.getLogger(AdministrarATParametroTiempo.class);

   @EJB
   PersistenciaCuadrillasInterface persistenciaCuadrillas;
   @EJB
   PersistenciaParametrosTiemposInterface persistenciaParametrosTiempos;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleadosInterface;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaTurnosEmpleadosInterface persistenciaTurnosEmpleados;
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
   //@Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Empleados> lovEmpleados() {
      try {
         return persistenciaEmpleadosInterface.buscarEmpleadosActivos(getEm());
      } catch (Exception e) {
         log.warn("Error lovEmpleados Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ParametrosTiempos consultarParametrosTiemposPorUsarioBD(String usuarioBD) {
      try {
         return persistenciaParametrosTiempos.buscarParametrosTiemposPorUsuarioBD(getEm(), usuarioBD);
      } catch (Exception e) {
         log.warn("Error consultarParametrosTiemposPorUsarioBD Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ActualUsuario obtenerActualUsuario() {
      try {
         return persistenciaActualUsuario.actualUsuarioBD(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerActualUsuario Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Cuadrillas> lovCuadrillas() {
      try {
         return persistenciaCuadrillas.buscarCuadrillas(getEm());
      } catch (Exception e) {
         log.warn("Error lovCuadrillas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerFechaInicialMinTurnosEmpleados() {
      try {
         return persistenciaTurnosEmpleados.obtenerFechaInicialMinimaTurnosEmpleados(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerFechaInicialMinTurnosEmpleados Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerFechaFinalMaxTurnosEmpleados() {
      try {
         Date fecha = persistenciaTurnosEmpleados.obtenerFechaFinalMaximaTurnosEmpleados(getEm());
         return fecha;
      } catch (Exception e) {
         log.warn("Error obtenerFechaFinalMaxTurnosEmpleados Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void ejecutarPKG_INSERTARCUADRILLA(BigInteger cuadrilla, Date fechaDesde, Date fechaHasta) {
      try {
         persistenciaParametrosTiempos.ejecutarPKG_INSERTARCUADRILLA(getEm(), cuadrilla, fechaDesde, fechaHasta);
      } catch (Exception e) {
         log.warn("Error ejecutarPKG_INSERTARCUADRILLA Admi : " + e.toString());
      }
   }

   @Override
   public void ejecutarPKG_SIMULARTURNOSEMPLEADOS(Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta) {
      try {
         persistenciaParametrosTiempos.ejecutarPKG_SIMULARTURNOSEMPLEADOS(getEm(), fechaDesde, fechaHasta, emplDesde, emplHasta);
      } catch (Exception e) {
         log.warn("Error ejecutarPKG_SIMULARTURNOSEMPLEADOS Admi : " + e.toString());
      }
   }

   @Override
   public void ejecutarPKG_LIQUIDAR(Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta, String formulaLiquidacion) {
      try {
         persistenciaParametrosTiempos.ejecutarPKG_LIQUIDAR(getEm(), fechaDesde, fechaHasta, emplDesde, emplHasta, formulaLiquidacion);
      } catch (Exception e) {
         log.warn("Error ejecutarPKG_LIQUIDAR Admi : " + e.toString());
      }
   }

   @Override
   public void ejecutarPKG_EliminarProgramacion(Date fechaDesde, Date fechaHasta) {
      try {
         persistenciaParametrosTiempos.ejecutarPKG_EliminarProgramacion(getEm(), fechaDesde, fechaHasta);
      } catch (Exception e) {
         log.warn("Error ejecutarPKG_EliminarProgramacion Admi : " + e.toString());
      }
   }

   @Override
   public void ejecutarPKG_ELIMINARSIMULACION(BigInteger cuadrilla, Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta) {
      try {
         persistenciaParametrosTiempos.ejecutarPKG_ELIMINARSIMULACION(getEm(), cuadrilla, fechaDesde, fechaHasta, emplDesde, emplHasta);
      } catch (Exception e) {
         log.warn("Error ejecutarPKG_ELIMINARSIMULACION Admi : " + e.toString());
      }
   }

   @Override
   public int ejecutarPKG_CONTARNOVEDADESLIQ(Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta) {
      try {
         int conteo = persistenciaTurnosEmpleados.ejecutarPKG_CONTARNOVEDADESLIQ(getEm(), fechaDesde, fechaHasta, emplDesde, emplHasta);
         return conteo;
      } catch (Exception e) {
         log.warn("Error ejecutarPKG_CONTARNOVEDADESLIQ Admi : " + e.toString());
         return -1;
      }
   }

   @Override
   public void ejecutarPKG_ELIMINARLIQUIDACION(Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta) {
      try {
         persistenciaTurnosEmpleados.ejecutarPKG_ELIMINARLIQUIDACION(getEm(), fechaDesde, fechaHasta, emplDesde, emplHasta);
      } catch (Exception e) {
         log.warn("Error ejecutarPKG_ELIMINARLIQUIDACION Admi : " + e.toString());
      }
   }

   @Override
   public void modificarParametroTiempo(ParametrosTiempos parametro) {
      try {
         persistenciaParametrosTiempos.editar(getEm(), parametro);
      } catch (Exception e) {
         log.warn("Error modificarParametroTiempo Admi : " + e.toString());
      }
   }

}
