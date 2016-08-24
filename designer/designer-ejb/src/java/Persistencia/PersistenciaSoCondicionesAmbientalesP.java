/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaSoCondicionesAmbientalesPInterface;
import Entidades.SoCondicionesAmbientalesP;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'SoCondicionesAmbientalesP' de la base de datos.
 *
 * @author John Pineda.
 */
@Stateless
public class PersistenciaSoCondicionesAmbientalesP implements PersistenciaSoCondicionesAmbientalesPInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, SoCondicionesAmbientalesP soCondicionesAmbientalesP) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(soCondicionesAmbientalesP);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoCondicionesAmbientalesP.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, SoCondicionesAmbientalesP soCondicionesAmbientalesP) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(soCondicionesAmbientalesP);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoCondicionesAmbientalesP.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, SoCondicionesAmbientalesP soCondicionesAmbientalesP) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(soCondicionesAmbientalesP));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoCondicionesAmbientalesP.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public SoCondicionesAmbientalesP buscarSoCondicionAmbientalP(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(SoCondicionesAmbientalesP.class, secuencia);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<SoCondicionesAmbientalesP> buscarSoCondicionesAmbientalesP(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT l FROM SoCondicionesAmbientalesP  l ORDER BY l.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP = query.getResultList();
            return listSoCondicionesAmbientalesP;
        } catch (Exception e) {
            System.err.println("ERROR BUSCAR CLASES SO CONDICIONES AMBIENTALES P :" + e);
            return null;
        }

    }

    @Override
    public BigInteger contadorSoAccidentesMedicos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM soaccidentesmedicos sam WHERE sam.condicionambientalp = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) new BigInteger(query.getSingleResult().toString());
            System.err.println("Contador PersistenciaSoCondicionesAmbientalesP contadorSoAccidentesMedicos persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoCondicionesAmbientalesP contadorSoAccidentesMedicos. " + e);
            return retorno;
        }
    }
}
