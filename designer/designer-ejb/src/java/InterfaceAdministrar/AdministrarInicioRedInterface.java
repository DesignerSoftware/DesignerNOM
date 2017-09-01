/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Conexiones;
//import Entidades.Empleados;
import Entidades.Recordatorios;
import java.util.List;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Administrator
 */
public interface AdministrarInicioRedInterface {

   public boolean conexionInicial(String baseDatos);

   public boolean conexionUsuario(String baseDatos, String usuario, String contraseña, String usapool, String idSesion);

   public boolean validarUsuario(String usuario, String usapool);

   public boolean validarConexionUsuario(String idSesion);

   public void cerrarSession(String idSesion);

   public boolean conexionDefault();

   public Recordatorios recordatorioAleatorio();

   public String nombreEmpresaPrincipal();

   public List<String> recordatoriosInicio();

   public List<Recordatorios> consultasInicio();

   public int cambioClave(String usuario, String nuevaClave);

   public void guardarDatosConexion(Conexiones conexion);

   public String usuarioBD();
}
