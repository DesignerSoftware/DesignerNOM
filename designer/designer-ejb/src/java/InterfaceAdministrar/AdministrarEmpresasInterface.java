/**
 * Documentación a cargo de AndresPineda
 */
package InterfaceAdministrar;

import Entidades.CentrosCostos;
import Entidades.Circulares;
import Entidades.Empresas;
import Entidades.Monedas;
import Entidades.VigenciasMonedasBases;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'Empresas'.
 *
 * @author AndresPineda.
 */
public interface AdministrarEmpresasInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de recuperar todas las Empresas.
     *
     * @return Retorna una lista de Empresas.
     */
    public List<Empresas> listaEmpresas();

    /**
     * Método encargado de crear Empresas.
     *
     * @param listaE Lista de las Empresas que se van a crear.
     */
    public void crearEmpresas(List<Empresas> listaE);

    /**
     * Método encargado de editar Conceptos.
     *
     * @param listaE Lista de las Empresas que se van a modificar.
     */
    public void editarEmpresas(List<Empresas> listaE);

    /**
     * Método encargado de borrar Conceptos.
     *
     * @param listaE Lista de las Empresas que se van a eliminar.
     */
    public void borrarEmpresas(List<Empresas> listaE);

    /**
     * Método encargado de recuperar todas las Circulares asociadas a la
     * secuencia de una Empresa dada por parametro.
     *
     * @param secuencia Secuencia de la Empresa
     * @return Retorna una lista de Circulares.
     */
    public List<Circulares> listaCircularesParaEmpresa(BigInteger secuencia);

    /**
     * Método encargado de crear Circulares.
     *
     * @param listaC Lista de las Circulares que se van a crear.
     */
    public void crearCirculares(List<Circulares> listaC);

    /**
     * Método encargado de editar Circulares.
     *
     * @param listaC Lista de las Circulares que se van a modificar.
     */
    public void editarCirculares(List<Circulares> listaC);

    /**
     * Método encargado de borrar Circulares.
     *
     * @param listaC Lista de las Circulares que se van a eliminar.
     */
    public void borrarCirculares(List<Circulares> listaC);

    /**
     * Método encargado de recuperar todas las VigenciasMonedasBases asociadas a
     * la secuencia de una Empresa dada por parametro.
     *
     * @param secuencia Secuencia de la Empresa
     * @return Retorna una lista de VigenciasMonedasBases.
     */
    public List<VigenciasMonedasBases> listaVigenciasMonedasBasesParaEmpresa(BigInteger secuencia);

    /**
     * Método encargado de crear VigenciasMonedasBases.
     *
     * @param listaVMB Lista de las VigenciasMonedasBases que se van a crear.
     */
    public void crearVigenciasMonedasBases(List<VigenciasMonedasBases> listaVMB);

    /**
     * Método encargado de editar VigenciasMonedasBases.
     *
     * @param listaVMB Lista de las VigenciasMonedasBases que se van a
     * modificar.
     */
    public void editarVigenciasMonedasBases(List<VigenciasMonedasBases> listaVMB);

    /**
     * Método encargado de borrar VigenciasMonedasBases.
     *
     * @param listaVMB Lista de las VigenciasMonedasBases que se van a eliminar.
     */
    public void borrarVigenciasMonedasBases(List<VigenciasMonedasBases> listaVMB);

    /**
     * Método encargado de recuperar los CentrosCostos de la base de datos
     *
     * @return Retorna una lista de CentrosCostos.
     */
    public List<CentrosCostos> lovCentrosCostos();

    /**
     * Método encargado de recuperar las Monedas de la base de datos
     *
     * @return Retorna una lista de Monedas.
     */
    public List<Monedas> lovMonedas();

    public Empresas consultarEmpresaSecuencia(BigInteger secEmpresa);

    public List<Empresas> listasEmpresasPorSecuenciaEmpresa(BigInteger secuencia);
    
    public String clonarEmpresa(short codOrigen, short codDestino);
}
