/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.EmpresasOpcionesKioskos;
import InterfacePersistencia.PersistenciaEmpresasOpcionesKioskosInterface;
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
public class PersistenciaEmpresasOpcionesKioskos implements PersistenciaEmpresasOpcionesKioskosInterface {

    @Override
    public void crear(EntityManager em, EmpresasOpcionesKioskos empresaok) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(empresaok);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEmpresasOpcionesKioskos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, EmpresasOpcionesKioskos empresaok) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(empresaok);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEmpresasOpcionesKioskos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, EmpresasOpcionesKioskos empresaok) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(empresaok));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEmpresasOpcionesKioskos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<EmpresasOpcionesKioskos> consultarEmpresaOpKioskos(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM EMPRESASOPCIONESKIOSKOS ";
            Query query = em.createNativeQuery(sql, EmpresasOpcionesKioskos.class);
            List< EmpresasOpcionesKioskos> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarPreguntasKioskos PersistenciaPreguntasKioscos : " + e.getMessage());
            return null;
        }
    }
}
