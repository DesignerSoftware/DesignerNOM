/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import ClasesAyuda.ExtraeCausaExcepcion;
import Entidades.InterconAux;
import Entidades.InterconTotal;
import InterfacePersistencia.PersistenciaInterconTotalInterface;
import excepciones.ExcepcionBD;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaInterconTotal implements PersistenciaInterconTotalInterface {

    private static Logger log = Logger.getLogger(PersistenciaInterconTotal.class);

    @Override
    public void crear(EntityManager em, InterconTotal interconTotal) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(interconTotal);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.crear: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, InterconTotal interconTotal) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(interconTotal);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.editar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, InterconTotal interconTotal) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(interconTotal));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.borrar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public InterconTotal buscarInterconTotalSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            Query query = em.createQuery("SELECT i FROM InterconTotal i WHERE i.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            InterconTotal interconTotal = (InterconTotal) query.getSingleResult();
            return interconTotal;
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.buscarInterconTotalSecuencia: " + e.toString());
            return null;
        }
    }

    @Override
    public List<InterconTotal> buscarInterconTotalParaParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal) {
        try {
            em.clear();
            String sql = "select * from intercon_total i where fechacontabilizacion between ? and ?\n"
                    + " and FLAG = 'CONTABILIZADO' AND SALIDA <> 'NETO'\n"
                    + " and exists (select 'x' from empleados e where e.secuencia=i.empleado)";
            Query query = em.createNativeQuery(sql, InterconTotal.class);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            List<InterconTotal> interconTotal = query.getResultList();
            if (interconTotal != null) {
                if (!interconTotal.isEmpty()) {
                    em.clear();
                    Query query2 = em.createNativeQuery(" select i.secuencia secuencia, pro.descripcion nombreproceso,per.primerapellido||' '||per.segundoapellido||' '||per.nombre nombreempleado from intercon_total i,empleados em,personas per,procesos pro where \n"
                            + " i.proceso=pro.secuencia and em.persona=per.secuencia\n"
                            + " and i.empleado=em.secuencia and fechacontabilizacion between ? and ? and FLAG = 'CONTABILIZADO' AND SALIDA <> 'NETO'\n"
                            + " and exists (select 'x' from empleados e where e.secuencia=i.empleado) ", InterconAux.class);
                    query2.setParameter(1, fechaInicial);
                    query2.setParameter(2, fechaFinal);
                    List<InterconAux> listaAux = query2.getResultList();
                    log.warn("PersistenciaAportesEntidades.consultarAportesEntidadesPorEmpresaMesYAnio() Ya consulo Transients");
                    if (listaAux != null) {
                        if (!listaAux.isEmpty()) {
                            log.warn("PersistenciaInterconInfor.buscarInterconInforParametroContable() listaAux.size(): " + listaAux.size());
                            for (InterconAux recAux : listaAux) {
                                for (InterconTotal recAporte : interconTotal) {
                                    if (recAux.getSecuencia().equals(recAporte.getSecuencia())) {
                                        recAporte.llenarTransients(recAux);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return interconTotal;
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.buscarInterconTotalParaParametroContable: " + e.toString());
            return null;
        }
    }

    @Override
    public Date obtenerFechaContabilizacionMaxInterconTotal(EntityManager em
    ) {
        try {
            String sql = "select max(i.fechacontabilizacion) from intercon_total i "
                    + " where flag = 'ENVIADO' and exists (select 'x' from  empleados e\n"
                    + " where e.secuencia=i.empleado )";
            Query query = em.createNativeQuery(sql);
            Date fecha = (Date) query.getSingleResult();
            return fecha;
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.obtenerFechaContabilizacionMaxInterconTotal : " + e.toString());
            return null;
        }
    }

    @Override
    public void actualizarFlagInterconTotal(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa
    ) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "UPDATE intercon_TOTAL SET FLAG = 'CONTABILIZADO' \n"
                    + " WHERE FECHACONTABILIZACION BETWEEN ? AND ? \n"
                    + " AND FLAG = 'ENVIADO' \n"
                    + " AND intercon_total.empresa_codigo=? \n"
                    + " AND EXISTS (SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA=INTERCON_TOTAL.EMPLEADO)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, empresa);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.actualizarFlagInterconTotal. " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void actualizarFlagInterconTotalProcesoDeshacer(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso
    ) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "UPDATE CONTABILIZACIONES SET FLAG='GENERADO' WHERE FLAG='CONTABILIZADO'\n"
                    + " AND FECHAGENERACION BETWEEN ? AND ? \n"
                    + " and exists (select 'x' from vwfempleados e, solucionesnodos sn where sn.empleado=e.secuencia and sn.secuencia=contabilizaciones.solucionnodo\n"
                    + " and sn.proceso=nvl(?,sn.proceso))";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, proceso);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.actualizarFlagInterconTotalProcesoDeshacer. " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void eliminarInterconTotal(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso
    ) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "DELETE INTERCON_TOTAL\n"
                    + " WHERE FECHACONTABILIZACION BETWEEN ? AND ?\n"
                    + " AND FLAG='CONTABILIZADO'\n"
                    + " and intercon_total.empresa_codigo=?\n"
                    + " AND nvl(INTERCON_TOTAL.PROCESO,0) = NVL(?,nvl(INTERCON_TOTAL.PROCESO,0))\n"
                    + " and exists (select 'x' from empleados e where e.secuencia=intercon_total.empleado)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, empresa);
            query.setParameter(4, proceso);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.eliminarInterconTotal. " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void ejecutarPKGUbicarnuevointercon_total(EntityManager em, BigInteger secuencia, Date fechaInicial, Date fechaFinal, BigInteger proceso
    ) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call INTERFASETOTAL$PKG.Ubicarnuevointercon_total(?,?,?,?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, fechaFinal);
            query.setParameter(4, proceso);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.ejecutarPKGUbicarnuevointercon_total. " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public int contarProcesosContabilizadosInterconTotal(EntityManager em, Date fechaInicial, Date fechaFinal
    ) {
        try {
            em.clear();
            String sql = "select COUNT(*) from intercon_total i where\n"
                    + " i.fechacontabilizacion between ? and ? \n"
                    + " and i.flag = 'CONTABILIZADO'";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            BigDecimal contador = (BigDecimal) query.getSingleResult();
            if (contador != null) {
                return contador.intValue();
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.contarProcesosContabilizadosInterconTotal. " + e.toString());
            return -1;
        }
    }

    public void ejecutarCierrePeriodoContableInterconTotal(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "UPDATE INTERCON_TOTAL I SET I.FLAG='ENVIADO'\n"
                    + " WHERE  \n"
                    + " I.FECHACONTABILIZACION BETWEEN ? AND ?\n"
                    + " and nvl(i.proceso,0) = nvl(?,nvl(i.proceso,0))\n"
                    + " and i.empresa_codigo=?\n"
                    + " and exists (select 'x' from empleados e where e.secuencia=i.empleado)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, proceso);
            query.setParameter(4, empresa);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.ejecutarCierrePeriodoContableInterconTotal. " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void cerrarProcesoContabilizacion(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "UPDATE INTERCON_TOTAL I SET I.FLAG='ENVIADO' WHERE  \n"
                    + "     I.FECHACONTABILIZACION BETWEEN ? AND ?\n"
                    + "     and nvl(i.proceso,0) = nvl(?,nvl(i.proceso,0))\n"
                    + "     and i.empresa_codigo=?"
                    + "   and exists (select 'x' from empleados e where e.secuencia=i.empleado)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, proceso);
            query.setParameter(4, empresa);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.cerrarProcesoContabilizacion. " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void ejecutarPKGRecontabilizacion(EntityManager em, Date fechaIni, Date fechaFin) throws ExcepcionBD {
        EntityTransaction tx = null;
        try {
            em.clear();
            tx = em.getTransaction();
            tx.begin();
            String sql = "call INTERFASETOTAL$PKG.Recontabilizacion(?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.executeUpdate();
            tx.commit();
        } catch (Exception pe) {
            log.error("Error en la persistencia causado por: " + pe.toString());
            throw new ExcepcionBD(ExtraeCausaExcepcion.obtenerMensajeSQLException(pe));
        } finally {
            if (tx != null) {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
        }
    }

    @Override
    public String ejecutarPKGCrearArchivoPlano(EntityManager em, int tipoTxt, Date fechaIni, Date fechaFin, BigInteger proceso, String nombreArchivo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "";
            if (tipoTxt == 1) {
                sql = "call INTERFASETOTAL$PKG.archivo_plano(?,?,?,?)";
            } else if (tipoTxt == 2) {
                sql = "call INTERFASETOTAL$PKG.archivoplanosemidetallado(?,?,?,?)";
            } else if (tipoTxt == 3) {
                sql = "call INTERFASETOTAL$PKG.archivoplanoresumido(?,?,?,?)";
            }
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, proceso);
            query.setParameter(4, nombreArchivo);
            query.executeUpdate();
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.ejecutarPKGCrearArchivoPlano : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof FileNotFoundException) {
                return e.toString();
            } else {
                return "Ha ocurrido un error al crear el Archivo Plano";
            }
        }
    }

    @Override
    public void ejecutarPKGCrearArchivoPlano_GEO(EntityManager em, int tipoTxt, Date fechaIni, Date fechaFin, BigInteger proceso, String nombreArchivo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "";
            if (tipoTxt == 1) {
                sql = "call INTERFASETOTAL$PKG.archivo_planoGEO(?,?,?,?)";
            } else if (tipoTxt == 2) {
                sql = "call INTERFASETOTAL$PKG.archivoplanosemidetalladoGEO(?,?,?,?)";
            } else if (tipoTxt == 3) {
                sql = "call INTERFASETOTAL$PKG.archivoplanoresumidoGEO(?,?,?,?)";
            }
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, proceso);
            query.setParameter(4, nombreArchivo);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconTotal.ejecutarPKGCrearArchivoPlano_GEO : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
