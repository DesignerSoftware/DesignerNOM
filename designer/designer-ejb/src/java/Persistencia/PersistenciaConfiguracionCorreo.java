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
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaConfiguracionCorreo implements PersistenciaConfiguracionCorreoInterface {

   private static Logger log = Logger.getLogger(PersistenciaConfiguracionCorreo.class);

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
            log.error("Error PersistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo: " + e);
            eManager.getTransaction().rollback();
            return null;
        }
    }
}
