/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.CentrosCostos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'CentrosCostos' de la base de datos.
 *
 * @author Hugo David Sin Gutiérrez
 */
public interface PersistenciaCentrosCostosInterface {

    /**
     * Método encargado de insertar un CentroCosto en la base de datos.
     *
     * @param centrosCostos CentroCosto que se quiere crear.
     */
    public void crear(EntityManager em,CentrosCostos centrosCostos);

    /**
     * Método encargado de modificar un CentroCosto de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param centrosCostos CentroCosto con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,CentrosCostos centrosCostos);

    /**
     * Método encargado de eliminar de la base de datos el CentroCosto que entra
     * por parámetro.
     *
     * @param centrosCostos CentroCosto que se quiere eliminar.
     */
    public void borrar(EntityManager em,CentrosCostos centrosCostos);

    /**
     * Método encargado de buscar todos los CentrosCostos existentes en la base
     * de datos.
     *
     * @return Retorna una lista de Centros Costos.
     */
    public List<CentrosCostos> buscarCentrosCostos(EntityManager em);

    /**
     * Método encargado de buscar el CentroCosto con la secCentroCosto dada por
     * parámetro.
     *
     * @param secCentroCosto Secuencia del CentroCosto que se quiere encontrar.
     * @return Retorna el CentroCosto identificado con la secCentroCosto dada
     * por parámetro.
     */
    public CentrosCostos buscarCentroCostoSecuencia(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de buscar los CentroCosto de una empresa específica.
     *
     * @param secEmpresa Identificador único de la empresa a la cual pertenecen
     * los centrosCostos.
     * @return Retorna una lista de CentrosCostos que pertenecen a la empresa
     * con secCentroCosto igual a la pasada por parametro.
     */
    public List<CentrosCostos> buscarCentrosCostosEmpr(EntityManager em,BigInteger secEmpresa);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún Comprobante Contable. además de la revisión, cuenta
     * cuantas relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de Comprobantes Contables relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorComprobantesContables(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún DetalleCCConsolidador. además de la revisión, cuenta
     * cuantas relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de DetalleCCConsolidador relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorDetallesCCConsolidador(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún DetallesCCDetalle. además de la revisión, cuenta
     * cuantas relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de DetallesCCDetalle relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorDetallesCCDetalle(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún Empresas. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de Empresas relacionados con el CentroCosto
     * cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorEmpresas(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún Estructuras. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de Estructuras relacionados con el CentroCosto
     * cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorEstructuras(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún InterconCondor. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de InterconCondor relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorInterconCondor(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún InterconCondor. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de InterconCondor relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorInterconDynamics(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún InterconGeneral. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de InterconGeneral relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorInterconGeneral(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún InterconHelisa. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de InterconHelisa relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorInterconHelisa(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún InterconSapbo. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de InterconSapbo relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorInterconSapbo(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún InterconSiigo. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de InterconSiigo relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorInterconSiigo(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún InterconTotal. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de InterconTotal relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorInterconTotal(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún NovedadesC. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de NovedadesC relacionados con el CentroCosto
     * cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorNovedadesC(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún NovedadesD. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de NovedadesD relacionados con el CentroCosto
     * cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorNovedadesD(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún ProcesosProductivos. además de la revisión, cuenta
     * cuantas relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de ProcesosProductivos relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorProcesosProductivos(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún Proyecciones. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de Proyecciones relacionados con el CentroCosto
     * cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorProyecciones(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún SolucionesNodosC. además de la revisión, cuenta
     * cuantas relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de SolucionesNodosC relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorSolucionesNodosC(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún SolucionesNodosD. además de la revisión, cuenta
     * cuantas relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de SolucionesNodosD relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorSolucionesNodosD(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún SoPanoramas. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de SoPanoramas relacionados con el CentroCosto
     * cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorSoPanoramas(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún Terceros. además de la revisión, cuenta cuantas
     * relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de Terceros relacionados con el CentroCosto
     * cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorTerceros(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún UnidadesRegistradas. además de la revisión, cuenta
     * cuantas relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de UnidadesRegistradas relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorUnidadesRegistradas(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún VigenciasCuentasC. además de la revisión, cuenta
     * cuantas relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de VigenciasCuentasC relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorVigenciasCuentasC(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún VigenciasCuentasD. además de la revisión, cuenta
     * cuantas relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de VigenciasCuentasD relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorVigenciasCuentasD(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de revisar si existe una relación entre un CentroCosto
     * específica y algún VigenciasProrrateos. además de la revisión, cuenta
     * cuantas relaciónes existen.
     *
     * @param secCentroCosto Secuencia del CentroCosto.
     * @return Retorna el número de VigenciasProrrateos relacionados con el
     * CentroCosto cuya secCentroCosto coincide con el parámetro.
     */
    public BigInteger contadorVigenciasProrrateos(EntityManager em,BigInteger secCentroCosto);

    /**
     * Método encargado de obtener la lista de CentrosCostos que correspondan a una Empresa
     *
     * @param secEmpresa Secuencia de la Empresa.
     * @return Retorna una lista de CentrosCostos
     */
    public List<CentrosCostos> buscarCentroCostoPorSecuenciaEmpresa(EntityManager em,BigInteger secEmpresa);
}
