package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "CONCEPTOSJURIDICOS")
public class ConceptosJuridicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 40)
    @Column(name = "EXPEDIDOPOR")
    private String expedidopor;
    @Size(max = 25)
    @Column(name = "QUIEN")
    private String quien;
    @Size(max = 1000)
    @Column(name = "TEXTO")
    private String texto;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @Transient
    private String strFecha;

    public ConceptosJuridicos() {
    }

    public ConceptosJuridicos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getStrFecha() {
         if (fecha != null) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            strFecha = formatoFecha.format(fecha);
        } else {
            strFecha = " ";
        }
        return strFecha;
    }

    public void setStrFecha(String strFecha) throws ParseException {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fecha = formatoFecha.parse(strFecha);
        this.strFecha = strFecha;
    }

    public String getExpedidopor() {
        return expedidopor;
    }

    public void setExpedidopor(String expedidopor) {
        this.expedidopor = expedidopor.toUpperCase();
    }

    public String getQuien() {
        return quien;
    }

    public void setQuien(String quien) {
        this.quien = quien.toUpperCase();
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto.toUpperCase();
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof ConceptosJuridicos)) {
            return false;
        }
        ConceptosJuridicos other = (ConceptosJuridicos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ConceptosJuridicos[ secuencia=" + secuencia + " ]";
    }

}
