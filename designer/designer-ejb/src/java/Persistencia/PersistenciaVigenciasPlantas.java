/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaVigenciasPlantasInterface;
import Entidades.VigenciasPlantas;
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
public class PersistenciaVigenciasPlantas implements PersistenciaVigenciasPlantasInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    public void crear(EntityManager em, VigenciasPlantas vigenciasPlantas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasPlantas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasPlantas.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, VigenciasPlantas vigenciasPlantas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasPlantas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasPlantas.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, VigenciasPlantas vigenciasPlantas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasPlantas));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasPlantas.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public VigenciasPlantas consultarVigenciaPlanta(EntityManager em, BigInteger secVigenciasPlantas) {
        try {
            em.clear();
            return em.find(VigenciasPlantas.class, secVigenciasPlantas);
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVigenciasPlantas.consultarVigenciaPlanta()" + e.getMessage());
            return null;
        }
    }

    public List<VigenciasPlantas> consultarVigenciasPlantas(EntityManager em) {
        try{
        em.clear();
        Query query = em.createQuery("SELECT te FROM VigenciasPlantas te ORDER BY te.codigo ASC ");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<VigenciasPlantas> listMotivosDemandas = query.getResultList();
        return listMotivosDemandas;
        }catch(Exception e){
            System.out.println("Persistencia.PersistenciaVigenciasPlantas.consultarVigenciasPlantas()" + e.getMessage());
            return null;
        }
    }

    public BigInteger contarPlantasVigenciaPlanta(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM plantas WHERE vigencia = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaVigenciasPlantas  contarPlantasVigenciaPlanta ERROR. " + e.getMessage());
            return retorno;
        }
    }

}
