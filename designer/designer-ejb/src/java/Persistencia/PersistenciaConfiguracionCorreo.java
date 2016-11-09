/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ConfiguracionCorreo;
import InterfacePersistencia.PersistenciaConfiguracionCorreoInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaConfiguracionCorreo implements PersistenciaConfiguracionCorreoInterface {

    @Override
    public ConfiguracionCorreo consultarConfiguracionServidorCorreo(EntityManager eManager, BigInteger secuenciaEmpresa) {
        try {
            eManager.getTransaction().begin();
            String sqlQuery = "SELECT cc FROM ConfiguracionCorreo cc WHERE cc.empresa.secuencia = :secuenciaEmpresa";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("secuenciaEmpresa", secuenciaEmpresa);
            ConfiguracionCorreo cc = (ConfiguracionCorreo) query.getSingleResult();
            eManager.getTransaction().commit();
            return cc;
        } catch (Exception e) {
            System.out.println("Error PersistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo: " + e);
            eManager.getTransaction().rollback();
            return null;
        }
    }
}
