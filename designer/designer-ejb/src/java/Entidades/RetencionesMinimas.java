package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "RETENCIONESMINIMAS")
public class RetencionesMinimas implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MENSUALIZADO")
    private BigInteger mensualizado;
    @Column(name = "RETENCION")
    private BigInteger retencion;
    @Column(name = "PORCENTAJE")
    private BigDecimal porcentaje;
    @Column(name = "RESTAUVT")
    private BigInteger restauvt;
    @JoinColumn(name = "VIGENCIARETENCIONMINIMA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private VigenciasRetencionesMinimas vigenciaretencionminima;

    public RetencionesMinimas() {
    }

    public RetencionesMinimas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public RetencionesMinimas(BigInteger secuencia, BigInteger mensualizado) {
        this.secuencia = secuencia;
        this.mensualizado = mensualizado;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getMensualizado() {
        return mensualizado;
    }

    public void setMensualizado(BigInteger mensualizado) {
        this.mensualizado = mensualizado;
    }

    public BigInteger getRetencion() {
        return retencion;
    }

    public void setRetencion(BigInteger retencion) {
        this.retencion = retencion;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigInteger getRestauvt() {
        return restauvt;
    }

    public void setRestauvt(BigInteger restauvt) {
        this.restauvt = restauvt;
    }

    public VigenciasRetencionesMinimas getVigenciasretencionminima() {
        if(vigenciaretencionminima == null){
            vigenciaretencionminima = new VigenciasRetencionesMinimas();
        }
        return vigenciaretencionminima;
    }

    public void setVigenciaretencionminima(VigenciasRetencionesMinimas vigenciaretencionminima) {
        this.vigenciaretencionminima = vigenciaretencionminima;
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
        if (!(object instanceof RetencionesMinimas)) {
            return false;
        }
        RetencionesMinimas other = (RetencionesMinimas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.RetencionesMinimas[ secuencia=" + secuencia + " ]";
    }
    
}
