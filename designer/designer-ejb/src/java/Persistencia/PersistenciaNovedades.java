/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.EmpleadosAux;
import Entidades.Novedades;
import InterfacePersistencia.PersistenciaNovedadesInterface;
import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Novedades' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaNovedades implements PersistenciaNovedadesInterface {

    private static Logger log = Logger.getLogger(PersistenciaNovedades.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public String crear(EntityManager em, Novedades novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(novedades);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaNovedades.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear la novedad ";
            }
        }
    }

    @Override
    public String editar(EntityManager em, Novedades novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(novedades);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaNovedades.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar la novedad ";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, Novedades novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(novedades));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaNovedades.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar la novedad ";
            }
        }
    }

    @Override
    public Novedades buscarNovedad(EntityManager em, BigInteger secNovedad) {
        try {
            em.clear();
            if (secNovedad != null) {
                Query query = em.createQuery("SELECT n FROM Novedades n WHERE n.secuencia = :secNovedad");
                query.setParameter("secNovedad", secNovedad);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                Novedades novedad = (Novedades) query.getSingleResult();
                return novedad;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error buscarNovedad PersistenciaNovedades :  ", e);
            return null;
        }
    }

    @Override
    public List<Novedades> novedadesParaReversar(EntityManager em, BigInteger usuarioBD, String documentoSoporte) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT n FROM Novedades n WHERE n.usuarioreporta.secuencia = :usuarioBD AND n.documentosoporte = :documentoSoporte");
            query.setParameter("usuarioBD", usuarioBD);
            query.setParameter("documentoSoporte", documentoSoporte);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Novedades> listNovedades = query.getResultList();
            return listNovedades;
        } catch (Exception e) {
            log.error("Error: (novedadesParaReversar) ", e);
            return null;
        }
    }

    @Override
    public List<Novedades> todasNovedadesEmpleado(EntityManager em, BigInteger secuenciaEmpleado) {
        log.warn("PersistenciaNovedades.todasNovedadesEmpleado() secuenciaEmpleado : " + secuenciaEmpleado);
        try {
            em.clear();
//            Query query = em.createQuery("SELECT n FROM Novedades n WHERE n.empleado.secuencia = :secuenciaEmpleado");
            Query query = em.createQuery("SELECT n FROM Novedades n WHERE n.tipo IN ('OCASIONAL', 'FIJA', 'PAGO POR FUERA') and n.empleado.secuencia=:secuenciaEmpleado", Novedades.class);
            query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Novedades> todasNovedades = query.getResultList();
            return todasNovedades;
        } catch (Exception e) {
            log.error("Error: (todasNovedades) ", e);
            return null;
        }
    }

    @Override
    public List<Novedades> todasNovedadesConcepto(EntityManager em, BigInteger secuenciaConcepto) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT n FROM Novedades n WHERE n.concepto.secuencia= :secuenciaConcepto");
            query.setParameter("secuenciaConcepto", secuenciaConcepto);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Novedades> todasNovedades = query.getResultList();
            return todasNovedades;
        } catch (Exception e) {
            log.error("Error: (todasNovedadesConcepto) ", e);
            return null;
        }
    }

    @Override
    public List<Novedades> todasNovedadesTercero(EntityManager em, BigInteger secuenciaTercero) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT n FROM Novedades n WHERE n.tercero.secuencia= :secuenciaTercero");
            query.setParameter("secuenciaTercero", secuenciaTercero);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Novedades> todasNovedades = query.getResultList();
            return todasNovedades;
        } catch (Exception e) {
            log.error("Error: (todasNovedadesTercero) ", e);
            return null;
        }
    }

    @Override
    public int reversarNovedades(EntityManager em, BigInteger usuarioBD, String documentoSoporte) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

