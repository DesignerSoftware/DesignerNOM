/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import ClasesAyuda.SessionEntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Administrador
 */
public interface AdministrarSesionesInterface {

   public void adicionarSesion(SessionEntityManager session);

   public void consultarSessionesActivas();

//   public EntityManager obtenerConexionSesion(String idSesion);
   public EntityManagerFactory obtenerConexionSesionEMF(String idSesion);

   public SessionEntityManager obtenerSesionEMF(String idSesion);

   public boolean reconectarUsuario(SessionEntityManager sem);

   public void borrarSesion(String idSesion);

   public void guardarDatosConexion(String baseDatos, String usuario, String contraseña, String idSesion);
}
