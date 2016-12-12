/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.SoAntecedentes;
import InterfacePersistencia.PersistenciaSoAntecedentesInterface;
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
public class PersistenciaSoAntecedentes implements PersistenciaSoAntecedentesInterface {

    @Override
    public void crear(EntityManager em, SoAntecedentes antecedente) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(antecedente);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoAntecedentes.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, SoAntecedentes antecedente) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(antecedente));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaSoAntecedentes.borrar: " + e);
            }
        }
    }

    @Override
    public void editar(EntityManager em, SoAntecedentes antecedente) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(antecedente);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoAntecedentes.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<SoAntecedentes> lovAntecedentes(EntityManager em, BigInteger secTipoAntecedente) {
        try {
            em.clear();
            String sql = "SELECT * FROM SOANTECEDENTES WHERE TIPOANTECEDENTE = ?";
            Query query = em.createNativeQuery(sql, SoAntecedentes.class);
            query.setParameter(1, secTipoAntecedente);
            List<SoAntecedentes> antecedentes = query.getResultList();
            return antecedentes;
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoAntecedentes.lovAntecedentes: " + e);
            return null;
        }
    }

    @Override
    public List<SoAntecedentes> listaAntecedentes(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM SOANTECEDENTES ORDER BY CODIGO";
            Query query = em.createNativeQuery(sql, SoAntecedentes.class);
            List<SoAntecedentes> antecedentes = query.getResultList();
            return antecedentes;
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoAntecedentes.listaTiposAntecedentes: " + e);
            return null;
        }
    }

}
