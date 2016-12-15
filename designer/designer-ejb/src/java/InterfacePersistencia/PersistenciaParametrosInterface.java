/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.CambiosMasivos;
import Entidades.Parametros;
import Entidades.ParametrosCambiosMasivos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Parametros' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaParametrosInterface {

    /**
     * Método encargado de recuperar los Parámetros que están asociados y
     * autorizados para el usuario. El usuario estaría autorizado a tener
     * Parámetros si tiene un registro en la tabla UsuariosInstancias. El
     * Parámetro estaría habilitado a ser mostrado si tiene un registro en
     * ParametrosInstancias. Como ParametrosInstancias y UsuariosInstancias
     * utilizarían la misma instancia, se realiza un cruce entre ambas
     * utilizando la instancia dada al usuario.
     *
     * @param usuarioBD Secuencia del usuario que esta usando el aplicativo.
     * @return Retorna una lista de Parámetros.
     */
    public List<Parametros> parametrosComprobantes(EntityManager em, String usuarioBD);

    /**
     * Método encargado de buscar los Parámetros de todos los empleados.
     *
     * @return Retorna una lista de Parámetros.
     */
    public List<Parametros> empleadosParametros(EntityManager em, String usuarioBD);

    /**
     * Método encargado de eliminar de la base de datos el Parámetro que entra
     * por parámetro.
     *
     * @param parametro Parámetro que se quiere eliminar.
     */
    public void borrar(EntityManager em, Parametros parametro);

    /**
     * Método encargado de eliminar los Parámetros de un ParametroEstructura.
     * (EntityManager em, En un PámetroEstructura están guardados los parámetros de liquidación,
     * mientras que en un Parámetro estan los empleados y unas referencias a
     * otras tablas)
     *
     * @param secParametrosEstructuras Secuencia de un ParametroEstructura.
     */
    public void borrarParametros(EntityManager em, BigInteger secParametrosEstructuras);

    /**
     * Método encargado de crear un Parámetro en la base de datos.
     *
     * @param parametro Parámetro que se quiere crear.
     */
    public void crear(EntityManager em, Parametros parametro);
        
   // Para Cambios Masivos : 
   public void crearCambiosMasivos(EntityManager em, CambiosMasivos cambioMasivo);

   public void editarCambiosMasivos(EntityManager em, CambiosMasivos cambioMasivo);

   public void borrarCambiosMasivos(EntityManager em, CambiosMasivos cambioMasivo);

   public CambiosMasivos buscarCambioMasivoSecuencia(EntityManager em, BigInteger secuencia);

   public List<CambiosMasivos> consultarCambiosMasivos(EntityManager em);

   public ParametrosCambiosMasivos consultarParametroCambiosMasivos(EntityManager em, String usuario);
}
