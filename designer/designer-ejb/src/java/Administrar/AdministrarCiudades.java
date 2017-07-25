/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Ciudades;
import InterfaceAdministrar.AdministrarCiudadesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaDepartamentosInterface;
import InterfacePersistencia.PersistenciaUbicacionesGeograficasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br> Clase encargada de realizar las operaciones lógicas para
 * la pantalla 'Ciudades'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarCiudades implements AdministrarCiudadesInterface {

   private static Logger log = Logger.getLogger(AdministrarCiudades.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaCiudades'.
    */
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaDepartamentos'.
    */
   @EJB
   PersistenciaDepartamentosInterface persistenciaDepartamentos;
   @EJB
   PersistenciaUbicacionesGeograficasInterface persistenciaUbicacionesGeograficas;
   /**
    * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
    * conexión del usuario que está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   private EntityManager em;

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------    
   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public List<Ciudades> consultarCiudades() {
      List<Ciudades> listaCiudades;
      listaCiudades = persistenciaCiudades.consultarCiudades(em);
      return listaCiudades;
   }

   @Override
   public void modificarCiudades(List<Ciudades> listaCiudades) {
      Ciudades c;
      for (int i = 0; i < listaCiudades.size(); i++) {
         log.warn("Modificando...");
         if (listaCiudades.get(i).getDepartamento().getSecuencia() == null) {
            listaCiudades.get(i).setDepartamento(null);
            c = listaCiudades.get(i);
         } else {
            c = listaCiudades.get(i);
         }
         persistenciaCiudades.editar(em, c);
      }
   }

   @Override
   public boolean borrarCiudades(List<Ciudades> listaCiudades) {
      boolean borradosTodos = true;
      for (int i = 0; i < listaCiudades.size(); i++) {
         if (listaCiudades.get(i).getDepartamento().getSecuencia() == null) {
            listaCiudades.get(i).setDepartamento(null);
         }
         if (!persistenciaCiudades.borrar(em, listaCiudades.get(i))) {
            borradosTodos = false;
         }
      }
      return borradosTodos;
   }

   @Override
   public void crearCiudades(List<Ciudades> listaCiudades) {
      for (int i = 0; i < listaCiudades.size(); i++) {
         log.warn("Creando...");
         if (listaCiudades.get(i).getDepartamento().getSecuencia() == null) {

            listaCiudades.get(i).setDepartamento(null);
            persistenciaCiudades.crear(em, listaCiudades.get(i));
         } else {
            persistenciaCiudades.crear(em, listaCiudades.get(i));
         }
      }
   }

   @Override
   public int existeenUbicacionesGeograficas(BigInteger secCiudad) {
      try {
         return persistenciaUbicacionesGeograficas.existeCiudadporSecuencia(em, secCiudad);
      } catch (Exception e) {
         log.error("ERROR: AdministrarCiudades. existeenUbicacionesGeograficas() : " + e);
         return 0;
      }
   }
}
