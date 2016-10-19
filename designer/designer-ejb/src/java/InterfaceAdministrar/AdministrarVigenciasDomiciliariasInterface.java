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

    public List<TiposEducaciones> lovTiposEducaciones();

    public List<Profesiones> lovProfesiones();

    public List<AdiestramientosF> lovAdiestramientos();

    public List<Instituciones> lovInstituciones();

    public List<MotivosRetiros> lovMotivosRetiros();

    public List<TiposFamiliares> lovTiposFamiliares();

    public List<Personas> lovPersonas();

    public List<SoAntecedentes> lovAntecedentes(BigInteger secTipoAntecedente);

    public List<SoTiposAntecedentes> lovTiposAntecedentes();

    public List<EstadosCiviles> lovVigenciasEstadosCiviles();

    public List<TiposTelefonos> lovTiposTelefonos();

    public List<Ciudades> lovCiduades();

    public List<Cargos> lovCargos();

    public Personas encontrarPersona(BigInteger secpersona);

    public Direcciones direccionesPersona(BigInteger secPersona);

    public Telefonos telefonoActualPersona(BigInteger secPersona);

    public VigenciasEstadosCiviles estadoCivilActualPersona(BigInteger secPersona);

    public Empleados buscarEmpleado(BigInteger secPersona);

    public List<SoAntecedentesMedicos> buscarAntecedentesMedicos(BigInteger secPersona);

    public List<Familiares> buscarFamiliares(BigInteger secPersona);

    public void crearPersona(Personas persona);

    public List<Telefonos> telefonosPersona(BigInteger secPersona);

    public List<Direcciones> consultarDireccionesPersona(BigInteger secPersona);
    
    public List<VigenciasEstadosCiviles> estadosCivilesPersona(BigInteger secPersona);
    
     public List<VigenciasFormales> vigenciasFormalesPersona(BigInteger secPersona);
     public HVHojasDeVida obtenerHojaVidaPersona(BigInteger secuencia);
      public List<HvExperienciasLaborales> experienciasLaboralesEmpleado(BigInteger secuencia);
      

}
