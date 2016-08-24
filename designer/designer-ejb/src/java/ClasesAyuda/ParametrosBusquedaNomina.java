package ClasesAyuda;

import Entidades.*;
import java.math.BigDecimal;
import java.util.Date;

public class ParametrosBusquedaNomina {

    private VigenciasCargos vigenciaCargo;
    private VigenciasLocalizaciones vigenciaLocalizacion;
    private VigenciasSueldos vigenciaSueldo;
    private VigenciasTiposContratos vigenciaTipoContrato;
    private VigenciasTiposTrabajadores vigenciaTipoTrabajador;
    private VigenciasReformasLaborales vigenciaReformasLaboral;
    private VigenciasContratos vigenciaContrato;
    private VigenciasUbicaciones vigenciaUbicacion;
    private VigenciasAfiliaciones vigenciaAfiliacion;
    private VigenciasFormasPagos vigenciaFormaPago;
    private Mvrs mvrs;
    private VigenciasNormasEmpleados vigenciaNormaEmpleado;
    private VigenciasJornadas vigenciaJornada;
    private MotivosRetiros motivosRetiros;
    //Valores
    private BigDecimal sueldoMinimo;
    private BigDecimal sueldoMaximo;
    private BigDecimal sueldoMinimoMvr;
    private BigDecimal sueldoMaximoMvr;
    private BigDecimal promedioMinimoSet;
    private BigDecimal promedioMaximoSet;
    //Texto
    private String tipoMetodoSet;
    //Fechas
    private Date fechaInicialCargo;
    private Date fechaFinalCargo;
    private Date fechaInicialCentroCosto;
    private Date fechaFinalCentroCosto;
    private Date fechaInicialSueldo;
    private Date fechaFinalSueldo;
    private Date fechaInicialTipoContrato;
    private Date fechaFinalTipoContrato;
    private Date fechaInicialTipoTrabajador;
    private Date fechaFinalTipoTrabajador;
    private Date fechaInicialReformaLaboral;
    private Date fechaFinalReformaLaboral;
    private Date fechaInicialLegislacionMI;
    private Date fechaFinalLegislacionMI;
    private Date fechaInicialLegislacionMF;
    private Date fechaFinalLegislacionMF;
    private Date fechaInicialUbicacion;
    private Date fechaFinalUbicacion;
    private Date fechaInicialAfiliacion;
    private Date fechaFinalAfiliacion;
    private Date fechaInicialFormaPago;
    private Date fechaFinalFormaPago;
    private Date fechaInicialMvr;
    private Date fechaFinalMvr;
    private Date fechaInicialSetMI;
    private Date fechaFinalSetMI;
    private Date fechaInicialSetMF;
    private Date fechaFinalSetMF;
    private Date fechaInicialNormaLaboral;
    private Date fechaFinalNormaLaboral;
    private Date fechaInicialVacacionesMI;
    private Date fechaFinalVacacionesMI;
    private Date fechaInicialVacacionesMF;
    private Date fechaFinalVacacionesMF;
    private Date fechaInicialJornadaLaboral;
    private Date fechaFinalJornadaLaboral;
    private Date fechaInicialRetiro;
    private Date fechaFinalRetiro;

    public ParametrosBusquedaNomina() {
        this.vigenciaCargo = new VigenciasCargos();
        this.vigenciaLocalizacion = new VigenciasLocalizaciones();
        this.vigenciaSueldo = new VigenciasSueldos();
        this.vigenciaTipoContrato = new VigenciasTiposContratos();
        this.vigenciaTipoTrabajador = new VigenciasTiposTrabajadores();
        this.vigenciaReformasLaboral = new VigenciasReformasLaborales();
        this.vigenciaContrato = new VigenciasContratos();
        this.vigenciaUbicacion = new VigenciasUbicaciones();
        this.vigenciaAfiliacion = new VigenciasAfiliaciones();
        this.vigenciaFormaPago = new VigenciasFormasPagos();
        mvrs = new Mvrs();
        vigenciaNormaEmpleado = new VigenciasNormasEmpleados();
        vigenciaJornada = new VigenciasJornadas();
        motivosRetiros = new MotivosRetiros();
    }

