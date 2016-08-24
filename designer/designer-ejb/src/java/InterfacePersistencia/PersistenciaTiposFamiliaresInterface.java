/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TiposFamiliares;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaTiposFamiliaresInterface {

    /**
     * Método encargado de insertar un TipoFamiliar en la base de datos.
     *
     * @param tiposFamiliares Moneda que se quiere crear.
     */
    public void crear(EntityManager em, TiposFamiliares tiposFamiliares);

    /**
     * Método encargado de modificar un TipoFamiliar de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tiposFamiliares TiposFamiliares con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, TiposFamiliares tiposFamiliares);

    /**
     * Método encargado de eliminar de la base de datos un TipoFamiliar que
     * entra por parámetro.
     *
     * @param tiposFamiliares TiposFamiliares que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposFamiliares tiposFamiliares);

    /**
     * Método encargado de buscar el TipoFamiliar con la secTiposFamiliares dada por
     * parámetro.
     *
     * @param secTiposFamiliares Secuencia del TipoFamiliar que se quiere encontrar.
     * @return Retorna el TipoFamiliar identificado con la secTiposFamiliares dada por
     * parámetro.
     */
    public TiposFamiliares buscarTiposFamiliares(EntityManager em, BigInteger secTiposFamiliares);

    /**
     * Método encargado de buscar todas los TiposFamiliares existentes en la
     * base de datos.
     *
     * @return Retorna una lista de TiposFamiliares.
     */
    public List<TiposFamiliares> buscarTiposFamiliares(EntityManager em );

    /**
     * Método encargado de revisar si existe una relacion entre un TipoFamiliar
     * específica y algúna HvReferencia. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secTiposFamiliares Secuencia del TipoFamiliar.
     * @return Retorna el número de HvReferencia relacionados con el
     * TipoFamiliar cuya secTiposFamiliares coincide con el parámetro.
     */
    public BigInteger contadorHvReferencias(EntityManager em, BigInteger secTiposFamiliares);

}
