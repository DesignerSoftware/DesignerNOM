package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author PROYECTO01
 */
@Entity
@Table(name = "DETALLESREFORMASLABORALES")
public class DetallesReformasLaborales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "TIPOPAGO")
    private String tipopago;
    @Column(name = "FACTOR")
    private BigDecimal factor;
    @JoinColumn(name = "REFORMALABORAL", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private ReformasLaborales reformalaboral;

    public DetallesReformasLaborales() {
    }

    public DetallesReformasLaborales(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public DetallesReformasLaborales(BigInteger secuencia, String tipopago, BigDecimal factor) {
        this.secuencia = secuencia;
        this.tipopago = tipopago;
        this.factor = factor;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getTipopago() {
        return tipopago;
    }

    public void setTipopago(String tipopago) {
        this.tipopago = tipopago;
    }

    public BigDecimal getFactor() {
        return factor;
    }

    public void setFactor(BigDecimal factor) {
        this.factor = factor;
    }

    public ReformasLaborales getReformalaboral() {
        return reformalaboral;
    }

    public void setReformalaboral(ReformasLaborales reformalaboral) {
        this.reformalaboral = reformalaboral;
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
        if (!(object instanceof DetallesReformasLaborales)) {
            return false;
        }
        DetallesReformasLaborales other = (DetallesReformasLaborales) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.DetallesReformasLaborales[ secuencia=" + secuencia + " ]";
    }

}
