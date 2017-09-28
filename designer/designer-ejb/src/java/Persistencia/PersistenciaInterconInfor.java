/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import ClasesAyuda.ExtraeCausaExcepcion;
import Entidades.InterconAux;
import Entidades.InterconInfor;
import InterfacePersistencia.PersistenciaInterconInforInterface;
import excepciones.ExcepcionBD;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaInterconInfor implements PersistenciaInterconInforInterface {

    private static Logger log = Logger.getLogger(PersistenciaInterconInfor.class);

    @Override
    public void crear(EntityManager em, InterconInfor intercon) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(intercon);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconInfor.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, InterconInfor intercon) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(intercon);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconInfor.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, InterconInfor intercon) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(intercon));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconInfor.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public InterconInfor buscarInterconInforSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT i FROM InterconInfor i WHERE i.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            InterconInfor intercon = (InterconInfor) query.getSingleResult();
            return intercon;
        } catch (Exception e) {
            log.error("Error PersistenciaInterconInfor.buscarInterconInforSecuencia:  ", e);
            return null;
        }
    }

    @Override
    public List<InterconInfor> buscarInterconInforParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal) {
        try {
            em.clear();
            String sql = "select * from intercon_infor i where fechacontabilizacion between ? and ? \n"
                    + " and FLAG = 'CONTABILIZADO' AND SALIDA <> 'NETO'\n"
                    + " and exists (select 'x' from empleados e where e.secuencia=i.empleado)";

            Query query = em.createNativeQuery(sql, InterconInfor.class);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            List<InterconInfor> intercon = query.getResultList();
            if (intercon != null) {
                if (!intercon.isEmpty()) {
                    em.clear();
                    Query query2 = em.createNativeQuery(" select i.secuencia secuencia, pro.descripcion nombreproceso,per.primerapellido||' '||per.segundoapellido||' '||per.nombre nombreempleado from intercon_infor i,empleados em,personas per,procesos pro where \n"
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
                                for (InterconInfor recAporte : intercon) {
                                    if (recAux.getSecuencia().equals(recAporte.getSecuencia())) {
                                        recAporte.llenarTransients(recAux);
                                    }
                                }
                            }
                        }
                    }
                }
                log.error("Lista intercon_infor intercon : " + intercon.size());
            } else {
                log.error("Lista Nula intercon_infor");
            }
            return intercon;
        } catch (Exception e) {
            log.error("Error PersistenciaInterconInfor.buscarInterconInforParametroContable:  ", e);
            return null;
        }
    }

    @Override
    public Date obtenerFechaMaxInterconInfor(EntityManager em) {
        try {
            em.clear();
            String sql = "select max(i.fechacontabilizacion) from intercon_infor i "
                    + " where flag = 'ENVIADO' and exists (select 'x' from  empleados e \n"
                    + " where e.secuencia=i.empleado )";
            Query query = em.createNativeQuery(sql);
            Date fecha = (Date) (query.getSingleResult());
            return fecha;
        } catch (Exception e) {
            log.error("Error PersistenciaInterconInfor.obtenerFechaMaxInterconInfor:  ", e);
            return null;
        }
    }

    @Override
    public void actualizarFlagInterconInfor(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "UPDATE intercon_infor SET FLAG = 'CONTABILIZADO' \n"
                    + " WHERE FECHACONTABILIZACION BETWEEN ? AND ? \n"
                    + " AND FLAG = 'ENVIADO' \n"
                    + " AND intercon_infor.empresa_codigo= ? \n"
                    + " AND EXISTS (SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA=intercon_infor.EMPLEADO)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, empresa);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconInfor.actualizarFlagInterconInfor.  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void ejecutarPKGUbicarNewInterCon_Infor(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASEInfor$PKG.UbicarNewInterCon_Infor(?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuencia);
            query.setParameter(2, fechaIni);
            query.setParameter(3, fechaFin);
            query.setParameter(4, proceso);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconInfor.ejecutarPKGUbicarNewInterCon_Infor :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void actualizarFlagInterconInforProcesoDeshacer(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso) {
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
            log.error("Error PersistenciaInterconInfor.actualizarFlagInterconInforProcesoDeshacer.  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void eliminarInterconInfor(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "DELETE INTERCON_INFOR\n"
                    + " WHERE FECHACONTABILIZACION BETWEEN ? AND ?\n"
                    + " AND FLAG='CONTABILIZADO'\n"
                    + " and intercon_infor.empresa_codigo=?\n"
                    + " AND nvl(intercon_infor.PROCESO,0) = NVL(?,nvl(intercon_infor.PROCESO,0))\n"
                    + " and exists (select 'x' from empleados e where e.secuencia=intercon_infor.empleado)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, empresa);
            query.setParameter(4, proceso);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconInfor.eliminarInterconInfor.  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public int contarProcesosContabilizadosInterconInfor(EntityManager em, Date fechaInicial, Date fechaFinal) {
        try {
            em.clear();
            String sql = "select COUNT(*) from intercon_infor i where\n"
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
            log.error("Error PersistenciaInterconInfor.contarProcesosContabilizadosInterconInfor.  ", e);
            return -1;
        }
    }

    @Override
    public void cerrarProcesoContabilizacion(EntityManager em, Date fechaIni, Date fechaFin, Short empresa, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "UPDATE INTERCON_INFOR I SET I.FLAG='ENVIADO' WHERE  \n"
                    + "     I.FECHACONTABILIZACION BETWEEN ? AND ?\n"
                    + "     and nvl(i.proceso,0) = nvl(?,nvl(i.proceso,0))\n"
                    + "     and i.empresa_codigo=?"
                    + "   and exists (select 'x' from empleados e where e.secuencia=i.empleado)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, proceso);
            query.setParameter(4, empresa);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInterconInfor.cerrarProcesoContabilizacion.  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void ejecutarPKGRecontabilizacion(EntityManager em, Date fechaIni, Date fechaFin) throws ExcepcionBD {
        EntityTransaction tx = em.getTransaction();
        try {
            em.clear();
            tx.begin();
            String sql = "call INTERFASEINFOR$PKG.Recontabilizacion(?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.executeUpdate();
            tx.commit();
        } catch (Exception pe) {
            log.error("Error PersistenciaInterconInfor.ejecutarPKGRecontabilizacion : " + pe.toString());
            throw new ExcepcionBD(ExtraeCausaExcepcion.obtenerMensajeSQLException(pe));
//        }
        } finally {
            if (tx != null) {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
        }
    }

    @Override
    public String ejecutarPKGCrearArchivoPlanoInfor(EntityManager em, Date fechaIni, Date fechaFin, BigInteger CodigoEmpresa, BigInteger proceso, String nombreArchivo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            StoredProcedureQuery query = em.createStoredProcedureQuery("INTERFASEINFOR$PKG.EnviarContabilidad_infor");
            query.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(3, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(4, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(5, String.class, ParameterMode.INOUT);

            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, CodigoEmpresa);
            query.setParameter(4, proceso);
            query.setParameter(5, nombreArchivo);

            query.execute();
            query.hasMoreResults();
            String nomArch = (String) query.getOutputParameterValue(5);
            log.warn("nomarchivo : " + nomArch);
            tx.commit();
            return nomArch;

        } catch (Exception e) {
            log.error("Error PersistenciaInterconInfor.ejecutarPKGCrearArchivoPlano :  ", e);
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

}
