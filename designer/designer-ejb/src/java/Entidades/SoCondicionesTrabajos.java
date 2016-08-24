package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "SOCONDICIONESTRABAJOS")
public class SoCondicionesTrabajos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Integer codigo;
    @Column(name = "FACTORRIESGO")
    private String factorriesgo;
    @Column(name = "FUENTE")
    private String fuente;
    @Column(name = "EFECTOAGUDO")
    private String efectoagudo;
    @Column(name = "EFECTOCRONICO")
    private String efectocronico;
    @Column(name = "OBSERVACION")
    private String observacion;
    @Column(name = "RECOMENDACION")
    private String recomendacion;
    @Column(name = "REPORTAR")
    private String reportar;

    public SoCondicionesTrabajos() {
    }

    public SoCondicionesTrabajos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public SoCondicionesTrabajos(BigInteger secuencia, Integer codigo, String factorriesgo) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.factorriesgo = factorriesgo;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getFactorriesgo() {
        return factorriesgo;
    }

    public void setFactorriesgo(String factorriesgo) {
        this.factorriesgo = factorriesgo.toUpperCase();
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getEfectoagudo() {
        return efectoagudo;
    }

    public void setEfectoagudo(String efectoagudo) {
        this.efectoagudo = efectoagudo;
    }

    public String getEfectocronico() {
        return efectocronico;
    }

    public void setEfectocronico(String efectocronico) {
        this.efectocronico = efectocronico;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getRecomendacion() {
        return recomendacion;
    }

    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }

    public String getReportar() {
        return reportar;
    }

    public void setReportar(String reportar) {
        this.reportar = reportar;
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
        if (!(object instanceof SoCondicionesTrabajos)) {
            return false;
        }
        SoCondicionesTrabajos other = (SoCondicionesTrabajos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Socondicionestrabajos[ secuencia=" + secuencia + " ]";
    }
    
}
