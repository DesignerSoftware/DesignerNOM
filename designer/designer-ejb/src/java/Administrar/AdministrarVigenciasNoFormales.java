/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.AdiestramientosNF;
import Entidades.Cursos;
import Entidades.Empleados;
import Entidades.Instituciones;
import Entidades.Personas;
import Entidades.VigenciasNoFormales;
import InterfaceAdministrar.AdministrarVigenciasNoFormalesInterface;
import InterfacePersistencia.PersistenciaAdiestramientosNFInterface;
import InterfacePersistencia.PersistenciaCursosInterface;
import InterfacePersistencia.PersistenciaInstitucionesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaVigenciasNoFormalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarVigenciasNoFormales implements AdministrarVigenciasNoFormalesInterface {

    @EJB
    PersistenciaVigenciasNoFormalesInterface persistenciaVigenciasNoFormales;
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    @EJB
    PersistenciaCursosInterface persistenciaCursos;
    @EJB
    PersistenciaInstitucionesInterface persistenciaInstituciones;
    @EJB
    PersistenciaAdiestramientosNFInterface persistenciaAdiestramientosNF;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private VigenciasNoFormales vNF;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<VigenciasNoFormales> vigenciasNoFormalesPersona(BigInteger secPersona) {
        try {
            return persistenciaVigenciasNoFormales.vigenciasNoFormalesPersona(em, secPersona);
        } catch (Exception e) {
            System.err.println("Error AdministrarVigenciasNoFormales.vigenciasNoFormalesPersona " + e);
            return null;
        }
    }

    @Override
    public Personas encontrarPersona(BigInteger secPersona) {
        return persistenciaPersonas.buscarPersonaSecuencia(em, secPersona);
    }

    //Listas de Valores Cursos, Instituciones, Adiestramiento
    @Override
    public List<Cursos> lovCursos() {
        return persistenciaCursos.cursos(em);
    }

    @Override
    public List<Instituciones> lovInstituciones() {
        return persistenciaInstituciones.instituciones(em);
    }

    @Override
    public List<AdiestramientosNF> lovAdiestramientosNF() {
        return persistenciaAdiestramientosNF.adiestramientosNF(em);
    }

    @Override
    public void modificarVigenciaNoFormal(List<VigenciasNoFormales> listaVigenciasNoFormalesModificar) {
        for (int i = 0; i < listaVigenciasNoFormalesModificar.size(); i++) {
            System.out.println("Modificando...");
            if (listaVigenciasNoFormalesModificar.get(i).getCurso().getSecuencia() == null) {
                listaVigenciasNoFormalesModificar.get(i).setCurso(null);
            }
            if (listaVigenciasNoFormalesModificar.get(i).getInstitucion().getSecuencia() == null) {
                listaVigenciasNoFormalesModificar.get(i).setInstitucion(null);
            }
//            if (listaVigenciasNoFormalesModificar.get(i).getAdiestramientonf().getSecuencia() == null) {
//                listaVigenciasNoFormalesModificar.get(i).setAdiestramientonf(null);
//            }
            persistenciaVigenciasNoFormales.editar(em, listaVigenciasNoFormalesModificar.get(i));
        }
    }

    @Override
    public void borrarVigenciaNoFormal(List<VigenciasNoFormales> listaVigenciasNoFormalesBorrar) {
        for (int i = 0; i < listaVigenciasNoFormalesBorrar.size(); i++) {
            System.out.println("Modificando...");
            if (listaVigenciasNoFormalesBorrar.get(i).getCurso().getSecuencia() == null) {
                listaVigenciasNoFormalesBorrar.get(i).setCurso(null);
            }
            if (listaVigenciasNoFormalesBorrar.get(i).getInstitucion().getSecuencia() == null) {
                listaVigenciasNoFormalesBorrar.get(i).setInstitucion(null);
            }
//            if (listaVigenciasNoFormalesBorrar.get(i).getAdiestramientonf().getSecuencia() == null) {
//                listaVigenciasNoFormalesBorrar.get(i).setAdiestramientonf(null);
//            }
            persistenciaVigenciasNoFormales.borrar(em, listaVigenciasNoFormalesBorrar.get(i));
        }
    }

    @Override
    public void crearVigenciaNoFormal(List<VigenciasNoFormales> listaVigenciasNoFormalesCrear) {
        for (int i = 0; i < listaVigenciasNoFormalesCrear.size(); i++) {
            System.out.println("Modificando...");
            if (listaVigenciasNoFormalesCrear.get(i).getCurso().getSecuencia() == null) {
                listaVigenciasNoFormalesCrear.get(i).setCurso(null);
            }
            if (listaVigenciasNoFormalesCrear.get(i).getInstitucion().getSecuencia() == null) {
                listaVigenciasNoFormalesCrear.get(i).setInstitucion(null);
            }
            if (listaVigenciasNoFormalesCrear.get(i).getAdiestramientonf().getSecuencia() == null) {
                listaVigenciasNoFormalesCrear.get(i).setAdiestramientonf(null);
                listaVigenciasNoFormalesCrear.get(i).setAcargo("S");
            } else {
                listaVigenciasNoFormalesCrear.get(i).setAcargo("N");
            }
            persistenciaVigenciasNoFormales.crear(em, listaVigenciasNoFormalesCrear.get(i));
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
