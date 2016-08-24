package Persistencia;

import Entidades.EersCabeceras;
import InterfacePersistencia.PersistenciaEersCabecerasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaEersCabeceras implements PersistenciaEersCabecerasInterface{

    @Override
    public void crear(EntityManager em, EersCabeceras eersCabeceras) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(eersCabeceras);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEersCabeceras.crear: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, EersCabeceras eersCabeceras) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(eersCabeceras);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEersCabeceras.crear: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, EersCabeceras eersCabeceras) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(eersCabeceras));
            tx.commit();

        } catch (Exception e) {
            System.out.println("Error PersistenciaEersCabeceras.borrar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<EersCabeceras> buscarEersCabecerasTotales(EntityManager em) {
        try {
            em.clear();
            String sql = "select * from EersCabeceras V where V.TIPOEER='TURNO' AND V.estado='EN PROCESO' \n"
                    + "AND EXISTS (SELECT 'X' FROM EERSCABECERAS EC2\n"
                    + "        WHERE EC2.ESTRUCTURAaprueba IN\n"
                    + "            (select e1.secuencia from estructuras e1\n"
                    + "             start with e1.estructurapadre = \n"
                    + "                (SELECT ESTRUCTURA FROM EERSAUTORIZACIONES EA, USUARIOS U \n"
                    + "                 WHERE U.secuencia=EA.usuario\n"
                    + "                 AND   U.alias=USER\n"
                    + "                 AND EA.eerestado=EC2.EERESTADO)\n"
                    + "             connect by prior e1.secuencia = e1.estructurapadre \n"
                    + "             UNION \n"
                    + "                SELECT ESTRUCTURA FROM EERSAUTORIZACIONES EA, USUARIOS U \n"
                    + "                WHERE U.secuencia=EA.usuario\n"
                    + "                AND   U.alias=USER\n"
                    + "                AND EA.eerestado = EC2.EERESTADO)\n"
                    + "        AND V.SECUENCIA=EC2.SECUENCIA)";
            Query query = em.createNativeQuery(sql, EersCabeceras.class);
            List<EersCabeceras> eersCabeceras = query.getResultList();
            return eersCabeceras;
        } catch (Exception e) {
            System.out.println("Error buscarEersCabecerasTotales PersistenciaEersCabeceras " + e.toString());
            return null;
        }
    }
    
    @Override
    public List<EersCabeceras> buscarEersCabecerasTotalesPorEmpleado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            String sql = "select * from EersCabeceras V where V.TIPOEER='TURNO' AND V.estado='EN PROCESO' and V.empleado = ? \n"
                    + "AND EXISTS (SELECT 'X' FROM EERSCABECERAS EC2\n"
                    + "        WHERE EC2.ESTRUCTURAaprueba IN\n"
                    + "            (select e1.secuencia from estructuras e1\n"
                    + "             start with e1.estructurapadre = \n"
                    + "                (SELECT ESTRUCTURA FROM EERSAUTORIZACIONES EA, USUARIOS U \n"
                    + "                 WHERE U.secuencia=EA.usuario\n"
                    + "                 AND   U.alias=USER\n"
                    + "                 AND EA.eerestado=EC2.EERESTADO)\n"
                    + "             connect by prior e1.secuencia = e1.estructurapadre \n"
                    + "             UNION \n"
                    + "                SELECT ESTRUCTURA FROM EERSAUTORIZACIONES EA, USUARIOS U \n"
                    + "                WHERE U.secuencia=EA.usuario\n"
                    + "                AND   U.alias=USER\n"
                    + "                AND EA.eerestado = EC2.EERESTADO)\n"
                    + "        AND V.SECUENCIA=EC2.SECUENCIA)";
            Query query = em.createNativeQuery(sql, EersCabeceras.class);
            query.setParameter(1, secuencia);
            List<EersCabeceras> eersCabeceras = query.getResultList();
            return eersCabeceras;
        } catch (Exception e) {
            System.out.println("Error buscarEersCabecerasTotalesPorEmpleado PersistenciaEersCabeceras " + e.toString());
            return null;
        }
    }
}
