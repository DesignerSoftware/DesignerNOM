/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.VigenciasPlantas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarVigenciasPlantasInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar VigenciasPlantas.
     *
     * @param listaVigenciasPlantas Lista VigenciasPlantas que se van a
     * modificar.
     */
    public void modificarVigenciasPlantas(List<VigenciasPlantas> listaVigenciasPlantas);

    /**
     * Método encargado de borrar VigenciasPlantas.
     *
     * @param listaVigenciasPlantas Lista VigenciasPlantas que se van a borrar.
     */
    public void borrarVigenciasPlantas(List<VigenciasPlantas> listaVigenciasPlantas);

    /**
     * Método encargado de crear VigenciasPlantas.
     *
     * @param listaVigenciasPlantas Lista VigenciasPlantas que se van a crear.
     */
    public void crearVigenciasPlantas(List<VigenciasPlantas> listaVigenciasPlantas);

    /**
     * Método encargado de recuperar las VigenciasPlantas para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de VigenciasPlantas.
     */
    public List<VigenciasPlantas> consultarVigenciasPlantas();

    /**
     * Método encargado de recuperar una VigenciaPlanta dada su secuencia.
     *
     * @param secVigenciasPlantas Secuencia de la VigenciaPlanta
     * @return Retorna una VigenciaPlanta.
     */
    public VigenciasPlantas consultarVigenciaPlanta(BigInteger secVigenciasPlantas);

    /**
     * Método encargado de contar la cantidad de Plantas relacionadas con una
     * VigenciaPlanta específico.
     *
     * @param secVigenciasPlantas Secuencia del VigenciaPlanta.
     * @return Retorna un número indicando la cantidad de Plantas cuya secuencia
     * coincide con el valor del parámetro.
     */
    public BigInteger contarPlantasVigenciaPlanta(BigInteger secVigenciasPlantas);
}
