/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Niveles;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarNivelesInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar Niveles.
     *
     * @param listaNiveles Lista Niveles que se van a modificar.
     */
    public void modificarNiveles(List<Niveles> listaNiveles);

    /**
     * Método encargado de borrar Niveles.
     *
     * @param listaNiveles Lista Niveles que se van a borrar.
     */
    public void borrarNiveles(List<Niveles> listaNiveles);

    /**
     * Método encargado de crear Niveles.
     *
     * @param listaNiveles Lista Niveles que se van a crear.
     */
    public void crearNiveles(List<Niveles> listaNiveles);

    /**
     * Método encargado de recuperar las Niveles para una tabla de la pantalla.
     *
     * @return Retorna una lista de Niveles.
     */
    public List<Niveles> consultarNiveles();

    /**
     * Método encargado de recuperar un Nivel dada su secuencia.
     *
     * @param secNiveles Secuencia del Nivel
     * @return Retorna un Nivel.
     */
    public Niveles consultarNivel(BigInteger secNiveles);

    /**
     * Método encargado de contar la cantidad de EvalConvocatorias relacionadas
     * con un Nivel específico.
     *
     * @param secNiveles Secuencia del Nivel.
     * @return Retorna un número indicando la cantidad de EvalConvocatorias cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarEvalConvocatoriasNivel(BigInteger secNiveles);

    /**
     * Método encargado de contar la cantidad de Plantas relacionadas con un
     * Nivel específico.
     *
     * @param secNiveles Secuencia del Nivel.
     * @return Retorna un número indicando la cantidad de Plantas cuya secuencia
     * coincide con el valor del parámetro.
     */
    public BigInteger contarPlantasNivel(BigInteger secNiveles);

    /**
     * Método encargado de contar la cantidad de PlantasPersonales relacionadas
     * con un Nivel específico.
     *
     * @param secNiveles Secuencia del Nivel.
     * @return Retorna un número indicando la cantidad de PlantasPersonales cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarPlantasPersonalesNivel(BigInteger secNiveles);
}
