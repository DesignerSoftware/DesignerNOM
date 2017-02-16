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
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaAnterioresContratos implements PersistenciaAnterioresContratosInterface {

    @Override
    public void crear(EntityManager em, AnterioresContratos anteriorContrato) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.merge(anteriorContrato);
            tx.commit();
        }catch(Exception e){
            System.out.println("Error PersistenciaAnterioresContratos.crear: " + e.getMessage());
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
            System.out.println("Error PersistenciaAnterioresContratos.editar: " + e.getMessage());
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
           System.out.println("Error PersitenciaAnterioresContratos.borrar: " + e.getMessage());   
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
           System.out.println("error PersistenciaAnterioresContratosInterface.anterioresContratosPersona : " + e.getMessage());
           return null;
       }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
