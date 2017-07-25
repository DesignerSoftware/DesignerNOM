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
 * @author Administrator
 */
@Entity
@Table(name = "FAMILIARES")
public class Familiares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Size(max = 1)
    @Column(name = "BENEFICIARIO")
    private String beneficiario;
    @Column(name = "FINALPENSION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finalpension;
    @Column(name = "INICIOPENSION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date iniciopension;
    @Size(max = 1)
    @Column(name = "SERVICIOMEDICO")
    private String serviciomedico;
    @Size(max = 1)
    @Column(name = "SUBSIDIOFAMILIAR")
    private String subsidiofamiliar;
    @Column(name = "PORCENTAJEPENSION")
    private BigDecimal porcentajepension;
    @Size(max = 100)
    @Column(name = "OCUPACION")
    private String ocupacion;
    @Size(max = 1)
    @Column(name = "CONVIVECONPERSONA")
    private String conviveconpersona;
    @Size(max = 1)
    @Column(name = "UPCADICIONAL")
    private String upcadicional;
    @Size(max = 1)
    @Column(name = "REQUERIDOAFILIACION")
    private String requeridoafiliacion;
    @Size(max = 1)
    @Column(name = "CONDICIONMAYOR18")
    private String condicionmayor18;
    @Column(name = "VALORUPCADICIONAL")
    private BigInteger valorupcadicional;
    @NotNull
    @JoinColumn(name = "TIPOFAMILIAR", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposFamiliares tipofamiliar;
    @NotNull
    @JoinColumn(name = "PERSONAFAMILIAR", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Personas personafamiliar;
    @NotNull
    @JoinColumn(name = "PERSONA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Personas persona;
    @Transient
    private boolean sm;
    @Transient
    private boolean sf;
    @Transient
    private boolean be;
    @Transient
    private boolean upcad;

    public Familiares() {
    }

    public Familiares(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public Date getFinalpension() {
        return finalpension;
    }

    public void setFinalpension(Date finalpension) {
        this.finalpension = finalpension;
    }

    public Date getIniciopension() {
        return iniciopension;
    }

    public void setIniciopension(Date iniciopension) {
        this.iniciopension = iniciopension;
    }

    public String getServiciomedico() {
        return serviciomedico;
    }

    public void setServiciomedico(String serviciomedico) {
        this.serviciomedico = serviciomedico;
    }

    public String getSubsidiofamiliar() {
        return subsidiofamiliar;
    }

    public void setSubsidiofamiliar(String subsidiofamiliar) {
        this.subsidiofamiliar = subsidiofamiliar;
    }

    public BigDecimal getPorcentajepension() {
        return porcentajepension;
    }

    public void setPorcentajepension(BigDecimal porcentajepension) {
        this.porcentajepension = porcentajepension;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getConviveconpersona() {
        return conviveconpersona;
    }

    public void setConviveconpersona(String conviveconpersona) {
        this.conviveconpersona = conviveconpersona;
    }

    public String getUpcadicional() {
        return upcadicional;
    }

    public void setUpcadicional(String upcadicional) {
        this.upcadicional = upcadicional;
    }

    public String getRequeridoafiliacion() {
        return requeridoafiliacion;
    }

    public void setRequeridoafiliacion(String requeridoafiliacion) {
        this.requeridoafiliacion = requeridoafiliacion;
    }

    public String getCondicionmayor18() {
        return condicionmayor18;
    }

    public void setCondicionmayor18(String condicionmayor18) {
        this.condicionmayor18 = condicionmayor18;
    }

    public BigInteger getValorupcadicional() {
        return valorupcadicional;
    }

    public void setValorupcadicional(BigInteger valorupcadicional) {
        this.valorupcadicional = valorupcadicional;
    }

    public TiposFamiliares getTipofamiliar() {
        return tipofamiliar;
    }

    public void setTipofamiliar(TiposFamiliares tipofamiliar) {
        this.tipofamiliar = tipofamiliar;
    }

    public Personas getPersonafamiliar() {
        return personafamiliar;
    }

    public void setPersonafamiliar(Personas personafamiliar) {
        this.personafamiliar = personafamiliar;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public boolean isSm() {
        if ("S".equals(serviciomedico)) {
            sm = true;
        } else {
            sm = false;
        }
        return sm;
    }

    public void setSm(boolean sm) {
        this.sm = sm;
        if (sm) {
            this.serviciomedico = "S";
        } else {
            this.serviciomedico = "N";
        }
    }

    public boolean isSf() {
        if ("S".equals(subsidiofamiliar)) {
            sf = true;
        } else {
            sf = false;
        }
        return sf;
    }

    public void setSf(boolean sf) {
        this.sf = sf;
        if (sf) {
            this.subsidiofamiliar = "S";
        } else {
            this.subsidiofamiliar = "N";
        }
    }

    public boolean isBe() {
        if ("S".equals(beneficiario)) {
            be = true;
        } else {
            be = false;
        }
        return be;
    }

    public void setBe(boolean be) {
        this.be = be;
        if (be) {
            this.beneficiario = "S";
        } else {
            this.beneficiario = "N";
        }
    }

    public boolean isUpcad() {
        if ("S".equals(upcadicional)) {
            upcad = true;
        } else {
            upcad = false;
        }
        return upcad;
    }

    public void setUpcad(boolean upcad) {
        this.upcad = upcad;
        if (upcad) {
            this.upcadicional = "S";
        } else {
            this.upcadicional = "N";
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
        if (!(object instanceof Familiares)) {
            return false;
        }
        Familiares other = (Familiares) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Familiares[ secuencia=" + secuencia + " ]";
    }

}
