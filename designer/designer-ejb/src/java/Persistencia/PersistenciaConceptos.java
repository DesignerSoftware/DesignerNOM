/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Conceptos;
import Entidades.ConceptosAux;
import InterfacePersistencia.PersistenciaConceptosInterface;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 * Clase Stateless Clase encargada de realizar operaciones sobre la tabla
 * 'Conceptos' de la base de datos
 *
 * @author Betelgeuse
 */
@Stateless
public class PersistenciaConceptos implements PersistenciaConceptosInterface {

    private static Logger log = Logger.getLogger(PersistenciaConceptos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     *
     * @param em
     */
    /*
     * @PersistenceContext(unitName = "DesignerRHN-ejbPU") private EntityManager em;
     */
    @Override
    public String crear(EntityManager em, Conceptos concepto) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(concepto);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaConceptos.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear el Concepto";
            }
        }
    }

    @Override
    public String editar(EntityManager em, Conceptos concepto) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(concepto);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaConceptos.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar el Concepto";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, Conceptos concepto) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(concepto));
            tx.commit();
            return "EXITO";

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaConceptos.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar el Concepto";
            }
        }
    }

    @Override
    public List<Conceptos> buscarConceptos(EntityManager em) {
        em.clear();
        try {
            Query query = em.createNativeQuery("SELECT * FROM CONCEPTOS ORDER BY DESCRIPCION", Conceptos.class);
            List<Conceptos> listaConceptos = query.getResultList();
            if (listaConceptos != null) {
                if (!listaConceptos.isEmpty()) {
                    em.clear();
                    Query query2 = em.createNativeQuery("SELECT c.SECUENCIA, U.NOMBRE NOMBREUNIDAD, U.CODIGO CODIGOUNIDAD,\n"
                            + " T.NOMBRE NOMBRETERCERO, E.NOMBRE NOMBREEMPRESA, E.NIT NITEMPRESA\n"
                            + " FROM Conceptos C, Unidades U, Terceros T, empresas E\n"
                            + " WHERE c.UNIDAD = U.SECUENCIA AND c.TERCERO = T.SECUENCIA(+) AND c.EMPRESA = E.SECUENCIA", ConceptosAux.class);
                    List<ConceptosAux> listaConceptosAux = query2.getResultList();
                    log.warn("PersistenciaConceptos.buscarConceptos() Ya consulo Transients");
                    if (listaConceptosAux != null) {
                        if (!listaConceptosAux.isEmpty()) {
                            log.warn("PersistenciaConceptos.buscarConceptos() listaConceptosAux.size(): " + listaConceptosAux.size());
                            for (ConceptosAux recAux : listaConceptosAux) {
                                for (Conceptos recConcepto : listaConceptos) {
                                    if (recAux.getSecuencia().equals(recConcepto.getSecuencia())) {
                                        recConcepto.llenarTransients(recAux);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return listaConceptos;
        } catch (Exception e) {
            log.error("Error en buscarConceptos() :  ", e);
            return null;
        }
    }

    @Override
    public List<Conceptos> buscarConceptosLovNovedades(EntityManager em) {
        em.clear();
        try {
            String sql = "SELECT * FROM CONCEPTOS C\n"
                    + " WHERE NOT EXISTS (SELECT 'X' FROM FORMULASCONCEPTOS FC, FORMULAS F, FORMULASCONTRATOS FCON\n"
                    + " WHERE F.SECUENCIA = FCON.FORMULA AND FC.CONCEPTO = C.SECUENCIA AND FC.FORMULA = F.SECUENCIA)\n"
                    + " AND C.ACTIVO = 'S' ORDER BY DESCRIPCION";
            Query query = em.createNativeQuery(sql, Conceptos.class);
            List<Conceptos> listaConceptos = query.getResultList();
            if (listaConceptos != null) {
                if (!listaConceptos.isEmpty()) {
                    em.clear();
                    Query query2 = em.createNativeQuery("SELECT c.SECUENCIA, U.NOMBRE NOMBREUNIDAD, U.CODIGO CODIGOUNIDAD,\n"
                            + " T.NOMBRE NOMBRETERCERO, E.NOMBRE NOMBREEMPRESA, E.NIT NITEMPRESA\n"
                            + " FROM CONCEPTOS C, Unidades U, Terceros T, empresas E\n"
                            + " WHERE NOT EXISTS (SELECT 'X' FROM FORMULASCONCEPTOS FC, FORMULAS F, FORMULASCONTRATOS FCON\n"
                            + " WHERE F.SECUENCIA = FCON.FORMULA AND FC.CONCEPTO = C.SECUENCIA AND FC.FORMULA = F.SECUENCIA)\n"
                            + " AND C.ACTIVO = 'S' AND c.UNIDAD = U.SECUENCIA AND c.TERCERO = T.SECUENCIA(+) AND c.EMPRESA = E.SECUENCIA ORDER BY DESCRIPCION", ConceptosAux.class);
                    List<ConceptosAux> listaConceptosAux = query2.getResultList();
                    log.warn("PersistenciaConceptos.buscarConceptosLovNovedades() Ya consulo Transients");
                    if (listaConceptosAux != null) {
                        if (!listaConceptosAux.isEmpty()) {
                            log.warn("PersistenciaConceptos.buscarConceptosLovNovedades() listaConceptosAux.size(): " + listaConceptosAux.size());
                            for (ConceptosAux recAux : listaConceptosAux) {
                                for (Conceptos recConcepto : listaConceptos) {
                                    if (recAux.getSecuencia().equals(recConcepto.getSecuencia())) {
                                        recConcepto.llenarTransients(recAux);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return listaConceptos;
        } catch (Exception e) {
            log.error("Error en buscarConceptosLovNovedades() :  ", e);
            return null;
        }
    }

    @Override
    public boolean verificarCodigoConcepto(EntityManager em, BigInteger codigoConcepto) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(c) FROM Conceptos c WHERE c.codigo = :codigo");
            query.setParameter("codigo", codigoConcepto);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            return resultado > 0;
        } catch (Exception e) {
            log.error("Exepcion:  ", e);
            return false;
        }
    }

    @Override
    public boolean verificarConceptoManual(EntityManager em, BigInteger secConcepto) {
        try {
            em.clear();
            Query query = em.createNativeQuery("SELECT COUNT(C.SECUENCIA) CONTEO FROM CONCEPTOS C WHERE secuencia = ? \n"
                    + "AND NOT EXISTS (SELECT 'X' FROM FORMULASCONCEPTOS FC \n"
                    + "WHERE FC.CONCEPTO = C.SECUENCIA) AND C.ACTIVO = 'S'");
            query.setParameter(1, secConcepto);
            Long resultado = (Long) query.getSingleResult();
            return resultado > 0;
        } catch (Exception e) {
            log.error("PersistenciaConceptos.verificarConceptoManual() ERROR:  ", e);
            return false;
        }
    }

    @Override
    public Conceptos validarCodigoConcepto(EntityManager em, BigInteger codigoConcepto, BigInteger secEmpresa) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM Conceptos c WHERE c.codigo = :codigo AND c.empresa = :secEmpresa");
            query.setParameter("codigo", codigoConcepto);
            query.setParameter("secEmpresa", secEmpresa);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Conceptos concepto = (Conceptos) query.getSingleResult();
            if (concepto != null) {
                if (concepto.getSecuencia() != null) {
                    em.clear();
                    Query query2 = em.createNativeQuery("SELECT c.SECUENCIA, U.NOMBRE NOMBREUNIDAD, U.CODIGO CODIGOUNIDAD, T.NOMBRE NOMBRETERCERO, E.NOMBRE NOMBREEMPRESA, E.NIT NITEMPRESA\n"
                            + " FROM Conceptos C, Unidades U, Terceros T, empresas E\n"
                            + " WHERE c.secuencia = " + concepto.getSecuencia() + "\n"
                            + " AND c.UNIDAD = U.SECUENCIA AND c.TERCERO = T.SECUENCIA(+) AND c.EMPRESA = E.SECUENCIA", ConceptosAux.class);
                    ConceptosAux conceptoAux = (ConceptosAux) query2.getSingleResult();
                    log.warn("PersistenciaConceptos.validarCodigoConcepto() conceptoAux: " + conceptoAux);
                    if (conceptoAux != null) {
                        if (conceptoAux.getSecuencia() != null && conceptoAux.getSecuencia().equals(concepto.getSecuencia())) {
                            concepto.llenarTransients(conceptoAux);
                        }
                    }
                }
            }
            return concepto;
        } catch (Exception e) {
            log.error("PersistenciaConceptos.validarCodigoConcepto() ERROR:  ", e);
            return null;
        }
    }

    @Override
    public List<Conceptos> conceptosPorEmpresa(EntityManager em, BigInteger secEmpresa) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM Conceptos c WHERE c.empresa = :secEmpresa ORDER BY c.codigo ASC");

            query.setParameter("secEmpresa", secEmpresa);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Conceptos> listaConceptos = query.getResultList();
            if (listaConceptos != null) {
                if (!listaConceptos.isEmpty()) {
                    em.clear();
                    Query query2 = em.createNativeQuery("SELECT  c.SECUENCIA, U.NOMBRE NOMBREUNIDAD, U.CODIGO CODIGOUNIDAD,\n"
                            + " T.NOMBRE NOMBRETERCERO, E.NOMBRE NOMBREEMPRESA, E.NIT NITEMPRESA\n"
                            + " FROM Conceptos C, Unidades U, Terceros T, empresas E\n"
                            + " WHERE c.empresa = " + secEmpresa + "\n"
                            + " AND c.UNIDAD = U.SECUENCIA AND c.TERCERO = T.SECUENCIA(+) AND c.EMPRESA = E.SECUENCIA\n"
                            + " ORDER BY c.codigo ASC", ConceptosAux.class);
                    List<ConceptosAux> listaConceptosAux = query2.getResultList();
                    log.warn("PersistenciaConceptos.conceptosPorEmpresa() Ya consulo Transients");
                    if (listaConceptosAux != null) {
                        if (!listaConceptosAux.isEmpty()) {
                            log.warn("PersistenciaConceptos.conceptosPorEmpresa() listaConceptosAux.size(): " + listaConceptosAux.size());
                            for (ConceptosAux recAux : listaConceptosAux) {
                                for (Conceptos recConcepto : listaConceptos) {
                                    if (recAux.getSecuencia().equals(recConcepto.getSecuencia())) {
                                        recConcepto.llenarTransients(recAux);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return listaConceptos;
        } catch (Exception e) {
            log.error("PersistenciaConceptos.conceptosPorEmpresa() ERROR:  ", e);
            return null;
        }
    }

    @Override
    public List<Conceptos> conceptosEmpresaActivos_Inactivos(EntityManager em, BigInteger secEmpresa, String estado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM Conceptos c WHERE c.empresa = :secEmpresa AND c.activo = :estado ORDER BY c.codigo ASC");
            query.setParameter("secEmpresa", secEmpresa);
            query.setParameter("estado", estado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Conceptos> listaConceptos = query.getResultList();
            if (listaConceptos != null) {
                if (!listaConceptos.isEmpty()) {
                    em.clear();
                    Query query2 = em.createNativeQuery("SELECT  c.SECUENCIA, U.NOMBRE NOMBREUNIDAD, U.CODIGO CODIGOUNIDAD,\n"
                            + " T.NOMBRE NOMBRETERCERO, E.NOMBRE NOMBREEMPRESA, E.NIT NITEMPRESA\n"
                            + " FROM Conceptos C, Unidades U, Terceros T, empresas E\n"
                            + " WHERE c.empresa = " + secEmpresa + " AND c.activo = '" + estado + "'\n"
                            + " AND c.UNIDAD = U.SECUENCIA AND c.TERCERO = T.SECUENCIA(+) AND c.EMPRESA = E.SECUENCIA\n"
                            + " ORDER BY c.codigo ASC", ConceptosAux.class);
                    List<ConceptosAux> listaConceptosAux = query2.getResultList();
                    log.warn("PersistenciaConceptos.conceptosEmpresaActivos_Inactivos() Ya consulo Transients");
                    if (listaConceptosAux != null) {
                        if (!listaConceptosAux.isEmpty()) {
                            log.warn("PersistenciaConceptos.conceptosEmpresaActivos_Inactivos() listaConceptosAux.size(): " + listaConceptosAux.size());
                            for (ConceptosAux recAux : listaConceptosAux) {
                                for (Conceptos recConcepto : listaConceptos) {
                                    if (recAux.getSecuencia().equals(recConcepto.getSecuencia())) {
                                        recConcepto.llenarTransients(recAux);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return listaConceptos;
        } catch (Exception e) {
            log.error("PersistenciaConceptos.conceptosEmpresaActivos_Inactivos() ERROR:  ", e);
            return null;
        }
    }

    @Override
    public List<Conceptos> conceptosEmpresaSinPasivos(EntityManager em, BigInteger secEmpresa) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM Conceptos c WHERE c.empresa = :secEmpresa AND c.naturaleza <> 'L' ORDER BY c.codigo ASC");
            query.setParameter("secEmpresa", secEmpresa);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Conceptos> listaConceptos = query.getResultList();
            if (listaConceptos != null) {
                if (!listaConceptos.isEmpty()) {
                    em.clear();
                    Query query2 = em.createNativeQuery("SELECT  c.SECUENCIA, U.NOMBRE NOMBREUNIDAD, U.CODIGO CODIGOUNIDAD,\n"
                            + " T.NOMBRE NOMBRETERCERO, E.NOMBRE NOMBREEMPRESA, E.NIT NITEMPRESA\n"
                            + " FROM Conceptos C, Unidades U, Terceros T, empresas E\n"
                            + " WHERE c.empresa = " + secEmpresa + " AND c.naturaleza <> 'L' \n"
                            + " AND c.UNIDAD = U.SECUENCIA AND c.TERCERO = T.SECUENCIA(+) AND c.EMPRESA = E.SECUENCIA\n"
                            + " ORDER BY c.codigo ASC", ConceptosAux.class);
                    List<ConceptosAux> listaConceptosAux = query2.getResultList();
                    log.warn("PersistenciaConceptos.conceptosEmpresaSinPasivos() Ya consulo Transients");
                    if (listaConceptosAux != null) {
                        if (!listaConceptosAux.isEmpty()) {
                            log.warn("PersistenciaConceptos.conceptosEmpresaSinPasivos() listaConceptosAux.size(): " + listaConceptosAux.size());
                            for (ConceptosAux recAux : listaConceptosAux) {
                                for (Conceptos recConcepto : listaConceptos) {
                                    if (recAux.getSecuencia().equals(recConcepto.getSecuencia())) {
                                        recConcepto.llenarTransients(recAux);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return listaConceptos;
        } catch (Exception e) {
            log.error("PersistenciaConceptos.conceptosEmpresaSinPasivos() ERROR:  ", e);
            return null;
        }
    }

    @Override
    public void clonarConcepto(EntityManager em, BigInteger secConceptoOrigen, BigInteger codigoConceptoNuevo, String descripcionConceptoNuevo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            StoredProcedureQuery query = em.createStoredProcedureQuery("CONCEPTOS_PKG.CLONARCONCEPTO");
            query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);

            query.setParameter(1, secConceptoOrigen);
            query.setParameter(2, codigoConceptoNuevo);
            query.setParameter(3, descripcionConceptoNuevo);
            query.execute();
            tx.commit();
            log.warn("clonarConcepto() Ya hiso commit");

//         String sqlQuery = "call CONCEPTOS_PKG.CLONARCONCEPTO(?, ?, ?)";
//         Query query = em.createNativeQuery(sqlQuery);
//         query.setParameter(1, secConceptoOrigen);
//         query.setParameter(2, codigoConceptoNuevo);
//         query.setParameter(3, descripcionConceptoNuevo);
//         query.executeUpdate();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.error("Error en clonarConcepto:  ", e);
        }
    }

    @Override
    public Conceptos conceptosPorSecuencia(EntityManager em, BigInteger secConcepto) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM Conceptos c WHERE c.secuencia=:secConcepto");
            query.setParameter("secConcepto", secConcepto);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Conceptos concepto = (Conceptos) query.getSingleResult();
            if (concepto != null) {
                if (concepto.getSecuencia() != null) {
                    em.clear();
                    Query query2 = em.createNativeQuery("SELECT c.SECUENCIA, U.NOMBRE NOMBREUNIDAD, U.CODIGO CODIGOUNIDAD, T.NOMBRE NOMBRETERCERO, E.NOMBRE NOMBREEMPRESA, E.NIT NITEMPRESA\n"
                            + " FROM Conceptos C, Unidades U, Terceros T, empresas E\n"
                            + " WHERE c.secuencia = " + concepto.getSecuencia() + "\n"
                            + " AND c.UNIDAD = U.SECUENCIA AND c.TERCERO = T.SECUENCIA(+) AND c.EMPRESA = E.SECUENCIA", ConceptosAux.class);
                    ConceptosAux conceptoAux = (ConceptosAux) query2.getSingleResult();
                    log.warn("PersistenciaConceptos.conceptosPorSecuencia() conceptoAux: " + conceptoAux);
                    if (conceptoAux != null) {
                        if (conceptoAux.getSecuencia() != null && conceptoAux.getSecuencia().equals(concepto.getSecuencia())) {
                            concepto.llenarTransients(conceptoAux);
                        }
                    }
                }
            }
            return concepto;
        } catch (Exception e) {
            log.error("Error Persistencia conceptosPorSecuencia :  ", e);
            return null;
        }
    }

    @Override
    public boolean eliminarConcepto(EntityManager em, BigInteger secuenciaConcepto) {
        try {
            em.clear();
            String sqlQuery = "call conceptos_pkg.Eliminarconcepto(:secuencia)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter("secuencia", secuenciaConcepto);
            query.executeUpdate();
            return true;
        } catch (Exception e) {
            log.error("Error eliminarConcepto PersistenciaConceptos :  ", e);
            return false;
        }
    }

    @Override
    public String conceptoParaFormulaContrato(EntityManager em, BigInteger secuencia, Date fechaFin) {
        try {
            em.clear();
            String sqlQuery = "select substr(c.codigo||'-'||c.descripcion,1,200)\n"
                    + "from formulasconceptos fc, conceptos c\n"
                    + "where fc.formula = ? \n"
                    + "and fc.concepto = c.secuencia\n"
                    + "and exists (select 'x' from empresas e where e.secuencia = c.empresa)\n"
                    + "and fechainicial = (select max(fechainicial) from formulasconceptos fci\n"
                    + "where fci.formula = fc.formula\n"
                    + "and fci.concepto = fc.concepto\n"
                    + "and fci.fechainicial <= nvl(?,sysdate))";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            query.setParameter(2, fechaFin);
            List<String> listaConceptos = query.getResultList();
            String conceptoString = " ";
            if (!listaConceptos.isEmpty()) {
                int tam = listaConceptos.size();
                if (tam == 1) {
                    conceptoString = listaConceptos.get(0);
                }
                if (tam >= 2) {
                    conceptoString = "... " + listaConceptos.get(0);
                }
            }
            return conceptoString;
        } catch (Exception e) {
            log.error("Error PersistenciaConceptos.conceptoParaFormulaContrato.  ", e);
            return null;
        }
    }

//   @Override
//   public List<Conceptos> conceptoEmpresa(EntityManager em) {
//      try {
//         em.clear();
//         String sqlQuery = "SELECT  C.*,\n"
//                 + " decode(c.naturaleza,'P','PAGO','D','DESCUENTO','L','PASIVO','G','GASTO','N','NETO') NATURALEZA\n"
//                 + " FROM CONCEPTOS C, EMPRESAS E\n"
//                 + " WHERE C.EMPRESA = E.SECUENCIA\n"
//                 + " ORDER BY C.DESCRIPCION";
//         Query query = em.createNativeQuery(sqlQuery, Conceptos.class);
//         List<Conceptos> listaConceptos = query.getResultList();
//         return listaConceptos;
//      } catch (Exception e) {
//         log.error("Error PersistenciaConceptos.conceptoEmpresa  ", e);
//         return null;
//      }
//   }
    @Override
    public List<Conceptos> novedadConceptos(EntityManager em) {
        try {
            em.clear();
            String sqlQuery = "SELECT c.* FROM Conceptos c WHERE EXISTS (select 'x' from empresas e where c.empresa=e.secuencia) AND NVL(c.ACTIVO,'S')='S' ORDER BY c.CODIGO";
            Query query = em.createNativeQuery(sqlQuery, Conceptos.class);
            List<Conceptos> listaConceptos = query.getResultList();
            if (listaConceptos != null) {
                if (!listaConceptos.isEmpty()) {
                    em.clear();
                    Query query2 = em.createNativeQuery("SELECT c.SECUENCIA, U.NOMBRE NOMBREUNIDAD, U.CODIGO CODIGOUNIDAD,\n"
                            + " T.NOMBRE NOMBRETERCERO, E.NOMBRE NOMBREEMPRESA, E.NIT NITEMPRESA\n"
                            + " FROM Conceptos C, Unidades U, Terceros T, empresas E\n"
                            + " WHERE EXISTS (select 'x' from empresas e where c.empresa=e.secuencia) AND NVL(c.ACTIVO,'S')='S'\n"
                            + " AND c.UNIDAD = U.SECUENCIA AND c.TERCERO = T.SECUENCIA(+) AND c.EMPRESA = E.SECUENCIA\n"
                            + " ORDER BY c.CODIGO", ConceptosAux.class);
                    List<ConceptosAux> listaConceptosAux = query2.getResultList();
                    log.warn("PersistenciaConceptos.novedadConceptos() Ya consulo Transients");
                    if (listaConceptosAux != null) {
                        if (!listaConceptosAux.isEmpty()) {
                            log.warn("PersistenciaConceptos.novedadConceptos() listaConceptosAux.size(): " + listaConceptosAux.size());
                            for (ConceptosAux recAux : listaConceptosAux) {
                                for (Conceptos recConcepto : listaConceptos) {
                                    if (recAux.getSecuencia().equals(recConcepto.getSecuencia())) {
                                        recConcepto.llenarTransients(recAux);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return listaConceptos;
        } catch (Exception e) {
            log.error("Error PersistenciaConceptos.novedadConceptos  ", e);
            return null;
        }
    }
}
