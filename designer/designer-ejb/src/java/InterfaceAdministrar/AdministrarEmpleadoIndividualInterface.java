package InterfaceAdministrar;

import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.Demandas;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.Encargaturas;
import Entidades.EvalResultadosConv;
import Entidades.Familiares;
import Entidades.Generales;
import Entidades.HVHojasDeVida;
import Entidades.HvEntrevistas;
import Entidades.HvExperienciasLaborales;
import Entidades.HvReferencias;
import Entidades.IdiomasPersonas;
import Entidades.InformacionesAdicionales;
import Entidades.Personas;
import Entidades.Telefonos;
import Entidades.TiposDocumentos;
import Entidades.VigenciasAficiones;
import Entidades.VigenciasDeportes;
import Entidades.VigenciasDomiciliarias;
import Entidades.VigenciasEstadosCiviles;
import Entidades.VigenciasEventos;
import Entidades.VigenciasFormales;
import Entidades.VigenciasIndicadores;
import Entidades.VigenciasProyectos;
import java.math.BigInteger;
import java.util.List;

public interface AdministrarEmpleadoIndividualInterface {

    public HVHojasDeVida hvHojaDeVidaPersona(BigInteger secPersona);

    public Telefonos primerTelefonoPersona(BigInteger secEmpleado);

    public Direcciones primeraDireccionPersona(BigInteger secEmpleado);

    public VigenciasEstadosCiviles estadoCivilPersona(BigInteger secEmpleado);

    public InformacionesAdicionales informacionAdicionalPersona(BigInteger secEmpleado);

    public Encargaturas reemplazoPersona(BigInteger secEmpleado);

    public VigenciasFormales educacionPersona(BigInteger secPersona);

    public IdiomasPersonas idiomasPersona(BigInteger secPersona);

    public VigenciasProyectos proyectosPersona(BigInteger secEmpleado);

    public HvReferencias referenciasPersonalesPersona(BigInteger secHv);

    public HvReferencias referenciasFamiliaresPersona(BigInteger secHv);

    public HvExperienciasLaborales experienciaLaboralPersona(BigInteger secHv);

    public VigenciasEventos eventosPersona(BigInteger secEmpl);

    public VigenciasDeportes deportesPersona(BigInteger secPersona);

    public VigenciasAficiones aficionesPersona(BigInteger secPersona);

    //public Familiares familiaresPersona(BigInteger secPersona);
    public String consultaFamiliaresPersona(BigInteger secPersona);

    public HvEntrevistas entrevistasPersona(BigInteger secHv);

    public VigenciasIndicadores indicadoresPersona(BigInteger secEmpl);

    public Demandas demandasPersona(BigInteger secEmpl);

    public VigenciasDomiciliarias visitasDomiciliariasPersona(BigInteger secPersona);

    public EvalResultadosConv pruebasAplicadasPersona(BigInteger secEmpleado);

    public List<TiposDocumentos> tiposDocumentos();

    public List<Ciudades> ciudades();

    public List<Cargos> cargos();

    public Empleados buscarEmpleado(BigInteger secuencia);

    public void modificarEmpleado(Empleados empleado);

    public void modificarHojaDeVida(HVHojasDeVida hojaVida);

    public void modificarPersona(Personas personas);

    public void actualizarFotoPersona(Personas persona);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public Generales obtenerRutaFoto();

    public String fotoEmpleado(Empleados empleado);

    public Personas encontrarPersona(BigInteger secPersona);

    public Personas obtenerPersonaPorEmpleado(BigInteger secPersona);

    public String consultarPrimerTelefonoPersona(BigInteger secPersona);

    public String consultarPrimeraDireccionPersona(BigInteger secPersona);

    public String consultarPrimerEstadoCivilPersona(BigInteger secPersona);

    public String consultarPrimeraInformacionAd(BigInteger secPersona);

    public String consultarPrimerReemplazo(BigInteger secPersona);

    public String consultarPrimeraVigenciaFormal(BigInteger secPersona);

    public String consultarPimerIdioma(BigInteger secPersona);

    public String consultarPrimerProyecto(BigInteger secPersona);
    
    public String consultarPrimerEvento(BigInteger secPersona);
    
    public String consultarPrimerDeporte(BigInteger secPersona);
    
    public String consultarPrimeraAficion(BigInteger secPersona);
    
    public String consultarPrimerFamiliar(BigInteger secPersona);
    
    public String consultarPrimerIndicador(BigInteger secPersona);
    
    public String consultarPrimeraDemanda(BigInteger Persona);
    
    public String consultarPrimeraVisita(BigInteger secPersona);
    
    public String consultarPrimeraEntrevista(BigInteger secHV);
    
    public String consultarPrimeraReferenciaF(BigInteger secHV);
    
    public String consultarPrimeraReferenciaP(BigInteger secHV);
    
    public String consultarPrimeraExpLaboral(BigInteger secHv);
    
    public String consultarPrimeraPrueba(BigInteger secPersona);

}
