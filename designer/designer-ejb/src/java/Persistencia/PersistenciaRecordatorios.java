/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Recordatorios;
import InterfacePersistencia.PersistenciaRecordatoriosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
//import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Recordatorios' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaRecordatorios implements PersistenciaRecordatoriosInterface {

//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Recordatorios recordatorios) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        System.out.println("PersistenciaRecordatorios.crear:EntityManager: "+ em );
        System.out.println("PersistenciaRecordatorios.crear: Recordatorios: "+ recordatorios );
        try {
            tx.begin();
            //em.merge(recordatorios);
            em.persist(recordatorios);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRecordatorios.crear: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Recordatorios recordatorios) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(recordatorios);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRecordatorios.editar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Recordatorios recordatorios) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(recordatorios));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRecordatorios.borrar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Recordatorios recordatorioRandom(EntityManager entity) {
        try {
            entity.clear();
            String consulta = "SELECT *\n"
                    + "    FROM (SELECT * FROM  RECORDATORIOS R\n"
                    + "    WHERE TIPO = 'PROVERBIO' \n"
                    + "    ORDER BY dbms_random.value)ra\n"
                    + "    WHERE ROWNUM=1";
            Query query = entity.createNativeQuery(consulta, Recordatorios.class);
            //Query query = entity.createNativeQuery(consulta); // obliga a hacer un casting al usar la lista.
            Recordatorios recordatorio = (Recordatorios) query.getSingleResult();
            return recordatorio;
        } catch (Exception e) {
            System.out.println("Error: PersistenciaRecordatorios.recordatorioRandom : " + e.toString());
            return null;
        }
    }

    @Override
    public List<String> recordatoriosInicio(EntityManager entity) {
        try {
            entity.clear();
            /*String consulta = "SELECT R.MENSAJE FROM RECORDATORIOS R WHERE R.TIPO='RECORDATORIO' "
                    + "AND (R.DIA=0 OR R.DIA=TO_NUMBER(TO_CHAR(SYSDATE,'DD'))) AND (R.MES=0 "
                    + "OR R.MES=TO_NUMBER(TO_CHAR(SYSDATE,'MM'))) AND (R.ANO=0 "
                    + "OR R.ANO=TO_NUMBER(TO_CHAR(SYSDATE,'YYYY'))) "
                    + "AND (R.USUARIO=(SELECT U.SECUENCIA FROM USUARIOS U "
                    + "WHERE U.ALIAS=USER) OR R.USUARIO IS NULL)";*/
            String consulta = "SELECT R.MENSAJE \n" +
                              "FROM RECORDATORIOS R \n" +
                              "WHERE R.TIPO='RECORDATORIO' \n" +
                              "AND (R.DIA=0 OR R.DIA=TO_NUMBER(TO_CHAR(SYSDATE,'DD')) OR (R.DIA - R.DIASPREVIOS) <= TO_NUMBER(TO_CHAR(SYSDATE,'DD')) ) \n" +
                              "AND (R.MES=0 OR R.MES=TO_NUMBER(TO_CHAR(SYSDATE,'MM'))) \n" +
                              "AND (R.ANO=0 OR R.ANO=TO_NUMBER(TO_CHAR(SYSDATE,'YYYY'))) \n" +
                              "AND (R.USUARIO=(SELECT U.SECUENCIA \n" +
                              "                FROM USUARIOS U \n" +
                              "                WHERE U.ALIAS=USER) \n" +
                              "     OR R.USUARIO IS NULL)";
            Query query = entity.createNativeQuery(consulta);
            List<String> listaRecordatorios = query.getResultList();
            return listaRecordatorios;
        } catch (Exception e) {
            System.out.println("Error: PersistenciaRecordatorios.recordatoriosInicio : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Recordatorios> consultasInicio(EntityManager entity) {
        try {
            entity.clear();
            String consulta = "SELECT SECUENCIA, TIPO, MENSAJE, USUARIO, CONSULTA, DIA, MES, ANO, DIASPREVIOS, NOMBREIMAGEN FROM RECORDATORIOS R WHERE R.TIPO='CONSULTA' "
                    + "AND (R.DIA=0 OR R.DIA=TO_NUMBER(TO_CHAR(SYSDATE,'DD'))) AND (R.MES=0 "
                    + "OR R.MES=TO_NUMBER(TO_CHAR(SYSDATE,'MM'))) AND (R.ANO=0 "
                    + "OR R.ANO=TO_NUMBER(TO_CHAR(SYSDATE,'YYYY'))) "
                    + "AND (R.USUARIO=(SELECT U.SECUENCIA FROM USUARIOS U "
                    + "WHERE U.ALIAS=USER) OR R.USUARIO IS NULL)";
            Query query = entity.createNativeQuery(consulta, Recordatorios.class);
            //Query query = entity.createNativeQuery(consulta);
            List<Recordatorios> listaConsultas = query.getResultList();
            return listaConsultas;
        } catch (Exception e) {
            System.out.println("Error: PersistenciaRecordatorios.consultasInicio : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Recordatorios> proverbiosRecordatorios(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Recordatorios r WHERE r.tipo = 'PROVERBIO'");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Recordatorios> recordatorios = query.getResultList();
            return recordatorios;
        } catch (Exception e) {
            System.out.println("Error: PersistenciaRecordatorios.proverbiosRecordatorios : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Recordatorios> mensajesRecordatorios(EntityManager em) {
        try {
            em.clear();
            String consulta = "SELECT * FROM RECORDATORIOS R WHERE R.TIPO='RECORDATORIO' and (usuario =(SELECT U.SECUENCIA FROM USUARIOS U "
                    + "WHERE U.ALIAS=USER) OR usuario IS NULL)";
            Query query = em.createNativeQuery(consulta, Recordatorios.class);
            //Query query = em.createNativeQuery(consulta);  //Obliga a hacer un casting cuando la lista se use.
            List<Recordatorios> listaConsultas = query.getResultList();
            return listaConsultas;
        } catch (Exception e) {
            System.out.println("Error: PersistenciaRecordatorios.mensajesRecordatorios : " + e.toString());
            return null;
        }
    }

    @Override
    public Recordatorios consultaRecordatorios(EntityManager em, BigInteger secuencia) throws Exception{
        /*em.clear();
         Query query = em.createQuery("SELECT r FROM Recordatorios r WHERE r.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Recordatorios recordatorios = (Recordatorios) query.getSingleResult();
         return recordatorios;*/
        try {
            em.clear();
            return em.find(Recordatorios.class, secuencia);
        }catch(Exception e){
            System.out.println("consultaRecordatorios en "+this.getClass().getName() + ": " );
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<String> ejecutarConsultaRecordatorio(EntityManager em, BigInteger secuencia) throws Exception{
        System.out.println("PersistenciaRecordatorios.ejecutarConsultaRecordatorio");
        try{
        Recordatorios recor = consultaRecordatorios(em, secuencia);
        em.clear();
        String consulta = recor.getConsulta();
        System.out.println("consulta: "+consulta);
        Query query = em.createNativeQuery(consulta);
        List<String> listaConsultas = query.getResultList();
        return listaConsultas;
        } catch(Exception e) {
            System.out.println("ejecutarConsultaRecordatorio en "+ this.getClass().getName() + ": " );
            e.printStackTrace();
            throw e;
        }
    }

}
