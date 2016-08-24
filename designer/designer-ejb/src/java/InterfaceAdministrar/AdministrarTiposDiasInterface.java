/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposDias;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposDiasInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposDias.
     *
     * @param listaTiposDias Lista TiposDias que se van a modificar.
     */
    public void modificarTiposDias(List<TiposDias> listaTiposDias);

    /**
     * Método encargado de borrar TiposDias.
     *
     * @param listaTiposDias Lista TiposDias que se van a borrar.
     */
    public void borrarTiposDias(List<TiposDias> listaTiposDias);

    /**
     * Método encargado de crear TiposDias.
     *
     * @param listaTiposDias Lista TiposDias que se van a crear.
     */
    public void crearTiposDias(List<TiposDias> listaTiposDias);

    /**
     * Método encargado de recuperar un TipoDia dada su secuencia.
     *
     * @param secTipoDia Secuencia del TipoDia.
     * @return Retorna la TipoDia cuya secuencia coincida con el valor del
     * parámetro.
     */
    public TiposDias mostrarTipoDia(BigInteger secTipoDia);

    /**
     * Metodo encargado de traer todas los TiposDias de la base de datos.
     *
     * @return Lista de TiposDias.
     */
    public List<TiposDias> mostrarTiposDias();

    /**
     * Método encargado de contar la cantidad de DiasLaborales relacionadas con
     * un TipoDia específico.
     *
     * @param secTipoDia Secuencia del TipoDia.
     * @return Retorna un número indicando la cantidad de DiasLaborales cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger verificarDiasLaborales(BigInteger secTipoDia);

    /**
     * Método encargado de contar la cantidad de ExtrasRecargos relacionadas con
     * un TipoDia específico.
     *
     * @param secTipoDia Secuencia del TipoDia.
     * @return Retorna un número indicando la cantidad de ExtrasRecargos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger verificarExtrasRecargos(BigInteger secTipoDia);
}
