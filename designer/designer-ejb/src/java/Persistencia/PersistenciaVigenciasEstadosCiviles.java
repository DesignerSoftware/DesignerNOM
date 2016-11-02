/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasEstadosCiviles;
import InterfacePersistencia.PersistenciaVigenciasEstadosCivilesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
//import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'VigenciasEstadosCiviles' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasEstadosCiviles implements PersistenciaVigenciasEstadosCivilesInterface {

    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     *
     * @param em
     * @param vigenciasEstadosCiviles
     * @return
     */
    @Override
    public boolean crear(EntityManager em, VigenciasEstadosCiviles vigenciasEstadosCiviles) {
        System.out.println(this.getClass().getName() + ".crear()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasEstadosCiviles);
            tx.commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error en crear");
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("se cerro la transaccion");
            return false;
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasEstadosCiviles vigenciasEstadosCiviles) {
        System.out.println(this.getClass().getName() + "editar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasEstadosCiviles);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error en editar");
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("se cerro la transaccion");
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasEstadosCiviles vigenciasEstadosCiviles) {
        System.out.println(this.getClass().getName() + ".borrar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasEstadosCiviles));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error en borrar");
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("se cerro la transaccion");
        }
    }

    @Override
    public VigenciasEstadosCiviles buscarVigenciaEstadoCivil(EntityManager em, BigInteger secuencia) {
        System.out.println(this.getClass().getName() + ".buscarVigenciaEstadoCivil()");
        try {
            em.clear();
            return em.find(VigenciasEstadosCiviles.class, secuencia);
        } catch (Exception e) {
            System.out.println("Error en buscarVigenciaEstadoCivil");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<VigenciasEstadosCiviles> consultarVigenciasEstadosCiviles(EntityManager em) {
        System.out.println(this.getClass().getName() + ".consultarVigenciasEstadosCiviles()");
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VigenciasEstadosCiviles.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("error en consultarVigenciasEstadosCiviles");
            e.printStackTrace();
            return null;
        }
    }

    private Long contarVigenciasEstadosCivielesPersona(EntityManager em, BigInteger secuenciaPersona) {
        System.out.println(this.getClass().getName() + ".contarVigenciasEstadosCivielesPersona()");
        Long resultado = null;
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(vec) FROM VigenciasEstadosCiviles vec WHERE vec.persona.secuencia = :secuenciaPersona");
            query.setParameter("secuenciaPersona", secuenciaPersona);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            resultado = (Long) query.getSingleResult();
            return resultado;
        } catch (Exception e) {
            System.out.println("Error en contarVigenciasEstadosCivielesPersona");
            e.printStackTrace();
            return resultado;
        }
    }

    @Override
    public List<VigenciasEstadosCiviles> consultarVigenciasEstadosCivilesPersona(EntityManager em, BigInteger secuenciaPersona) {
        Long resultado = this.contarVigenciasEstadosCivielesPersona(em, secuenciaPersona);
        System.out.println(this.getClass().getName() + ".consultarVigenciasEstadosCivilesPersona()");
        if (resultado != null && resultado > 0) {
            try {
                /*em.clear();
                 Query query = em.createQuery("SELECT COUNT(vec) FROM VigenciasEstadosCiviles vec WHERE vec.persona.secuencia = :secuenciaPersona");
                 query.setParameter("secuenciaPersona", secuenciaPersona);
                 query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                 Long resultado = (Long) query.getSingleResult();*/
                Query queryFinal = em.createQuery("SELECT vec FROM VigenciasEstadosCiviles vec WHERE vec.persona.secuencia = :secuenciaPersona and vec.fechavigencia = (SELECT MAX(veci.fechavigencia) FROM VigenciasEstadosCiviles veci WHERE veci.persona.secuencia = :secuenciaPersona)");
                queryFinal.setParameter("secuenciaPersona", secuenciaPersona);
                List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles = queryFinal.getResultList();
                return listaVigenciasEstadosCiviles;
            } catch (Exception e) {
                System.out.println("Error PersistenciaVigenciasEstadosCiviles.estadoCivilPersona" + e);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<VigenciasEstadosCiviles> consultarVigenciasEstadosCivilesPorPersona(EntityManager em, BigInteger secuenciaPersona) {
        System.out.println(this.getClass().getName() + ".consultarVigenciasEstadosCivilesPorPersona()");
        try {
            em.clear();
            Query query = em.createQuery("SELECT vec FROM VigenciasEstadosCiviles vec WHERE vec.persona.secuencia = :secuenciaPersona");
            query.setParameter("secuenciaPersona", secuenciaPersona);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles = query.getResultList();
            return listaVigenciasEstadosCiviles;
        } catch (Exception e) {
            System.out.println("error en consultarVigenciasEstadosCivilesPorPersona");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public VigenciasEstadosCiviles estadoCivilActual(EntityManager em, BigInteger secuenciaPersona) {
        try {
            em.clear();
            String sql = "SELECT * FROM VIGENCIASESTADOSCIVILES WHERE FECHAVIGENCIA =(SELECT MAX(FECHAVIGENCIA) FROM VIGENCIASESTADOSCIVILES WHERE PERSONA = ? )";
            Query query = em.createNativeQuery(sql, VigenciasEstadosCiviles.class);
            query.setParameter(1, secuenciaPersona);
            VigenciasEstadosCiviles estadoc = (VigenciasEstadosCiviles) query.getSingleResult();
            return estadoc;
        } catch (Exception e) {
            System.out.println("Error en estadoCivilActual : " + e.toString());
            return null;
        }
    }

    @Override
    public String consultarPrimerEstadoCivil(EntityManager em, BigInteger secuenciaPersona) {
        String EstadoCivil;
        try {
            em.clear();
            String sql = "SELECT SUBSTR(B.DESCRIPCION||' '||TO_CHAR(A.FECHAVIGENCIA,'DD-MM-YYYY'),1,30) \n"
                    + "   FROM VIGENCIASESTADOSCIVILES A, ESTADOSCIVILES B \n"
                    + "   WHERE A.PERSONA = ? AND \n"
                    + "   A.ESTADOCIVIL = B.SECUENCIA \n"
                    + "   AND A.FECHAVIGENCIA = (SELECT MAX(V.FECHAVIGENCIA) FROM VIGENCIASESTADOSCIVILES V WHERE V.PERSONA = A.PERSONA)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuenciaPersona);
            EstadoCivil = (String) query.getSingleResult();
            return EstadoCivil;
        } catch (Exception e) {
            EstadoCivil = "";
            return EstadoCivil;
        }
    }
}
