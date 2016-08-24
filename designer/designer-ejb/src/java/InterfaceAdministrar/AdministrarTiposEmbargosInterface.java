/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposEmbargos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposEmbargosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposEmbargos.
     *
     * @param listaTiposEmbargos Lista TiposEmbargos que se van a modificar.
     */
    public void modificarTiposPrestamos(List<TiposEmbargos> listaTiposEmbargos);

    /**
     * Método encargado de borrar TiposEmbargos.
     *
     * @param listaTiposEmbargos Lista TiposEmbargos que se van a borrar.
     */
    public void borrarTiposPrestamos(List<TiposEmbargos> listaTiposEmbargos);

    /**
     * Método encargado de crear TiposEmbargos.
     *
     * @param listaTiposEmbargos Lista TiposEmbargos que se van a crear.
     */
    public void crearTiposPrestamos(List<TiposEmbargos> listaTiposEmbargos);

    /**
     * Método encargado de recuperar las TiposEmbargos para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de TiposEmbargos.
     */
    public List<TiposEmbargos> consultarTiposPrestamos();

    /**
     * Método encargado de recuperar un TipoEmbargo dada su secuencia.
     *
     * @param secTiposEmbargos Secuencia del TipoEmbargo
     * @return Retorna un TiposEmbargos.
     */
    public TiposEmbargos consultarTipoPrestamo(BigInteger secTiposEmbargos);

    /**
     * Método encargado de contar la cantidad de DiasLaborales relacionadas con
     * un TipoEmbargo específico.
     *
     * @param secTiposEmbargos Secuencia del TipoEmbargo.
     * @return Retorna un número indicando la cantidad de DiasLaborales cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarDiasLaboralesTipoEmbargo(BigInteger secTiposEmbargos);

    /**
     * Método encargado de contar la cantidad de ExtrasRecargos relacionadas con
     * un TipoEmbargo específico.
     *
     * @param secTiposEmbargos Secuencia del TipoEmbargo.
     * @return Retorna un número indicando la cantidad de ExtrasRecargos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarExtrasRecargosTipoEmbargo(BigInteger secTiposEmbargos);
}
