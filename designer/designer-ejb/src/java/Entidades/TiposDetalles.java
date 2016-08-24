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
@Table(name = "TIPOSDETALLES")
public class TiposDetalles implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private BigInteger codigo;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "ENFOQUE", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Enfoques enfoque;

    public TiposDetalles() {
    }

    public TiposDetalles(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getCodigo() {
        return codigo;
    }

    public void setCodigo(BigInteger codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Enfoques getEnfoque() {
        return enfoque;
    }

    public void setEnfoque(Enfoques enfoque) {
        this.enfoque = enfoque;
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
        if (!(object instanceof TiposDetalles)) {
            return false;
        }
        TiposDetalles other = (TiposDetalles) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TiposDetalles[ secuencia=" + secuencia + " ]";
    }
    
}
