package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Cacheable(false)
public class VWActualesAfiliacionesSalud implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.DATE)
    private Date fechaInicial;
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.DATE)
    private Date fechaFinal;
    @JoinColumn(name = "TERCERO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TercerosSucursales tercerosSucursales;
    @JoinColumn(name = "TIPOENTIDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposEntidades tiposEntidades;
    @Size(max = 12)
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "MODALIDAD")
    private Integer modalidad;
    @Column(name = "VALOR")
    private BigInteger valor;
    @Column(name = "FORMULACONTRATO")
    private BigInteger formulaContrato;
    @Column(name = "CONCEPTO")
    private BigInteger concepto;
    @Column(name = "FORMULA")
    private BigInteger formula;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public TercerosSucursales getTercerosSucursales() {
        return tercerosSucursales;
    }

    public void setTercerosSucursales(TercerosSucursales tercerosSucursales) {
        this.tercerosSucursales = tercerosSucursales;
    }

    public TiposEntidades getTiposEntidades() {
        return tiposEntidades;
    }

    public void setTiposEntidades(TiposEntidades tiposEntidades) {
        this.tiposEntidades = tiposEntidades;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getModalidad() {
        return modalidad;
    }

    public void setModalidad(Integer modalidad) {
        this.modalidad = modalidad;
    }

    public BigInteger getValor() {
        return valor;
    }

    public void setValor(BigInteger valor) {
        this.valor = valor;
    }

    public BigInteger getFormulaContrato() {
        return formulaContrato;
    }

    public void setFormulaContrato(BigInteger formulaContrato) {
        this.formulaContrato = formulaContrato;
    }

    public BigInteger getConcepto() {
        return concepto;
    }

    public void setConcepto(BigInteger concepto) {
        this.concepto = concepto;
    }

    public BigInteger getFormula() {
        return formula;
    }

    public void setFormula(BigInteger formula) {
        this.formula = formula;
    }
}