    //GETTER AND SETTER UNICAMENTE
    public VigenciasCargos getVigenciaCargo() {
        return vigenciaCargo;
    }

    public void setVigenciaCargo(VigenciasCargos vigenciaCargo) {
        this.vigenciaCargo = vigenciaCargo;
    }

    public VigenciasLocalizaciones getVigenciaLocalizacion() {
        return vigenciaLocalizacion;
    }

    public void setVigenciaLocalizacion(VigenciasLocalizaciones vigenciaLocalizacion) {
        this.vigenciaLocalizacion = vigenciaLocalizacion;
    }

    public VigenciasSueldos getVigenciaSueldo() {
        return vigenciaSueldo;
    }

    public void setVigenciaSueldo(VigenciasSueldos vigenciaSueldo) {
        this.vigenciaSueldo = vigenciaSueldo;
    }

    public VigenciasTiposContratos getVigenciaTipoContrato() {
        return vigenciaTipoContrato;
    }

    public void setVigenciaTipoContrato(VigenciasTiposContratos vigenciaTipoContrato) {
        this.vigenciaTipoContrato = vigenciaTipoContrato;
    }

    public VigenciasAfiliaciones getVigenciaAfiliacion() {
        return vigenciaAfiliacion;
    }

    public void setVigenciaAfiliacion(VigenciasAfiliaciones vigenciaAfiliacion) {
        this.vigenciaAfiliacion = vigenciaAfiliacion;
    }

    public VigenciasContratos getVigenciaContrato() {
        return vigenciaContrato;
    }

    public void setVigenciaContrato(VigenciasContratos vigenciaContrato) {
        this.vigenciaContrato = vigenciaContrato;
    }

    public VigenciasReformasLaborales getVigenciaReformasLaboral() {
        return vigenciaReformasLaboral;
    }

    public void setVigenciaReformasLaboral(VigenciasReformasLaborales vigenciaReformasLaboral) {
        this.vigenciaReformasLaboral = vigenciaReformasLaboral;
    }

    public VigenciasTiposTrabajadores getVigenciaTipoTrabajador() {
        return vigenciaTipoTrabajador;
    }

    public void setVigenciaTipoTrabajador(VigenciasTiposTrabajadores vigenciaTipoTrabajador) {
        this.vigenciaTipoTrabajador = vigenciaTipoTrabajador;
    }

    public VigenciasUbicaciones getVigenciaUbicacion() {
        return vigenciaUbicacion;
    }

    public void setVigenciaUbicacion(VigenciasUbicaciones vigenciaUbicacion) {
        this.vigenciaUbicacion = vigenciaUbicacion;
    }

    public MotivosRetiros getMotivosRetiros() {
        return motivosRetiros;
    }

    public void setMotivosRetiros(MotivosRetiros motivosRetiros) {
        this.motivosRetiros = motivosRetiros;
    }

    public Mvrs getMvrs() {
        return mvrs;
    }

    public void setMvrs(Mvrs mvrs) {
        this.mvrs = mvrs;
    }

    public VigenciasFormasPagos getVigenciaFormaPago() {
        return vigenciaFormaPago;
    }

    public void setVigenciaFormaPago(VigenciasFormasPagos vigenciaFormaPago) {
        this.vigenciaFormaPago = vigenciaFormaPago;
    }

    public VigenciasJornadas getVigenciaJornada() {
        return vigenciaJornada;
    }

    public void setVigenciaJornada(VigenciasJornadas vigenciasJornada) {
        this.vigenciaJornada = vigenciasJornada;
    }

    public VigenciasNormasEmpleados getVigenciaNormaEmpleado() {
        return vigenciaNormaEmpleado;
    }

