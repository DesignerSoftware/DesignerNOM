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
 * @author Administrator
 */
@Entity
@Table(name = "HVEXPERIENCIASLABORALES")
public class HvExperienciasLaborales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 50)
    @Column(name = "EMPRESA")
    private String empresa;
    @Column(name = "FECHADESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadesde;
    @Column(name = "FECHAHASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechahasta;
    @Size(max = 100)
    @Column(name = "JEFEINMEDIATO")
    private String jefeinmediato;
    @Column(name = "TELEFONO")
    private Long telefono;
    @Size(max = 50)
    @Column(name = "CARGO")
    private String cargo;
    @Size(max = 4000)
    @Column(name = "ALCANCE")
    private String alcance;
    @JoinColumn(name = "MOTIVORETIRO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private MotivosRetiros motivoretiro;
    @JoinColumn(name = "HOJADEVIDA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private HVHojasDeVida hojadevida;
    @JoinColumn(name = "SECTORECONOMICO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private SectoresEconomicos sectoreconomico;
    @Transient
    private String strFechaDesde;
    @Transient
    private String strFechaHasta;

    public HvExperienciasLaborales() {
    }

    public HvExperienciasLaborales(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public HvExperienciasLaborales(BigInteger secuencia, Date fechadesde) {
        this.secuencia = secuencia;
        this.fechadesde = fechadesde;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Date getFechadesde() {
        //System.out.println("Entidad GET fechadesde : "+fechadesde);
        return fechadesde;
    }

    public void setFechadesde(Date fechita) {
        this.fechadesde = fechita;
        System.out.println("Entidad SET fechadesde : " + fechadesde);
    }

    public String getStrFechaDesde() {
        if (fechadesde != null) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            strFechaDesde = formatoFecha.format(fechadesde);
        } else {
            strFechaDesde = " ";
        }
        return strFechaDesde;
    }

    public void setStrFechaDesde(String strFechaDesde) throws ParseException {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fechadesde = formatoFecha.parse(strFechaDesde);
        this.strFechaDesde = strFechaDesde;
    }

    public Date getFechahasta() {
        return fechahasta;
    }

    public void setFechahasta(Date fechahasta) {
        this.fechahasta = fechahasta;
    }

    public String getStrFechaHasta() {
        if (fechahasta != null) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            strFechaHasta = formatoFecha.format(fechahasta);
        } else {
            strFechaHasta = " ";
        }
        return strFechaHasta;
    }

    public void setStrFechaHasta(String strFechaHasta) throws ParseException {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fechahasta = formatoFecha.parse(strFechaHasta);
        this.strFechaHasta = strFechaHasta;
    }

    public String getJefeinmediato() {
        return jefeinmediato;
    }

    public void setJefeinmediato(String jefeinmediato) {
        this.jefeinmediato = jefeinmediato;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getAlcance() {
        return alcance;
    }

    public void setAlcance(String alcance) {
        this.alcance = alcance;
    }

    public MotivosRetiros getMotivoretiro() {
        return motivoretiro;
    }

    public void setMotivoretiro(MotivosRetiros motivoretiro) {
        this.motivoretiro = motivoretiro;
    }

    public HVHojasDeVida getHojadevida() {
        return hojadevida;
    }

    public void setHojadevida(HVHojasDeVida hojadevida) {
        this.hojadevida = hojadevida;
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
        if (!(object instanceof HvExperienciasLaborales)) {
            return false;
        }
        HvExperienciasLaborales other = (HvExperienciasLaborales) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.HvExperienciasLaborales[ secuencia=" + secuencia + " ]";
    }

    public SectoresEconomicos getSectoreconomico() {
        return sectoreconomico;
    }

    public void setSectoreconomico(SectoresEconomicos sectoreconomico) {
        this.sectoreconomico = sectoreconomico;
    }

}
