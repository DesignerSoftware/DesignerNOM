/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TempNovedades;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'TempNovedades' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaTempNovedadesInterface {

   /**
    * Método encargado de insertar una TempNovedad en la base de datos.
    *
    * @param tempNovedades TempNovedad que se quiere crear.
    */
   public void crear(EntityManager em, TempNovedades tempNovedades);

   /**
    * Método encargado de modificar una TempNovedad de la base de datos. Este
    * método recibe la información del parámetro para hacer un 'merge' con la
    * información de la base de datos.
    *
    * @param tempNovedades TempNovedad con los cambios que se van a realizar.
    */
   public void editar(EntityManager em, TempNovedades tempNovedades);

   /**
    * Método encargado de eliminar de la base de datos la TempNovedad cuyo
    * ESTADO es igual a 'N' y esta relacionada con el usuario que entra por
    * parámetro.
    *
    * @param usuarioBD Alias del usuario asociado a la TempNovedad.
    */

   public void borrar(EntityManager em, TempNovedades tempNovedades);

   public void borrarRegistrosTempNovedades(EntityManager em, String usuarioBD);

   /**
    * Método encargado de buscar las TempNovedad cuyo ESTADO es igual a 'N' y
    * esta relacionada con el usuario que entra por parámetro.
    *
    * @param usuarioBD Alias del usuario asociado a la TempNovedad.
    * @return Retorna una lista de TempNovedades.
    */
   public List<TempNovedades> obtenerTempNovedades(EntityManager em, String usuarioBD);

   /**
    * Método encargado de obtener los datos del DocumentosSoporte de cada una de
    * las TempNovedades cuyo ESTADO es igual a 'C' y esta relacionada con el
    * usuario que entra por parámetro.
    *
    * @param usuarioBD Alias del usuario asociado a la TempNovedad.
    * @return Retorna una lista de String con los DocumentosSoporte de las
    * TempNovedades que cumplen las condiciones.
    */
   public List<String> obtenerDocumentosSoporteCargados(EntityManager em, String usuarioBD);

   /**
    * Método encargado de cargar las TempNovedades a Novedades. Cuando una
    * TempNovedad es cargada en Novedades, esta cambia de Estado 'N' a estado
    * 'C'.
    *
    * @param fechaReporte String con la Fecha en la que se cargó el Archivo
    * Plano a TempNovedades.
    * @param nombreCortoFormula Nombre corto de la formula.
    * @param usarFormula Indica si se va a usar la formula del concepto o la
    * formula pasada por parametro. Sus posibles valores son 'S' o 'N'.
    */
   public void cargarTempNovedades(EntityManager em, String fechaReporte, String nombreCortoFormula, String usarFormula);

   /**
    * Método encargado de revertir la TempNovedades con un documentoSoporte
    * específico. Para esto se eliminan los TempNovedades cuyo estado es igual a
    * 'C' y estan relacionados con un usuario determinado.
    *
    * @param usuarioBD Alias del usuario.
    * @param documentoSoporte DocumentopSoporte de la TempNovedades.
    */
   public void reversarTempNovedades(EntityManager em, String usuarioBD, String documentoSoporte);
}
