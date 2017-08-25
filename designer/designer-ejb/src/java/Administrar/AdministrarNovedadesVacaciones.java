/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import InterfaceAdministrar.AdministrarNovedadesVacacionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaNovedadesSistemaInterface;
import InterfacePersistencia.PersistenciaVacacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
@Local
public class AdministrarNovedadesVacaciones implements AdministrarNovedadesVacacionesInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadesVacaciones.class);

   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
   @EJB
   PersistenciaNovedadesSistemaInterface persistenciaNovedadesSistema;
   @EJB
   PersistenciaVacacionesInterface persistenciaVacaciones;
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
   public List<Empleados> empleadosVacaciones() {
      try {
         return persistenciaEmpleados.buscarEmpleadosActivos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Date obtenerFechaContratacionEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasTiposContratos.fechaFinalContratacionVacaciones(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error obtenerFechaContratacionEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void adelantarPeriodo(BigInteger secEmpleado) {
      try {
         log.warn("Administrar novedades Vacaciones. adelantar Periodo para el empleado : " + secEmpleado);
         persistenciaVacaciones.adelantarPeriodo(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public BigDecimal validarJornadaVacaciones(BigInteger secEmpleado, Date fechaInicialDisfrute) {
      try {
         log.warn("entró a validarJornadaVacaciones");
         log.warn("empleado a consultar : " + secEmpleado);
         log.warn("fecha inicial : " + fechaInicialDisfrute);
         return persistenciaVacaciones.consultarJornadaVacaciones(getEm(), secEmpleado, fechaInicialDisfrute);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public boolean validarFestivoVacaciones(Date fechaInicial, BigDecimal tipoJornada) {
      try {
         return persistenciaVacaciones.validarFestivoVacaciones(getEm(), fechaInicial, tipoJornada);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return false;
      }
   }

   @Override
   public boolean validarDiaLaboralVacaciones(BigDecimal tipoJornada, String dia) {
      try {
         return persistenciaVacaciones.validarDiaLaboralVacaciones(getEm(), tipoJornada, dia);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return false;
      }
   }

   @Override
   public Date fechaSiguiente(Date fecha, BigInteger numeroDias, BigDecimal jornada) {
      try {
         return persistenciaVacaciones.siguienteDia(getEm(), fecha, numeroDias, jornada);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger periodicidadEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVacaciones.periodicidadEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Date anteriorFechaLimite(Date fechafinvaca, BigInteger secPeriodicidad) {
      try {
         return persistenciaVacaciones.anteriorFechaLimiteCalendario(getEm(), fechafinvaca, secPeriodicidad);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Date despuesFechaLimite(Date fechafinvaca, BigInteger secPeriodicidad) {
      try {
         return persistenciaVacaciones.despuesFechaLimiteCalendario(getEm(), fechafinvaca, secPeriodicidad);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Date fechaUltimoCorte(BigInteger secEmpleado, int codigoProceso) {
      try {
         return persistenciaVacaciones.fechaUltimoCorte(getEm(), secEmpleado, codigoProceso);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
