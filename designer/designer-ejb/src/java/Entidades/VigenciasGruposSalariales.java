package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "VIGENCIASGRUPOSSALARIALES")
public class VigenciasGruposSalariales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "FECHAVIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechavigencia;
    @Column(name = "VALOR")
    private BigInteger valor;
    @JoinColumn(name = "GRUPOSALARIAL", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private GruposSalariales gruposalarial;

    public VigenciasGruposSalariales() {
    }

    public VigenciasGruposSalariales(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public VigenciasGruposSalariales(BigInteger secuencia, Date fechavigencia, BigInteger valor) {
        this.secuencia = secuencia;
        this.fechavigencia = fechavigencia;
        this.valor = valor;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechavigencia() {
        return fechavigencia;
    }

    public void setFechavigencia(Date fechavigencia) {
        this.fechavigencia = fechavigencia;
    }

    public BigInteger getValor() {
        return valor;
    }

    public void setValor(BigInteger valor) {
        this.valor = valor;
    }

    public GruposSalariales getGruposalarial() {
        return gruposalarial;
    }

    public void setGruposalarial(GruposSalariales gruposalarial) {
        this.gruposalarial = gruposalarial;
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
        if (!(object instanceof VigenciasGruposSalariales)) {
            return false;
        }
        VigenciasGruposSalariales other = (VigenciasGruposSalariales) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VigenciasGruposSalariales[ secuencia=" + secuencia + " ]";
    }
    
}
