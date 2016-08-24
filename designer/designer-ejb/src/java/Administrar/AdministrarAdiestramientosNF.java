/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.AdiestramientosNF;
import InterfaceAdministrar.AdministrarAdiestramientosNFInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaAdiestramientosNFInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla 'AdiestramientosNF'.
 * @author betelgeuse
 */
@Stateful
public class AdministrarAdiestramientosNF implements AdministrarAdiestramientosNFInterface{
    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia 'persistenciaAdiestramientosNF'.
     */
    @EJB
    PersistenciaAdiestramientosNFInterface persistenciaAdiestramientosNF;
    	/**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;
    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
        @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    @Override
    public List<AdiestramientosNF> consultarAdiestramientosNF(){
        return persistenciaAdiestramientosNF.adiestramientosNF(em);
    }    
}
