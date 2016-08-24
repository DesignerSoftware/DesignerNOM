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
@Table(name = "FORMULASCONTRATOSENTIDADES")
public class FormulasContratosEntidades implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "TIPOENTIDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposEntidades tipoentidad;
    @JoinColumn(name = "FORMULACONTRATO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Formulascontratos formulacontrato;

    public FormulasContratosEntidades() {
    }

    public FormulasContratosEntidades(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposEntidades getTipoentidad() {
        return tipoentidad;
    }

    public void setTipoentidad(TiposEntidades tipoentidad) {
        this.tipoentidad = tipoentidad;
    }

    public Formulascontratos getFormulacontrato() {
        return formulacontrato;
    }

    public void setFormulacontrato(Formulascontratos formulacontrato) {
        this.formulacontrato = formulacontrato;
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
        if (!(object instanceof FormulasContratosEntidades)) {
            return false;
        }
        FormulasContratosEntidades other = (FormulasContratosEntidades) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Formulascontratosentidades[ secuencia=" + secuencia + " ]";
    }
    
}
