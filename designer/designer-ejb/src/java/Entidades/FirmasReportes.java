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
@Table(name = "FIRMASREPORTES")
public class FirmasReportes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Integer codigo;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "SUBTITULOFIRMA")
    private String subtitulofirma;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @JoinColumn(name = "PERSONAFIRMA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Personas personafirma;
    @JoinColumn(name = "CARGOFIRMA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cargos cargofirma;

    public FirmasReportes() {
    }

    public FirmasReportes(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public FirmasReportes(BigInteger secuencia, Integer codigo, String descripcion, String subtitulofirma) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.subtitulofirma = subtitulofirma;
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
        this.descripcion = descripcion;
    }

    public String getSubtitulofirma() {
        return subtitulofirma;
    }

    public void setSubtitulofirma(String subtitulofirma) {
        if (subtitulofirma != null) {
            this.subtitulofirma = subtitulofirma.toUpperCase();
        } else {
            this.subtitulofirma = subtitulofirma;
        }
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Personas getPersonafirma() {
        return personafirma;
    }

    public void setPersonafirma(Personas personafirma) {
        this.personafirma = personafirma;
    }

    public Cargos getCargofirma() {
        return cargofirma;
    }

    public void setCargofirma(Cargos cargofirma) {
        this.cargofirma = cargofirma;
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
        if (!(object instanceof FirmasReportes)) {
            return false;
        }
        FirmasReportes other = (FirmasReportes) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.FirmasReportes[ secuencia=" + secuencia + " ]";
    }

}
