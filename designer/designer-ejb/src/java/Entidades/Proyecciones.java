package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "PROYECCIONES")
public class Proyecciones implements Serializable {

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
    @Column(name = "FECHADESDE")
    @Temporal(TemporalType.DATE)
    private Date fechaDesde;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAHASTA")
    @Temporal(TemporalType.DATE)
    private Date fechaHasta;
    @Basic(optional = false)
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Size(max = 40)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 10)
    @Column(name = "ESTADO")
    private String estado;
    @Size(max = 10)
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "SALDO")
    private BigDecimal saldo;
    @Column(name = "VALORINCREMENTO")
    private BigInteger valorincremento;
    @Size(max = 500)
    @Column(name = "PARCIALES")
    private String parciales;
    @Column(name = "LOCALIZACION")
    private BigInteger localizacion;
    @JoinColumn(name = "NIT", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Terceros nit;
    @JoinColumn(name = "FORMULA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Formulas formula;
    @JoinColumn(name = "CUENTAD", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cuentas cuentaD;
    @JoinColumn(name = "CUENTAC", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cuentas cuentaC;
    @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Conceptos concepto;
    @JoinColumn(name = "CENTROCOSTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private CentrosCostos centroCosto;

    public Proyecciones() {
    }

    public Proyecciones(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Proyecciones(BigInteger secuencia, Empleados empleado, Date fechadesde, Date fechahasta, BigDecimal valor) {
        this.secuencia = secuencia;
        this.empleado = empleado;
        this.fechaDesde = fechadesde;
        this.fechaHasta = fechahasta;
        this.valor = valor;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

   

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigInteger getValorincremento() {
        return valorincremento;
    }

    public void setValorincremento(BigInteger valorincremento) {
        this.valorincremento = valorincremento;
    }

    public String getParciales() {
        return parciales;
    }

    public void setParciales(String parciales) {
        this.parciales = parciales;
    }

    public BigInteger getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(BigInteger localizacion) {
        this.localizacion = localizacion;
    }

    public Terceros getNit() {
        return nit;
    }

    public void setNit(Terceros nit) {
        this.nit = nit;
    }

    public Formulas getFormula() {
        return formula;
    }

    public void setFormula(Formulas formula) {
        this.formula = formula;
    }

    
    public Conceptos getConcepto() {
        return concepto;
    }

    public void setConcepto(Conceptos concepto) {
        this.concepto = concepto;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Cuentas getCuentaD() {
        return cuentaD;
    }

    public void setCuentaD(Cuentas cuentaD) {
        this.cuentaD = cuentaD;
    }

    public Cuentas getCuentaC() {
        return cuentaC;
    }

    public void setCuentaC(Cuentas cuentaC) {
        this.cuentaC = cuentaC;
    }

    public CentrosCostos getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(CentrosCostos centroCosto) {
        this.centroCosto = centroCosto;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proyecciones)) {
            return false;
        }
        Proyecciones other = (Proyecciones) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Proyecciones[ secuencia=" + secuencia + " ]";
    }
    
}
