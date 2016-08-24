/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.AdiestramientosF;
import Entidades.Empleados;
import Entidades.Instituciones;
import Entidades.Personas;
import Entidades.Profesiones;
import Entidades.TiposEducaciones;
import Entidades.VigenciasFormales;
import InterfaceAdministrar.AdministrarVigenciasFormalesInterface;
import InterfacePersistencia.PersistenciaAdiestramientosFInterface;
import InterfacePersistencia.PersistenciaInstitucionesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaProfesionesInterface;
import InterfacePersistencia.PersistenciaTiposEducacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasFormalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarVigenciasFormales implements AdministrarVigenciasFormalesInterface {

    @EJB
    PersistenciaVigenciasFormalesInterface persistenciaVigenciasFormales;
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    @EJB
    PersistenciaTiposEducacionesInterface persistenciaTiposEducaciones;
    @EJB
    PersistenciaProfesionesInterface persistenciaProfesiones;
    @EJB
    PersistenciaInstitucionesInterface persistenciaInstituciones;
    @EJB
    PersistenciaAdiestramientosFInterface persistenciaAdiestramientosF;
     @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    
    private VigenciasFormales vF;
    private EntityManager em;
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<VigenciasFormales> vigenciasFormalesPersona(BigInteger secPersona) {
        try {
            return persistenciaVigenciasFormales.vigenciasFormalesPersona(em, secPersona);
        } catch (Exception e) {
            System.err.println("Error AdministrarVigenciasFormales.vigenciasFormalesPersona " + e);
            return null;
        }
    }

    @Override
    public Personas encontrarPersona(BigInteger secPersona) {
        return persistenciaPersonas.buscarPersonaSecuencia(em, secPersona);
    }

    //Listas de Valores Educacion, Profesion, Instituciones, Adiestramiento
    @Override
    public List<TiposEducaciones> lovTiposEducaciones() {
        return persistenciaTiposEducaciones.tiposEducaciones(em);
    }

    @Override
    public List<Profesiones> lovProfesiones() {
        return persistenciaProfesiones.profesiones(em);
    }

    @Override
    public List<Instituciones> lovInstituciones() {
        return persistenciaInstituciones.instituciones(em);
    }
    
    @Override
    public List<AdiestramientosF> lovAdiestramientosF() {
        return persistenciaAdiestramientosF.adiestramientosF(em);
    }
    
        @Override
    public void modificarVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesModificar) {
        for (int i = 0; i < listaVigenciasFormalesModificar.size(); i++) {
            System.out.println("Modificando...");
            if (listaVigenciasFormalesModificar.get(i).getTipoeducacion().getSecuencia() == null) {
                listaVigenciasFormalesModificar.get(i).setTipoeducacion(null);
                vF = listaVigenciasFormalesModificar.get(i);
            } else {
                vF = listaVigenciasFormalesModificar.get(i);
            }
            if (listaVigenciasFormalesModificar.get(i).getProfesion().getSecuencia() == null) {
                listaVigenciasFormalesModificar.get(i).setProfesion(null);
                vF = listaVigenciasFormalesModificar.get(i);
            } else {
                vF = listaVigenciasFormalesModificar.get(i);
            }
            if (listaVigenciasFormalesModificar.get(i).getInstitucion().getSecuencia() == null) {
                listaVigenciasFormalesModificar.get(i).setInstitucion(null);
                vF = listaVigenciasFormalesModificar.get(i);
            } else {
                vF = listaVigenciasFormalesModificar.get(i);
            }
            if (listaVigenciasFormalesModificar.get(i).getAdiestramientof().getSecuencia() == null) {
                listaVigenciasFormalesModificar.get(i).setAdiestramientof(null);
                vF = listaVigenciasFormalesModificar.get(i);
            } else {
                vF = listaVigenciasFormalesModificar.get(i);
            }
            
            
            persistenciaVigenciasFormales.editar(em, vF);
        }
    }

    @Override
    public void borrarVigenciaFormal(VigenciasFormales vigenciasFormales) {
        persistenciaVigenciasFormales.borrar(em, vigenciasFormales);
    }


    @Override
    public void crearVigenciaFormal(VigenciasFormales vigenciasFormales) {
        persistenciaVigenciasFormales.crear(em, vigenciasFormales);
    }
    
    @Override
    public Empleados empleadoActual(BigInteger secuenciaE){
        try{
        Empleados retorno = persistenciaEmpleado.buscarEmpleado(em, secuenciaE);
        return retorno;
        }catch(Exception  e){
            System.out.println("Error empleadoActual Admi : "+e.toString());
            return null;
        }
    }
    
}
