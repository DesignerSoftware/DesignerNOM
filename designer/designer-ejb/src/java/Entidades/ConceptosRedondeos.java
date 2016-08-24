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
@Table(name = "CONCEPTOSREDONDEOS")
public class ConceptosRedondeos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "TIPOREDONDEO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposRedondeos tiporedondeo;
    @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Conceptos concepto;

    public ConceptosRedondeos() {
    }

    public ConceptosRedondeos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposRedondeos getTiporedondeo() {
        if (tiporedondeo == null) {
            tiporedondeo = new TiposRedondeos();
        }
        return tiporedondeo;
    }

    public void setTiporedondeo(TiposRedondeos tiporedondeo) {
        this.tiporedondeo = tiporedondeo;
    }

    public Conceptos getConcepto() {
        if (concepto == null) {
            concepto = new Conceptos();
        }
        return concepto;
    }

    public void setConcepto(Conceptos concepto) {
        this.concepto = concepto;
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
        if (!(object instanceof ConceptosRedondeos)) {
            return false;
        }
        ConceptosRedondeos other = (ConceptosRedondeos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ConceptosRedondeos[ secuencia=" + secuencia + " ]";
    }

}
