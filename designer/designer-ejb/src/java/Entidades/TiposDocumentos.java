package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "TIPOSDOCUMENTOS")
public class TiposDocumentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "NOMBRELARGO")
    private String nombrelargo;
    @Column(name = "NOMBRECORTO")
    private String nombrecorto;

    public TiposDocumentos() {
    }

    public TiposDocumentos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposDocumentos(BigInteger secuencia, String nombrelargo) {
        this.secuencia = secuencia;
        this.nombrelargo = nombrelargo;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getNombrelargo() {
        if (nombrelargo == null) {
            nombrelargo = " ";
        }
        return nombrelargo;
    }

    public void setNombrelargo(String nombrelargo) {
        this.nombrelargo = nombrelargo.toUpperCase();
    }

    public String getNombrecorto() {
        if (nombrecorto == null) {
            nombrecorto = " ";
        }
        return nombrecorto;
    }

    public void setNombrecorto(String nombrecorto) {
        this.nombrecorto = nombrecorto.toUpperCase();
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
        if (!(object instanceof TiposDocumentos)) {
            return false;
        }
        TiposDocumentos other = (TiposDocumentos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tiposdocumentos[ secuencia=" + secuencia + " ]";
    }

}
