/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Vacaciones;
import InterfacePersistencia.PersistenciaVacacionesInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaVacaciones implements PersistenciaVacacionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVacaciones.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
    */
   @Override
   public List<Vacaciones> periodoVacaciones(EntityManager em, BigInteger secuenciaEmpleado) {
      try {
         em.clear();
         List<Vacaciones> listaPeriodos;
         String sqlQuery = "SELECT V.*  "
                 + "FROM VACACIONES V, NOVEDADES N "
                 + "WHERE V.NOVEDAD=N.SECUENCIA "
                 + "AND V.DIASPENDIENTES > 0"
                 + " AND N.TIPO = 'VACACION PENDIENTE' "
                 + "AND n.empleado = ? "
                 + "AND V.INICIALCAUSACION>=NVL(EMPLEADOCURRENT_PKG.FechaTipoContrato(N.empleado, SYSDATE),EMPLEADOCURRENT_PKG.FechaTipoContrato(N.empleado, (SELECT FECHA FROM REPORTECURRENT)))";
         Query query = em.createNativeQuery(sqlQuery, Vacaciones.class);
         query.setParameter(1, secuenciaEmpleado);
         listaPeriodos = query.getResultList();
         return listaPeriodos;
      } catch (Exception e) {
         log.error("Error PersistenciaVacaciones.periodoVacaciones ", e);
         return null;
      }
   }

   @Override
   public void adelantarPeriodo(EntityManager em, BigInteger secEmpleado) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "call VACACIONES_PKG.AdelantarPeriodo(?)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secEmpleado);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error persistenciaNovedadesSistema.adelantarPeriodo  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }

   }

   @Override
   public int diasPendientes(EntityManager em, BigInteger secEmpleado, Date fechainicialcausado) {
      try {
         em.clear();
         int diaspendientes;
         String sqlQuery = "SELECT \n"
                 + "      NVL(SUM(diasPendientes),0)\n"
                 + "      FROM vacaciones v, novedades n\n"
                 + "      WHERE n.empleado = ? \n"
                 + "      AND n.secuencia = v.novedad\n"
                 + "      AND tipo = 'VACACION PENDIENTE'\n"
                 + "      AND V.INICIALCAUSACION >=  ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secEmpleado);
         query.setParameter(2, fechainicialcausado);
         diaspendientes = (int) query.getSingleResult();
         return diaspendientes;
      } catch (Exception e) {
         log.error("error en diasPendientes :  ", e);
         return 0;
      }

   }

   @Override
   public BigDecimal consultarJornadaVacaciones(EntityManager em, BigInteger secEmpleado, Date fechainicialDisfrute) {
      try {
         em.clear();
         BigDecimal jornada;
         String sql = "select nvl(j.codigo,1)\n"
                 + "from vigenciasjornadas v, jornadaslaborales j\n"
                 + "where v.empleado = ? \n"
                 + "and j.secuencia = v.jornadatrabajo\n"
                 + "and v.fechavigencia =(select max(fechavigencia) from vigenciasjornadas vj\n"
                 + "                        where vj.empleado = v.empleado\n"
                 + "	                      and vj.fechavigencia<= ?) ";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secEmpleado);
         query.setParameter(2, fechainicialDisfrute);
         jornada = (BigDecimal) query.getSingleResult();
         log.warn("el codigo de la jornada es : " + jornada);
         return jornada;

      } catch (Exception e) {
         log.error("error en consultarJornadaVacaciones :  ", e);
         return BigDecimal.valueOf(1);
      }
   }

   @Override
   public boolean validarFestivoVacaciones(EntityManager em, Date FechaInicialDisfrute, BigDecimal Jornada) {
      boolean esfesitvo = false;
      try {
         em.clear();
         BigDecimal contadorFestivo;
         String sql = "select COUNT(*)\n"
                 + "      from festivos f, paises p\n"
                 + "      where p.secuencia = f.pais\n"
                 + "      AND p.nombre = 'COLOMBIA'"
                 + "      and f.dia = ? ";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, FechaInicialDisfrute);
         contadorFestivo = (BigDecimal) query.getSingleResult();
         if (contadorFestivo.shortValueExact() > 0) {
            esfesitvo = true;
         } else {
            esfesitvo = false;
         }
         return esfesitvo;
      } catch (Exception e) {
         log.error("Error en validarFestivoVacaciones :  ", e);
         return esfesitvo;
      }
   }

   @Override
   public boolean validarDiaLaboralVacaciones(EntityManager em, BigDecimal tipoJornada, String dia) {
      boolean eslaboral = false;
      try {
         em.clear();
         String sql = "SELECT js.dia \n"
                 + "        FROM jornadassemanales js, jornadaslaborales jl \n"
                 + "        WHERE jl.secuencia = js.jornadalaboral \n"
                 + "        AND jl.codigo = ? \n"
                 + "        AND js.dia = ? ";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, tipoJornada);
         query.setParameter(2, dia);
         String diaresultado;
         diaresultado = query.getSingleResult().toString();
         log.warn("diasdasi" + diaresultado);
         if (diaresultado.isEmpty()) {
            eslaboral = true;
         } else {
            eslaboral = false;
         }
         return eslaboral;
      } catch (Exception e) {
         log.error("error en validarDiaLaboralVacaciones :  ", e);
         return true;
      }
   }

   @Override
   public Date siguienteDia(EntityManager em, Date fecha, BigInteger numeroDias, BigDecimal jornada) {
      EntityTransaction tx = em.getTransaction();
      try {
         em.clear();
         tx.begin();
         String sql = "select VACACIONES_PKG.SiguienteDia(?,?,'DIA',?,'COLOMBIA') from dual ";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, fecha);
         query.setParameter(2, numeroDias);
         query.setParameter(3, jornada);
         Date siguienteDia = (Date) query.getSingleResult();
         query.executeUpdate();
         tx.commit();
         return siguienteDia;
      } catch (Exception e) {
         log.error("error en siguiente dia:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         return null;
      }
   }

   @Override
   public BigInteger periodicidadEmpleado(EntityManager em, BigInteger secEmpleado) {
      try {
         em.clear();
         String sql = "select formapago from vwactualesformaspagos where empleado = ?";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secEmpleado);
         BigInteger secPeriodicidad = (BigInteger) query.getSingleResult();
         return secPeriodicidad;
      } catch (Exception e) {
         log.error("error en periodicidadEmpleado ", e);
         return null;
      }

   }

   @Override
   public Date anteriorFechaLimiteCalendario(EntityManager em, Date fechafinvaca, BigInteger secPeriodicidad) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "SELECT DETALLESPERIODICIDADES_PKG.AnteriorFechaLimiteCalendario(?,?) FROM DUAL";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, fechafinvaca);
         query.setParameter(2, secPeriodicidad);
         Date anteriorFechaLimite = (Date) query.getSingleResult();
         query.executeUpdate();
         tx.commit();
         return anteriorFechaLimite;
      } catch (Exception e) {
         log.error("Error persistenciaNovedadesSistema.anteriorFechaLimiteCalendario  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         return null;
      }
   }

   @Override
   public Date despuesFechaLimiteCalendario(EntityManager em, Date fechafinvaca, BigInteger secPeriodicidad) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "SELECT DETALLESPERIODICIDADES_PKG.SiguienteFechaLimiteCalendario(?,?) FROM DUAL";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, fechafinvaca);
         query.setParameter(2, secPeriodicidad);
         Date despuesFechaLimite = (Date) query.getSingleResult();
         query.executeUpdate();
         tx.commit();
         return despuesFechaLimite;
      } catch (Exception e) {
         log.error("Error persistenciaNovedadesSistema.despuesFechaLimiteCalendario  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         return null;
      }
   }

   @Override
   public Date fechaUltimoCorte(EntityManager em, BigInteger secEmpleado, int codigoProceso) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "select cortesprocesos_pkg.CapturarCorteProceso(?,?) from dual";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secEmpleado);
         query.setParameter(2, codigoProceso);
         Date fechaUltimoCorte;
         fechaUltimoCorte = (Date) query.getSingleResult();
         query.executeUpdate();
         tx.commit();
         log.warn("fechaUltimoCorte : " + fechaUltimoCorte);
         return fechaUltimoCorte;
      } catch (Exception e) {
         log.error("Error persistenciaNovedadesSistema.fechaUltimoCorte  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         return null;
      }
   }

   @Override
   public void adicionaVacacionCambiosMasivos(EntityManager em, BigInteger ndias, Date fechaCambio, Date fechaPago) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.AdicionaVacacion");
         query.registerStoredProcedureParameter(1, int.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(3, Date.class, ParameterMode.IN);

         query.setParameter(1, ndias);
         query.setParameter(2, fechaCambio);
         query.setParameter(3, fechaPago);
         query.execute();
      } catch (Exception e) {
         log.error(this.getClass().getName() + ".adicionaVacacionCambiosMasivos() ERROR:  ", e);
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      } finally {
         tx.commit();
      }
   }

   @Override
   public void undoAdicionaVacacionCambiosMasivos(EntityManager em, BigInteger ndias, Date fechaCambio, Date fechaPago) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.UndoAdicionaVacacion");
         query.registerStoredProcedureParameter(1, int.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(3, Date.class, ParameterMode.IN);

         query.setParameter(1, ndias);
         query.setParameter(2, fechaCambio);
         query.setParameter(3, fechaPago);
         query.execute();
      } catch (Exception e) {
         log.error(this.getClass().getName() + ".undoAdicionaVacacionCambiosMasivos() ERROR:  ", e);
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      } finally {
         tx.commit();
      }
   }

}
