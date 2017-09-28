/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.AnterioresContratos;
import InterfacePersistencia.PersistenciaAnterioresContratosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaAnterioresContratos implements PersistenciaAnterioresContratosInterface {

   private static Logger log = Logger.getLogger(PersistenciaAnterioresContratos.class);

    @Override
    public void crear(EntityManager em, AnterioresContratos anteriorContrato) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.merge(anteriorContrato);
            tx.commit();
        }catch(Exception e){
            log.error("Error PersistenciaAnterioresContratos.crear:  ", e);
            if(tx.isActive()){
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, AnterioresContratos anteriorContrato) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try{
        tx.begin();
        em.merge(anteriorContrato);
        tx.commit();
        }catch(Exception e){
            log.error("Error PersistenciaAnterioresContratos.editar:  ", e);
            if(tx.isActive()){
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, AnterioresContratos anteriorContrato) {
       em.clear();
       EntityTransaction tx = em.getTransaction();
       try{
        tx.begin();
        em.remove(em.merge(anteriorContrato));
        tx.commit();
       }catch(Exception e){
           log.error("Error PersitenciaAnterioresContratos.borrar:  ", e);   
           if(tx.isActive()){
               tx.rollback();
           }
       }
    }

    @Override
    public List<AnterioresContratos> anterioresContratosPersona(EntityManager em, BigInteger secPersona) {
       try{
       em.clear();
       String sql = "SELECT * FROM ANTERIORESCONTRATOS WHERE PERSONA = ?";
       Query query = em.createNativeQuery(sql, AnterioresContratos.class);
       query.setParameter(1, secPersona);
       List<AnterioresContratos> listAnterioresContratos = query.getResultList();
       return listAnterioresContratos;
       }catch(Exception e){
           log.error("error PersistenciaAnterioresContratosInterface.anterioresContratosPersona :  ", e);
           return null;
       }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
