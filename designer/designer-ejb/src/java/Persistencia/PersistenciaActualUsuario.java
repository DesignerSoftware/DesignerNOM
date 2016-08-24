/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.ActualUsuario;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
import javax.persistence.Query;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;

/**
 * Clase Stateless. <br> Clase encargada de realizar operaciones sobre la tabla
 * 'ActualUsuario' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaActualUsuario implements PersistenciaActualUsuarioInterface {

    //private final static Logger logger = Logger.getLogger("connectionSout");
    //private Date fechaDia;
    //private final SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     *
     * @param em
     */
    /*
     * @PersistenceContext(unitName = "DesignerRHN-ejbPU") private EntityManager em;
     */
    @Override
    public ActualUsuario actualUsuarioBD(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT au FROM ActualUsuario au");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            ActualUsuario actualUsuario;
            actualUsuario = (ActualUsuario) query.getSingleResult();
            return actualUsuario;
        } catch (Exception e) {
            //  PropertyConfigurator.configure("log4j.properties");
            //logger.error("Metodo: actualUsuarioBD - PersistenciaActualUsuario - Fecha : " + format.format(fechaDia) + " - Error : " + e.toString());
            return null;
        }
    }

    @Override
    public String actualAliasBD(EntityManager em) {
        System.err.println(this.getClass().getName() + ".actualAliasBD()");
        try {
            em.clear();
            String sqlQuery = "SELECT au.ALIAS FROM VWActualUsuario au";
            Query query = em.createNativeQuery(sqlQuery);
            String alias;
            alias = (String) query.getSingleResult();
            System.out.println("alias: " + alias);
            return alias;
        } catch (Exception e) {
            System.out.println("persistenciaActualUsuario .actualAliasBD  :    Sali con errores");
            //PropertyConfigurator.configure("log4j.properties");
            //logger.error("Metodo: actualAliasBD - PersistenciaActualUsuario - Fecha : " + format.format(fechaDia) + " - Error : " + e.toString());
            return null;
        }
    }

    public String actualAliasBD_EM(EntityManager emg) {
        try {
            emg.clear();
            String sqlQuery = "SELECT au.ALIAS FROM VWActualUsuario au";
            Query query = emg.createNativeQuery(sqlQuery);
            String alias;
            alias = (String) query.getSingleResult();
            return alias;
        } catch (Exception e) {
            //PropertyConfigurator.configure("log4j.properties");
            //logger.error("Metodo: actualAliasBD_EM - PersistenciaActualUsuario - Fecha : " + format.format(fechaDia) + " - Error : " + e.toString());
            return null;
        }
    }
}
