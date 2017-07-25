/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Bancos;
import InterfacePersistencia.PersistenciaBancosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Bancos' de la base de
 * datos
 *
 * @author Andrés Pineda
 */
@Stateless
public class PersistenciaBancos implements PersistenciaBancosInterface {

   private static Logger log = Logger.getLogger(PersistenciaBancos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     * @param em
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, Bancos bancos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(bancos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaBancos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Bancos bancos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(bancos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaBancos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Bancos bancos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(bancos));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaBancos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Bancos> buscarBancos(EntityManager em) {
        try {
            em.clear();
            String sql= "SELECT * FROM BANCOS ORDER BY CODIGO";
            Query query = em.createNativeQuery(sql, Bancos.class);
            List<Bancos> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error buscarBancos persistencia bancos : " + e.toString());
            return null;
        }
    }

    @Override
    public Bancos buscarBancosPorSecuencia(EntityManager em, BigInteger secuencia) {
        try{
            em.clear();
            String sql = "SELECT * FROM BANCOS WHERE SECUENCIA = ?";
            Query query = em.createNativeQuery(sql, Bancos.class);
            query.setParameter(1, secuencia);
            Bancos banco = (Bancos) query.getSingleResult();
            return banco;
        }catch(Exception e){
            log.error("error en BuscarBancosPorSecuencia persistencia bancos : " + e.toString());
            return null;
        }
    }
        }
