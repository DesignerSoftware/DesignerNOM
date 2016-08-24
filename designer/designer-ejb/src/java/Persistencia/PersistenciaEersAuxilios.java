/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.EersAuxilios;
import InterfacePersistencia.PersistenciaEersAuxiliosInterface;
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
public class PersistenciaEersAuxilios implements PersistenciaEersAuxiliosInterface{

    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public List<EersAuxilios> auxilios(EntityManager em) {
        try {
            em.clear();
            String sqlQuery = "select * from eersauxilios e where EXISTS (SELECT 'X' FROM EMPLEADOS EM WHERE EM.SECUENCIA=E.EMPLEADO)";
            Query query = em.createNativeQuery(sqlQuery, EersAuxilios.class);
            List<EersAuxilios> listaAuxilios = query.getResultList();
            return listaAuxilios;
        } catch (Exception e) {
            System.out.println("Exepcion en PersistenciaEersAuxilios.auxilios" + e);
            return null;
        }
    }
}
