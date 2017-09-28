package Persistencia;

import Entidades.EersCabeceras;
import InterfacePersistencia.PersistenciaEersCabecerasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaEersCabeceras implements PersistenciaEersCabecerasInterface {

   private static Logger log = Logger.getLogger(PersistenciaEersCabeceras.class);

   @Override
   public void crear(EntityManager em, EersCabeceras eersCabeceras) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(eersCabeceras);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaEersCabeceras.crear:  ", e);
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
         log.error("Error PersistenciaEersCabeceras.crear:  ", e);
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
         log.error("Error PersistenciaEersCabeceras.borrar:  ", e);
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
         log.error("Error buscarEersCabecerasTotales PersistenciaEersCabeceras  ", e);
         e.printStackTrace();
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
         e.printStackTrace();
         log.error("Error buscarEersCabecerasTotalesPorEmpleado PersistenciaEersCabeceras  ", e);
         return null;
      }
   }
}
