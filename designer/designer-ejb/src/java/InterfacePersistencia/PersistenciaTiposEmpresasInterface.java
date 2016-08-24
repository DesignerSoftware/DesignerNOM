/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TiposEmpresas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaTiposEmpresasInterface {

    /**
     * Método encargado de insertar un TipoEmpresa en la base de datos.
     *
     * @param tiposEmpresas TiposEmpresa que se quiere crear.
     */
    public void crear(EntityManager em, TiposEmpresas tiposEmpresas);

    /**
     * Método encargado de modificar un TipoEmpresa de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tiposEmpresas TiposEmpresas con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TiposEmpresas tiposEmpresas);

    /**
     * Método encargado de eliminar de la base de datos un TipoEmpresa que entra
     * por parámetro.
     *
     * @param tiposEmpresas TiposEmpresas que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposEmpresas tiposEmpresas);

    /**
     * Método encargado de buscar el TipoEmpresa con la secTiposEmpresas dada
     * por parámetro.
     *
     * @param secTiposEmpresas Secuencia del TipoEmpresa que se quiere
     * encontrar.
     * @return Retorna el TipoEmpresa identificado con la secTiposEmpresas dada
     * por parámetro.
     */
    public TiposEmpresas buscarTipoEmpresa(EntityManager em, BigInteger secTiposEmpresas);

    /**
     * Método encargado de buscar todas los TiposEmpresas existentes en la base
     * de datos.
     *
     * @return Retorna una lista de TiposEmpresas.
     */
    public List<TiposEmpresas> buscarTiposEmpresas(EntityManager em );

    /**
     * Método encargado de revisar si existe una relacion entre un TipoEmpresa
     * específica y algún SueldoMercado. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secTiposEmpresas Secuencia del TipoEmpresa.
     * @return Retorna el número de SueldoMercado relacionados con la moneda
     * cuya secTiposEmpresas coincide con el parámetro.
     */
    public BigInteger contadorSueldosMercados(EntityManager em, BigInteger secTiposEmpresas);
}
