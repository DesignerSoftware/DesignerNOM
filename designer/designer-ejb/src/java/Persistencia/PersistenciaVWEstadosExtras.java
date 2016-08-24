/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.VWEstadosExtras;
import InterfacePersistencia.PersistenciaVWEstadosExtrasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaVWEstadosExtras implements PersistenciaVWEstadosExtrasInterface {

    @Override
    public List<VWEstadosExtras> buscarVWEstadosExtras(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWEstadosExtras vw WHERE vw.turnoempleado.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VWEstadosExtras> vwEstadosExtras = query.getResultList();
            return vwEstadosExtras;
        } catch (Exception e) {
            System.out.println("Error buscarVWEstadosExtras.buscarVWEstadosExtras " + e.toString());
            return null;
        }
    }
}
