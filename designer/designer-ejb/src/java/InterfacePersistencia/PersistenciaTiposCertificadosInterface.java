/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposCertificados;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'TiposCertificados' 
 * de la base de datos.
 * @author betelgeuse
 */
@Local
public interface PersistenciaTiposCertificadosInterface {
    /**
     * Método encargado de insertar un TipoCertificado en la base de datos.
     * @param tiposCertificados TipoCertificado que se quiere crear.
     */
    public void crear(EntityManager em, TiposCertificados tiposCertificados);
    /**
     * Método encargado de modificar un TipoCertificado de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param tiposCertificados TipoCertificado con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TiposCertificados tiposCertificados);
    /**
     * Método encargado de eliminar de la base de datos el TipoCertificado que entra por parámetro.
     * @param tiposCertificados TipoCertificado que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposCertificados tiposCertificados);
    /**
     * Método encargado de buscar el TipoCertificado con la secTiposCertificados dada por parámetro.
     * @param secTiposCertificados Secuencia del TipoCertificado que se quiere encontrar.
     * @return Retorna el TipoCertificado identificado con la secTiposCertificados dada por parámetro.
     */
    public TiposCertificados buscarTipoCertificado(EntityManager em, BigInteger secTiposCertificados);
    /**
     * Método encargado de buscar todos los TiposCertificados existentes en la base de datos.
     * @return Retorna una lista de TiposCertificados.
     */
    public List<TiposCertificados> buscarTiposCertificados(EntityManager em );
}