    public void setVigenciaNormaEmpleado(VigenciasNormasEmpleados vigenciasNormasEmpleados) {
        this.vigenciaNormaEmpleado = vigenciasNormasEmpleados;
    }

    public Date getFechaFinalCargo() {
        return fechaFinalCargo;
    }

    public void setFechaFinalCargo(Date fechaFinalCargo) {
        this.fechaFinalCargo = fechaFinalCargo;
    }

    public Date getFechaInicialCargo() {
        return fechaInicialCargo;
    }

    public void setFechaInicialCargo(Date fechaInicialCargo) {
        this.fechaInicialCargo = fechaInicialCargo;
    }

    public Date getFechaFinalCentroCosto() {
        return fechaFinalCentroCosto;
    }

    public void setFechaFinalCentroCosto(Date fechaFinalCentroCosto) {
        this.fechaFinalCentroCosto = fechaFinalCentroCosto;
    }

    public Date getFechaInicialCentroCosto() {
        return fechaInicialCentroCosto;
    }

    public void setFechaInicialCentroCosto(Date fechaInicialCentroCosto) {
        this.fechaInicialCentroCosto = fechaInicialCentroCosto;
    }

    public BigDecimal getSueldoMaximo() {
        return sueldoMaximo;
    }

    public void setSueldoMaximo(BigDecimal sueldoMaximo) {
        this.sueldoMaximo = sueldoMaximo;
    }

    public BigDecimal getSueldoMinimo() {
        return sueldoMinimo;
    }

    public void setSueldoMinimo(BigDecimal sueldoMinimo) {
        this.sueldoMinimo = sueldoMinimo;
    }

    public Date getFechaFinalSueldo() {
        return fechaFinalSueldo;
    }

    public void setFechaFinalSueldo(Date fechaFinalSueldo) {
        this.fechaFinalSueldo = fechaFinalSueldo;
    }

    public Date getFechaInicialSueldo() {
        return fechaInicialSueldo;
    }

    public void setFechaInicialSueldo(Date fechaInicialSueldo) {
        this.fechaInicialSueldo = fechaInicialSueldo;
    }

    public Date getFechaFinalTipoContrato() {
        return fechaFinalTipoContrato;
    }

    public void setFechaFinalTipoContrato(Date fechaFinalTipoContrato) {
        this.fechaFinalTipoContrato = fechaFinalTipoContrato;
    }

    public Date getFechaInicialTipoContrato() {
        return fechaInicialTipoContrato;
    }

    public void setFechaInicialTipoContrato(Date fechaInicialTipoContrato) {
        this.fechaInicialTipoContrato = fechaInicialTipoContrato;
    }

    public Date getFechaFinalTipoTrabajador() {
        return fechaFinalTipoTrabajador;
    }

    public void setFechaFinalTipoTrabajador(Date fechaFinalTipoTrabajador) {
        this.fechaFinalTipoTrabajador = fechaFinalTipoTrabajador;
    }

    public Date getFechaInicialTipoTrabajador() {
        return fechaInicialTipoTrabajador;
    }

    public void setFechaInicialTipoTrabajador(Date fechaInicialTipoTrabajador) {
        this.fechaInicialTipoTrabajador = fechaInicialTipoTrabajador;
    }

    public Date getFechaFinalReformaLaboral() {
        return fechaFinalReformaLaboral;
    }

    public void setFechaFinalReformaLaboral(Date fechaFinalReformaLaboral) {
        this.fechaFinalReformaLaboral = fechaFinalReformaLaboral;
    }

    public Date getFechaInicialReformaLaboral() {
        return fechaInicialReformaLaboral;
    }

    public void setFechaInicialReformaLaboral(Date fechaInicialReformaLaboral) {
        this.fechaInicialReformaLaboral = fechaInicialReformaLaboral;
    }

    public Date getFechaFinalLegislacionMI() {
        return fechaFinalLegislacionMI;
    }

