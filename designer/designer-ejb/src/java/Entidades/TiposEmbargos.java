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
@Table(name = "TIPOSEMBARGOS")
public class TiposEmbargos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "MANEJASALDO")
    private String manejaSaldo;
    @Column(name = "CODIGO")
    private Integer codigo;
    @Transient
    private Boolean manejaSaldoPromedio;

    public TiposEmbargos() {
    }

    public TiposEmbargos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposEmbargos(BigInteger secuencia, String descripcion) {
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
        if(descripcion == null){
            descripcion = " ";
        }
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion.toUpperCase();
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
        if (!(object instanceof TiposEmbargos)) {
            return false;
        }
        TiposEmbargos other = (TiposEmbargos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tiposembargos[ secuencia=" + secuencia + " ]";
    }

    public String getManejaSaldo() {
        if (manejaSaldo == null) {
            manejaSaldo = "";
        }
        return manejaSaldo;
    }

    public void setManejaSaldo(String manejaSaldo) {
        this.manejaSaldo = manejaSaldo;
    }

    public Boolean getManejaSaldoPromedio() {
        if (manejaSaldoPromedio == null) {
            getManejaSaldo();
            if (manejaSaldo.equals("N")) {
                manejaSaldoPromedio = false;
            } else if (manejaSaldo.equals("S")) {
                manejaSaldoPromedio = true;
            }
        }
        return manejaSaldoPromedio;
    }

    public void setManejaSaldoPromedio(Boolean manejaSaldoPromedio) {
        this.manejaSaldoPromedio = manejaSaldoPromedio;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
