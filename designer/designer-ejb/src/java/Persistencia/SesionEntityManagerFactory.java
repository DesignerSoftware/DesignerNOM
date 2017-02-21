/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.SesionEntityManagerFactoryInterface;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Remove;
import javax.ejb.Singleton;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Clase de tipo singleton. <br>
 * Esta clase tiene la responsabilidad de crear y controlar las conexiones a la
 * base de datos que se están usando. Al ser ‘singleton’ solo permite crear una
 * instancia a la base de datos.
 *
 * @author Felipe Triviño
 */
@Singleton
public class SesionEntityManagerFactory implements SesionEntityManagerFactoryInterface, Serializable {

    /**
     * Atributo EntityManagerFactory.
     */
    private EntityManagerFactory emf;

    @Override
    public boolean crearFactoryInicial(String baseDatos) {
        try {
            System.out.println("Entro y la bd es: " + baseDatos);
            emf = Persistence.createEntityManagerFactory(baseDatos);
            return true;
        } catch (Exception e) {
            System.out.println("error crearFactoryInicial" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean crearFactoryUsuario(String usuario, String contraseña, String baseDatos) {
        try {
            System.out.println("entro a crearFactoryUsuario");
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("javax.persistence.jdbc.user", usuario);
            //properties.put("hibernate.connection.username", usuario);
            properties.put("javax.persistence.jdbc.password", contraseña);
            //properties.put("hibernate.connection.password", contraseña);
            System.out.println("Usuario: " + usuario);
            System.out.println("Contraseña: " + contraseña);
            System.out.println("Base de Datos: " + baseDatos);
            emf = Persistence.createEntityManagerFactory(baseDatos, properties);
            System.out.println("emf=Persistence.createEntityManagerFactory(baseDatos, properties)" + emf.getProperties() );
            return true;
        } catch (Exception e) {
            System.out.println("Error crearFactoryUsuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public EntityManagerFactory getEmf() {
        return emf;
    }

    @Override
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Remove
    @Override
    public void adios() {
        System.out.println("Cerrando xD");
    }
}
