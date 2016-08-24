package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "TIPOSSUELDOS")
public class TiposSueldos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "CODIGO")
    private Short codigo;
    @Size(max = 1)
    @Column(name = "CAPACIDADENDEUDAMIENTO")
    private String capacidadendeudamiento;
    @Size(max = 1)
    @Column(name = "BASICO")
    private String basico;
    @Size(max = 1)
    @Column(name = "ADICIONALBASICO")
    private String adicionalbasico;
    @Transient
    private String strCapacidad;
    @Transient
    private String strBasico;
    @Transient
    private String strAdicional;

    public TiposSueldos() {
    }

    public TiposSueldos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposSueldos(BigInteger secuencia, String descripcion) {
        this.secuencia = secuencia;
        this.descripcion = descripcion;
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

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public String getCapacidadendeudamiento() {
        if (capacidadendeudamiento == null) {
            capacidadendeudamiento = "N";
        }
        return capacidadendeudamiento;
    }

    public void setCapacidadendeudamiento(String capacidadendeudamiento) {
        this.capacidadendeudamiento = capacidadendeudamiento;
    }

    public String getBasico() {
        if (basico == null) {
            basico = "N";
        }
        return basico;
    }

    public void setBasico(String basico) {
        this.basico = basico;
    }

    public String getAdicionalbasico() {
        if (adicionalbasico == null) {
            adicionalbasico = "N";
        }
        return adicionalbasico;
    }

    public void setAdicionalbasico(String adicionalbasico) {
        this.adicionalbasico = adicionalbasico;
    }

    public String getStrCapacidad() {
        getCapacidadendeudamiento();
        if (capacidadendeudamiento.equalsIgnoreCase("N")) {
            strCapacidad = "SI";
        } else {
            strCapacidad = "NO";
        }
        return strCapacidad;
    }

    public void setStrCapacidad(String strCapacidad) {
        this.strCapacidad = strCapacidad;
    }

    public String getStrBasico() {
        getBasico();
        if (basico.equalsIgnoreCase("N")) {
            strBasico = "SI";
        } else {
            strBasico = "NO";
        }
        return strBasico;
    }

    public void setStrBasico(String strBasico) {
        this.strBasico = strBasico;
    }

    public String getStrAdicional() {
        getAdicionalbasico();
        if (adicionalbasico.equalsIgnoreCase("N")) {
            strAdicional = "SI";
        } else {
            strAdicional = "NO";
        }
        return strAdicional;
    }

    public void setStrAdicional(String strAdicional) {
        this.strAdicional = strAdicional;
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
        if (!(object instanceof TiposSueldos)) {
            return false;
        }
        TiposSueldos other = (TiposSueldos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tipossueldos[ secuencia=" + secuencia + " ]";
    }
}
