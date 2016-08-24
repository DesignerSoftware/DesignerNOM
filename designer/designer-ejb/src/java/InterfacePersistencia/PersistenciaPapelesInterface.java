/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Papeles;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */

public interface PersistenciaPapelesInterface {

    /**
     * Método encargado de insertar un Papel en la base de datos.
     *
     * @param papel Papel que se quiere crear.
     */
    public void crear(EntityManager em, Papeles papel);

    /**
     * Método encargado de modificar un Papel de la base de datos. Este método
     * recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param papel Papel con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Papeles papel);

    /**
     * Método encargado de eliminar de la base de datos el Papel que entra por
     * parámetro.
     *
     * @param papel Papel con los cambios que se van a realizar.
     */
    public void borrar(EntityManager em, Papeles papel);

    /**
     * Método encargado de buscar todos los Papeles existentes en la base de
     * datos, ordenadas por código.
     *
     * @return Retorna una lista de Papeles.
     */
    public List<Papeles> consultarPapeles(EntityManager em);

    /**
     * Método encargado de buscar los Papeles de una empresa específica.
     *
     * @param secEmpresa Identificador único de la empresa a la cual pertenecen
     * los Papeles.
     * @return Retorna una lista de Pepeles que pertenecen a la empresa con
     * secEmpresa igual a la pasada por parametro.
     */
    public List<Papeles> consultarPapelesEmpresa(EntityManager em, BigInteger secEmpresa);

    /**
     * Método encargado de buscar el Papel con la secPapel dada por parámetro.
     *
     * @param secPapel Secuencia del Papel que se quiere encontrar.
     * @return Retorna el Papel identificado con la secPapel dada por parámetro.
     */
    public Papeles consultarPapel(EntityManager em, BigInteger secPapel);

    /**
     * Método encargado de contar cuantas VigenciasCargos están asociados a un
     * Papel específico.
     *
     * @param secPapel Secuencia del Papel.
     * @return Retorna el número de VigenciasCargos cuyo atributo 'papel' tiene
     * como secuencia el valor dado por parámetro.
     */
    public BigInteger contarVigenciasCargosPapel(EntityManager em, BigInteger secPapel);
}
