/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ClasesAccidentes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaClasesAccidentesInterface {

    /**
     * Método encargado de insertar un ClaseAccidente en la base de datos.
     *
     * @param clasesAccidentes ClasesAccidentes que se quiere crear.
     */
    public void crear(EntityManager em,ClasesAccidentes clasesAccidentes);

    /**
     * Método encargado de modificar un ClaseAccidente de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param clasesAccidentes ClasesAccidentes con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em,ClasesAccidentes clasesAccidentes);

    /**
     * Método encargado de eliminar de la base de datos un ClaseAccidente que
     * entra por parámetro.
     *
     * @param clasesAccidentes ClasesAccidentes que se quiere eliminar.
     */
    public void borrar(EntityManager em,ClasesAccidentes clasesAccidentes);

    /**
     * Método encargado de buscar un ClaseAccidente con la secClasesAccidentes
     * dada por parámetro.
     *
     * @param secClasesAccidentes secClasesAccidentes de un ClaseAccidente que
     * se quiere encontrar.
     * @return Retorna un ClaseAccidente identificado con la secClasesAccidentes
     * dada por parámetro.
     */
    public ClasesAccidentes buscarClaseAccidente(EntityManager em,BigInteger secClasesAccidentes);

    /**
     * Método encargado de buscar todas las ClasesAccidentes existentes en la
     * base de datos.
     *
     * @return Retorna una lista de ClasesAccidentes.
     */
    public List<ClasesAccidentes> buscarClasesAccidentes(EntityManager em);

    /**
     * Método encargado de revisar si existe una relación entre un
     * ClaseAccidente específico y algún AccidenteMedico. Adémas de la revisión,
     * cuenta cuantas relaciones existen.
     *
     * @param secClasesAccidentes Secuencia de un ClaseAccidente.
     * @return Retorna el número de proyectos relacionados con un ClaseAccidente
     * cuya secuencia coincide con el parámetro.
     */
    public BigInteger contadorSoAccidentesMedicos(EntityManager em,BigInteger secClasesAccidentes);
}
