/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ConfiguracionCorreo;
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
        ConfiguracionCorreo retorno = null;
        try {
            eManager.getTransaction().begin();
            String sqlQuery = "SELECT cc FROM ConfiguracionCorreo cc WHERE cc.empresa.secuencia = :secuenciaEmpresa";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("secuenciaEmpresa", secuenciaEmpresa);
            ConfiguracionCorreo cc = (ConfiguracionCorreo) query.getSingleResult();
            eManager.getTransaction().commit();
            retorno = cc;
            return retorno;
        } catch (Exception e) {
//            System.out.println("Error PersistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo: " + e);
//            eManager.getTransaction().rollback();
//            return retorno;
//        } catch (Exception e) {
            System.out.println("Error PersistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo:");
            System.out.println("Error consultando Configuracion");
            System.out.println("ex: " + e);
        }
        return retorno;
    }
}
