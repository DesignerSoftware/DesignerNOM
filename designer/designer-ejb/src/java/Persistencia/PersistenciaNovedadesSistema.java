/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.NovedadesSistema;
import InterfacePersistencia.PersistenciaNovedadesSistemaInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'NovedadesSistema' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaNovedadesSistema implements PersistenciaNovedadesSistemaInterface {

   private static Logger log = Logger.getLogger(PersistenciaNovedadesSistema.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     *
     * @param em
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, NovedadesSistema novedades) {
        log.error("Persistencia.PersistenciaNovedadesSistema.crear()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(novedades);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaNovedadesSistema.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, NovedadesSistema novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(novedades);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaNovedadesSistema.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, NovedadesSistema novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(novedades));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaNovedadesSistema.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<NovedadesSistema> novedadesEmpleado(EntityManager em, BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT n FROM NovedadesSistema n WHERE n.tipo = 'DEFINITIVA' and n.empleado.secuencia = :secuenciaEmpleado ORDER BY n.fechainicialdisfrute DESC");
            query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<NovedadesSistema> novedadesSistema = query.getResultList();
            return novedadesSistema;
        } catch (Exception e) {
            log.error("Error: (novedadesEmpleado)" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<NovedadesSistema> novedadesEmpleadoCesantias(EntityManager em, BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT n FROM NovedadesSistema n WHERE n.tipo = 'CESANTIA' and n.empleado.secuencia = :secuenciaEmpleado ORDER BY n.fechainicialdisfrute DESC ");
            query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
            List<NovedadesSistema> novedadesSistema = query.getResultList();
            if (novedadesSistema != null) {
                if (novedadesSistema.isEmpty()) {
                    return null;
                } else {
                    return novedadesSistema;
                }
            } else {
                return novedadesSistema;
            }
        } catch (Exception e) {
            log.error("Error: (novedadesEmpleadoCesantias)" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<NovedadesSistema> novedadesEmpleadoVacaciones(EntityManager em, BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            String sql = " SELECT * FROM NOVEDADESSISTEMA WHERE TIPO='VACACION' AND EXISTS (SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA=NOVEDADESSISTEMA.EMPLEADO AND E.SECUENCIA = ?)";
            Query query = em.createNativeQuery(sql, NovedadesSistema.class);
            query.setParameter(1, secuenciaEmpleado);
            List<NovedadesSistema> novedadesSistema = query.getResultList();
            if (novedadesSistema != null) {
                if (novedadesSistema.isEmpty()) {
                    return null;
                } else {
                    return novedadesSistema;
                }
            } else {
                return novedadesSistema;
            }
        } catch (Exception e) {
            log.error("Error: (novedadesEmpleado)" + e.getMessage());
            return null;
        }
    }

    @Override
    public String buscarEstadoVacaciones(EntityManager em, BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            Date ultimaFechaDisfrute, fechaRegreso;
            try {
                Query query = em.createQuery("SELECT MAX(ns.fechainicialdisfrute) FROM NovedadesSistema ns WHERE ns.empleado.secuencia = :secuenciaEmpleado AND ns.tipo = 'VACACION'");
                query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                ultimaFechaDisfrute = (Date) query.getSingleResult();
            } catch (Exception e) {
                log.error("Persistencia.PersistenciaNovedadesSistema.buscarEstadoVacaciones()" + e.getMessage());
                ultimaFechaDisfrute = null;
            }
            try {
                if (ultimaFechaDisfrute != null) {
                    Query query = em.createNativeQuery("SELECT MAX(FECHASIGUIENTEFINVACA)-1 FROM NovedadesSistema ns WHERE ns.empleado = ? AND ns.tipo = 'VACACION' AND ns.fechainicialdisfrute = TO_DATE( ? ,'dd/mm/yyyy')");
                    query.setParameter(1, secuenciaEmpleado);
                    query.setParameter(2, formato.format(ultimaFechaDisfrute));
                    fechaRegreso = (Date) query.getSingleResult();
                } else {
                    fechaRegreso = null;
                }
            } catch (Exception e) {
                log.error("Error: (buscarEstadoVacaciones) Fecha regreso ultimo disfrute \n" + e.getMessage());
                fechaRegreso = null;
            }

            if (fechaRegreso == null) {
                return "SIN VACACIONES DISFRUTADAS";
            } else if (ultimaFechaDisfrute != null) {
                return "Disfr. " + formato.format(ultimaFechaDisfrute) + " > " + formato.format(fechaRegreso);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error: (novedadesEmpleado)" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigDecimal valorCesantias(EntityManager em, BigInteger secuenciaEmpleado) {
        BigDecimal valorcesantias;
        try {

            em.clear();
            String qr = "SELECT sn1.SALDO \n"
                    + "FROM SOLUCIONESNODOS SN1, EMPLEADOS E, vigenciascargos vc, estructuras est, organigramas org\n"
                    + "WHERE sn1.EMPLEADO= E.SECUENCIA\n"
                    + "and e.secuencia=vc.empleado\n"
                    + "and vc.estructura=est.secuencia\n"
                    + "and est.organigrama=org.secuencia\n"
                    + "AND sn1.TIPO = 'PASIVO'\n"
                    + "AND sn1.ESTADO='CERRADO'\n"
                    + "AND sn1.CONCEPTO = (SELECT gc.CONCEPTO \n"
                    + "                FROM GRUPOSPROVISIONES GC\n"
                    + "                WHERE gc.NOMBRE='CESANTIAS' AND GC.empresa=org.EMPRESA)\n"
                    + "and vc.fechavigencia=(select max(vci.fechavigencia) \n"
                    + "                      from vigenciascargos vci\n"
                    + "                      where vc.empleado=vci.empleado\n"
                    + "                      and vc.fechavigencia<=sysdate)\n"
                    + "AND sn1.FECHAHASTA = (SELECT MAX(sn2.FECHAHASTA)\n"
                    + "                      FROM SOLUCIONESNODOS SN2\n"
                    + "                      WHERE SN1.EMPLEADO=SN2.EMPLEADO\n"
                    + "                      AND SN1.CONCEPTO= SN2.CONCEPTO\n"
                    + "                      AND SN2.ESTADO='CERRADO'"
                    + "                      AND sn2.fechadesde <= sysdate)\n"
                    + "AND E.SECUENCIA= ? \n";
            Query query = em.createNativeQuery(qr);
            query.setParameter(1, secuenciaEmpleado);
            valorcesantias = (BigDecimal) query.getSingleResult();
        } catch (Exception e) {
            log.error("entró al catch Error: (valorCesantias)" + e.getMessage());
            valorcesantias = BigDecimal.ZERO;
        }
        return valorcesantias;
    }

    @Override
    public BigDecimal valorIntCesantias(EntityManager em, BigInteger secuenciaEmpleado) {
        BigDecimal valorintcesantias;
        try {
            em.clear();
            String qr = "SELECT sn1.SALDO  \n"
                    + "FROM SOLUCIONESNODOS SN1, EMPLEADOS E, vigenciascargos vc, estructuras est, organigramas org\n"
                    + "WHERE sn1.EMPLEADO= E.SECUENCIA\n"
                    + "and e.secuencia=vc.empleado\n"
                    + "and vc.estructura=est.secuencia\n"
                    + "and est.organigrama=org.secuencia\n"
                    + "AND sn1.TIPO = 'PASIVO'\n"
                    + "AND sn1.ESTADO='CERRADO'\n"
                    + "AND sn1.CONCEPTO = (SELECT gc.CONCEPTO \n"
                    + "                FROM GRUPOSPROVISIONES GC\n"
                    + "                WHERE gc.NOMBRE='INTERES CESANTIAS' AND GC.empresa=org.EMPRESA)\n"
                    + "and vc.fechavigencia=(select max(vci.fechavigencia) \n"
                    + "                      from vigenciascargos vci\n"
                    + "                      where vc.empleado=vci.empleado\n"
                    + "                      and vc.fechavigencia<=sysdate)\n"
                    + "AND sn1.FECHAHASTA = (SELECT MAX(sn2.FECHAHASTA)\n"
                    + "                      FROM SOLUCIONESNODOS SN2\n"
                    + "                      WHERE SN1.EMPLEADO=SN2.EMPLEADO\n"
                    + "                      AND SN1.CONCEPTO= SN2.CONCEPTO\n"
                    + "                      AND SN2.ESTADO='CERRADO'"
                    + "                      AND sn2.fechadesde <= sysdate)\n"
                    + "AND E.SECUENCIA= ? \n";
            Query query = em.createNativeQuery(qr);
            query.setParameter(1, secuenciaEmpleado);
            valorintcesantias = (BigDecimal) query.getSingleResult();
        } catch (Exception e) {
            log.error("Error: (valorIntCesantias)" + e.getMessage());
            valorintcesantias = BigDecimal.ZERO;
        }
        return valorintcesantias;
    }

    @Override
    public List<NovedadesSistema> novedadsistemaPorEmpleadoYVacacion(EntityManager em, BigInteger secEmpleado, BigInteger secVacacion) {
        try {
            em.clear();
            String strngQuery = "SELECT * FROM NOVEDADESSISTEMA WHERE TIPO = 'VACACION' AND EMPLEADO = ? AND VACACION = ? ";
            Query query = em.createNativeQuery(strngQuery, NovedadesSistema.class);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, secVacacion);
            List<NovedadesSistema> novedadesSistema = query.getResultList();

            if (novedadesSistema != null) {
                if (!novedadesSistema.isEmpty()) {
                    em.clear();
                    try {
                        for (int i = 0; i < novedadesSistema.size(); i++) {
                            String sql = "SELECT SUM(SN.VALOR)\n"
                                    + "	FROM SOLUCIONESNODOS SN, SOLUCIONESFORMULAS SF, DETALLESNOVEDADESSISTEMA DNS\n"
                                    + "	WHERE SN.SECUENCIA = SF.solucionnodo\n"
                                    + "	AND SF.novedad = DNS.novedad\n"
                                    + "	AND DNS.novedadsistema=" + novedadesSistema.get(i).getSecuencia();
                            Query query2 = em.createNativeQuery(sql);
                            BigDecimal valorp = (BigDecimal) query2.getSingleResult();
                            novedadesSistema.get(i).setValorpagado(valorp);
                        }
                    } catch (NoResultException nre) {
                        log.error("No result exception en valor pagado : " + nre.getMessage());
                    }
                }
            }

            return novedadesSistema;
        } catch (Exception e) {
            log.error("Error: novedadsistemaPorEmpleadoYVacacion : " + e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param em
     * @param secNovedadSistema
     * @return
     */
    @Override
    public BigDecimal consultarValorTotalDetalleVacacion(EntityManager em, BigInteger secNovedadSistema) {
        try {
            em.clear();
            String qr = "SELECT SUM(SN.VALOR) FROM SOLUCIONESNODOS SN, SOLUCIONESFORMULAS SF, DETALLESNOVEDADESSISTEMA DNS \n"
                    + "WHERE SN.SECUENCIA = SF.solucionnodo AND SF.novedad = DNS.novedad \n"
                    + "AND DNS.novedadsistema = " + secNovedadSistema;
            Query query = em.createNativeQuery(qr);
            BigDecimal valorTotal = (BigDecimal) query.getSingleResult();
            log.error("consultarValorTotalDetalleVacacion valorTotal : " + valorTotal);
            return valorTotal;
        } catch (Exception e) {
            log.error("Error: consultarValorTotalDetalleVacacion : " + e.getMessage());
            return null;
        }
    }

    @Override
    public void adicionaNovedadCambiosMasivos(EntityManager em,
            String tipo, BigInteger secConcepto, BigInteger secPeriodicidad,
            BigInteger secTercero, BigInteger secFormula, BigInteger valor,
            BigInteger saldo, Date fechaCambioInicial, Date fechaCambioFinal,
            BigInteger unidadParteEntera, BigInteger unidadParteFraccion) {

        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.AdicionaNovedad");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(3, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(4, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(5, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(6, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(7, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(8, Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(9, Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(10, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(11, BigInteger.class, ParameterMode.IN);

            query.setParameter(1, tipo);
            query.setParameter(2, secConcepto);
            query.setParameter(3, secPeriodicidad);
            query.setParameter(4, secTercero);
            query.setParameter(5, secFormula);
            query.setParameter(6, valor);
            query.setParameter(7, saldo);
            query.setParameter(8, fechaCambioInicial);
            query.setParameter(9, fechaCambioFinal);
            query.setParameter(10, unidadParteEntera);
            query.setParameter(11, unidadParteFraccion);
            query.execute();
        } catch (Exception e) {
            log.error(this.getClass().getName() + ".adicionaNovedadCambiosMasivos() ERROR: " + e.getMessage());
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            tx.commit();
        }
    }

    @Override
    public void undoAdicionaNovedadCambiosMasivos(EntityManager em,
            String tipo, BigInteger secConcepto, BigInteger secPeriodicidad,
            BigInteger secTercero, BigInteger secFormula, BigInteger valor,
            BigInteger saldo, Date fechaCambioInicial, Date fechaCambioFinal) {

        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.UndoAdicionaNovedad");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(3, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(4, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(5, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(6, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(7, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(8, Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(9, Date.class, ParameterMode.IN);

            query.setParameter(1, tipo);
            query.setParameter(2, secConcepto);
            query.setParameter(3, secPeriodicidad);
            query.setParameter(4, secTercero);
            query.setParameter(5, secFormula);
            query.setParameter(6, valor);
            query.setParameter(7, saldo);
            query.setParameter(8, fechaCambioInicial);
            query.setParameter(9, fechaCambioFinal);
            query.execute();
        } catch (Exception e) {
            log.error(this.getClass().getName() + ".undoAdicionaNovedadCambiosMasivos() ERROR: " + e.getMessage());
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            tx.commit();
        }
    }

}
