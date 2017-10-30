/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.SolucionesNodos;
import Entidades.SolucionesNodosAux;
import Entidades.TiposTrabajadores;
import InterfacePersistencia.PersistenciaEmplSolucionesNodosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaEmplSolucionesNodos implements PersistenciaEmplSolucionesNodosInterface {

    private static Logger log = Logger.getLogger(PersistenciaEmplSolucionesNodos.class);

    @Override
    public String crear(EntityManager em, SolucionesNodos snodo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(snodo);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaEmplSolucionesNodos.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear la solución nodo ";
            }
        }
    }

    @Override
    public String editar(EntityManager em, SolucionesNodos snodo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(snodo);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaEmplSolucionesNodos.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al editar la solución nodo ";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, SolucionesNodos snodo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(snodo));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaEmplSolucionesNodos.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al borrar la solución nodo ";
            }
        }
    }

    @Override
    public List<SolucionesNodos> solucionesNodosXEmpleado(EntityManager em, BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            String sql = "SELECT sn.* \n"
                    + " FROM SOLUCIONESNODOS sn "
                    + "WHERE sn.EMPLEADO = " + secuenciaEmpleado;
            Query query = em.createNativeQuery(sql, SolucionesNodos.class);
            List<SolucionesNodos> listSNodos = query.getResultList();
            if (listSNodos != null) {
                if (!listSNodos.isEmpty()) {
                    for (int i = 0; i < listSNodos.size(); i++) {
                        em.clear();
                        String nitTercero = "NULL";
                        if (listSNodos.get(i).getNittercero() != null) {
                            nitTercero = "" + listSNodos.get(i).getNittercero();
                        }
                        String stringSQLQuery = "SELECT sn.secuencia,\n"
                                + " c.CODIGO CODIGOCONCEPTO,\n"
                                + " c.DESCRIPCION NOMBRECONCEPTO,\n"
                                + " (select NOMBRE from TERCEROS WHERE " + nitTercero + " = SECUENCIA(+)) NOMBRETERCERO,\n"
                                + " (select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentad() + ") CODIGOCUENTAD,\n"
                                + " (select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentac() + ") CODIGOCUENTAC,\n"
                                + " (select p.PRIMERAPELLIDO||' '|| p.SEGUNDOAPELLIDO ||' '||p.NOMBRE NOMBREEMPLEADO from personas p, empleados e where e.persona = p.secuencia and e.secuencia = " + secuenciaEmpleado + ") NOMBREEMPLEADO,\n"
                                + " (select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostod() + ") NOMBRECENTROCOSTOD, \n"
                                + " (select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostoc() + ") NOMBRECENTROCOSTOC, \n"
                                + " (select DESCRIPCION from PROCESOS WHERE SECUENCIA = " + listSNodos.get(i).getProceso() + ") NOMBREPROCESO\n"
                                + " FROM SOLUCIONESNODOS sn, (select CODIGO,DESCRIPCION from conceptos where secuencia = " + listSNodos.get(i).getConcepto() + ") c \n"
                                + " where sn.SECUENCIA = " + listSNodos.get(i).getSecuencia();
                        Query query2 = em.createNativeQuery(stringSQLQuery, SolucionesNodosAux.class);
                        SolucionesNodosAux sNAux = (SolucionesNodosAux) query2.getSingleResult();
                        listSNodos.get(i).setCodigoconcepto(sNAux.getCodigoconcepto());
                        listSNodos.get(i).setNombreconcepto(sNAux.getNombreconcepto());
                        listSNodos.get(i).setNombretercero(sNAux.getNombretercero());
                        listSNodos.get(i).setCodigocuentad(sNAux.getCodigocuentad());
                        listSNodos.get(i).setCodigocuentac(sNAux.getCodigocuentac());
                        listSNodos.get(i).setNombreempleado(sNAux.getNombreempleado());
                        listSNodos.get(i).setNombrecentrocostod(sNAux.getNombrecentrocostod());
                        listSNodos.get(i).setNombrecentrocostoc(sNAux.getNombrecentrocostoc());
                        listSNodos.get(i).setNombreproceso(sNAux.getNombreproceso());
                    }
                }
            }
            return listSNodos;
        } catch (Exception e) {
            log.error("Error: (PersistenciaEmplSolucionesNodos.solucionesNodosXEmpleado) ", e);
            return null;
        }
    }

    @Override
    public BigDecimal tipottXEmpleado(EntityManager em, BigInteger secuenciaEmpleado) {
        try {
            String sql = "SELECT tipotrabajador \n"
                    + "	FROM vigenciastipostrabajadores vtt\n"
                    + "	WHERE empleado=? \n"
                    + "	AND VTT.fechavigencia = (select max(fechavigencia) from vigenciastipostrabajadores vtti\n"
                    + "	                         where vtti.empleado= ? \n"
                    + "	                         and vtti.fechavigencia <= sysdate) ";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuenciaEmpleado);
            query.setParameter(2, secuenciaEmpleado);
            BigDecimal tt = (BigDecimal) query.getSingleResult();
            return tt;
        } catch (Exception e) {
            log.error("Error: (PersistenciaEmplSolucionesNodos.tipottXEmpleado) ", e);
            return null;
        }
    }

}
