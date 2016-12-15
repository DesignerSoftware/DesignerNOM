/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ClasesRiesgos;
import InterfacePersistencia.PersistenciaClasesRiesgosInterface;
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
public class PersistenciaClasesRiesgos implements PersistenciaClasesRiesgosInterface {

    @Override
    public void crear(EntityManager em, ClasesRiesgos claseRiesgo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(claseRiesgo);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaClasesRiesgos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }

    }

    @Override
    public void editar(EntityManager em, ClasesRiesgos claseRiesgo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(claseRiesgo);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaClasesRiesgos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, ClasesRiesgos claseRiesgo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(claseRiesgo));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaClasesRiesgos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<ClasesRiesgos> consultarListaClasesRiesgos(EntityManager em) {
       try{
       em.clear();
       String sql ="SELECT * FROM CLASESRIESGOS";
       Query query = em.createNativeQuery(sql, ClasesRiesgos.class);
       List<ClasesRiesgos> listClasesRiesgos =(List<ClasesRiesgos>) query.getResultList();
       return listClasesRiesgos;
           
       }catch(Exception e){
           System.out.println("Error PersistenciaClasesRiesgos.consultarClasesRiesgos : " + e.toString());
           return null;
       }
    }

}
