/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Familiares;
import InterfacePersistencia.PersistenciaFamiliaresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Familiares' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaFamiliares implements PersistenciaFamiliaresInterface {

    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     *
     * @param em
     * @param secuenciaPersona
     * @return
     */
    @Override
    public List<Familiares> familiaresPersona(EntityManager em, BigInteger secuenciaPersona) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(f) FROM Familiares f WHERE f.persona.secuencia = :secuenciaPersona");
            query.setParameter("secuenciaPersona", secuenciaPersona);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                Query queryFinal = em.createQuery("SELECT f FROM Familiares f WHERE f.persona.secuencia = :secuenciaPersona");
                queryFinal.setParameter("secuenciaPersona", secuenciaPersona);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                List<Familiares> listFamiliares = queryFinal.getResultList();
                return listFamiliares;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error PersistenciaFamiliares.familiaresPersona" + e);
            return null;
        }
    }

    @Override
    public void crear(EntityManager em, Familiares familiar) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(familiar);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaFamiliares.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Familiares familiar) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(familiar);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaFamiliares.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Familiares familiar) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(familiar));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaFamiliares.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public String consultaFamiliar(EntityManager em, BigInteger secuenciaPersona) {
        String resultado = null;
        try {
            em.clear();
            String consulta = "SELECT SUBSTR(B.TIPO ||'  '|| P.PRIMERAPELLIDO ||' '|| P.NOMBRE,1,30)\n"
                    + "   FROM  FAMILIARES A, TIPOSFAMILIARES B,PERSONAS P\n"
                    + "   WHERE A.TIPOFAMILIAR = B.SECUENCIA \n"
                    + "   AND A.PERSONAFAMILIAR = P.SECUENCIA \n"
                    + "   AND A.SECUENCIA = (SELECT MAX(V.SECUENCIA) FROM FAMILIARES V WHERE V.PERSONA = A.PERSONA)\n"
                    + "   AND A.PERSONA = " + secuenciaPersona + " ";
            Query query = em.createNativeQuery(consulta);
            resultado = (String) query.getSingleResult();
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".consultaFamiliar()");
            System.out.println("error: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public String consultarPrimerFamiliar(EntityManager em, BigInteger secuenciaPersona) {
        String familiar;
        try {
            em.clear();
            String sql = "SELECT SUBSTR(B.TIPO ||'  '|| P.PRIMERAPELLIDO ||' '|| P.NOMBRE,1,30)\n"
                    + "    FROM  FAMILIARES A, TIPOSFAMILIARES B,PERSONAS P\n"
                    + "   WHERE A.PERSONA = ? AND\n"
                    + "   A.TIPOFAMILIAR = B.SECUENCIA AND\n"
                    + "   A.PERSONAFAMILIAR = P.SECUENCIA \n"
                    + "   AND A.SECUENCIA = (SELECT MAX(V.SECUENCIA) FROM FAMILIARES V WHERE V.PERSONA = A.PERSONA)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuenciaPersona);
            familiar = (String) query.getSingleResult();
            return familiar;
        } catch (Exception e) {
            familiar = "SIN REGISTRAR";
            return familiar;
        }
    }
}
