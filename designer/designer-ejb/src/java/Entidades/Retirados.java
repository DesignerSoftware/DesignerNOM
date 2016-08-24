package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "RETIRADOS")
public class Retirados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    
    @Column(name = "FECHARETIRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecharetiro;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "VIGENCIATIPOTRABAJADOR", referencedColumnName = "SECUENCIA")
    @OneToOne
    private VigenciasTiposTrabajadores vigenciatipotrabajador;
    @JoinColumn(name = "MOTIVORETIRO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private MotivosRetiros motivoretiro;

    public Retirados() {
    }

    public Retirados(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Retirados(BigInteger secuencia, Date fecharetiro) {
        this.secuencia = secuencia;
        this.fecharetiro = fecharetiro;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFecharetiro() {
        return fecharetiro;
    }

    public void setFecharetiro(Date fecharetiro) {
        this.fecharetiro = fecharetiro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public VigenciasTiposTrabajadores getVigenciatipotrabajador() {
        return vigenciatipotrabajador;
    }

    public void setVigenciatipotrabajador(VigenciasTiposTrabajadores vigenciatipotrabajador) {
        this.vigenciatipotrabajador = vigenciatipotrabajador;
    }

    public MotivosRetiros getMotivoretiro() {
        return motivoretiro;
    }

    public void setMotivoretiro(MotivosRetiros motivoretiro) {
        this.motivoretiro = motivoretiro;
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
        if (!(object instanceof Retirados)) {
            return false;
        }
        Retirados other = (Retirados) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Retirados[ secuencia=" + secuencia + " ]";
    }
}
