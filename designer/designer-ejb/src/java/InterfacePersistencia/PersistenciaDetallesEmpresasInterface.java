/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.DetallesEmpresas;
import Entidades.TercerosSucursales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'DetallesEmpresas' de la base de datos.
 *
 * @author betelgeuse
 * @version 1.1 AndresPineda
 * (Crear-Editar-Borrar-BuscarDetallesEmpresas-BuscarDetalleEmpresaPorSecuencia)
 */
public interface PersistenciaDetallesEmpresasInterface {

    /**
     * Método encargado de insertar un DetalleEmpresa en la base de datos.
     *
     * @param em
     * @param detallesEmpresas DetalleEmpresa que se quiere crear.
     */
    public void crear(EntityManager em, DetallesEmpresas detallesEmpresas);

    /**
     * Método encargado de modificar un DetalleEmpresa de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param em
     * @param detallesEmpresas DetalleEmpresa con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, DetallesEmpresas detallesEmpresas);

    /**
     * Método encargado de eliminar de la base de datos el DetalleEmpresa que
     * entra por parámetro.
     *
     * @param em
     * @param detallesEmpresas DetalleEmpresa que se quiere eliminar.
     */
    public void borrar(EntityManager em, DetallesEmpresas detallesEmpresas);

    /**
     * Método encargado de buscar todos los DetallesEmpresas existentes en la
     * base de datos.
     *
     * @param em
     * @return Retorna una lista de DetallesEmpresas.
     */
    public List<DetallesEmpresas> buscarDetallesEmpresas(EntityManager em);

    /**
     * Método encargado de buscar los DetallesEmpresa de una Empresa especifica
     * por medio de su secuencia
     *
     * @param em
     * @param secEmpresa Secuencia Empresa
     * @return Retorna el DetalleEmpresa de la Empresa.
     */
    public DetallesEmpresas buscarDetalleEmpresaPorSecuencia(EntityManager em, BigInteger secEmpresa);

    /**
     * Método encargado de buscar el DetalleEmpresa con la secuencia dada por
     * parámetro.
     *
     * @param em
     * @param codigoEmpresa Secuencia de la Empresa de la cual se quiere el
     * detalle.
     * @return Retorna el DetalleEmpresa de la emprersa identificada con la
     * secuencia dada por parámetro.
     */
    public DetallesEmpresas buscarDetalleEmpresa(EntityManager em, Short codigoEmpresa);

    public TercerosSucursales buscarARLPorEmpresa(EntityManager em, BigInteger secuencia);

    public List<TercerosSucursales> LovTercerosSucursales(EntityManager em);

}
