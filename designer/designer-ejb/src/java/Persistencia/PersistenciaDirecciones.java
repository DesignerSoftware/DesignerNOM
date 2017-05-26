/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Direcciones;
import InterfacePersistencia.PersistenciaDireccionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
//import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Direcciones' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaDirecciones implements PersistenciaDireccionesInterface {

    /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     *
     * @param em
     * @param direcciones
     */
    @Override
    public boolean crear(EntityManager em, Direcciones direcciones) {
        System.out.println(this.getClass().getName() + ".crear()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(direcciones);
            tx.commit();
            return true;
        } catch (Exception e) {
            System.out.println("error en crear");
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("se cerro la transaccion");
            return false;
        }
    }

    @Override
    public void editar(EntityManager em, Direcciones direcciones) {
        System.out.println(this.getClass().getName() + ".editar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(direcciones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("error en editar");
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("se cerro la transaccion");
        }
    }

    @Override
    public void borrar(EntityManager em, Direcciones direcciones) {
        System.out.println(this.getClass().getName() + ".borrar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(direcciones));
            tx.commit();
        } catch (Exception e) {
            System.out.println("error en borrar");
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("se cerro la transaccion");
        }
    }

    @Override
    public Direcciones buscarDireccion(EntityManager em, BigInteger secuencia) {
        System.out.println(this.getClass().getName() + "buscarDireccion()");
        try {
            em.clear();
            return em.find(Direcciones.class, secuencia);
        } catch (Exception e) {
            System.out.println("error en buscarDireccion");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Direcciones> buscarDirecciones(EntityManager em) {
        System.out.println(this.getClass().getName() + ".buscarDirecciones()");
        try {
            em.clear();
            javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Direcciones.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("error en buscarDirecciones");
            e.printStackTrace();
            return null;
        }
    }

    private Long contarDireccionesPersona(EntityManager em, BigInteger secuenciaPersona) {
        System.out.println(this.getClass().getName() + ".contarDireccionesPersona()");
        Long resultado = null;
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(d) FROM Direcciones d WHERE d.persona.secuencia = :secuenciaPersona");
            query.setParameter("secuenciaPersona", secuenciaPersona);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            resultado = (Long) query.getSingleResult();
            return resultado;
        } catch (Exception e) {
            System.out.println("error en contarDireccionesPersona");
            e.printStackTrace();
            return resultado;
        }
    }

    @Override
    public List<Direcciones> direccionPersona(EntityManager em, BigInteger secuenciaPersona) {
        Long resultado = this.contarDireccionesPersona(em, secuenciaPersona);
        System.out.println(this.getClass().getName() + ".direccionPersona()");
        if (resultado != null && resultado > 0) {
            try {
                /*em.clear();
                 Query query = em.createQuery("SELECT COUNT(d) FROM Direcciones d WHERE d.persona.secuencia = :secuenciaPersona");
                 query.setParameter("secuenciaPersona", secuenciaPersona);
                 query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                 Long resultado = (Long) query.getSingleResult();*/
                Query queryFinal = em.createQuery("SELECT d FROM Direcciones d WHERE d.persona.secuencia = :secuenciaPersona and d.fechavigencia = (SELECT MAX(di.fechavigencia) FROM Direcciones di WHERE di.persona.secuencia = :secuenciaPersona) ");
                queryFinal.setParameter("secuenciaPersona", secuenciaPersona);
                queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
                List<Direcciones> listaDirecciones = queryFinal.getResultList();
                return listaDirecciones;
            } catch (Exception e) {
                System.out.println("error en direccionPersona");
                return null;
            }
        } else {
            System.out.println("No hubo una respuesta de conteo exitosa");
            return null;
        }
    }

    @Override
    public List<Direcciones> direccionesPersona(EntityManager em, BigInteger secuenciaPersona) {
        System.out.println(this.getClass().getName() + ".direccionesPersona()");
        Long resultado = this.contarDireccionesPersona(em, secuenciaPersona);
        if (resultado != null && resultado > 0) {
            try {
                /*em.clear();
                 Query query = em.createQuery("SELECT COUNT(d) FROM Direcciones d WHERE d.persona.secuencia = :secuenciaPersona");
                 query.setParameter("secuenciaPersona", secuenciaPersona);
                 query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                 Long resultado = (Long) query.getSingleResult();*/
                Query queryFinal = em.createQuery("SELECT d FROM Direcciones d WHERE d.persona.secuencia = :secuenciaPersona ");
                queryFinal.setParameter("secuenciaPersona", secuenciaPersona);
                queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
                List<Direcciones> listaDirecciones = queryFinal.getResultList();
                return listaDirecciones;
            } catch (Exception e) {
                System.out.println("error en direccionesPersona");
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("No hubo una respuesta de conteo exitosa");
            return null;
        }
    }

    @Override
    public List<Direcciones> listaDireccionPersona(EntityManager em, BigInteger secuenciaPersona) {
        try {
            em.clear();
            String sql = "SELECT * FROM DIRECCIONES WHERE PERSONA = ? ORDER BY FECHAVIGENCIA DESC ";
            Query query = em.createNativeQuery(sql, Direcciones.class);
            query.setParameter(1, secuenciaPersona);
            List<Direcciones> direccion = query.getResultList();
            return direccion;
        } catch (Exception e) {
            System.out.println("Error en direccionActualPersona : " + e.toString());
            return null;
        }
    }

    @Override
    public String consultarPrimeraDireccion(EntityManager em, BigInteger secuenciaPersona) {
        String direccion;
        try {
            em.clear();
            String sql = "SELECT substr(NVL(direccionalternativa,A.TIPOPPAL ||'. '||A.PPAL ||' # '||A.SECUNDARIO||' '||B.NOMBRE),1,35)\n"
                    + "   FROM VWDIRECCIONES A, CIUDADES B \n"
                    + "   WHERE  A.PERSONA = ? \n"
                    + "   AND A.CIUDAD=B.SECUENCIA \n"
                    + "   AND A.SECUENCIA =(SELECT MAX(V.SECUENCIA) FROM VWDIRECCIONES V WHERE V.persona = A.PERSONA)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuenciaPersona);
            direccion = (String) query.getSingleResult();
            if (direccion == null || direccion.equals("")) {
                direccion = " ";
            } 
            return direccion;
        } catch (NoResultException e) {
            System.out.println("Persistencia.PersistenciaDirecciones.consultarPrimeraDireccion()" + e.getMessage());
            direccion = " ";
            return direccion;
        }
    }

    @Override
    public List<Direcciones> direccionesBanco(EntityManager em, BigInteger secuenciaBanco) {
        try {
            em.clear();
            String sql = "SELECT * FROM DIRECCIONES WHERE BANCO = ? ORDER BY FECHAVIGENCIA DESC ";
            Query query = em.createNativeQuery(sql, Direcciones.class);
            query.setParameter(1, secuenciaBanco);
            List<Direcciones> direccion = query.getResultList();
            return direccion;
        } catch (Exception e) {
            System.out.println("Error en direccionesBanco : " + e.toString());
            return null;
        }
    }
}
