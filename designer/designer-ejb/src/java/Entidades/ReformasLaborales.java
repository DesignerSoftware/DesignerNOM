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
@Table(name = "REFORMASLABORALES")
public class ReformasLaborales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private short codigo;
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 1)
    @Column(name = "INTEGRAL")
    private String integral;
    @Transient
    private String strIntegral;

    public ReformasLaborales() {
    }

    public ReformasLaborales(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public ReformasLaborales(BigInteger secuencia, short codigo, String nombre) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public short getCodigo() {
        return codigo;
    }

    public void setCodigo(short codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIntegral() {
        if (integral == null) {
            integral = "N";
        }
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getStrIntegral() {
        getIntegral();
        if (integral == null || integral.equalsIgnoreCase("N")) {
            strIntegral = "NO";
        } else {
            strIntegral = "SI";
        }
        return strIntegral;
    }

    public void setStrIntegral(String strIntegral) {
        this.strIntegral = strIntegral;
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
        if (!(object instanceof ReformasLaborales)) {
            return false;
        }
        ReformasLaborales other = (ReformasLaborales) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ReformasLaborales[ secuencia=" + secuencia + " ]";
    }
}
