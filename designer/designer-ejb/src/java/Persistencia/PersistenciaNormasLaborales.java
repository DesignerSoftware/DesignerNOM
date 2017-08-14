/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.NormasLaborales;
import InterfacePersistencia.PersistenciaNormasLaboralesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'NormasLaborales'
 * (Para verificar que esta asociado a una VigenciaNormaEmpleado, se realiza la
 * operacion sobre la tabla VigenciasNormasEmpleados) de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaNormasLaborales implements PersistenciaNormasLaboralesInterface {

   private static Logger log = Logger.getLogger(PersistenciaNormasLaborales.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, NormasLaborales normasLaborales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(normasLaborales);
            tx.commit();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaNormasLaborales.crear()" + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    @Override
    public void editar(EntityManager em, NormasLaborales normasLaborales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(normasLaborales);
            tx.commit();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaNormasLaborales.editar()" + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    @Override
    public void borrar(EntityManager em, NormasLaborales normasLaborales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(normasLaborales));
            tx.commit();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaNormasLaborales.borrar()" + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    @Override
    public NormasLaborales consultarNormaLaboral(EntityManager em, BigInteger secuenciaNL) {
        try {
            em.clear();
            return em.find(NormasLaborales.class, secuenciaNL);
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaNormasLaborales.consultarNormaLaboral()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<NormasLaborales> consultarNormasLaborales(EntityManager em) {
        try{
        em.clear();
        Query query = em.createQuery("SELECT m FROM NormasLaborales m");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<NormasLaborales> lista = query.getResultList();
        return lista;
        }catch(Exception e){
            log.error("Persistencia.PersistenciaNormasLaborales.consultarNormasLaborales()" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contarVigenciasNormasEmpleadosNormaLaboral(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            Query query = em.createQuery("SELECT count(vn) FROM VigenciasNormasEmpleados vn WHERE vn.normalaboral.secuencia =:secNormaLaboral ");
            query.setParameter("secNormaLaboral", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("PersistenciaMotivosCambiosSueldos retorno ==" + retorno.intValue());

        } catch (Exception e) {
            log.error("ERROR EN PersistenciaMotivosCambiosSueldos verificarBorradoVigenciasSueldos ERROR :" + e.getMessage());
        } finally {
            return retorno;
        }
    }
}
