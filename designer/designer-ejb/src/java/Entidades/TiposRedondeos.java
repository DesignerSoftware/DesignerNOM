package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "TIPOSREDONDEOS")
public class TiposRedondeos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "REDONDEO")
    private short redondeo;
    @Size(max = 10)
    @Column(name = "FORMAREDONDEO")
    private String formaredondeo;

    public TiposRedondeos() {
    }

    public TiposRedondeos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposRedondeos(BigInteger secuencia, String descripcion, short redondeo) {
        this.secuencia = secuencia;
        this.descripcion = descripcion;
        this.redondeo = redondeo;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getRedondeo() {
        return redondeo;
    }

    public void setRedondeo(short redondeo) {
        this.redondeo = redondeo;
    }

    public String getFormaredondeo() {
        return formaredondeo;
    }

    public void setFormaredondeo(String formaredondeo) {
        this.formaredondeo = formaredondeo;
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
        if (!(object instanceof TiposRedondeos)) {
            return false;
        }
        TiposRedondeos other = (TiposRedondeos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TiposRedondeos[ secuencia=" + secuencia + " ]";
    }
    
}
