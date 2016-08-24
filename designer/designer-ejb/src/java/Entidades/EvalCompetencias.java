package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author betelgeuse
 */
@Entity
@Table(name = "EVALCOMPETENCIAS")
public class EvalCompetencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private Integer codigo;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 1000)
    @Column(name = "DESCOMPETENCIA")
    private String desCompetencia;

    public EvalCompetencias() {
    }

    public EvalCompetencias(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public EvalCompetencias(BigInteger secuencia, Integer codigo, String descripcion) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion != null) {
            this.descripcion = descripcion.toUpperCase();
        } else {
            this.descripcion = descripcion;
        }
    }

    public String getDesCompetencia() {
        return desCompetencia;
    }

    public void setDesCompetencia(String desCompetencia) {
        if (desCompetencia != null) {
            this.desCompetencia = desCompetencia.toUpperCase();
        } else {
            this.desCompetencia = desCompetencia;
        }
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
        if (!(object instanceof EvalCompetencias)) {
            return false;
        }
        EvalCompetencias other = (EvalCompetencias) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.EvalCompetencias[ secuencia=" + secuencia + " ]";
    }

}
