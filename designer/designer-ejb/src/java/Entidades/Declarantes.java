package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "DECLARANTES")
public class Declarantes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @Column(name = "RETENCIONDESEADA")
    private BigInteger retenciondeseada;
    @Size(max = 1)
    @Column(name = "DECLARANTE")
    private String declarante;
    @JoinColumn(name = "RETENCIONMINIMA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private RetencionesMinimas retencionminima;
    @JoinColumn(name = "PERSONA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Personas persona;
    @Transient
    private boolean estadoDeclarante;
    @Transient
    private BigDecimal equivalencia;

    public Declarantes() {
    }

    public Declarantes(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Declarantes(BigInteger secuencia, Date fechainicial, Date fechafinal) {
        this.secuencia = secuencia;
        this.fechainicial = fechainicial;
        this.fechafinal = fechafinal;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public BigInteger getRetenciondeseada() {
        return retenciondeseada;
    }

    public void setRetenciondeseada(BigInteger retenciondeseada) {
        this.retenciondeseada = retenciondeseada;
    }

    public String getDeclarante() {
        if(declarante == null){
            declarante = "N";
        }
        return declarante;
    }

    public void setDeclarante(String declarante) {
        this.declarante = declarante;
    }

    public RetencionesMinimas getRetencionminima() {
        if(retencionminima == null){
            retencionminima = new RetencionesMinimas();
        }
        return retencionminima;
    }

    public void setRetencionminima(RetencionesMinimas retencionminima) {
        this.retencionminima = retencionminima;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public boolean isEstadoDeclarante() {
        if (declarante != null) {
            if (declarante.equals("S")) {
                estadoDeclarante = true;
            } else if (declarante.equals("N")){
                estadoDeclarante = false;
            }
        } else {
            estadoDeclarante = false;
        }
        return estadoDeclarante;
    }

    public void setEstadoDeclarante(boolean estadoDeclarante) {
        if (estadoDeclarante == true) {
            declarante = "S";
        } else {
            declarante = "N";
        }
        this.estadoDeclarante = estadoDeclarante;
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
        if (!(object instanceof Declarantes)) {
            return false;
        }
        Declarantes other = (Declarantes) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Declarantes[ secuencia=" + secuencia + " ]";
    }

}
