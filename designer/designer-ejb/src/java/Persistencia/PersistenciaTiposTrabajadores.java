/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposTrabajadores;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
//import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
//import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br> Clase encargada de realizar operaciones sobre la tabla
 * 'TiposTrabajadores' de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaTiposTrabajadores implements PersistenciaTiposTrabajadoresInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*
     * @PersistenceContext(unitName = "DesignerRHN-ejbPU") private EntityManager
     * em;
     */
    @Override
    public void crear(EntityManager em, TiposTrabajadores tiposTrabajadores) {

        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposTrabajadores);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposTrabajadores.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposTrabajadores tiposTrabajadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposTrabajadores);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposTrabajadores.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposTrabajadores tiposTrabajadores) {

        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposTrabajadores));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposTrabajadores.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposTrabajadores> buscarTiposTrabajadores(EntityManager em) {
        em.clear();
        String sql = "select * from TiposTrabajadores";
        Query query = em.createNativeQuery(sql, TiposTrabajadores.class);
        List<TiposTrabajadores> tipoTLista = query.getResultList();
        return tipoTLista;
    }

    @Override
    public TiposTrabajadores buscarTipoTrabajadorSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tt FROM TiposTrabajadores e WHERE tt.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposTrabajadores tipoT = (TiposTrabajadores) query.getSingleResult();
            return tipoT;
        } catch (Exception e) {
        }
        TiposTrabajadores tipoT = null;
        return tipoT;
    }

    @Override
    public String plantillaValidarTipoTrabajadorReformaLaboral(EntityManager em, BigInteger tipoTrabajador, BigInteger reformaLaboral) {
        try {
            System.out.println("plantillaValidarTipoTrabajadorReformaLaboral() tipoTrabajador: " + tipoTrabajador);
            System.out.println("plantillaValidarTipoTrabajadorReformaLaboral() reformaLaboral: " + reformaLaboral);
//            
//            int retorno = 0;
//            em.clear();
//            String sqlQuery = "SELECT COUNT(*)FROM PLANTILLASVALIDARL RL WHERE RL.TIPOTRABAJADOR = ?";
//            Query query = em.createNativeQuery(sqlQuery);
//            query.setParameter(1, tipoTrabajador);
//            retorno = Integer.parseInt(query.getSingleResult().toString());
//            System.out.println("plantillaValidarTipoTrabajadorReformaLaboral() retornoTT : " + retorno);
//
//            if (retorno == 0) {
//                return "S";
//            } else {
//                retorno = 0;
//                em.clear();
//                String sqlQuery2 = "SELECT COUNT(*)FROM PLANTILLASVALIDARL RL WHERE RL.TIPOTRABAJADOR = ? AND RL.REFORMALABORAL = ?";
//                Query query2 = em.createNativeQuery(sqlQuery2);
//                query2.setParameter(1, tipoTrabajador);
//                query2.setParameter(2, reformaLaboral);
//                retorno = Integer.parseInt(query2.getSingleResult().toString());
//                System.out.println("plantillaValidarTipoTrabajadorReformaLaboral() retornoTT & RL : " + retorno);
//                if(retorno == 0){
//                    return "N";
//                }else{
//                    return "S";
//                }
//            }
            
            
            em.clear();
            String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidarl(?,?) FROM dual";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, tipoTrabajador);
            query.setParameter(2, reformaLaboral);
            String retorno = (String) query.getSingleResult();
            System.out.println("retorno : " + retorno);
//            if (retorno == null) {
//                retorno = "N";
//            }
            return retorno;
        } catch (Exception e) {
            System.out.println("Error plantillaValidarTipoTrabajadorReformaLaboral PersistenciaTiposTrabajadores : " + e.toString());
            return null;
        }
    }

    @Override
    public String plantillaValidarTipoTrabajadorTipoSueldo(EntityManager em, BigInteger tipoTrabajador, BigInteger tipoSueldo) {
        try {
            System.out.println("plantillaValidarTipoTrabajadorTipoSueldo() tipoTrabajador: " + tipoTrabajador);
            System.out.println("plantillaValidarTipoTrabajadorTipoSueldo() tipoSueldo: " + tipoSueldo);

            em.clear();
            String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidats(?,?) FROM dual";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, tipoTrabajador);
            query.setParameter(2, tipoSueldo);
            String retorno = (String) query.getSingleResult();
            
            System.out.println("plantillaValidarTipoTrabajadorTipoSueldo() retorno : " + retorno);
            return retorno;
            
        } catch (Exception e) {
            System.out.println("Error plantillaValidarTipoTrabajadorTipoSueldo PersistenciaTiposTrabajadores : " + e.toString());
            return null;
        }
    }

    @Override
    public String plantillaValidarTipoTrabajadorTipoContrato(EntityManager em, BigInteger tipoTrabajador, BigInteger tipoContrato) {
        try {
            System.out.println("plantillaValidarTipoTrabajadorTipoContrato() tipoTrabajador: " + tipoTrabajador);
            System.out.println("plantillaValidarTipoTrabajadorTipoContrato() tipoContrato: " + tipoContrato);
            
            em.clear();
            String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidatc(?,?) FROM dual";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, tipoTrabajador);
            query.setParameter(2, tipoContrato);
            String retorno = (String) query.getSingleResult();
            System.out.println("retorno : " + retorno);

            return retorno;
        } catch (Exception e) {
            System.out.println("Error plantillaValidarTipoTrabajadorTipoContrato PersistenciaTiposTrabajadores : " + e.toString());
            return null;
        }
    }

    @Override
    public String plantillaValidarTipoTrabajadorContrato(EntityManager em, BigInteger tipoTrabajador, BigInteger contrato) {
        try {
            System.out.println("plantillaValidarTipoTrabajadorContrato() tipoTrabajador: " + tipoTrabajador);
            System.out.println("plantillaValidarTipoTrabajadorContrato() contrato: " + contrato);

            em.clear();
            String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidall(?,?) FROM dual";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, tipoTrabajador);
            query.setParameter(2, contrato);
            String retorno = (String) query.getSingleResult();
            System.out.println("retorno : " + retorno);
            return retorno;
            
        } catch (Exception e) {
            System.out.println("Error plantillaValidarTipoTrabajadorContrato PersistenciaTiposTrabajadores : " + e.toString());
            return null;
        }
    }

    @Override
    public String plantillaValidarTipoTrabajadorNormaLaboral(EntityManager em, BigInteger tipoTrabajador, BigInteger normaLaboral) {
        try {
            System.out.println("plantillaValidarTipoTrabajadorNormaLaboral() tipoTrabajador: " + tipoTrabajador);
            System.out.println("plantillaValidarTipoTrabajadorNormaLaboral() normaLaboral: " + normaLaboral);

            em.clear();
            String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidanl(?,?) FROM dual";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, tipoTrabajador);
            query.setParameter(2, normaLaboral);
            String retorno = (String) query.getSingleResult();
            System.out.println("retorno : " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error plantillaValidarTipoTrabajadorNormaLaboral PersistenciaTiposTrabajadores : " + e.toString());
            return null;
        }
    }

    @Override
    public TiposTrabajadores buscarTipoTrabajadorCodigoTiposhort(EntityManager em, short codigo) {
        try {
            em.clear();
            String sql = "SELECT * FROM TiposTrabajadores WHERE codigo=?";
            /*
             * Query query = em.createQuery("SELECT t FROM TiposTrabajadores t
             * WHERE t.codigo=:codigo");
             */
            Query query = em.createNativeQuery(sql, TiposTrabajadores.class);
            query.setParameter(1, codigo);
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposTrabajadores tipoTC = (TiposTrabajadores) query.getSingleResult();
            return tipoTC;
        } catch (Exception e) {
            TiposTrabajadores tipoTC = null;
            return null;
        }
    }

    @Override
    public String clonarTipoT(EntityManager em, String nombreNuevo, Short codigoNuevo, BigInteger secTTClonado) {
        try {
            em.clear();
            String sqlQuery = "call XXXXXX_PKG.CLONARTIPOTRABAJADOR(?, ?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secTTClonado);
            query.setParameter(2, codigoNuevo);
            query.setParameter(3, nombreNuevo);
            query.executeUpdate();
            return "S";
        } catch (Exception e) {
            return ("ERROR PersistenciaTiposTrabajadores.clonarTipoT" + e.toString());
        }
    }
}
