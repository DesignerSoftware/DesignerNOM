/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposDescansos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposDescansosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposDescansos.
     *
     * @param listaTiposDescansos Lista TiposDescansos que se van a modificar.
     */
    public void modificarTiposDescansos(List<TiposDescansos> listaTiposDescansos);

    /**
     * Método encargado de borrar TiposDescansos.
     *
     * @param listaTiposDescansos Lista TiposDescansos que se van a borrar.
     */
    public void borrarTiposDescansos(List<TiposDescansos> listaTiposDescansos);

    /**
     * Método encargado de crear TiposDescansos.
     *
     * @param listaTiposDescansos Lista TiposDescansos que se van a crear.
     */
    public void crearTiposDescansos(List<TiposDescansos> listaTiposDescansos);

    /**
     * Método encargado de recuperar las TiposDescansos para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de TiposDescansos.
     */
    public List<TiposDescansos> consultarTiposDescansos();

    /**
     * Método encargado de recuperar un TipoDescanso dada su secuencia.
     *
     * @param secTiposDescansos Secuencia del TipoDescanso
     * @return Retorna un TiposDescansos.
     */
    public TiposDescansos consultarTipoDescanso(BigInteger secTiposDescansos);

    /**
     * Método encargado de contar la cantidad de VigenciasJornadas
     * relacionadas con un TipoDescanso específica.
     *
     * @param secTiposDescansos Secuencia del TipoDescanso.
     * @return Retorna un número indicando la cantidad de VigenciasJornadas
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarVigenciasJornadasTipoDescanso(BigInteger secTiposDescansos);
}
