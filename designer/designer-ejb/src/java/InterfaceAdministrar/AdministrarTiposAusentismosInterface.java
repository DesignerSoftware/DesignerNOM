/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Tiposausentismos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposAusentismosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposAusentismos.
     *
     * @param listaTiposAusentismos Lista TiposAusentismos que se van a
     * modificar.
     */
    public void modificarTiposAusentismos(List<Tiposausentismos> listaTiposAusentismos);

    /**
     * Método encargado de borrar TiposAusentismos.
     *
     * @param listaTiposAusentismos Lista TiposAusentismos que se van a borrar.
     */
    public void borrarTiposAusentismos(List<Tiposausentismos> listaTiposAusentismos);

    /**
     * Método encargado de crear TiposAusentismos.
     *
     * @param listaTiposAusentismos Lista TiposAusentismos que se van a crear.
     */
    public void crearTiposAusentismos(List<Tiposausentismos> listaTiposAusentismos);

    /**
     * Método encargado de recuperar las TiposAusentismos para un tabla de la
     * pantalla.
     *
     * @return Retorna un lista de TiposAusentismos.
     */
    public List<Tiposausentismos> consultarTiposAusentismos();

    /**
     * Método encargado de recuperar un TipoAusentismo dada su secuencia.
     *
     * @param secTiposAusentismos Secuencia del TipoAusentismo
     * @return Retorna un TipoAusentismo.
     */
    public Tiposausentismos consultarTipoAusentismo(BigInteger secTiposAusentismos);

    /**
     * Método encargado de contar la cantidad de ClasesAusentimos relacionadas con un
     * TipoAusentismo específico.
     *
     * @param secTiposAusentismos Secuencia del TipoAusentismo.
     * @return Retorna un número indicando la cantidad de ClasesAusentimos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarClasesAusentimosTipoAusentismo(BigInteger secTiposAusentismos);

    /**
     * Método encargado de contar la cantidad de SOAusentimos relacionadas con un
     * TipoAusentismo específico.
     *
     * @param secTiposAusentismos Secuencia del TipoAusentismo.
     * @return Retorna un número indicando la cantidad de SOAusentimos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarSOAusentimosTipoAusentismo(BigInteger secTiposAusentismos);
}
