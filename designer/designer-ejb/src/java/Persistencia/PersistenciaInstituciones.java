/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Instituciones;
import InterfacePersistencia.PersistenciaInstitucionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Instituciones' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaInstituciones implements PersistenciaInstitucionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaInstituciones.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//   @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Instituciones instituciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(instituciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInstituciones.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Instituciones instituciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(instituciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInstituciones.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Instituciones instituciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(instituciones));
            tx.commit();

        } catch (Exception e) {
            log.error("Error PersistenciaInstituciones.borrar: " + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    @Override
    public Instituciones buscarInstitucion(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            BigInteger sec = new BigInteger(secuencia.toString());
            return em.find(Instituciones.class, sec);
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaInstituciones.buscarInstitucion()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Instituciones> instituciones(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM INSTITUCIONES ORDER by CODIGO";
            Query query = em.createNativeQuery(sql, Instituciones.class);
            List<Instituciones> listaInstituciones = query.getResultList();
            return listaInstituciones;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaInstituciones.instituciones()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Instituciones> lovInstituciones(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT ALL INSTITUCIONES.SECUENCIA, \n"
                    + "INSTITUCIONES.CODIGO, INSTITUCIONES.DESCRIPCION\n"
                    + "FROM INSTITUCIONES  ORDER BY INSTITUCIONES.CODIGO ASC";
            Query query = em.createNativeQuery(sql, Instituciones.class);
            List<Instituciones> listaInstituciones = query.getResultList();
            return listaInstituciones;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaInstituciones.lovInstituciones()" + e.getMessage());
            return null;
        }
    }
}
