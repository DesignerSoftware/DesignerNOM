/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Novedades;
import InterfacePersistencia.PersistenciaNovedadesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Novedades' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaNovedades implements PersistenciaNovedadesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Novedades novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(novedades);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaNovedades.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Novedades novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(novedades);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaNovedades.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Novedades novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(novedades));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaNovedades.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
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
            System.out.println("Error buscarNovedad PersistenciaNovedades : " + e.toString());
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
            System.out.println("Error: (novedadesParaReversar)" + e);
            return null;
        }
    }

    @Override
    public List<Novedades> todasNovedadesEmpleado(EntityManager em, BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT n FROM Novedades n WHERE n.empleado.secuencia = :secuenciaEmpleado");
            query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Novedades> todasNovedades = query.getResultList();
            return todasNovedades;
        } catch (Exception e) {
            System.out.println("Error: (todasNovedades)" + e);
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
            System.out.println("Error: (todasNovedadesConcepto)" + e);
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
            System.out.println("Error: (todasNovedadesTercero)" + e);
            return null;
        }
    }

    @Override
    public int reversarNovedades(EntityManager em, BigInteger usuarioBD, String documentoSoporte) {
        try {
            em.clear();
            Query query = em.createQuery("DELETE FROM Novedades n WHERE n.usuarioreporta.secuencia = :usuarioBD AND n.documentosoporte = :documentoSoporte");
            query.setParameter("usuarioBD", usuarioBD);
            query.setParameter("documentoSoporte", documentoSoporte);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            int rows = query.executeUpdate();
            return rows;
        } catch (Exception e) {
            System.out.println("No se pudo borrar el registro. (reversarNovedades)" + e);
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
            System.out.println("Error PersistenciaNovedades.novedadesEmpleado" + e);
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
            return listaNovedades;
        } catch (Exception e) {
            System.out.println("Error PersistenciaNovedades.novedadesConcepto" + e);
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
            return listaNovedades;
        } catch (Exception e) {
            System.out.println("Error PersistenciaNovedades.novedadesConcepto" + e);
            return null;
        }
    }

}
