/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.AdiestramientosF;
import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.EstadosCiviles;
import Entidades.Familiares;
import Entidades.HVHojasDeVida;
import Entidades.HvExperienciasLaborales;
import Entidades.Instituciones;
import Entidades.MotivosRetiros;
import Entidades.Personas;
import Entidades.Profesiones;
import Entidades.SoAntecedentes;
import Entidades.SoAntecedentesMedicos;
import Entidades.SoTiposAntecedentes;
import Entidades.Telefonos;
import Entidades.TiposEducaciones;
import Entidades.TiposFamiliares;
import Entidades.TiposTelefonos;
import Entidades.VigenciasDomiciliarias;
import Entidades.VigenciasEstadosCiviles;
import Entidades.VigenciasFormales;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVigenciasDomiciliariasInterface;
import InterfacePersistencia.PersistenciaAdiestramientosFInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaDireccionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaFamiliaresInterface;
import InterfacePersistencia.PersistenciaHVHojasDeVidaInterface;
import InterfacePersistencia.PersistenciaHvExperienciasLaboralesInterface;
import InterfacePersistencia.PersistenciaInstitucionesInterface;
import InterfacePersistencia.PersistenciaMotivosRetirosInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaProfesionesInterface;
import InterfacePersistencia.PersistenciaSoAntecedentesInterface;
import InterfacePersistencia.PersistenciaSoAntecedentesMedicosInterface;
import InterfacePersistencia.PersistenciaSoTiposAntecedentesInterface;
import InterfacePersistencia.PersistenciaTelefonosInterface;
import InterfacePersistencia.PersistenciaTiposEducacionesInterface;
import InterfacePersistencia.PersistenciaTiposFamiliaresInterface;
import InterfacePersistencia.PersistenciaTiposTelefonosInterface;
import InterfacePersistencia.PersistenciaVigenciasDomiciliariasInterface;
import InterfacePersistencia.PersistenciaVigenciasEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaVigenciasFormalesInterface;
import Persistencia.PersistenciaAdiestramientosF;
import Persistencia.PersistenciaEmpleados;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarVigenciasDomiciliarias implements AdministrarVigenciasDomiciliariasInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaVigenciasDomiciliariasInterface persistenciaVigenciasDomiciliarias;
    @EJB
    PersistenciaCargosInterface persistenciaCargos;
    @EJB
    PersistenciaTiposEducacionesInterface persistenciaTiposEducaciones;
    @EJB
    PersistenciaProfesionesInterface persistenciaProfesiones;
    @EJB
    PersistenciaAdiestramientosFInterface persistenciaAdiestramientos;
    @EJB
    PersistenciaInstitucionesInterface persistenciaInstituciones;
    @EJB
    PersistenciaMotivosRetirosInterface persistenciaMotivosRetiros;
    @EJB
    PersistenciaTiposFamiliaresInterface persistenciaTiposFamiliares;
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    @EJB
    PersistenciaSoAntecedentesInterface persistenciaAntecedentes;
    @EJB
    PersistenciaSoTiposAntecedentesInterface persistenciaTiposAntecedentes;
    @EJB
    PersistenciaEstadosCivilesInterface persistenciaEstadosCiviles;
    @EJB
    PersistenciaTiposTelefonosInterface persistenciaTiposTelefonos;
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
    @EJB
    PersistenciaDireccionesInterface persistenciaDirecciones;
    @EJB
    PersistenciaTelefonosInterface persistenciaTelefonos;
    @EJB
    PersistenciaVigenciasEstadosCivilesInterface persistenciaVigEstadosCiviles;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaSoAntecedentesMedicosInterface persistenciaAntecdentesM;
    @EJB
    PersistenciaFamiliaresInterface persistenciaFamiliares;
    @EJB
    PersistenciaVigenciasFormalesInterface persistenciaVigenciasFormales;
    @EJB
    PersistenciaHVHojasDeVidaInterface persistenciahv;
    @EJB
    PersistenciaHvExperienciasLaboralesInterface persistenciaHvExp;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void modificarVigencia(List<VigenciasDomiciliarias> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaVigenciasDomiciliarias.editar(em, listaModificar.get(i));
        }
    }

    @Override
    public void crearVigencia(List<VigenciasDomiciliarias> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaVigenciasDomiciliarias.crear(em, listaCrear.get(i));
        }
    }

    @Override
    public void borrarVigencia(List<VigenciasDomiciliarias> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaVigenciasDomiciliarias.borrar(em, listaBorrar.get(i));
        }
    }

    @Override
    public List<VigenciasDomiciliarias> vigenciasDomiciliariasporPersona(BigInteger secPersona) {
        List<VigenciasDomiciliarias> listVigencias = persistenciaVigenciasDomiciliarias.visitasDomiciliariasPersona(em, secPersona);
        return listVigencias;
    }

    @Override
    public List<TiposEducaciones> lovTiposEducaciones() {
        List<TiposEducaciones> lovTiposEducaciones = persistenciaTiposEducaciones.tiposEducaciones(em);
        return lovTiposEducaciones;
    }

    @Override
    public List<Profesiones> lovProfesiones() {
        List<Profesiones> lovProfesiones = persistenciaProfesiones.profesiones(em);
        return lovProfesiones;
    }

    @Override
    public List<AdiestramientosF> lovAdiestramientos() {
        List<AdiestramientosF> lovAdiestramientosF = persistenciaAdiestramientos.adiestramientosF(em);
        return lovAdiestramientosF;
    }

    @Override
    public List<Instituciones> lovInstituciones() {
        List<Instituciones> lovInstituciones = persistenciaInstituciones.lovInstituciones(em);
        return lovInstituciones;
    }

    @Override
    public List<MotivosRetiros> lovMotivosRetiros() {
        List<MotivosRetiros> lovMotivosRetiros = persistenciaMotivosRetiros.consultarMotivosRetiros(em);
        return lovMotivosRetiros;
    }

    @Override
    public List<TiposFamiliares> lovTiposFamiliares() {
        List<TiposFamiliares> lovTiposFamiliares = persistenciaTiposFamiliares.buscarTiposFamiliares(em);
        return lovTiposFamiliares;
    }

    @Override
    public List<Personas> lovPersonas() {
        List<Personas> lovPersonas = persistenciaPersonas.consultarPersonas(em);
        return lovPersonas;
    }

    @Override
    public List<SoAntecedentes> lovAntecedentes(BigInteger secTipoAntecedente) {
        List<SoAntecedentes> lovAntecedentes = persistenciaAntecedentes.lovAntecedentes(em, secTipoAntecedente);
        return lovAntecedentes;
    }

    @Override
    public List<SoTiposAntecedentes> lovTiposAntecedentes() {
        List<SoTiposAntecedentes> lovTiposAntecedentes = persistenciaTiposAntecedentes.lovTiposAntecedentes(em);
        return lovTiposAntecedentes;
    }

    @Override
    public List<EstadosCiviles> lovVigenciasEstadosCiviles() {
        List<EstadosCiviles> lovEstadosCiviles = persistenciaEstadosCiviles.consultarEstadosCiviles(em);
        return lovEstadosCiviles;
    }

    @Override
    public List<TiposTelefonos> lovTiposTelefonos() {
        List<TiposTelefonos> lovTiposTelefonos = persistenciaTiposTelefonos.tiposTelefonos(em);
        return lovTiposTelefonos;
    }

    @Override
    public List<Ciudades> lovCiduades() {
        List<Ciudades> lovCiudades = persistenciaCiudades.lovCiudades(em);
        return lovCiudades;
    }

    @Override
    public List<Cargos> lovCargos() {
        List<Cargos> lovCargos = persistenciaCargos.lovCargos(em);
        return lovCargos;
    }

    @Override
    public Personas encontrarPersona(BigInteger secpersona) {
        Personas persona = persistenciaPersonas.buscarPersona(em, secpersona);
        return persona;
    }

    @Override
    public Direcciones direccionesPersona(BigInteger secPersona) {
        Direcciones direccion = persistenciaDirecciones.direccionActualPersona(em, secPersona);
        return direccion;
    }

    @Override
    public Telefonos telefonoActualPersona(BigInteger secPersona) {
        Telefonos telefono = persistenciaTelefonos.telefonoActual(em, secPersona);
        return telefono;
    }

    @Override
    public VigenciasEstadosCiviles estadoCivilActualPersona(BigInteger secPersona) {
        VigenciasEstadosCiviles vigestadoC = persistenciaVigEstadosCiviles.estadoCivilActual(em, secPersona);
        return vigestadoC;
    }

    @Override
    public Empleados buscarEmpleado(BigInteger secPersona) {
        Empleados empleado = persistenciaEmpleados.buscarEmpleadoSecuenciaPersona(em, secPersona);
        return empleado;
    }

    @Override
    public List<SoAntecedentesMedicos> buscarAntecedentesMedicos(BigInteger secPersona) {
        List<SoAntecedentesMedicos> listAntecedentesM = persistenciaAntecdentesM.listaAntecedentesMedicos(em, secPersona);
        return listAntecedentesM;
    }

    @Override
    public List<Familiares> buscarFamiliares(BigInteger secPersona) {
        List<Familiares> listFamiliares = persistenciaFamiliares.familiaresPersona(em, secPersona);
        return listFamiliares;
    }

    @Override
    public void crearPersona(Personas persona) {
        persistenciaPersonas.crear(em, persona);
    }

    @Override
    public List<Telefonos> telefonosPersona(BigInteger secPersona) {
        List<Telefonos> listTelefonos = persistenciaTelefonos.telefonosPersona(em, secPersona);
        return listTelefonos;
    }

    @Override
    public List<Direcciones> consultarDireccionesPersona(BigInteger secPersona) {
        List<Direcciones> listDirecciones = persistenciaDirecciones.direccionPersona(em, secPersona);
        return listDirecciones;
    }

    @Override
    public List<VigenciasEstadosCiviles> estadosCivilesPersona(BigInteger secPersona) {
        List<VigenciasEstadosCiviles> listVigEstadosCiviles = persistenciaVigEstadosCiviles.consultarVigenciasEstadosCivilesPorPersona(em, secPersona);
        return listVigEstadosCiviles;
    }

    @Override
    public List<VigenciasFormales> vigenciasFormalesPersona(BigInteger secPersona) {
        List<VigenciasFormales> listEducacion = persistenciaVigenciasFormales.vigenciasFormalesPersona(em, secPersona);
        return listEducacion;
    }

    @Override
    public HVHojasDeVida obtenerHojaVidaPersona(BigInteger secuencia) {
        HVHojasDeVida hojaVida = persistenciahv.hvHojaDeVidaPersona(em, secuencia);
        return hojaVida;
    }

    @Override
    public List<HvExperienciasLaborales> experienciasLaboralesEmpleado(BigInteger secuencia) {
        List<HvExperienciasLaborales> listExp = persistenciaHvExp.experienciasLaboralesSecuenciaEmpleado(em, secuencia);
        return listExp;
    }

}
