package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "INSTANCIAS")
public class Instancias implements Serializable {
    
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
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INSTANCIA")
    private short instancia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMEROREGISTROS")
    private int numeroregistros;
    @Column(name = "CODIGO")
    private Short codigo;
    @Size(max = 60)
    @Column(name = "DESCRIPCION")
    private String descripcion;

    public Instancias() {
    }

    public Instancias(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Instancias(BigDecimal secuencia, String estado, Date fechainicial, short instancia, int numeroregistros) {
        this.secuencia = secuencia;
        this.estado = estado;
        this.fechainicial = fechainicial;
        this.instancia = instancia;
        this.numeroregistros = numeroregistros;
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

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public short getInstancia() {
        return instancia;
    }

    public void setInstancia(short instancia) {
        this.instancia = instancia;
    }

    public int getNumeroregistros() {
        return numeroregistros;
    }

    public void setNumeroregistros(int numeroregistros) {
        this.numeroregistros = numeroregistros;
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof Instancias)) {
            return false;
        }
        Instancias other = (Instancias) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Instancias[ secuencia=" + secuencia + " ]";
    }
    
}
