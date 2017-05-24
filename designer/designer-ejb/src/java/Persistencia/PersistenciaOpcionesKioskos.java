/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.OpcionesKioskos;
import InterfacePersistencia.PersistenciaOpcionesKioskosInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaOpcionesKioskos implements PersistenciaOpcionesKioskosInterface {

    @Override
    public List<OpcionesKioskos> consultarOpcionesKioskos(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM OPCIONESKIOSKOS  ORDER BY CODIGO ASC";
            Query query = em.createNativeQuery(sql, OpcionesKioskos.class);
            List< OpcionesKioskos> lista = query.getResultList();
            return lista;

        } catch (Exception e) {
            System.out.println("Error consultarOpcionesKioskos PersistenciaOpcionesKioskos : " + e.getMessage());
            return null;
        }
    }

}
