/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Idiomas;
import Entidades.IdiomasPersonas;
import InterfaceAdministrar.AdministrarIdiomaPersonaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaIdiomasInterface;
import InterfacePersistencia.PersistenciaIdiomasPersonasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarIdiomaPersona implements AdministrarIdiomaPersonaInterface {

   private static Logger log = Logger.getLogger(AdministrarIdiomaPersona.class);

    @EJB
    PersistenciaIdiomasPersonasInterface persistenciaIdiomasPersonas;
    @EJB
    PersistenciaIdiomasInterface persistenciaIdiomas;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;

        /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public void crearIdiomasPersonas(List<IdiomasPersonas> listaID) {
        try {
            for (int i = 0; i < listaID.size(); i++) {
                persistenciaIdiomasPersonas.crear(em, listaID.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearIdiomarPersonas Admi : " + e.toString());
        }
    }

    @Override
    public void borrarIdiomasPersonas(List<IdiomasPersonas> listaID) {
        try {
            for (int i = 0; i < listaID.size(); i++) {
                if (listaID.get(i).getIdioma().getSecuencia() == null) {
                    listaID.get(i).setIdioma(null);
                }
                persistenciaIdiomasPersonas.borrar(em, listaID.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearIdiomarPersonas Admi : " + e.toString());
        }
    }

    @Override
    public void editarIdiomasPersonas(List<IdiomasPersonas> listaID) {
        try {
            for (int i = 0; i < listaID.size(); i++) {
                if (listaID.get(i).getIdioma().getSecuencia() == null) {
                    listaID.get(i).setIdioma(null);
                }
                persistenciaIdiomasPersonas.editar(em, listaID.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearIdiomarPersonas Admi : " + e.toString());
        }
    }

    @Override
    public List<IdiomasPersonas> listIdiomasPersonas(BigInteger secuencia) { 
        try {
            List<IdiomasPersonas> lista = persistenciaIdiomasPersonas.idiomasPersona(em, secuencia);
            return lista;
        } catch (Exception e) {
            log.warn("Error listIdiomasPersonas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Idiomas> listIdiomas() {
        try {
            List<Idiomas> lista = persistenciaIdiomas.buscarIdiomas(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error lisIdiomas Admi : " + e.toString());
            return null;
        }
    }
    
    public Empleados empleadoActual(BigInteger secuencia){
        try{
            Empleados empl = persistenciaEmpleado.buscarEmpleado(em, secuencia);
            return empl;
        }catch(Exception e){
            log.warn("Error empleadoActual Admi : "+e.toString());
            return null;
        }
    }
}
