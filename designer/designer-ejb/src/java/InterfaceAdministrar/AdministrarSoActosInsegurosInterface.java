/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.SoActosInseguros;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarSoActosInsegurosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar SoActosInseguros.
     *
     * @param listaSoActosInseguros Lista SoActosInseguros que se van a
     * modificar.
     */
    public void modificarSoActosInseguros(List<SoActosInseguros> listaSoActosInseguros);

    /**
     * Método encargado de borrar SoActosInseguros.
     *
     * @param listaSoActosInseguros Lista SoActosInseguros que se van a borrar.
     *
     */
    public void borrarSoActosInseguros(List<SoActosInseguros> listaSoActosInseguros);

    /**
     * Método encargado de crear SoActosInseguros.
     *
     * @param listaSoActosInseguros Lista SoActosInseguros que se van a crear.
     *
     */
    public void crearSoActosInseguros(List<SoActosInseguros> listaSoActosInseguros);

    /**
     * Metodo encargado de traer todas las SoActosInseguros de la base de datos.
     *
     * @return Lista de SoActosInseguros.
     */
    public List<SoActosInseguros> consultarSoActosInseguros();

    /**
     * Método encargado de recuperar un SoActosInseguros dada su secuencia.
     *
     * @param secSoActoInseguro Secuencia del SoActoInseguro.
     * @return Retorna el SoActoInseguro cuya secuencia coincida con el valor
     * del parámetro.
     */
    public SoActosInseguros consultarSoActoInseguro(BigInteger secSoActoInseguro);

    /**
     * Método encargado de contar la cantidad de SoAccidentesMedicos
     * relacionadas con un SoActoInseguro específico.
     *
     * @param secSoActoInseguro Secuencia del SoActoInseguro.
     * @return Retorna un número indicando la cantidad de SoAccidentesMedicos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger verificarSoAccidentesMedicos(BigInteger secSoActoInseguro);
}
