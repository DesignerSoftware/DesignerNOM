/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.SoAntecedentes;
import Entidades.SoAntecedentesMedicos;
import Entidades.SoTiposAntecedentes;
import InterfacePersistencia.PersistenciaSoAntecedentesMedicosInterface;
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
public class PersistenciaSoAntecedentesMedicos implements PersistenciaSoAntecedentesMedicosInterface {

   private static Logger log = Logger.getLogger(PersistenciaSoAntecedentesMedicos.class);

    @Override
    public void crear(EntityManager em, SoAntecedentesMedicos antecedenteM) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(antecedenteM);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaSoAntecedentesMedicos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, SoAntecedentesMedicos antecedenteM) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(antecedenteM));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                log.error("Error PersistenciaSoAntecedentesMedicos.borrar:  ", e);
            }
        }
    }

    @Override
    public void editar(EntityManager em, SoAntecedentesMedicos antecedenteM) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(antecedenteM);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaSoAntecedentesMedicos.editar:  ", e);
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
            log.error("Error PersistenciaSoAntecedentesMedicos.lovAntecedentes:  ", e);
            return null;
        }
    }

    @Override
    public List<SoTiposAntecedentes> lovTiposAntecedentes(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SoAntecedentesMedicos> listaAntecedentesMedicos(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            String sql = "SELECT * FROM SOANTECEDENTESMEDICOS WHERE EMPLEADO = (SELECT SECUENCIA FROM EMPLEADOS WHERE PERSONA = ?)";
            Query query = em.createNativeQuery(sql, SoAntecedentesMedicos.class);
            query.setParameter(1, secEmpleado);
            List<SoAntecedentesMedicos> antecedentesM = query.getResultList();
            return antecedentesM;
        } catch (Exception e) {
            log.error("Error PersistenciaSoAntecedentesMedicos.listaAntecedentesMedicos:  ", e);
            return null;
        }
    }

}
