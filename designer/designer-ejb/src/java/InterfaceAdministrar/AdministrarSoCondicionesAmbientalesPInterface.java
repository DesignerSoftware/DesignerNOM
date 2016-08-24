/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.SoCondicionesAmbientalesP;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarSoCondicionesAmbientalesPInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar SoCondicionesAmbientalesP.
     *
     * @param listSoCondicionesAmbientalesP Lista SoCondicionesAmbientalesP que
     * se van a modificar.
     */
    public void modificarSoCondicionesAmbientalesP(List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP);

    /**
     * Método encargado de borrar SoCondicionesAmbientalesP.
     *
     * @param listSoCondicionesAmbientalesP Lista SoCondicionesAmbientalesP que
     * se van a borrar.
     */
    public void borrarSoCondicionesAmbientalesP(List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP);

    /**
     * Método encargado de crear SoCondicionesAmbientalesP.
     *
     * @param listSoCondicionesAmbientalesP Lista SoCondicionesAmbientalesP que
     * se van a crear.
     */
    public void crearSoCondicionesAmbientalesP(List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP);

    /**
     * Metodo encargado de traer todas las SoCondicionesAmbientalesP de la base
     * de datos.
     *
     * @return Lista de SoCondicionesAmbientalesP.
     */
    public List<SoCondicionesAmbientalesP> consultarSoCondicionesAmbientalesP();

    /**
     * Método encargado de recuperar un SoCondicionesAmbientalesP dada su
     * secuencia.
     *
     * @param secSoCondicionAmbientalPeligrosa Secuencia del
     * SoCondicionesAmbientalesP.
     * @return Retorna el SoCondicionAmbientalP cuya secuencia coincida con el
     * valor del parámetro.
     */
    public SoCondicionesAmbientalesP consultarSoCondicionAmbientalP(BigInteger secSoCondicionAmbientalPeligrosa);

    /**
     * Método encargado de contar la cantidad de SoAccidentesMedicos
     * relacionadas con un SoCondicionesAmbientalesP específico.
     *
     * @param secSoCondicionAmbientalPeligrosa Secuencia del
     * SoCondicionesAmbientalesP.
     * @return Retorna un número indicando la cantidad de SoAccidentesMedicos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger verificarSoAccidentesMedicos(BigInteger secSoCondicionAmbientalPeligrosa);
}
