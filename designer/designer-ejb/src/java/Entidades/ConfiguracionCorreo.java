package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@Table(name = "CONFIGURACIONCORREOS")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "ConfiguracionCorreo.findAll", query = "SELECT c FROM ConfiguracionCorreo c")})
public class ConfiguracionCorreo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "SERVIDORSMTP")
    private String servidorSmtp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "PUERTO")
    private String puerto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "AUTENTICADO")
    private String autenticado;
    @Size(max = 1)
    @Column(name = "STARTTLS")
    private String starttls;
    @Size(max = 1)
    @Column(name = "USARSSL")
    private String usarssl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "REMITENTE")
    private String remitente;
    @Size(max = 50)
    @Column(name = "CLAVE")
    private String clave;

    public ConfiguracionCorreo() {
    }

    public ConfiguracionCorreo(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public ConfiguracionCorreo(BigDecimal secuencia, String servidorSmtp, String puerto, String autenticado, String remitente) {
        this.secuencia = secuencia;
        this.servidorSmtp = servidorSmtp;
        this.puerto = puerto;
        this.autenticado = autenticado;
        this.remitente = remitente;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public String getServidorSmtp() {
        return servidorSmtp;
    }

    public void setServidorSmtp(String servidorSmtp) {
        this.servidorSmtp = servidorSmtp;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getAutenticado() {
        return autenticado;
    }

    public void setAutenticado(String autenticado) {
        this.autenticado = autenticado;
    }

    public String getStarttls() {
        return starttls;
    }

    public void setStarttls(String starttls) {
        this.starttls = starttls;
    }

    public String getUsarssl() {
        return usarssl;
    }

    public void setUsarssl(String usarssl) {
        this.usarssl = usarssl;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the secuencia fields are not set
        if (!(object instanceof ConfiguracionCorreo)) {
            return false;
        }
        ConfiguracionCorreo other = (ConfiguracionCorreo) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ConfiguracionCorreo[ secuencia=" + secuencia + " ]";
    }

}
