/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.CambiosMasivos;
import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.Parametros;
import Entidades.ParametrosCambiosMasivos;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.TiposTrabajadores;
import Entidades.Usuarios;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface AdministrarParametrosInterface {

   /**
    * Método encargado de obtener el Entity Manager el cual tiene asociado la
    * sesion del usuario que utiliza el aplicativo.
    *
    * @param idSesion Identificador se la sesion.
    */
   public void obtenerConexion(String idSesion);

   public Usuarios usuarioActual();

   public ParametrosEstructuras parametrosLiquidacion();

   public List<Estructuras> lovEstructuras();

   public List<TiposTrabajadores> lovTiposTrabajadores();

   public List<Procesos> lovProcesos(String aut);

   public List<Parametros> empleadosParametros();

   public String estadoParametro(BigInteger secuenciaParametro);

   public void crearParametroEstructura(ParametrosEstructuras parametroEstructura);

   public void eliminarParametros(List<Parametros> listaParametros);

   public void crearParametros(List<Parametros> listaParametros);

   public void adicionarEmpleados(BigInteger secParametroEstructura);

   public void borrarParametros(BigInteger secParametroEstructura);

   public Integer empleadosParametrizados(BigInteger secProceso);

   public Integer diferenciaDias(String fechaInicial, String fechaFinal);

   public List<Empleados> empleadosLov();

   //Para CambiosMasivos : 
//   public List<Parametros> consultarEmpleadosParametros();

   public List<CambiosMasivos> consultarUltimosCambiosMasivos();

   public ParametrosCambiosMasivos consultarParametrosCambiosMasivos();
}
