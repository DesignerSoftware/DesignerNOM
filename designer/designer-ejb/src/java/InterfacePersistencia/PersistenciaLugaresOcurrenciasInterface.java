/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.LugaresOcurrencias;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaLugaresOcurrenciasInterface {

    /**
     * Método encargado de insertar un Lugar Ocurrencia en la base de datos.
     *
     * @param lugaresOcurrencias que se quiere crear.
     */
    public void crear(EntityManager em, LugaresOcurrencias lugaresOcurrencias);

    /**
     * Método encargado de modificar un Lugar Ocurrencia de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con
     * la información de la base de datos.
     *
     * @param lugaresOcurrencias LugaresOcurrencias con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, LugaresOcurrencias lugaresOcurrencias);

    /**
     * Método encargado de eliminar de la base de datos un LugarOcurrencia que
     * entra por parámetro.
     *
     * @param lugaresOcurrencias LugaresOcurrencias que se quiere eliminar.
     */
    public void borrar(EntityManager em, LugaresOcurrencias lugaresOcurrencias);

    /**
     * Método encargado de buscar un LugarOcurrencia con la secLugaresOcurrencias dada por
     * parámetro.
     *
     * @param secLugaresOcurrencias Secuencia del Lugar Ocurrencia que se quiere
     * encontrar.
     * @return Retorna un LugarOcurrencia identificado con la secLugaresOcurrencias dada por
     * parámetro.
     */
    public LugaresOcurrencias buscarLugaresOcurrencias(EntityManager em, BigInteger secLugaresOcurrencias);

    /**
     * Método encargado de buscar todas los LugaresOcurrencias existentes en la
     * base de datos.
     *
     * @return Retorna una lista de Lugares Ocurrencias.
     */
    public List<LugaresOcurrencias> buscarLugaresOcurrencias(EntityManager em);

    /**
     * Método encargado de revisar si existe una relacion entre una
     * LugaresOcurrencias específica y algún Proyecto. Adémas de la revisión,
     * cuenta cuantas relaciones existen.
     *
     * @param secLugaresOcurrencias Secuencia del LugarOcurrencia.
     * @return Retorna el número de SoAccidntes relacionados con el
     * LugarOcurrencia cuya secLugaresOcurrencias coincide con el parámetro.
     */
    public BigInteger contadorSoAccidentes(EntityManager em, BigInteger secLugaresOcurrencias);
}
