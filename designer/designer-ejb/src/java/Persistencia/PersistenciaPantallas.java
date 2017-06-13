/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Pantallas;
import InterfacePersistencia.PersistenciaPantallasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
import javax.persistence.*;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Pantallas' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaPantallas implements PersistenciaPantallasInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public Pantallas buscarPantalla(EntityManager em, BigInteger secuenciaTab) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p from Pantallas p where p.tabla.secuencia = :secuenciaTab");
            query.setParameter("secuenciaTab", secuenciaTab);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Pantallas pantalla = (Pantallas) query.getSingleResult();
            return pantalla;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaPantallas.buscarPantalla()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Pantallas> buscarPantallas(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT pan FROM Pantallas pan ORDER BY pan.codigo");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Pantallas> todosPantallas = query.getResultList();
            return todosPantallas;
        } catch (Exception e) {
            System.err.println("Error: PersistenciaPantallas consultarPantallas ERROR " + e.getMessage());
            return null;
        }
    }

    @Override
    public String buscarIntContable(EntityManager em, BigInteger secEmpresa) {
        try {
            em.clear();
            String sql = "SELECT NOMBRE FROM PANTALLAS WHERE CODIGO = 902 AND EMPRESA = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secEmpresa);
            String intcontables = (String) query.getSingleResult();
            return intcontables;
        } catch (Exception e) {
            System.err.println("Error: PersistenciaPantallas buscarIntContable ERROR " + e.getMessage());
            return " ";
        }
    }
    
    @Override
    public String buscarPantallaPorCodigoEmpresa(EntityManager em, BigInteger secEmpresa, Short codigo) {
        String sql;
        String intcontables = " ";
        Query query;
        try {
            em.clear();
            sql = "SELECT NOMBRE FROM PANTALLAS WHERE CODIGO = ? AND EMPRESA = ? ";
            query = em.createNativeQuery(sql);
            query.setParameter(1, codigo);
            query.setParameter(2, secEmpresa);
            intcontables = (String) query.getSingleResult();
            return intcontables;
        } catch (NoResultException nre)  {
            System.err.println("Error: PersistenciaPantallas buscarPantallaPorCodigoEmpresa ERROR " + nre.getMessage());
            return " ";
        }
    }

    @Override
    public String buscarPantallaPorCodigo(EntityManager em, Short codigo) {
        String sql;
        String intcontables = " ";
        Query query;
        try {
            em.clear();
            sql = "SELECT NOMBRE FROM PANTALLAS WHERE CODIGO = ?";
            query = em.createNativeQuery(sql);
            query.setParameter(1, codigo);
            intcontables = (String) query.getSingleResult();
        } catch (NonUniqueResultException nure) {
            System.err.println("Error: PersistenciaPantallas buscarPantallaPorCodigo ERROR " + nure.getMessage());
            try {
                em.clear();
                sql = "SELECT NOMBRE FROM PANTALLAS WHERE CODIGO = ? AND ROWNUM <= 1 ";
                query = em.createNativeQuery(sql);
                query.setParameter(1, codigo);
                intcontables = (String) query.getSingleResult();
            } catch (NoResultException nre) {
                intcontables = " ";
            }
        } finally {
            return intcontables;
        }
    }

}
