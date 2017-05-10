/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
public class VWContabilidadDetallada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "PROCESO")
    private String proceso;
    @Column(name = "CONTABILIZACION")
    private BigInteger contabilizacion;
    @Column(name = "FLAG")
    @Size(max = 28)
    private String flag;
    @Column(name = "FECHACONTABILIZACION")
    @Temporal(TemporalType.DATE)
    private Date fechacontabilizacion;
    @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Conceptos concepto;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empleados empleado;
    @JoinColumn(name = "TERCERO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Terceros tercero;
    @Column(name = "CENTROCOSTO")
    @Size(max = 6)
    private String centrocosto;
    @Column(name = "VALORC")
    private BigDecimal valorc;
    @Column(name = "VALORD")
    private BigDecimal valord;
    @Column(name = "CUENTACONABLE")
    @Size(max = 10)
    private String cuentacontable;
    @Column(name = "EMPRESA")
    private String empresa;

    public VWContabilidadDetallada() {
    }

    public VWContabilidadDetallada(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getProceso() {
        if (proceso == null) {
            proceso = "";
        }
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public BigInteger getContabilizacion() {
        return contabilizacion;
    }

    public void setContabilizacion(BigInteger contabilizacion) {
        this.contabilizacion = contabilizacion;
    }

    public String getFlag() {
        if (flag == null) {
            flag = "";
        }
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Date getFechacontabilizacion() {
        return fechacontabilizacion;
    }

    public void setFechacontabilizacion(Date fechacontabilizacion) {
        this.fechacontabilizacion = fechacontabilizacion;
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

    public Terceros getTercero() {
        return tercero;
    }

    public void setTercero(Terceros tercero) {
        this.tercero = tercero;
    }

    public String getCentrocosto() {
        return centrocosto;
    }

    public void setCentrocosto(String centrocosto) {
        this.centrocosto = centrocosto;
    }

    public BigDecimal getValorc() {
        return valorc;
    }

    public void setValorc(BigDecimal valorc) {
        this.valorc = valorc;
    }

    public BigDecimal getValord() {
        return valord;
    }

    public void setValord(BigDecimal valord) {
        this.valord = valord;
    }

    public String getCuentacontable() {
        if (cuentacontable == null) {
            cuentacontable = "";
        }
        return cuentacontable;
    }

    public void setCuentacontable(String cuentacontable) {
        this.cuentacontable = cuentacontable;
    }

    public String getEmpresa() {
        if (empresa == null) {
            empresa = "";
        }
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof VWContabilidadDetallada)) {
            return false;
        }
        VWContabilidadDetallada other = (VWContabilidadDetallada) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VWContabilidadDetallada[ secuencia=" + secuencia + " ]";
    }

}
