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
@Table(name = "TERCEROS")
public class Terceros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGOALTERNATIVO")
    private Long codigoalternativo;
    @Basic(optional = false)
    @NotNull
    @Size(max = 30)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "DIGITOVERIFICACION")
    private Short digitoverificacion;
    @Column(name = "CODIGO")
    private Long codigo;
    @Size(max = 6)
    @Column(name = "CODIGOSS")
    private String codigoss;
    @Size(max = 6)
    @Column(name = "CODIGOSP")
    private String codigosp;
    @Size(max = 6)
    @Column(name = "CODIGOSC")
    private String codigosc;
    @Size(max = 30)
    @Column(name = "NITALTERNATIVO")
    private String nitalternativo;
    @Column(name = "CODIGOALTERNATIVODEUDOR")
    private Long codigoalternativodeudor;
    @Size(max = 1)
    @Column(name = "TIPONIT")
    private String tiponit;
    @Size(max = 5)
    @Column(name = "CODIGOTERCEROSAP")
    private String codigotercerosap;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NIT")
    private long nit;
    @JoinColumn(name = "TERCEROCONSOLIDADOR", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Terceros terceroconsolidador;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @JoinColumn(name = "CIUDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Ciudades ciudad;
    @JoinColumn(name = "CENTROCOSTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private CentrosCostos centrocosto;
    @Transient
    private String strNit;
    @Transient
    private String strCodAlt;

    public Terceros() {
    }

    public Terceros(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Terceros(BigInteger secuencia, long nit, String nombre) {
        this.secuencia = secuencia;
        this.nit = nit;
        this.nombre = nombre;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getStrNit() {
        getNit();
        if (nit > 0) {
            strNit = String.valueOf(nit);
        } else {
            strNit = "";
        }
        return strNit;
    }

    public void setStrNit(String strNit) {
        if (strNit.equals("")) {
            nit = 0;
        } else {
            nit = Long.parseLong(strNit);
        }
        this.strNit = strNit;
    }

    public Long getCodigoalternativo() {
        return codigoalternativo;
    }

    public void setCodigoalternativo(Long codigoalternativo) {
        this.codigoalternativo = codigoalternativo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Short getDigitoverificacion() {
        return digitoverificacion;
    }

    public void setDigitoverificacion(Short digitoverificacion) {
        this.digitoverificacion = digitoverificacion;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getCodigoss() {
        return codigoss;
    }

    public void setCodigoss(String codigoss) {
        this.codigoss = codigoss;
    }

    public String getCodigosp() {
        return codigosp;
    }

    public void setCodigosp(String codigosp) {
        this.codigosp = codigosp;
    }

    public String getCodigosc() {
        return codigosc;
    }

    public void setCodigosc(String codigosc) {
        this.codigosc = codigosc;
    }

    public String getNitalternativo() {
        return nitalternativo;
    }

    public void setNitalternativo(String nitalternativo) {
        this.nitalternativo = nitalternativo;
    }

    public Long getCodigoalternativodeudor() {
        return codigoalternativodeudor;
    }

    public void setCodigoalternativodeudor(Long codigoalternativodeudor) {
        this.codigoalternativodeudor = codigoalternativodeudor;
    }

    public String getTiponit() {
        return tiponit;
    }

    public void setTiponit(String tiponit) {
        this.tiponit = tiponit;
    }

    public String getCodigotercerosap() {
        return codigotercerosap;
    }

    public void setCodigotercerosap(String codigotercerosap) {
        this.codigotercerosap = codigotercerosap;
    }

    public Terceros getTerceroconsolidador() {
        if (terceroconsolidador == null) {
            terceroconsolidador = new Terceros();
        }
        return terceroconsolidador;
    }

    public void setTerceroconsolidador(Terceros terceroconsolidador) {
        this.terceroconsolidador = terceroconsolidador;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Ciudades getCiudad() {
        if (ciudad == null) {
            ciudad = new Ciudades();
        }
        return ciudad;
    }

    public void setCiudad(Ciudades ciudad) {
        this.ciudad = ciudad;
    }

    public CentrosCostos getCentrocosto() {
        return centrocosto;
    }

    public void setCentrocosto(CentrosCostos centrocosto) {
        this.centrocosto = centrocosto;
    }

    public long getNit() {
        return nit;
    }

    public void setNit(long nit) {
        this.nit = nit;
    }

    public String getStrCodAlt() {
        getCodigoalternativo();
        if (codigoalternativo != null) {
        if (codigoalternativo > 0) {
            strCodAlt = String.valueOf(codigoalternativo);
        }else{
            strCodAlt = "";
        }
        } else {
            strCodAlt = "";
        }
        return strCodAlt;
    }

    public void setStrCodAlt(String strCodAlt) {
        if (strCodAlt.equals("")) {
            codigoalternativo = Long.valueOf(0);
        } else {
            codigoalternativo = Long.parseLong(strCodAlt);
        }

        this.strCodAlt = strCodAlt;
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
        if (!(object instanceof Terceros)) {
            return false;
        }
        Terceros other = (Terceros) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Terceros[ secuencia=" + secuencia + " ]";
    }

}
