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
            }
            if (listaVigenciasFormalesModificar.get(i).getProfesion().getSecuencia() == null) {
                listaVigenciasFormalesModificar.get(i).setProfesion(null);
            }
            if (listaVigenciasFormalesModificar.get(i).getInstitucion().getSecuencia() == null) {
                listaVigenciasFormalesModificar.get(i).setInstitucion(null);
            }
            if (listaVigenciasFormalesModificar.get(i).getAdiestramientof().getSecuencia() == null) {
                listaVigenciasFormalesModificar.get(i).setAdiestramientof(null);
            }
            persistenciaVigenciasFormales.editar(em, listaVigenciasFormalesModificar.get(i));
        }
    }

    @Override
    public void borrarVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesBorrar) {
        for (int i = 0; i < listaVigenciasFormalesBorrar.size(); i++) {
            System.out.println("Borrando...");
            if (listaVigenciasFormalesBorrar.get(i).getTipoeducacion().getSecuencia() == null) {
                listaVigenciasFormalesBorrar.get(i).setTipoeducacion(null);
            }
            if (listaVigenciasFormalesBorrar.get(i).getProfesion().getSecuencia() == null) {
                listaVigenciasFormalesBorrar.get(i).setProfesion(null);
            }
            if (listaVigenciasFormalesBorrar.get(i).getInstitucion().getSecuencia() == null) {
                listaVigenciasFormalesBorrar.get(i).setInstitucion(null);
            }
//            if (listaVigenciasFormalesBorrar.get(i).getAdiestramientof().getSecuencia() == null) {
//                listaVigenciasFormalesBorrar.get(i).setAdiestramientof(null);
//            }
            persistenciaVigenciasFormales.borrar(em, listaVigenciasFormalesBorrar.get(i));
        }
    }

    @Override
    public void crearVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesCrear) {
        for (int i = 0; i < listaVigenciasFormalesCrear.size(); i++) {
            System.out.println("Modificando...");
            if (listaVigenciasFormalesCrear.get(i).getTipoeducacion().getSecuencia() == null) {
                listaVigenciasFormalesCrear.get(i).setTipoeducacion(null);
            }
            if (listaVigenciasFormalesCrear.get(i).getProfesion().getSecuencia() == null) {
                listaVigenciasFormalesCrear.get(i).setProfesion(null);
            }
            if (listaVigenciasFormalesCrear.get(i).getInstitucion().getSecuencia() == null) {
                listaVigenciasFormalesCrear.get(i).setInstitucion(null);
            }
            if (listaVigenciasFormalesCrear.get(i).getAdiestramientof().getSecuencia() == null) {
                listaVigenciasFormalesCrear.get(i).setAdiestramientof(null);
            }
            if (listaVigenciasFormalesCrear.get(i).getNumerotarjeta() != null) {
                listaVigenciasFormalesCrear.get(i).setTarjetaprofesional("S");
            } else {
                listaVigenciasFormalesCrear.get(i).setTarjetaprofesional("N");
            }

            if (listaVigenciasFormalesCrear.get(i).getAdiestramientof().getDescripcion() != null) {
                listaVigenciasFormalesCrear.get(i).setAcargo("S");
            } else {
                listaVigenciasFormalesCrear.get(i).setAcargo("N");
            }
            persistenciaVigenciasFormales.crear(em, listaVigenciasFormalesCrear.get(i));
        }
    }

    @Override
    public Empleados empleadoActual(BigInteger secuenciaE) {
        try {
            Empleados retorno = persistenciaEmpleado.buscarEmpleado(em, secuenciaE);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error empleadoActual Admi : " + e.toString());
            return null;
        }
    }

}
