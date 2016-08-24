package ClasesAyuda;

import Entidades.*;
import java.math.BigInteger;
import java.util.Date;

public class ParametrosBusquedaPersonal {

    private Empleados empleado;
    private EstadosCiviles estadoCivil;
    private IdiomasPersonas idiomaPersona;
    private VigenciasIndicadores vigenciaIndicador;
    private VigenciasFormales vigenciaFormal;
    private VigenciasNoFormales vigenciaNoFormal;
    private HvExperienciasLaborales hvExperienciaLaboral;
    private VigenciasProyectos vigenciaProyecto;
    private Cargos cargo;
    //FECHAS
    private Date fechaInicialDatosPersonales;
    private Date fechaFinalDatosPersonales;
    private Date fechaInicialEstadoCivil;
    private Date fechaFinalEstadoCivil;
    private Date fechaInicialCenso;
    private Date fechaFinalCenso;
    private Date fechaInicialFormal;
    private Date fechaFinalFormal;
    private Date fechaInicialNoFormal;
    private Date fechaFinalNoFormal;
    private Date fechaInicialExperienciaLaboral;
    private Date fechaFinalExperienciaLaboral;
    private Date fechaInicialProyecto;
    private Date fechaFinalProyecto;
    
    //VALORES
    private BigInteger conversacionDesde;
    private BigInteger conversacionHasta;
    private BigInteger lecturaDesde;
    private BigInteger lecturaHasta;
    private BigInteger escrituraDesde;
    private BigInteger escrituraHasta;
    
    private String desarrolladoEducacionFormal;
    private String desarrolladoEducacionNoFormal;
    private String empresaExperienciaLaboral;
    private String cargoExperienciaLaboral;

    public ParametrosBusquedaPersonal() {
        empleado = new Empleados();
        empleado.setPersona(new Personas());
        estadoCivil = new EstadosCiviles();
        idiomaPersona = new IdiomasPersonas();
        vigenciaIndicador = new VigenciasIndicadores();
        vigenciaFormal = new VigenciasFormales();
        vigenciaNoFormal = new VigenciasNoFormales();
        hvExperienciaLaboral = new HvExperienciasLaborales();
        vigenciaProyecto = new VigenciasProyectos();
        cargo = new Cargos();
    }

    public Cargos getCargo() {
        return cargo;
    }

