/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposEmbargos;
import InterfacePersistencia.PersistenciaTiposEmbargosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposEmbargos' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaTiposEmbargos implements PersistenciaTiposEmbargosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, TiposEmbargos tiposEmbargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposEmbargos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposEmbargos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposEmbargos tiposEmbargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposEmbargos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposEmbargos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposEmbargos tiposEmbargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposEmbargos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposEmbargos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public TiposEmbargos buscarTipoEmbargo(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(TiposEmbargos.class, secuencia);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<TiposEmbargos> buscarTiposEmbargos(EntityManager em) {
        em.clear();
        Query query = em.createQuery("SELECT m FROM TiposEmbargos m ORDER BY m.codigo ASC");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<TiposEmbargos> listaMotivosPrestamos = query.getResultList();
        return listaMotivosPrestamos;
    }

    @Override
    public BigInteger contadorEerPrestamos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM eersprestamos ee WHERE ee.tipoembargo = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("PERSISTENCIATIPOSEMBARGOS CONTADOREERPRESTAMOS = " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("ERROR PERSISTENCIATIPOSEMBARGOS CONTADOREERPRESTAMOS ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    @Override
    public BigInteger contadorFormasDtos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM formasdtos fdts WHERE fdts.tipoembargo = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("PERSISTENCIATIPOSEMBARGOS CONTADORFORMASDTOS = " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("ERROR PERSISTENCIATIPOSEMBARGOS CONTADORFORMASDTOS ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }
}
