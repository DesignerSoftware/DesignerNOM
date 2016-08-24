/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Ciudades;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.Personas;
import InterfaceAdministrar.AdministrarDireccionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaDireccionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'Direcciones'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarDirecciones implements AdministrarDireccionesInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    

    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaPersonas'.
     */
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'PersistenciaCiudades'.
     */
    @EJB
    PersistenciaCiudadesInterface PersistenciaCiudades;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaDirecciones'.
     */
    @EJB
    PersistenciaDireccionesInterface persistenciaDirecciones;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;

    private EntityManager em;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Direcciones> consultarDireccionesPersona(BigInteger secPersona) {
        try {
            return persistenciaDirecciones.direccionesPersona(em,secPersona);
        } catch (Exception e) {
            System.err.println("Error AdministrarTelefonos.telefonosPersona " + e);
            return null;
        }
    }

    @Override
    public Personas consultarPersona(BigInteger secPersona) {
        return persistenciaPersonas.buscarPersonaSecuencia(em,secPersona);
    }

    @Override
    public List<Ciudades> consultarLOVCiudades() {
        return PersistenciaCiudades.consultarCiudades(em);
    }

    @Override
    public void modificarDirecciones(List<Direcciones> listaDirecciones) {
        Direcciones d;
        for (int i = 0; i < listaDirecciones.size(); i++) {
            System.out.println("Modificando...");
            if (listaDirecciones.get(i).getCiudad().getSecuencia() == null) {
                listaDirecciones.get(i).setCiudad(null);
                d = listaDirecciones.get(i);
            } else {
                d = listaDirecciones.get(i);
            }
            persistenciaDirecciones.editar(em,d);
        }
    }

    @Override
    public void borrarDirecciones(List<Direcciones> listaDirecciones) {
        for (int i = 0; i < listaDirecciones.size(); i++) {
            System.out.println("Borrando...");
            if (listaDirecciones.get(i).getHipoteca() == null) {
                listaDirecciones.get(i).setHipoteca("N");
            }
            persistenciaDirecciones.borrar(em,listaDirecciones.get(i));
        }
    }

    @Override
    public void crearDirecciones(List<Direcciones> listaDirecciones) {
        for (int i = 0; i < listaDirecciones.size(); i++) {
            System.out.println("Borrando...");
            if (listaDirecciones.get(i).getHipoteca() == null) {
                listaDirecciones.get(i).setHipoteca("N");
            }
            persistenciaDirecciones.crear(em,listaDirecciones.get(i));
        }
    }
    
     @Override
    public Empleados empleadoActual(BigInteger secuenciaP){
        try{
        Empleados retorno = persistenciaEmpleado.buscarEmpleado(em, secuenciaP);
        return retorno;
        }catch(Exception  e){
            System.out.println("Error empleadoActual Admi : "+e.toString());
            return null;
        }
    }
    
}
