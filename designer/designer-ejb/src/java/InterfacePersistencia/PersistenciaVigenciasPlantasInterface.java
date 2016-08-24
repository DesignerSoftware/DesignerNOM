/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.VigenciasPlantas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaVigenciasPlantasInterface {

    /**
     * Método encargado de insertar una VigenciaPlanta en la base de datos.
     *
     * @param vigenciasPlantas VigenciaPlanta que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasPlantas vigenciasPlantas);

    /**
     * Método encargado de modificar una VigenciaPlanta de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con
     * la información de la base de datos.
     *
     * @param vigenciasPlantas VigenciaPlanta con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, VigenciasPlantas vigenciasPlantas);

    /**
     * Método encargado de eliminar de la base de datos la VigenciaPlanta que
     * entra por parámetro.
     *
     * @param vigenciasPlantas VigenciaPlanta que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasPlantas vigenciasPlantas);

    /**
     * Método encargado de buscar todos los VigenciaPlantaes existentes en la
     * base de datos, ordenados por nombre.
     *
     * @return Retorna una lista de VigenciaPlantaes ordenados por nombre.
     */
    public List<VigenciasPlantas> consultarVigenciasPlantas(EntityManager em );

    /**
     * Método encargado de buscar el VigenciaPlanta con la secVigenciasPlantas
     * dada por parámetro.
     *
     * @param secVigenciasPlantas Secuencia del VigenciaPlanta que se quiere
     * encontrar.
     * @return Retorna el VigenciaPlanta identificado con la secVigenciasPlantas
     * dada por parámetro.
     */
    public VigenciasPlantas consultarVigenciaPlanta(EntityManager em, BigInteger secVigenciasPlantas);

    /**
     * Método encargado de contar los Plantas que están asociados a una
     * VigenciaPlanta específico.
     *
     * @param secVigenciasPlantas Secuencia del VigenciaPlanta.
     * @return Retorna la cantidad de Plantas cuyo VigenciaPlanta tiene como
     * secVigenciasPlantas el valor dado por parámetro.
     */
    public BigInteger contarPlantasVigenciaPlanta(EntityManager em, BigInteger secVigenciasPlantas);
}
