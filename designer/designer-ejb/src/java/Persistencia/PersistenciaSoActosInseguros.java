/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.SoActosInseguros;
import InterfacePersistencia.PersistenciaSoActosInsegurosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'SoActosInseguros' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaSoActosInseguros implements PersistenciaSoActosInsegurosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, SoActosInseguros SoActosInseguros) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(SoActosInseguros);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoActosInseguros.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, SoActosInseguros SoActosInseguros) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(SoActosInseguros);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoActosInseguros.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, SoActosInseguros SoActosInseguros) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(SoActosInseguros));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoActosInseguros.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public SoActosInseguros buscarSoActoInseguro(EntityManager em, BigInteger secuenciaSCAP) {
        try {
            em.clear();
            return em.find(SoActosInseguros.class, secuenciaSCAP);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<SoActosInseguros> buscarSoActosInseguros(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT l FROM SoActosInseguros  l ORDER BY l.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<SoActosInseguros> listSoCondicionesAmbientalesP = query.getResultList();
            return listSoCondicionesAmbientalesP;
        } catch (Exception e) {
            System.err.println("ERROR BUSCAR CLASES SOACTOSINSEGUROS :" + e);
            return null;
        }
    }

    @Override
    public BigInteger contadorSoAccidentesMedicos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM soaccidentesmedicos sam WHERE sam.actoinseguro = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) new BigInteger(query.getSingleResult().toString());
            System.err.println("Contador PersistenciaSoActosInseguros contadorSoAccidentesMedicos persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoActosInseguros contadorSoAccidentesMedicos. " + e);
            return retorno;
        }
    }
}