//         Query query = em.createQuery("DELETE FROM Novedades n WHERE n.usuarioreporta.secuencia = :usuarioBD AND n.documentosoporte = :documentoSoporte");
            Query query = em.createNativeQuery("DELETE Novedades n WHERE\n"
                    + " n.usuarioreporta = ?\n"
                    + " AND n.documentosoporte = ?\n"
                    + " AND NOT EXISTS (SELECT 1 FROM SolucionesFormulas sf WHERE sf.novedad = n.SECUENCIA)");
            query.setParameter(1, usuarioBD);
            query.setParameter(2, documentoSoporte);
            int rows = query.executeUpdate();
            log.warn("PersistenciaNovedades.reversarNovedades() rows: " + rows);
            tx.commit();
            return rows;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.error("No se pudo borrar el registro. (reversarNovedades) ", e);
            return 0;
        }
    }

    @Override
    public List<Novedades> novedadesEmpleado(EntityManager em, BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            List<Novedades> listaNovedades;
            String sqlQuery = "SELECT * FROM Novedades n WHERE  n.empleado = ? AND n.tipo IN ('FIJA','PAGO POR FUERA','OCASIONAL') and ((n.fechafinal IS NULL AND NVL(SALDO,99999) > 0) OR (n.saldo > 0 and n.fechainicial >= (SELECT fechadesdecausado FROM vwactualesfechas)) OR n.fechafinal > (SELECT fechadesdecausado FROM vwactualesfechas )) ORDER BY n.fechafinal";
            Query query = em.createNativeQuery(sqlQuery, Novedades.class);
            query.setParameter(1, secuenciaEmpleado);
            listaNovedades = query.getResultList();
            return listaNovedades;
        } catch (Exception e) {
            log.error("Error PersistenciaNovedades.novedadesEmpleado ", e);
            return null;
        }
    }

    public List<Novedades> novedadesConcepto(EntityManager em, BigInteger secuenciaConcepto) {
        try {
            em.clear();
            List<Novedades> listaNovedades;
            String sqlQuery = "SELECT N.* from novedades N where N.concepto = ? and tipo in ('FIJA','PAGO POR FUERA','OCASIONAL' ) and \n"
                    + "EXISTS (SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA=N.EMPLEADO) \n"
                    + "and ((FECHAFINAL IS NULL AND NVL(SALDO,99999)>0) OR (SALDO>0 and fechainicial>=(SELECT FECHADESDECAUSADO FROM VWACTUALESFECHAS)) OR FECHAFINAL>(SELECT FECHADESDECAUSADO FROM VWACTUALESFECHAS))";
            Query query = em.createNativeQuery(sqlQuery, Novedades.class);
            query.setParameter(1, secuenciaConcepto);
            listaNovedades = query.getResultList();
            if (listaNovedades != null) {
                if (!listaNovedades.isEmpty()) {
                    em.clear();
                    Query query2 = em.createNativeQuery("SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                            + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                            + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P, novedades N\n"
                            + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND E.secuencia = n.empleado\n"
                            + " and N.concepto = " + secuenciaConcepto + " and tipo in ('FIJA','PAGO POR FUERA','OCASIONAL' ) and\n"
                            + " ((FECHAFINAL IS NULL AND NVL(SALDO,99999)>0) OR (SALDO>0 and fechainicial>=(SELECT FECHADESDECAUSADO FROM VWACTUALESFECHAS))\n"
                            + " OR FECHAFINAL>(SELECT FECHADESDECAUSADO FROM VWACTUALESFECHAS))", EmpleadosAux.class);
                    List<EmpleadosAux> listaEmpleadosAux = query2.getResultList();
                    log.warn("PersistenciaNovedades.novedadesConcepto() Ya consulo Transients");
                    if (listaEmpleadosAux != null) {
                        if (!listaEmpleadosAux.isEmpty()) {
                            log.warn("PersistenciaNovedades.novedadesConcepto() listaEmpleadosAux.size(): " + listaEmpleadosAux.size());
                            for (EmpleadosAux recAux : listaEmpleadosAux) {
                                for (Novedades recNovedadEmp : listaNovedades) {
                                    if (recAux.getSecuencia().equals(recNovedadEmp.getEmpleado().getSecuencia())) {
                                        recNovedadEmp.getEmpleado().llenarTransients(recAux);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return listaNovedades;
        } catch (Exception e) {
            log.error("Error PersistenciaNovedades.novedadesConcepto ", e);
            return null;
        }
    }

    public List<Novedades> novedadesTercero(EntityManager em, BigInteger secuenciaTercero) {
        try {
            em.clear();
            List<Novedades> listaNovedades;
            String sqlQuery = "SELECT N.* from novedades N where N.tercero = ? and tipo in ('FIJA','PAGO POR FUERA','OCASIONAL' )\n"
                    + "AND EXISTS (SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA=N.EMPLEADO)\n"
                    + "and exists (select 'x' from empresas empr, conceptos conc \n"
                    + "where empr.secuencia=conc.empresa\n"
                    + "and conc.secuencia=n.concepto) AND ((FECHAFINAL IS NULL AND NVL(SALDO,99999)>0) OR (SALDO>0 and fechainicial>=(SELECT FECHADESDECAUSADO FROM VWACTUALESFECHAS)) OR FECHAFINAL>(SELECT FECHADESDECAUSADO FROM VWACTUALESFECHAS))";
            Query query = em.createNativeQuery(sqlQuery, Novedades.class);
            query.setParameter(1, secuenciaTercero);
            listaNovedades = query.getResultList();
            if (listaNovedades != null) {
                if (!listaNovedades.isEmpty()) {
                    em.clear();
                    Query query2 = em.createNativeQuery("SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                            + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                            + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P, novedades N\n"
                            + " where N.tercero = " + secuenciaTercero + " and tipo in ('FIJA','PAGO POR FUERA','OCASIONAL' )\n"
                            + " AND E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND E.secuencia = n.empleado\n"
                            + " AND EXISTS (SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA=N.EMPLEADO)\n"
                            + " and exists (select 'x' from empresas empr, conceptos conc\n"
                            + " where empr.secuencia=conc.empresa\n"
                            + " and conc.secuencia=n.concepto) AND ((FECHAFINAL IS NULL AND NVL(SALDO,99999)>0)\n"
                            + " OR (SALDO>0 and fechainicial>=(SELECT FECHADESDECAUSADO FROM VWACTUALESFECHAS))\n"
                            + " OR FECHAFINAL>(SELECT FECHADESDECAUSADO FROM VWACTUALESFECHAS))", EmpleadosAux.class);
                    List<EmpleadosAux> listaEmpleadosAux = query2.getResultList();
                    log.warn("PersistenciaNovedades.novedadesTercero() Ya consulo Transients");
                    if (listaEmpleadosAux != null) {
                        if (!listaEmpleadosAux.isEmpty()) {
                            log.warn("PersistenciaNovedades.novedadesTercero() listaEmpleadosAux.size(): " + listaEmpleadosAux.size());
                            for (EmpleadosAux recAux : listaEmpleadosAux) {
                                for (Novedades recNovedadEmp : listaNovedades) {
                                    if (recAux.getSecuencia().equals(recNovedadEmp.getEmpleado().getSecuencia())) {
                                        recNovedadEmp.getEmpleado().llenarTransients(recAux);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return listaNovedades;
        } catch (Exception e) {
            log.error("Error PersistenciaNovedades.novedadesConcepto ", e);
            return null;
        }
    }

}
