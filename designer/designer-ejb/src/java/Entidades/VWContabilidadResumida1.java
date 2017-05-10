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
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Cacheable(false)
public class VWContabilidadResumida1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "PROCESO")
    private String proceso;
    @Column(name = "CUENTACONABLE")
    @Size(max = 10)
    private String cuentacontable;
    @Column(name = "FECHACONTABILIZACION")
    @Temporal(TemporalType.DATE)
    private Date fechacontabilizacion;
    @Column(name = "NIVELRESUMEN1")
    private BigInteger nivelresumen1;
    @Column(name = "VALORC")
    private BigDecimal valorc;
    @Column(name = "VALORD")
    private BigDecimal valord;
    @Column(name = "EMPRESA")
    private String empresa;

    public VWContabilidadResumida1() {
    }

    public VWContabilidadResumida1(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getProceso() {
        if(proceso == null){
            proceso = "";
        }
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getCuentacontable() {
        if(cuentacontable == null){
            cuentacontable = "";
        }
        return cuentacontable;
    }

    public void setCuentacontable(String cuentacontable) {
        this.cuentacontable = cuentacontable;
    }

    public Date getFechacontabilizacion() {
        return fechacontabilizacion;
    }

    public void setFechacontabilizacion(Date fechacontabilizacion) {
        this.fechacontabilizacion = fechacontabilizacion;
    }

    public BigInteger getNivelresumen1() {
        return nivelresumen1;
    }

    public void setNivelresumen1(BigInteger nivelresumen1) {
        this.nivelresumen1 = nivelresumen1;
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

    public String getEmpresa() {
        if(empresa == null){
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
        if (!(object instanceof VWContabilidadResumida1)) {
            return false;
        }
        VWContabilidadResumida1 other = (VWContabilidadResumida1) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VWContabilidadResumida1[ secuencia=" + secuencia + " ]";
    }

}
