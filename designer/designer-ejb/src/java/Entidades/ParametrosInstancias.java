package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "PARAMETROSINSTANCIAS")
public class ParametrosInstancias implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ESTADO")
    private String estado;
    @JoinColumn(name = "PARAMETRO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Parametros parametro;
    @JoinColumn(name = "INSTANCIA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Instancias instancia;

    public ParametrosInstancias() {
    }

    public ParametrosInstancias(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public ParametrosInstancias(BigDecimal secuencia, String estado) {
        this.secuencia = secuencia;
        this.estado = estado;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Parametros getParametro() {
        return parametro;
    }

    public void setParametro(Parametros parametro) {
        this.parametro = parametro;
    }

    public Instancias getInstancia() {
        return instancia;
    }

    public void setInstancia(Instancias instancia) {
        this.instancia = instancia;
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
        if (!(object instanceof ParametrosInstancias)) {
            return false;
        }
        ParametrosInstancias other = (ParametrosInstancias) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ParametrosInstancias[ secuencia=" + secuencia + " ]";
    }
    
}
