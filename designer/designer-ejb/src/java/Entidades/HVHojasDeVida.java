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
@Table(name = "HVHOJASDEVIDA")
public class HVHojasDeVida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 1000)
    @Column(name = "PERFILPROFESIONAL")
    private String perfilprofesional;
    @Column(name = "HIJOS")
    private Short hijos;
    @Size(max = 1000)
    @Column(name = "INFORMEEJECUTIVO")
    private String informeejecutivo;
    @Size(max = 10)
    @Column(name = "CONCLUSION")
    private String conclusion;
    @JoinColumn(name = "PERSONA", referencedColumnName = "SECUENCIA")
    @OneToOne(optional = false)
    private Personas persona;
    @JoinColumn(name = "ESTADOCIVIL", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private EstadosCiviles estadocivil;
    @JoinColumn(name = "CARGO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cargos cargo;

    public HVHojasDeVida() {
    }

    public HVHojasDeVida(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getPerfilprofesional() {
        return perfilprofesional;
    }

    public void setPerfilprofesional(String perfilprofesional) {
        this.perfilprofesional = perfilprofesional;
    }

    public Short getHijos() {
        return hijos;
    }

    public void setHijos(Short hijos) {
        this.hijos = hijos;
    }

    public String getInformeejecutivo() {
        return informeejecutivo;
    }

    public void setInformeejecutivo(String informeejecutivo) {
        this.informeejecutivo = informeejecutivo;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public EstadosCiviles getEstadocivil() {
        return estadocivil;
    }

    public void setEstadocivil(EstadosCiviles estadocivil) {
        this.estadocivil = estadocivil;
    }

    public Cargos getCargo() {
        if (cargo == null) {
            cargo = new Cargos();
        }
        return cargo;
    }

    public void setCargo(Cargos cargo) {
        this.cargo = cargo;
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
        if (!(object instanceof HVHojasDeVida)) {
            return false;
        }
        HVHojasDeVida other = (HVHojasDeVida) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.HVHojasDeVida[ secuencia=" + secuencia + " ]";
    }
}
