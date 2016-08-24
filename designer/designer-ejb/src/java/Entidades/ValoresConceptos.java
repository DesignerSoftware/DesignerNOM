
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
@Table(name = "VALORESCONCEPTOS")
public class ValoresConceptos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "VALORUNITARIO")
    private Integer valorunitario;
    @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Conceptos concepto;

    public ValoresConceptos() {
    }

    public ValoresConceptos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public ValoresConceptos(BigInteger secuencia, Integer valorunitario) {
        this.secuencia = secuencia;
        this.valorunitario = valorunitario;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getValorunitario() {
        return valorunitario;
    }

    public void setValorunitario(Integer valorunitario) {
        this.valorunitario = valorunitario;
    }

    public Conceptos getConcepto() {
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
        if (!(object instanceof ValoresConceptos)) {
            return false;
        }
        ValoresConceptos other = (ValoresConceptos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ValoresConceptos[ secuencia=" + secuencia + " ]";
    }

}