    public void setFechaFinalLegislacionMI(Date fechaFinalLegislacion) {
        this.fechaFinalLegislacionMI = fechaFinalLegislacion;
    }

    public Date getFechaInicialLegislacionMI() {
        return fechaInicialLegislacionMI;
    }

    public void setFechaInicialLegislacionMI(Date fechaInicialLegislacion) {
        this.fechaInicialLegislacionMI = fechaInicialLegislacion;
    }

    public Date getFechaFinalLegislacionMF() {
        return fechaFinalLegislacionMF;
    }

    public void setFechaFinalLegislacionMF(Date fechaFinalLegislacionMF) {
        this.fechaFinalLegislacionMF = fechaFinalLegislacionMF;
    }

    public Date getFechaInicialLegislacionMF() {
        return fechaInicialLegislacionMF;
    }

    public void setFechaInicialLegislacionMF(Date fechaInicialLegislacionMF) {
        this.fechaInicialLegislacionMF = fechaInicialLegislacionMF;
    }

    public Date getFechaFinalAfiliacion() {
        return fechaFinalAfiliacion;
    }

    public void setFechaFinalAfiliacion(Date fechaFinalAfiliacion) {
        this.fechaFinalAfiliacion = fechaFinalAfiliacion;
    }

    public Date getFechaFinalUbicacion() {
        return fechaFinalUbicacion;
    }

    public void setFechaFinalUbicacion(Date fechaFinalUbicacion) {
        this.fechaFinalUbicacion = fechaFinalUbicacion;
    }

    public Date getFechaInicialAfiliacion() {
        return fechaInicialAfiliacion;
    }

    public void setFechaInicialAfiliacion(Date fechaInicialAfiliacion) {
        this.fechaInicialAfiliacion = fechaInicialAfiliacion;
    }

    public Date getFechaInicialUbicacion() {
        return fechaInicialUbicacion;
    }

    public void setFechaInicialUbicacion(Date fechaInicialUbicacion) {
        this.fechaInicialUbicacion = fechaInicialUbicacion;
    }

    public Date getFechaFinalFormaPago() {
        return fechaFinalFormaPago;
    }

    public void setFechaFinalFormaPago(Date fechaFinalFormaPago) {
        this.fechaFinalFormaPago = fechaFinalFormaPago;
    }

    public Date getFechaFinalJornadaLaboral() {
        return fechaFinalJornadaLaboral;
    }

    public void setFechaFinalJornadaLaboral(Date fechaFinalJornadaLaboral) {
        this.fechaFinalJornadaLaboral = fechaFinalJornadaLaboral;
    }

    public Date getFechaFinalMvr() {
        return fechaFinalMvr;
    }

    public void setFechaFinalMvr(Date fechaFinalMvr) {
        this.fechaFinalMvr = fechaFinalMvr;
    }

    public Date getFechaFinalNormaLaboral() {
        return fechaFinalNormaLaboral;
    }

    public void setFechaFinalNormaLaboral(Date fechaFinalNormaLaboral) {
        this.fechaFinalNormaLaboral = fechaFinalNormaLaboral;
    }

    public Date getFechaFinalSetMF() {
        return fechaFinalSetMF;
    }

    public void setFechaFinalSetMF(Date fechaFinalSetMF) {
        this.fechaFinalSetMF = fechaFinalSetMF;
    }

    public Date getFechaFinalSetMI() {
        return fechaFinalSetMI;
    }

    public void setFechaFinalSetMI(Date fechaFinalSetMI) {
        this.fechaFinalSetMI = fechaFinalSetMI;
    }

    public Date getFechaFinalVacacionesMF() {
        return fechaFinalVacacionesMF;
    }

    public void setFechaFinalVacacionesMF(Date fechaFinalVacacionesMF) {
        this.fechaFinalVacacionesMF = fechaFinalVacacionesMF;
    }

    public Date getFechaFinalVacacionesMI() {
        return fechaFinalVacacionesMI;
    }

