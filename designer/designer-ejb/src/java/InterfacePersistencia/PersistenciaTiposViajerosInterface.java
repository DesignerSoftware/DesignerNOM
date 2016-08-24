/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Tiposviajeros;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaTiposViajerosInterface {

    /**
     * Método encargado de insertar un TipoViajero en la base de datos.
     *
     * @param subCategorias Moneda que se quiere crear.
     */
    public void crear(EntityManager em, Tiposviajeros subCategorias);

    /**
     * Método encargado de modificar un TipoViajero de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param subCategorias TiposViajeros con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Tiposviajeros subCategorias);

    /**
     * Método encargado de eliminar de la base de datos un TipoViajero que entra
     * por parámetro.
     *
     * @param subCategorias TiposViajeros que se quiere eliminar.
     */
    public void borrar(EntityManager em, Tiposviajeros subCategorias);

    /**
     * Método encargado de buscar todas los TiposViajeros existentes en la base
     * de datos.
     *
     * @return Retorna una lista de TiposViajeros.
     */
    public List<Tiposviajeros> consultarTiposViajeros(EntityManager em );

    /**
     * Método encargado de buscar el TipoViajero con la secTiposViajeros dada
     * por parámetro.
     *
     * @param secTiposViajeros Secuencia del TipoViajero que se quiere
     * encontrar.
     * @return Retorna el TipoViajero identificado con la secTiposViajeros dada
     * por parámetro.
     */
    public Tiposviajeros consultarSubCategoria(EntityManager em, BigInteger secTiposViajeros);

    /**
     * Método encargado de revisar si existe una relacion entre un TipoViajero
     * específica y algúna VigenciasViajeros. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secTiposViajeros Secuencia del TipoViajero.
     * @return Retorna el número de VigenciasViajeros relacionados con el
     * TipoViajero cuya secTiposViajeros coincide con el parámetro.
     */
    public BigInteger contarVigenciasViajerosTipoViajero(EntityManager em, BigInteger secTiposViajeros);

    /**
     * Método encargado de revisar si existe una relacion entre un TipoViajero
     * específica y algúna TiposLegalizaciones. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secTiposViajeros Secuencia del TipoViajero.
     * @return Retorna el número de TiposLegalizaciones relacionados con el
     * TipoViajero cuya secTiposViajeros coincide con el parámetro.
     */
    public BigInteger contarTiposLegalizacionesTipoViajero(EntityManager em, BigInteger secTiposViajeros);
}
