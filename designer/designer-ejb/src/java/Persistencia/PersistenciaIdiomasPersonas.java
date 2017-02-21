/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.IdiomasPersonas;
import InterfacePersistencia.PersistenciaIdiomasPersonasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
//import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'IdiomasPersonas' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaIdiomasPersonas implements PersistenciaIdiomasPersonasInterface {

//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     *
     * @param em
     * @param idiomasPersonas
     */
    @Override
    public void crear(EntityManager em, IdiomasPersonas idiomasPersonas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(idiomasPersonas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaIdiomasPersonas.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, IdiomasPersonas idiomasPersonas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(idiomasPersonas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaIdiomasPersonas.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, IdiomasPersonas idiomasPersonas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(idiomasPersonas));
            tx.commit();

        } catch (Exception e) {
        System.out.println("Error PersistenciaIdiomasPersonas.borrar: " + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    private Long contarIdiomasPersona(EntityManager em, BigInteger secuenciaPersona) {
        Long resultado = null;
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(ip) FROM IdiomasPersonas ip WHERE ip.persona.secuencia = :secuenciaPersona");
            query.setParameter("secuenciaPersona", secuenciaPersona);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            resultado = (Long) query.getSingleResult();
            return resultado;
        } catch (Exception e) {
            System.out.println("error en contarIdiomasPersona" + e.getMessage());
            e.printStackTrace();
            return resultado;
        }
    }

    @Override
    public List<IdiomasPersonas> idiomasPersona(EntityManager em, BigInteger secuenciaPersona) {
        try {
            Query queryFinal = em.createQuery("SELECT ip FROM IdiomasPersonas ip WHERE ip.persona.secuencia = :secuenciaPersona");
            queryFinal.setParameter("secuenciaPersona", secuenciaPersona);
            queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<IdiomasPersonas> listaIdiomasPersonas = queryFinal.getResultList();
            return listaIdiomasPersonas;
        } catch (Exception e) {
            System.out.println("Error PersistenciaIdiomasPersonas.idiomasPersona" + e.toString());
            return null;
        }
    }

    @Override
    public List<IdiomasPersonas> totalIdiomasPersonas(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT ip FROM IdiomasPersonas ip");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<IdiomasPersonas> resultado = (List<IdiomasPersonas>) query.getResultList();
            return resultado;
        } catch (Exception e) {
            System.out.println("Error PersistenciaIdiomasPersonas.totalIdiomasPersonas" + e.getMessage());
            return null;
        }
    }

    @Override
    public String primerIdioma(EntityManager em,BigInteger secuenciaPersona) {
        String idioma;
        try {
            em.clear();
            String sql = "SELECT SUBSTR(B.NOMBRE,1,30)\n"
                    + "   FROM  VWIDIOMASPERSONAS A, IDIOMAS B\n"
                    + "   WHERE A.PERSONA = ? AND\n"
                    + "   A.IDIOMA = B.SECUENCIA\n"
                    + "   AND A.SECUENCIA = (SELECT MAX(V.SECUENCIA) FROM VWIDIOMASPERSONAS V WHERE V.PERSONA = A.PERSONA) ";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1,secuenciaPersona);
            idioma = (String) query.getSingleResult();
            return idioma;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaIdiomasPersonas.primerIdioma()" + e.getMessage());
            idioma = "";
            return idioma;
        }
    }
}
