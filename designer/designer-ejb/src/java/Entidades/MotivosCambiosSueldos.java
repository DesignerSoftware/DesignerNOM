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
@Table(name = "MOTIVOSCAMBIOSSUELDOS")
public class MotivosCambiosSueldos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Integer codigo;
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 1)
    @Column(name = "SUELDOPROMEDIO")
    private String sueldopromedio;
    @Transient
    private Boolean estadoSueldoPromedio;

    public MotivosCambiosSueldos() {
    }

    public MotivosCambiosSueldos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public MotivosCambiosSueldos(BigInteger secuencia, Integer codigo, String nombre) {
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

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null) {
            this.nombre = nombre;
        } else {
            this.nombre = nombre.toUpperCase();
        }
    }

    public String getSueldopromedio() {
        return sueldopromedio;
    }

    public void setSueldopromedio(String sueldopromedio) {
        this.sueldopromedio = sueldopromedio;
    }

    public Boolean getEstadoSueldoPromedio() {
        if (this.sueldopromedio == null || this.sueldopromedio.equals("N")) {
            this.estadoSueldoPromedio = false;
        } else {
            this.estadoSueldoPromedio = true;
        }
        return estadoSueldoPromedio;
    }

    public void setEstadoSueldoPromedio(Boolean estadoSueldoPromedio) {
        if (estadoSueldoPromedio == null || estadoSueldoPromedio == false) {
            sueldopromedio = "N";
        } else {
            this.sueldopromedio = "S";
        }
        this.estadoSueldoPromedio = estadoSueldoPromedio;
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
        if (!(object instanceof MotivosCambiosSueldos)) {
            return false;
        }
        MotivosCambiosSueldos other = (MotivosCambiosSueldos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Motivoscambiossueldos[ secuencia=" + secuencia + " ]";
    }

}
