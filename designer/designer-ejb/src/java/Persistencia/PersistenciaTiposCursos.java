/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposCursos;
import InterfacePersistencia.PersistenciaTiposCursosInterface;
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
public class PersistenciaTiposCursos implements PersistenciaTiposCursosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public void crear(EntityManager em, TiposCursos tiposCursos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposCursos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposCursos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposCursos tiposCursos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposCursos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposCursos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposCursos tiposCursos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposCursos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposCursos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposCursos> consultarTiposCursos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT g FROM TiposCursos g ORDER BY g.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List< TiposCursos> listMotivosDemandas = query.getResultList();
            return listMotivosDemandas;

        } catch (Exception e) {
            System.out.println("Error consultarTiposCursos PersistenciaTiposCursos : " + e.toString());
            return null;
        }
    }

    @Override
    public TiposCursos consultarTipoCurso(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT te FROM TiposCursos te WHERE te.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposCursos tiposCursos = (TiposCursos) query.getSingleResult();
            return tiposCursos;
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposCursos consultarTipoCurso : " + e.toString());
            TiposCursos tiposEntidades = null;
            return tiposEntidades;
        }
    }

    @Override
    public BigInteger contarCursosTipoCurso(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM cursos WHERE tipocurso = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.err.println("Contador TiposCursos contadorVigenciasIndicadores persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error TiposCursos contadorVigenciasIndicadores. " + e);
            return retorno;
        }
    }
}
