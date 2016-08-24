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
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'SubCategorias' de la
 * base de datos.
 *
 * @author Andres Pineda
 */
@Stateless
public class PersistenciaSubCategorias implements PersistenciaSubCategoriasInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, SubCategorias subCategorias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(subCategorias);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSubCategorias.crear: " + e);
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
            System.out.println("Error PersistenciaSubCategorias.editar: " + e);
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
            System.out.println("Error PersistenciaSubCategorias.borrar: " + e);
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
            System.err.println("ERROR PersistenciaSubCategorias ConsultarSubCategorias ERROR :" + e);
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
            System.out.println("Contador PersistenciaSubCategorias contarEscalafones persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaSubCategorias contarEscalafones. " + e);
            return retorno;
        }
    }
}
