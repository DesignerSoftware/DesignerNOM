/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Empresas;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Empresas' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaEmpresasInterface {

   /**
    * Método encargado de insertar una Empresa en la base de datos.
    *
    * @param em
    * @param empresas Empresa que se quiere crear.
    */
   public void crear(EntityManager em, Empresas empresas);

   /**
    * Método encargado de modificar una Empresa de la base de datos. Este método
    * recibe la información del parámetro para hacer un 'merge' con la
    * información de la base de datos.
    *
    * @param empresas Empresa con los cambios que se van a realizar.
    */
   public void editar(EntityManager em, Empresas empresas);

   /**
    * Método encargado de eliminar de la base de datos el Contrato que entra por
    * parámetro.
    *
    * @param empresas Empresa que se quiere eliminar.
    */
   public void borrar(EntityManager em, Empresas empresas);

   /**
    * Método encargado de buscar todas las Empresas existentes en la base de
    * datos.
    *
    * @return Retorna una lista de Empresas.
    */
   public List<Empresas> consultarEmpresas(EntityManager em);

   /**
    * Método encargado de buscar la Empresa con la secuencia dada por parámetro.
    *
    * @param secuencia Secuencia de la Empresa que se quiere encontrar.
    * @return Retorna la Empresa identificada con la secuencia dada por
    * parámetro.
    */
   public Empresas buscarEmpresasSecuencia(EntityManager em, BigInteger secuencia);

   /**
    * Método encargado de recuperar la información del campo barraconsultadatos
    * de la tabla 'Empresas' de la base de datos. Recibe como resultado 'S' o
    * 'N' y define si se muestra el proceso despues de liquidar o no
    *
    * @param secuenciaEmpresa Secuencia de la Empresa.
    * @return Retorna un String 'S' si se muestra el proceso despues de liquidar
    * o 'N' de lo contrario.
    */
   public String estadoConsultaDatos(EntityManager em, BigInteger secuenciaEmpresa);

   /**
    * Método encargado de indicar si hay cero, una o mas empresas registradas en
    * el aplicativo. Si solo hay una empresa registrada, se retorna el nombre de
    * la empresa. Si no hay empresas registradas se retorna 'Sin Registrar' y si
    * hay mas de una empresa se retorna '(Multiempresa)'
    *
    * @param entity EntityManager encargado de la comunicación con la base de
    * datos.
    * @return Retorna un String especificando si en el aplicativo hay
    * registradas cero, una o más empresas
    * @throws java.lang.Exception
    */
   public String nombreEmpresa(EntityManager entity) throws Exception;

   /**
    * Método encargado de obtener el codigo de la empresa. Obtiene el código de
    * la empresa dependiendo del usuario conectado, si es producción se tomara
    * por defecto la empresa con código 1.
    *
    * @return Retorna un Short especificando el codigo de la empresa con la que
    * se esta conectado
    */
   public Short codigoEmpresa(EntityManager em);

   public List<Empresas> buscarEmpresas(EntityManager em);

   public String consultarPrimeraEmpresa(EntityManager em);

   public Empresas consultarPrimeraEmpresaSinPaquete(EntityManager em);

   public BigInteger calcularControlEmpleadosEmpresa(EntityManager em, BigInteger secuencia);

   public BigInteger obtenerMaximoEmpleadosEmpresa(EntityManager em, BigInteger secuencia);

   public List<Empresas> buscarEmpresasLista(EntityManager em, BigInteger secuencia);

   public Empresas consultarEmpresaPorCodigo(EntityManager em, short codigo);

   public String obtenerEnvioInterfaseContabilidadEmpresa(EntityManager em, short codigoEmpresa);

   public BigDecimal consultarEmpresaPorEmpleado(EntityManager em, BigInteger secEmpleado);

   public String clonarEmpresa(EntityManager em, short codOrigen, short codDestino);
}
