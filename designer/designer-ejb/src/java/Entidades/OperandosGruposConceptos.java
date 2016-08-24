package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author PROYECTO01
 */
@Entity
@Table(name = "OPERANDOSGRUPOSCONCEPTOS")
public class OperandosGruposConceptos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "PROCESO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Procesos proceso;
    @JoinColumn(name = "OPERANDO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Operandos operando;
    @JoinColumn(name = "GRUPOCONCEPTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private GruposConceptos grupoconcepto;

    public OperandosGruposConceptos() {
    }

    public OperandosGruposConceptos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Procesos getProceso() {
        return proceso;
    }

    public void setProceso(Procesos proceso) {
        this.proceso = proceso;
    }

    public Operandos getOperando() {
        return operando;
    }

    public void setOperando(Operandos operando) {
        this.operando = operando;
    }

    public GruposConceptos getGrupoconcepto() {
        return grupoconcepto;
    }

    public void setGrupoconcepto(GruposConceptos grupoconcepto) {
        this.grupoconcepto = grupoconcepto;
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
        if (!(object instanceof OperandosGruposConceptos)) {
            return false;
        }
        OperandosGruposConceptos other = (OperandosGruposConceptos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.OperandosGruposConceptos[ secuencia=" + secuencia + " ]";
    }
    
}
