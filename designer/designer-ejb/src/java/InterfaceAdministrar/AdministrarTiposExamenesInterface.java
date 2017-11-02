/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposExamenes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
public interface AdministrarTiposExamenesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposExamenes.
     *
     * @param listaTiposExamenes Lista TiposExamenes que se van a modificar.
     */
    public String modificarTiposExamenes(TiposExamenes tipoExamen);

    /**
     * Método encargado de borrar TiposExamenes.
     *
     * @param listaTiposExamenes Lista TiposExamenes que se van a borrar.
     */
    public String borrarTiposExamenes(TiposExamenes tipoExamen);

    /**
     * Método encargado de crear TiposExamenes.
     *
     * @param listaTiposExamenes Lista TiposExamenes que se van a crear.
     */
    public String crearTiposExamenes(TiposExamenes tipoExamen);

    /**
     * Método encargado de recuperar las TiposExamenes para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de TiposExamenes.
     */
    public List<TiposExamenes> consultarTiposExamenes();

    /**
     * Método encargado de recuperar un TipoExamen dada su secuencia.
     *
     * @param secTiposExamenes Secuencia del TipoExamen
     * @return Retorna un TiposExamenes.
     */
    public TiposExamenes consultarTipoExamen(BigInteger secTiposExamenes);

    /**
     * Método encargado de contar la cantidad de TiposExamenesCargos relacionadas con
     * un TipoExamen específica.
     *
     * @param secTiposExamenes Secuencia del TipoExamen.
     * @return Retorna un número indicando la cantidad de TiposExamenesCargos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarTiposExamenesCargosTipoExamen(BigInteger secTiposExamenes);

    /**
     * Método encargado de contar la cantidad de VigenciasExamenesMedicos relacionadas con
     * un TipoExamen específica.
     *
     * @param secTiposExamenes Secuencia del TipoExamen.
     * @return Retorna un número indicando la cantidad de VigenciasExamenesMedicos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarVigenciasExamenesMedicosTipoExamen(BigInteger secTiposExamenes);
}
