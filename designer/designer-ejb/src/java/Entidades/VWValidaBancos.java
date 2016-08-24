package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;

/**
 *
 * @author Administrador
 */
@Entity
public class VWValidaBancos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger secuencia;
    @Column(name = "CODIGOPRIMARIO")
    private BigInteger codigoprimario;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getCodigoprimario() {
        return codigoprimario;
    }

    public void setCodigoprimario(BigInteger codigoprimario) {
        this.codigoprimario = codigoprimario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the secuencia fields are not set
        if (!(object instanceof VWValidaBancos)) {
            return false;
        }
        VWValidaBancos other = (VWValidaBancos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VWValidaBancos[ secuencia=" + secuencia + " ]";
    }

}