    public void setCargo(Cargos cargo) {
        this.cargo = cargo;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public EstadosCiviles getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadosCiviles estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Date getFechaFinalCenso() {
        return fechaFinalCenso;
    }

    public void setFechaFinalCenso(Date fechaFinalCenso) {
        this.fechaFinalCenso = fechaFinalCenso;
    }

    public Date getFechaFinalDatosPersonales() {
        return fechaFinalDatosPersonales;
    }

    public void setFechaFinalDatosPersonales(Date fechaFinalDatosPersonales) {
        this.fechaFinalDatosPersonales = fechaFinalDatosPersonales;
    }

    public Date getFechaFinalEstadoCivil() {
        return fechaFinalEstadoCivil;
    }

    public void setFechaFinalEstadoCivil(Date fechaFinalEstadoCivil) {
        this.fechaFinalEstadoCivil = fechaFinalEstadoCivil;
    }

    public Date getFechaFinalExperienciaLaboral() {
        return fechaFinalExperienciaLaboral;
    }

    public void setFechaFinalExperienciaLaboral(Date fechaFinalExperienciaLaboral) {
        this.fechaFinalExperienciaLaboral = fechaFinalExperienciaLaboral;
    }

    public Date getFechaFinalFormal() {
        return fechaFinalFormal;
    }

    public void setFechaFinalFormal(Date fechaFinalFormal) {
        this.fechaFinalFormal = fechaFinalFormal;
    }

    public Date getFechaFinalNoFormal() {
        return fechaFinalNoFormal;
    }

    public void setFechaFinalNoFormal(Date fechaFinalNoFormal) {
        this.fechaFinalNoFormal = fechaFinalNoFormal;
    }

    public Date getFechaFinalProyecto() {
        return fechaFinalProyecto;
    }

    public void setFechaFinalProyecto(Date fechaFinalProyecto) {
        this.fechaFinalProyecto = fechaFinalProyecto;
    }

    public Date getFechaInicialCenso() {
        return fechaInicialCenso;
    }

    public void setFechaInicialCenso(Date fechaInicialCenso) {
        this.fechaInicialCenso = fechaInicialCenso;
    }

    public Date getFechaInicialDatosPersonales() {
        return fechaInicialDatosPersonales;
    }

    public void setFechaInicialDatosPersonales(Date fechaInicialDatosPersonales) {
        this.fechaInicialDatosPersonales = fechaInicialDatosPersonales;
    }

    public Date getFechaInicialEstadoCivil() {
        return fechaInicialEstadoCivil;
    }

    public void setFechaInicialEstadoCivil(Date fechaInicialEstadoCivil) {
        this.fechaInicialEstadoCivil = fechaInicialEstadoCivil;
    }

    public Date getFechaInicialExperienciaLaboral() {
        return fechaInicialExperienciaLaboral;
    }

    public void setFechaInicialExperienciaLaboral(Date fechaInicialExperienciaLaboral) {
        this.fechaInicialExperienciaLaboral = fechaInicialExperienciaLaboral;
    }

    public Date getFechaInicialFormal() {
        return fechaInicialFormal;
    }

    public void setFechaInicialFormal(Date fechaInicialFormal) {
        this.fechaInicialFormal = fechaInicialFormal;
    }

    public Date getFechaInicialNoFormal() {
        return fechaInicialNoFormal;
    }

    public void setFechaInicialNoFormal(Date fechaInicialNoFormal) {
        this.fechaInicialNoFormal = fechaInicialNoFormal;
    }

    public Date getFechaInicialProyecto() {
        return fechaInicialProyecto;
    }

    public void setFechaInicialProyecto(Date fechaInicialProyecto) {
        this.fechaInicialProyecto = fechaInicialProyecto;
    }

    public HvExperienciasLaborales getHvExperienciaLaboral() {
        return hvExperienciaLaboral;
    }

    public void setHvExperienciaLaboral(HvExperienciasLaborales hvExperienciaLaboral) {
        this.hvExperienciaLaboral = hvExperienciaLaboral;
    }

    public IdiomasPersonas getIdiomaPersona() {
        return idiomaPersona;
    }

    public void setIdiomaPersona(IdiomasPersonas idiomaPersona) {
        this.idiomaPersona = idiomaPersona;
    }

    public VigenciasFormales getVigenciaFormal() {
        return vigenciaFormal;
    }

    public void setVigenciaFormal(VigenciasFormales vigenciaFormal) {
        this.vigenciaFormal = vigenciaFormal;
    }

    public VigenciasIndicadores getVigenciaIndicador() {
        return vigenciaIndicador;
    }

    public void setVigenciaIndicador(VigenciasIndicadores vigenciaIndicador) {
        this.vigenciaIndicador = vigenciaIndicador;
    }

    public VigenciasNoFormales getVigenciaNoFormal() {
        return vigenciaNoFormal;
    }

    public void setVigenciaNoFormal(VigenciasNoFormales vigenciaNoFormal) {
        this.vigenciaNoFormal = vigenciaNoFormal;
    }

    public VigenciasProyectos getVigenciaProyecto() {
        return vigenciaProyecto;
    }

    public void setVigenciaProyecto(VigenciasProyectos vigenciaProyecto) {
        this.vigenciaProyecto = vigenciaProyecto;
    }

    public String getCargoExperienciaLaboral() {
        return cargoExperienciaLaboral;
    }

    public void setCargoExperienciaLaboral(String cargoExperienciaLaboral) {
        this.cargoExperienciaLaboral = cargoExperienciaLaboral;
    }

    public BigInteger getConversacionDesde() {
        return conversacionDesde;
    }

    public void setConversacionDesde(BigInteger conversacionDesde) {
        this.conversacionDesde = conversacionDesde;
    }

    public BigInteger getConversacionHasta() {
        return conversacionHasta;
    }

    public void setConversacionHasta(BigInteger conversacionHasta) {
        this.conversacionHasta = conversacionHasta;
    }

    public String getDesarrolladoEducacionFormal() {
        return desarrolladoEducacionFormal;
    }

    public void setDesarrolladoEducacionFormal(String desarrolladoEducacionFormal) {
        this.desarrolladoEducacionFormal = desarrolladoEducacionFormal;
    }

    public String getDesarrolladoEducacionNoFormal() {
        return desarrolladoEducacionNoFormal;
    }

    public void setDesarrolladoEducacionNoFormal(String desarrolladoEducacionNoFormal) {
        this.desarrolladoEducacionNoFormal = desarrolladoEducacionNoFormal;
    }

    public String getEmpresaExperienciaLaboral() {
        return empresaExperienciaLaboral;
    }

    public void setEmpresaExperienciaLaboral(String empresaExperienciaLaboral) {
        this.empresaExperienciaLaboral = empresaExperienciaLaboral;
    }

    public BigInteger getEscrituraDesde() {
        return escrituraDesde;
    }

    public void setEscrituraDesde(BigInteger escrituraDesde) {
        this.escrituraDesde = escrituraDesde;
    }

    public BigInteger getEscrituraHasta() {
        return escrituraHasta;
    }

    public void setEscrituraHasta(BigInteger escrituraHasta) {
        this.escrituraHasta = escrituraHasta;
    }

    public BigInteger getLecturaDesde() {
        return lecturaDesde;
    }

    public void setLecturaDesde(BigInteger lecturaDesde) {
        this.lecturaDesde = lecturaDesde;
    }

    public BigInteger getLecturaHasta() {
        return lecturaHasta;
    }

    public void setLecturaHasta(BigInteger lecturaHasta) {
        this.lecturaHasta = lecturaHasta;
    }
}
