/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

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
import Entidades.SectoresEconomicos;
import Entidades.SoAntecedentes;
import Entidades.SoAntecedentesMedicos;
import Entidades.SoTiposAntecedentes;
import Entidades.Telefonos;
import Entidades.TiposDocumentos;
import Entidades.TiposEducaciones;
import Entidades.TiposFamiliares;
import Entidades.TiposTelefonos;
import Entidades.VigenciasDomiciliarias;
import Entidades.VigenciasEstadosCiviles;
import Entidades.VigenciasFormales;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarVigenciasDomiciliariasInterface {

    public void obtenerConexion(String idSesion);

    public void modificarVigencia(List<VigenciasDomiciliarias> listaModificar);

    public void crearVigencia(List<VigenciasDomiciliarias> listaCrear);

    public void borrarVigencia(List<VigenciasDomiciliarias> listaBorrar);

    public List<VigenciasDomiciliarias> vigenciasDomiciliariasporPersona(BigInteger secPersona);

    public VigenciasDomiciliarias vigenciaDomiciliariaActual(BigInteger secPersona);
    
    public List<EstadosCiviles> lovVigenciasEstadosCiviles();

    public List<TiposTelefonos> lovTiposTelefonos();

    public List<Ciudades> lovCiudades();

    public List<Cargos> lovCargos();

    public Telefonos telefonoActualPersona(BigInteger secPersona);

    public VigenciasEstadosCiviles estadoCivilActualPersona(BigInteger secPersona);

    public Empleados buscarEmpleado(BigInteger secPersona);

//familiares
    public List<Familiares> buscarFamiliares(BigInteger secPersona);

    public void crearPersona(Personas persona);

    public void modificarFamiliares(List<Familiares> listaModificar);

    public void borrarFamiliares(List<Familiares> listaBorrar);

    public void crearFamilares(List<Familiares> listaCrear);

    public List<TiposFamiliares> lovTiposFamiliares();

    public List<Personas> lovPersonas();

    public Personas encontrarPersona(BigInteger secpersona);
    
    public List<TiposDocumentos> consultarTiposDocumentos();
    
    ///direcciones
    public void crearDirecciones(List<Direcciones> listaCrear);

    public void borrarDirecciones(List<Direcciones> listaBorrar);

    public void modificarDirecciones(List<Direcciones> listaModificar);

    public List<Direcciones> direccionesPersona(BigInteger secPersona);

    public List<Direcciones> consultarDireccionesPersona(BigInteger secPersona);

    //telefonos
    public List<Telefonos> telefonosPersona(BigInteger secPersona);

    public void crearTelefonos(Telefonos telefono);

    public void borrarTelefonos(Telefonos telefono);

    public void modificarTelefonos(List<Telefonos> listModificar);

    // estados civiles
    public List<VigenciasEstadosCiviles> estadosCivilesPersona(BigInteger secPersona);

    public void modificarVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles);

    public void borrarVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles);

    public void crearVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles);

    // ANTECEDENTES MEDICOS
    public List<SoAntecedentes> lovAntecedentes(BigInteger secTipoAntecedente);

    public List<SoTiposAntecedentes> lovTiposAntecedentes();

    public List<SoAntecedentesMedicos> buscarAntecedentesMedicos(BigInteger secPersona);

    public void crearAntecedenteM(List<SoAntecedentesMedicos> listaCrear);

    public void borrarAntecedenteM(List<SoAntecedentesMedicos> listaBorrar);

    public void modificarAntecedenteM(List<SoAntecedentesMedicos> listaModificar);

    //educacion formal
    public List<VigenciasFormales> vigenciasFormalesPersona(BigInteger secPersona);

    public List<TiposEducaciones> lovTiposEducaciones();

    public List<Profesiones> lovProfesiones();

    public List<AdiestramientosF> lovAdiestramientos();

    public List<Instituciones> lovInstituciones();

    public void modificarVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesModificar);

    public void borrarVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesBorrar);

    public void crearVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesCrear);

////exp laboral
    public HVHojasDeVida obtenerHojaVidaPersona(BigInteger secuencia);
    
    public void editarHojadeVida(List<HVHojasDeVida> listaEditar);
    
    public void editarPersona(List<Personas> listaEditar);
    
    public List<HvExperienciasLaborales> experienciasLaboralesEmpleado(BigInteger secuencia);

    public void crearExperienciaLaboral(List<HvExperienciasLaborales> listHEL);

    public void editarExperienciaLaboral(List<HvExperienciasLaborales> listHEL);

    public void borrarExperienciaLaboral(List<HvExperienciasLaborales> listHEL);

    public List<MotivosRetiros> lovMotivosRetiros();

    public List<SectoresEconomicos> lovSectoresEconomicos();
}
