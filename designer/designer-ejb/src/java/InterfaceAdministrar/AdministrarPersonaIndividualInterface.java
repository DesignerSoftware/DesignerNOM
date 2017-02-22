/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.Comprobantes;
import Entidades.Contratos;
import Entidades.CortesProcesos;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.EstadosCiviles;
import Entidades.Estructuras;
import Entidades.JornadasLaborales;
import Entidades.MetodosPagos;
import Entidades.MotivosCambiosCargos;
import Entidades.MotivosCambiosSueldos;
import Entidades.MotivosContratos;
import Entidades.MotivosLocalizaciones;
import Entidades.NormasLaborales;
import Entidades.Papeles;
import Entidades.Periodicidades;
import Entidades.Personas;
import Entidades.Procesos;
import Entidades.ReformasLaborales;
import Entidades.Sets;
import Entidades.Sucursales;
import Entidades.Telefonos;
import Entidades.TercerosSucursales;
import Entidades.TiposContratos;
import Entidades.TiposDocumentos;
import Entidades.TiposEntidades;
import Entidades.TiposSueldos;
import Entidades.TiposTelefonos;
import Entidades.TiposTrabajadores;
import Entidades.UbicacionesGeograficas;
import Entidades.Unidades;
import Entidades.VWValidaBancos;
import Entidades.VigenciasAfiliaciones;
import Entidades.VigenciasCargos;
import Entidades.VigenciasContratos;
import Entidades.VigenciasEstadosCiviles;
import Entidades.VigenciasFormasPagos;
import Entidades.VigenciasJornadas;
import Entidades.VigenciasLocalizaciones;
import Entidades.VigenciasNormasEmpleados;
import Entidades.VigenciasReformasLaborales;
import Entidades.VigenciasSueldos;
import Entidades.VigenciasTiposContratos;
import Entidades.VigenciasTiposTrabajadores;
import Entidades.VigenciasUbicaciones;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface AdministrarPersonaIndividualInterface {

    public void obtenerConexion(String idSesion);

    public void crearPersona(Personas persona);

    public List<Empleados> lovEmpleados();

    public List<TiposTelefonos> lovTiposTelefonos();

    public List<EstadosCiviles> lovEstadosCiviles();

    public List<TercerosSucursales> lovTercerosSucursales(BigInteger secuencia);

    public List<MetodosPagos> lovMetodosPagos();

    public List<Sucursales> lovSucursales();

    public List<Periodicidades> lovPeriodicidades();

    public List<Unidades> lovUnidades();

    public List<MotivosCambiosSueldos> lovMotivosCambiosSueldos();

    public List<MotivosContratos> lovMotivosContratos();

    public List<Papeles> lovPapeles();

    public List<JornadasLaborales> lovJornadasLaborales();

    public List<UbicacionesGeograficas> lovUbicacionesGeograficas(BigInteger secuencia);

    public List<MotivosLocalizaciones> lovMotivosLocalizaciones();

    public List<Estructuras> lovEstructurasModCargos(BigInteger secEmpresa, Date fechaIngreso);

    public List<Estructuras> lovEstructurasModCentroCosto(BigInteger secEmpresa);

    public List<MotivosCambiosCargos> lovMotivosCambiosCargos();

    public List<Cargos> lovCargos();

    public List<Ciudades> lovCiudades();

    public List<TiposDocumentos> lovTiposDocumentos();

    public List<Empresas> lovEmpresas();

    public List<TiposTrabajadores> lovTiposTrabajadores();

    public List<TiposSueldos> lovTiposSueldosValidos(BigInteger secTT);

    public List<Contratos> lovContratosValidos(BigInteger secTT);

    public List<NormasLaborales> lovNormasLaboralesValidos(BigInteger secTT);

    public List<ReformasLaborales> lovReformasLaboralesValidos(BigInteger secTT);

    public List<TiposContratos> lovTiposContratosValidos(BigInteger secTT);
    
    public Empresas obtenerEmpresa(BigInteger secEmpresa);

    public TiposEntidades buscarTipoEntidadPorCodigo(Short codigo);

    public String buscarCodigoSCTercero(BigInteger secuencia);

    public String buscarCodigoSSTercero(BigInteger secuencia);

    public String buscarCodigoSPTercero(BigInteger secuencia);

    public BigInteger calcularNumeroEmpleadosEmpresa(BigInteger secuencia);

    public BigInteger obtenerMaximoEmpleadosEmpresa(BigInteger secuencia);

    public Personas buscarPersonaPorNumeroDocumento(BigInteger numeroDocumento);

    public Empleados buscarEmpleadoPorCodigoyEmpresa(BigDecimal codigo, BigInteger empresa);

    public String obtenerPreValidadContabilidad();

    public String obtenerPreValidaBloqueAIngreso();

    public VWValidaBancos validarCodigoPrimarioVWValidaBancos(BigInteger documento);

    public String validarTipoTrabajadorReformaLaboral(BigInteger tipoTrabajador, BigInteger reformaLaboral);

    public String validarTipoTrabajadorTipoSueldo(BigInteger tipoTrabajador, BigInteger tipoSueldo);

    public String validarTipoTrabajadorTipoContrato(BigInteger tipoTrabajador, BigInteger tipoContrato);

    public String validarTipoTrabajadorNormaLaboral(BigInteger tipoTrabajador, BigInteger normaLaboral);

    public String validarTipoTrabajadorContrato(BigInteger tipoTrabajador, BigInteger contrato);

    public String obtenerCheckIntegralReformaLaboral(BigInteger reformaLaboral);

    public void crearNuevaPersona(Personas persona);

    public Personas obtenerUltimoRegistroPersona(BigInteger documento);

    public Empleados crearEmpl_Con_VCargo( BigDecimal codigoEmpleado, BigInteger secPersona, BigInteger secEmpresa, VigenciasCargos vigenciaCargo);

    public VigenciasCargos obtenerUltimaVigenciaCargo(BigInteger empresa, BigInteger secEmpleado);
    
    public void modificarVigenciaCargo(VigenciasCargos vigencia);

    public boolean crearVigenciaLocalizacion(VigenciasLocalizaciones vigencia);

    public boolean crearVigenciaTipoTrabajador(VigenciasTiposTrabajadores vigencia);

    public boolean crearVigenciaReformaLaboral(VigenciasReformasLaborales vigencia);

    public boolean crearVigenciaSueldo(VigenciasSueldos vigencia);

    public boolean crearVigenciaTipoContrato(VigenciasTiposContratos vigencia);

    public boolean crearVigenciaNormaEmpleado(VigenciasNormasEmpleados vigencia);

    public boolean crearVigenciaContrato(VigenciasContratos vigencia);

    public boolean crearVigenciaUbicacion(VigenciasUbicaciones vigencia);

    public boolean crearVigenciaJornada(VigenciasJornadas vigencia);

    public boolean crearVigenciaFormaPago(VigenciasFormasPagos vigencia);

    public boolean crearVigenciaAfiliacion(VigenciasAfiliaciones vigencia);

    public boolean crearEstadoCivil(VigenciasEstadosCiviles estado);

    public boolean crearDireccion(Direcciones direccion);

    public boolean crearTelefono(Telefonos telefono);

    public boolean crearSets(Sets set);

    public Procesos buscarProcesoPorCodigo(short codigo);

    public BigDecimal obtenerNumeroMaximoComprobante();

    public boolean crearComprobante(Comprobantes comprobante);

    public Comprobantes buscarComprobanteParaPrimerRegistroEmpleado(BigInteger secEmpleado);
    
    public boolean crearCortesProcesos(CortesProcesos corte);
    
    public TiposTrabajadores buscarTipoTrabajadorPorCodigo(short codigo);

    public List<Cargos> lovCargosXEmpresa(BigInteger empresa);
    
    public TercerosSucursales consultarARL(BigInteger secEmpresa);
    
    public Date consultarFechaHastaCausado();
    
    public boolean eliminarEmpleadoCompleto(BigInteger secEmpleado, BigInteger secPersona);
   
}