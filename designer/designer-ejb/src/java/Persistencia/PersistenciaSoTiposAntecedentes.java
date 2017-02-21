/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.SoTiposAntecedentes;
import InterfacePersistencia.PersistenciaSoTiposAntecedentesInterface;
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
public class PersistenciaSoTiposAntecedentes implements PersistenciaSoTiposAntecedentesInterface {

    @Override
    public void crear(EntityManager em, SoTiposAntecedentes tipoantecedente) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tipoantecedente);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoTiposAntecedentes.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, SoTiposAntecedentes tipoantecedente) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tipoantecedente));
            tx.commit();

        } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                System.out.println("Error PersistenciaSoAntecedentes.borrar: " + e.getMessage());
        }
    }

    @Override
    public void editar(EntityManager em, SoTiposAntecedentes tipoantecedente) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tipoantecedente);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoTiposAntecedentes.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<SoTiposAntecedentes> lovTiposAntecedentes(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT ALL SOTIPOSANTECEDENTES.SECUENCIA, \n"
                    + "SOTIPOSANTECEDENTES.CODIGO, SOTIPOSANTECEDENTES.DESCRIPCION \n"
                    + "FROM SOTIPOSANTECEDENTES WHERE DESCRIPCION IN ('PERSONALES','HABITOS')";
            Query query = em.createNativeQuery(sql, SoTiposAntecedentes.class);
            List<SoTiposAntecedentes> tiposantecedentes = query.getResultList();
            return tiposantecedentes;
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoTiposAntecedentes.lovTiposAntecedentes: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<SoTiposAntecedentes> listaTiposAntecedentes(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM SOTIPOSANTECEDENTES";
            Query query = em.createNativeQuery(sql, SoTiposAntecedentes.class);
            List<SoTiposAntecedentes> tiposantecedentes = query.getResultList();
            return tiposantecedentes;
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoTiposAntecedentes.listaTiposAntecedentes: " + e.getMessage());
            return null;
        }
    }

}
