/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.SoCondicionesTrabajos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarSoCondicionesTrabajosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar SoCondicionesTrabajos.
     *
     * @param listaSoCondicionesTrabajos Lista SoCondicionesTrabajos que se van
     * a modificar.
     */
    public void modificarSoCondicionesTrabajos(List<SoCondicionesTrabajos> listaSoCondicionesTrabajos);

    /**
     * Método encargado de borrar SoCondicionesTrabajos.
     *
     * @param listaSoCondicionesTrabajos Lista SoCondicionesTrabajos que se van
     * a borrar.
     */
    public void borrarSoCondicionesTrabajos(List<SoCondicionesTrabajos> listaSoCondicionesTrabajos);

    /**
     * Método encargado de crear SoCondicionesTrabajos.
     *
     * @param listaSoCondicionesTrabajos Lista SoCondicionesTrabajos que se van
     * a crear.
     */
    public void crearSoCondicionesTrabajos(List<SoCondicionesTrabajos> listaSoCondicionesTrabajos);

    /**
     * Método encargado de recuperar las SoCondicionesTrabajos para una tabla de
     * la pantalla.
     *
     * @return Retorna una lista de SoCondicionesTrabajos.
     */
    public List<SoCondicionesTrabajos> consultarSoCondicionesTrabajos();

    /**
     * Método encargado de recuperar una SoCondicionesTrabajos dada su
     * secuencia.
     *
     * @param secSoCondicionesTrabajos Secuencia del SoCondicionTrabajo
     * @return Retorna un SoCondicionesTrabajos.
     */
    public SoCondicionesTrabajos consultarSoCondicionTrabajo(BigInteger secSoCondicionesTrabajos);

    /**
     * Método encargado de contar la cantidad de Inpescciones relacionadas con
     * una SoCondicionTrabajo específico.
     *
     * @param secSoCondicionesTrabajos Secuencia del SoCondicionTrabajo.
     * @return Retorna un número indicando la cantidad de SoAccidentesMedicos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarInspeccionesSoCondicionTrabajo(BigInteger secSoCondicionesTrabajos);

    /**
     * Método encargado de contar la cantidad de SoAccidentesMedicos
     * relacionadas con una SoCondicionTrabajo específico.
     *
     * @param secSoCondicionesTrabajos Secuencia del SoCondicionTrabajo.
     * @return Retorna un número indicando la cantidad de SoAccidentesMedicos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarSoAccidentesMedicosSoCondicionTrabajo(BigInteger secSoCondicionesTrabajos);

    /**
     * Método encargado de contar la cantidad de SoDetallesPanoramas
     * relacionadas con una SoCondicionTrabajo específico.
     *
     * @param secSoCondicionesTrabajos Secuencia del SoCondicionTrabajo.
     * @return Retorna un número indicando la cantidad de SoDetallesPanoramas
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarSoDetallesPanoramasSoCondicionTrabajo(BigInteger secSoCondicionesTrabajos);

    /**
     * Método encargado de contar la cantidad de SoExposicionesFr relacionadas
     * con una SoCondicionTrabajo específico.
     *
     * @param secSoCondicionesTrabajos Secuencia del SoCondicionTrabajo.
     * @return Retorna un número indicando la cantidad de SoExposicionesFr cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarSoExposicionesFrSoCondicionTrabajo(BigInteger secSoCondicionesTrabajos);
}
