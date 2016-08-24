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
@Table(name = "SUELDOSMERCADOS")
public class SueldosMercados implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "SUELDOMIN")
    private BigDecimal sueldomin;
    @Column(name = "SUELDOMAX")
    private BigDecimal sueldomax;
    @JoinColumn(name = "TIPOEMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposEmpresas tipoempresa;
    @JoinColumn(name = "CARGO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Cargos cargo;

    public SueldosMercados() {
    }

    public SueldosMercados(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigDecimal getSueldomin() {
        return sueldomin;
    }

    public void setSueldomin(BigDecimal sueldomin) {
        this.sueldomin = sueldomin;
    }

    public BigDecimal getSueldomax() {
        return sueldomax;
    }

    public void setSueldomax(BigDecimal sueldomax) {
        this.sueldomax = sueldomax;
    }

    public TiposEmpresas getTipoempresa() {
        return tipoempresa;
    }

    public void setTipoempresa(TiposEmpresas tipoempresa) {
        this.tipoempresa = tipoempresa;
    }

    public Cargos getCargo() {
        return cargo;
    }

    public void setCargo(Cargos cargo) {
        this.cargo = cargo;
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
        if (!(object instanceof SueldosMercados)) {
            return false;
        }
        SueldosMercados other = (SueldosMercados) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.SueldosMercados[ secuencia=" + secuencia + " ]";
    }
    
}
