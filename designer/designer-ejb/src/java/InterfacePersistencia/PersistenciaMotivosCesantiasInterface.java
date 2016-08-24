/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.MotivosCesantias;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
import javax.persistence.EntityManager;
public interface PersistenciaMotivosCesantiasInterface {

    /**
     * Método encargado de insertar un Motivo Cesantia en la base de datos.
     *
     * @param em
     * @param motivosCesantias Nibeda que se quiere crear.
     */
    public void crear(EntityManager em, MotivosCesantias motivosCesantias);

    /**
     * Método encargado de modificar un Motivo Cesantia de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con
     * la información de la base de datos.
     *
     * @param em
     * @param motivosCesantias Monedas con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, MotivosCesantias motivosCesantias);

    /**
     * Método encargado de eliminar de la base de datos un Motivo Cesantia que
     * entra por parámetro.
     *
     * @param em
     * @param motivosCesantias Monedas que se quiere eliminar.
     */
    public void borrar(EntityManager em, MotivosCesantias motivosCesantias);

    /**
     * Método encargado de buscar la Moneda con la secMotivosCesantias dada por parámetro.
     *
     * @param em
     * @param secMotivosCesantias Secuencia de la Moneda que se quiere encontrar.
     * @return Retorna la Moneda identificada con la secMotivosCesantias dada por
     * parámetro.
     */
    public MotivosCesantias buscarMotivoCensantia(EntityManager em, BigInteger secMotivosCesantias);

    /**
     * Método encargado de buscar todas los Motivos Cesantias existentes en la
     * base de datos.
     *
     * @param em
     * @return Retorna una lista de MotivosCesantias.
     */
    public List<MotivosCesantias> buscarMotivosCesantias(EntityManager em);

    /**
     * Método encargado de revisar si existe una relacion entre un Motivo
     * Cesantia específica y algúna Novedad Sistema. Adémas de la revisión,
     * cuenta cuantas relaciones existen.
     *
     * @param em
     * @param secMotivosCesantias Secuencia del Motivo Cesantia.
     * @return Retorna el número de proyectos relacionados con el Motivo
     * Censantia cuya secMotivosCesantias coincide con el parámetro.
     */
    public BigInteger contadorNovedadesSistema(EntityManager em, BigInteger secMotivosCesantias);
}
