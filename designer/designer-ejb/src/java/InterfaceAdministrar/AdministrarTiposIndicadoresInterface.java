/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposIndicadores;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposIndicadoresInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposIndicadores.
     *
     * @param listaTiposIndicadores Lista TiposIndicadores que se van a
     * modificar.
     */
    public void modificarTiposIndicadores(List<TiposIndicadores> listaTiposIndicadores);

    /**
     * Método encargado de borrar TiposIndicadores.
     *
     * @param listaTiposIndicadores Lista TiposIndicadores que se van a borrar.
     */
    public void borrarTiposIndicadores(List<TiposIndicadores> listaTiposIndicadores);

    /**
     * Método encargado de crear TiposIndicadores.
     *
     * @param listaTiposIndicadores Lista TiposIndicadores que se van a crear.
     */
    public void crearTiposIndicadores(List<TiposIndicadores> listaTiposIndicadores);

    /**
     * Método encargado de recuperar las TiposIndicadores para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de TiposIndicadores.
     */
    public List<TiposIndicadores> consultarTiposIndicadores();

    /**
     * Método encargado de recuperar un TipoIndicador dada su secuencia.
     *
     * @param secTiposIndicadores Secuencia del TipoIndicador
     * @return Retorna un TipoIndicador.
     */
    public TiposIndicadores consultarTipoIndicador(BigInteger secTiposIndicadores);

    /**
     * Método encargado de contar la cantidad de VigenciasIndicadores
     * relacionadas con un TipoIndicador específica.
     *
     * @param secTiposIndicadores Secuencia del TipoIndicador.
     * @return Retorna un número indicando la cantidad de VigenciasIndicadores
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarVigenciasIndicadoresTipoIndicador(BigInteger secTiposIndicadores);
}
