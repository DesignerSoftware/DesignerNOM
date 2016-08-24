/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TiposAccidentes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaTiposAccidentesInterface {

    /**
     * Método encargado de insertar un TipoAccidente en la base de datos.
     *
     * @param tiposAccidentes Moneda que se quiere crear.
     */
    public void crear(EntityManager em, TiposAccidentes tiposAccidentes);

    /**
     * Método encargado de modificar un TipoAccidente de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tiposAccidentes TiposAccidentes con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, TiposAccidentes tiposAccidentes);

    /**
     * Método encargado de eliminar de la base de datos un TipoAccidente que
     * entra por parámetro.
     *
     * @param tiposAccidentes TiposAccidentes que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposAccidentes tiposAccidentes);

    /**
     * Método encargado de buscar el TipoAccidente con la secTiposAccidentes dada por
     * parámetro.
     *
     * @param secTiposAccidentes Secuencia del TipoAccidente que se quiere encontrar.
     * @return Retorna el TipoAccidente identificado con la secTiposAccidentes dada por
     * parámetro.
     */
    public TiposAccidentes buscarTipoAccidente(EntityManager em, BigInteger secTiposAccidentes);

    /**
     * Método encargado de buscar todas los TiposAccidentes existentes en la
     * base de datos.
     *
     * @return Retorna una lista de TiposAccidentes.
     */
    public List<TiposAccidentes> buscarTiposAccidentes(EntityManager em);

    /**
     * Método encargado de revisar si existe una relacion entre un TipoAccidente
     * específica y algún SoAccidenteMedico. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secTiposAccidentes Secuencia del TipoAccidente.
     * @return Retorna el número de SoAccidenteMedico relacionados con la moneda
     * cuya secTiposAccidentes coincide con el parámetro.
     */
    public BigInteger contadorSoAccidentesMedicos(EntityManager em, BigInteger secTiposAccidentes);

    /**
     * Método encargado de revisar si existe una relacion entre un TipoAccidente
     * específica y algún Accidente. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secTiposAccidentes Secuencia del TipoAccidente.
     * @return Retorna el número de Accidentes relacionados con la moneda cuya
     * secTiposAccidentes coincide con el parámetro.
     */
    public BigInteger contadorAccidentes(EntityManager em, BigInteger secTiposAccidentes);
}
