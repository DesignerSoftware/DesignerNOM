/**
 * Documentación a cargo de Andres Pineda
 */
package Persistencia;

import Entidades.SubCategorias;
import InterfacePersistencia.PersistenciaSubCategoriasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaSubCategorias implements PersistenciaSubCategoriasInterface {

   private static Logger log = Logger.getLogger(PersistenciaSubCategorias.class);

    @Override
    public void crear(EntityManager em, SubCategorias subCategorias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(subCategorias);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaSubCategorias.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, SubCategorias subCategorias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(subCategorias);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaSubCategorias.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, SubCategorias subCategorias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(subCategorias));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaSubCategorias.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<SubCategorias> consultarSubCategorias(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT l FROM SubCategorias  l ORDER BY l.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<SubCategorias> listSubCategorias = query.getResultList();
            return listSubCategorias;
        } catch (Exception e) {
            log.error("ERROR PersistenciaSubCategorias ConsultarSubCategorias ERROR :  ", e);
            return null;
        }

    }

    @Override
    public SubCategorias consultarSubCategoria(EntityManager em, BigInteger secSubCategoria) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT sc FROM SubCategorias sc WHERE sc.secuencia=:secuencia");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("secuencia", secSubCategoria);
            SubCategorias subCategorias = (SubCategorias) query.getSingleResult();
            return subCategorias;
        } catch (Exception e) {
            log.error("PersistenciaSubCategorias.consultarSubCategoria():  ", e);
            return null;
        }
    }

    @Override
    public BigInteger contarEscalafones(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM escalafones WHERE subcategoria = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaSubCategorias contarEscalafones.  ", e);
            return retorno;
        }
    }
}
