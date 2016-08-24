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
@Table(name = "TIPOSEXAMENES")
public class TiposExamenes implements Serializable {

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
    @Column(name = "MEDIDA")
    private String medida;
    @Column(name = "MINIMONORMAL")
    private BigInteger minimonormal;
    @Column(name = "MAXIMONORMAL")
    private BigInteger maximonormal;
    @Column(name = "DIASRECURRENCIA")
    private BigInteger diasrecurrencia;

    public TiposExamenes() {
    }

    public TiposExamenes(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposExamenes(BigInteger secuencia, Integer codigo, String nombre) {
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
        if (nombre != null) {
            this.nombre = nombre.toUpperCase();
        } else {
            this.nombre = nombre;
        }
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public BigInteger getMinimonormal() {
        return minimonormal;
    }

    public void setMinimonormal(BigInteger minimonormal) {
        this.minimonormal = minimonormal;
    }

    public BigInteger getMaximonormal() {
        return maximonormal;
    }

    public void setMaximonormal(BigInteger maximonormal) {
        this.maximonormal = maximonormal;
    }

    public BigInteger getDiasrecurrencia() {
        return diasrecurrencia;
    }

    public void setDiasrecurrencia(BigInteger diasrecurrencia) {
        this.diasrecurrencia = diasrecurrencia;
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
        if (!(object instanceof TiposExamenes)) {
            return false;
        }
        TiposExamenes other = (TiposExamenes) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tiposexamenes[ secuencia=" + secuencia + " ]";
    }

}
