/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposEmpresas;
import InterfacePersistencia.PersistenciaTiposEmpresasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaTiposEmpresas implements PersistenciaTiposEmpresasInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    public void crear(EntityManager em, TiposEmpresas tiposEmpresas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposEmpresas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposEmpresas.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, TiposEmpresas tiposEmpresas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposEmpresas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposEmpresas.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, TiposEmpresas tiposEmpresas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposEmpresas));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposEmpresas.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public TiposEmpresas buscarTipoEmpresa(EntityManager em, BigInteger secuenciaTE) {
        try {
            em.clear();
            return em.find(TiposEmpresas.class, secuenciaTE);
        } catch (Exception e) {
            return null;
        }
    }

    public List<TiposEmpresas> buscarTiposEmpresas(EntityManager em) {
        em.clear();
        Query query = em.createQuery("SELECT m FROM TiposEmpresas m ORDER BY m.codigo ASC ");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<TiposEmpresas> listMotivosDemandas = query.getResultList();
        return listMotivosDemandas;

    }

    @Override
    public BigInteger contadorSueldosMercados(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT (*)FROM sueldosmercados sm WHERE tipoempresa = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.err.println("Contador TiposEmpresas contadorIdiomasPersonas persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error TiposEmpresas contadorIdiomasPersonas. " + e);
            return retorno;
        }
    }
}