    public void setFechaFinalVacacionesMI(Date fechaFinalVacacionesMI) {
        this.fechaFinalVacacionesMI = fechaFinalVacacionesMI;
    }

    public Date getFechaInicialFormaPago() {
        return fechaInicialFormaPago;
    }

    public void setFechaInicialFormaPago(Date fechaInicialFormaPago) {
        this.fechaInicialFormaPago = fechaInicialFormaPago;
    }

    public Date getFechaInicialJornadaLaboral() {
        return fechaInicialJornadaLaboral;
    }

    public void setFechaInicialJornadaLaboral(Date fechaInicialJornadaLaboral) {
        this.fechaInicialJornadaLaboral = fechaInicialJornadaLaboral;
    }

    public Date getFechaInicialMvr() {
        return fechaInicialMvr;
    }

    public void setFechaInicialMvr(Date fechaInicialMvr) {
        this.fechaInicialMvr = fechaInicialMvr;
    }

    public Date getFechaInicialNormaLaboral() {
        return fechaInicialNormaLaboral;
    }

    public void setFechaInicialNormaLaboral(Date fechaInicialNormaLaboral) {
        this.fechaInicialNormaLaboral = fechaInicialNormaLaboral;
    }

    public Date getFechaInicialSetMF() {
        return fechaInicialSetMF;
    }

    public void setFechaInicialSetMF(Date fechaInicialSetMF) {
        this.fechaInicialSetMF = fechaInicialSetMF;
    }

    public Date getFechaInicialSetMI() {
        return fechaInicialSetMI;
    }

    public void setFechaInicialSetMI(Date fechaInicialSetMI) {
        this.fechaInicialSetMI = fechaInicialSetMI;
    }

    public Date getFechaInicialVacacionesMF() {
        return fechaInicialVacacionesMF;
    }

    public void setFechaInicialVacacionesMF(Date fechaInicialVacacionesMF) {
        this.fechaInicialVacacionesMF = fechaInicialVacacionesMF;
    }

    public Date getFechaInicialVacacionesMI() {
        return fechaInicialVacacionesMI;
    }

    public void setFechaInicialVacacionesMI(Date fechaInicialVacacionesMI) {
        this.fechaInicialVacacionesMI = fechaInicialVacacionesMI;
    }

    public Date getFechaFinalRetiro() {
        return fechaFinalRetiro;
    }

    public void setFechaFinalRetiro(Date fechaFinalRetiro) {
        this.fechaFinalRetiro = fechaFinalRetiro;
    }

    public Date getFechaInicialRetiro() {
        return fechaInicialRetiro;
    }

    public void setFechaInicialRetiro(Date fechaInicialRetiro) {
        this.fechaInicialRetiro = fechaInicialRetiro;
    }

    public BigDecimal getSueldoMaximoMvr() {
        return sueldoMaximoMvr;
    }

    public void setSueldoMaximoMvr(BigDecimal sueldoMaximoMvr) {
        this.sueldoMaximoMvr = sueldoMaximoMvr;
    }

    public BigDecimal getSueldoMinimoMvr() {
        return sueldoMinimoMvr;
    }

    public void setSueldoMinimoMvr(BigDecimal sueldoMinimoMvr) {
        this.sueldoMinimoMvr = sueldoMinimoMvr;
    }

    public BigDecimal getPromedioMaximoSet() {
        return promedioMaximoSet;
    }

    public void setPromedioMaximoSet(BigDecimal promedioMaximoSet) {
        this.promedioMaximoSet = promedioMaximoSet;
    }

    public BigDecimal getPromedioMinimoSet() {
        return promedioMinimoSet;
    }

    public void setPromedioMinimoSet(BigDecimal promedioMinimoSet) {
        this.promedioMinimoSet = promedioMinimoSet;
    }

    public String getTipoMetodoSet() {
        return tipoMetodoSet;
    }

    public void setTipoMetodoSet(String tipoMetodoSet) {
        this.tipoMetodoSet = tipoMetodoSet;
    }
}
