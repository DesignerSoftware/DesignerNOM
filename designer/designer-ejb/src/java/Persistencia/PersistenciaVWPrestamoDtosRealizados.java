/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Entidades.VWActualesJornadas;
import Entidades.VWPrestamoDtosRealizados;
import InterfacePersistencia.PersistenciaVWPrestamoDtosRealizadosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaVWPrestamoDtosRealizados implements PersistenciaVWPrestamoDtosRealizadosInterface {

    @Override
    public List<VWPrestamoDtosRealizados> buscarPrestamosDtos(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWPrestamoDtosRealizados vw WHERE vw.eerprestamodto.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VWPrestamoDtosRealizados> listaPrestamos =  query.getResultList();
            return listaPrestamos;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVWPrestamoDtosRealizados.buscarPrestamosDtos()" + e.getMessage());
            return null;
        }
    }
}
