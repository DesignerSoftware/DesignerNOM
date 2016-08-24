/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposPensionados;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposPensionadosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposPensionados.
     *
     * @param listaTiposPensionados Lista TiposPensionados que se van a modificar.
     */
    public void modificarTiposPensionados(List<TiposPensionados> listaTiposPensionados);

    /**
     * Método encargado de borrar TiposPensionados.
     *
     * @param listaTiposPensionados Lista TiposPensionados que se van a borrar.
     */
    public void borrarTiposPensionados(List<TiposPensionados> listaTiposPensionados);

    /**
     * Método encargado de crear TiposPensionados.
     *
     * @param listaTiposPensionados Lista TiposPensionados que se van a crear.
     */
    public void crearTiposPensionados(List<TiposPensionados> listaTiposPensionados);

    /**
     * Método encargado de recuperar las TiposPensionados para un tabla de la
     * pantalla.
     *
     * @return Retorna un lista de TiposPensionados.
     */
    public List<TiposPensionados> consultarTiposPensionados();

    /**
     * Método encargado de recuperar un TipoPensionado dada su secuencia.
     *
     * @param secTiposPensionados Secuencia del TipoPensionado
     * @return Retorna un TipoPensionado.
     */
    public TiposPensionados consultarTipoPensionado(BigInteger secTiposPensionados);

    /**
     * Método encargado de contar la cantidad de Retirados relacionadas con un
     * TipoPensionado específico.
     *
     * @param secTiposPensionados Secuencia del TipoPensionado.
     * @return Retorna un número indicando la cantidad de Retirados cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarRetiradosTipoPensionado(BigInteger secTiposPensionados);
}
