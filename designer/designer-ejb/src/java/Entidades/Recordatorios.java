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
@Table(name = "RECORDATORIOS")
public class Recordatorios implements Serializable {

    @Size(max = 30)
    @Column(name = "NOMBREIMAGEN")
    private String nombreimagen;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 20)
    @Column(name = "TIPO")
    private String tipo;
    @Size(max = 500)
    @Column(name = "MENSAJE")
    private String mensaje;
    @Size(max = 4000)
    @Column(name = "CONSULTA")
    private String consulta;
    @Column(name = "DIA")
    private Short dia;
    @Column(name = "MES")
    private Short mes;
    @Column(name = "ANO")
    private Short ano;
    @Column(name = "DIASPREVIOS")
    private Short diasprevios;
    @JoinColumn(name = "USUARIO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Usuarios usuario;
    @Transient
    private String estadoAno;
    @Transient
    private String estadoMes;
    @Transient
    private String estadoDia;

    public Recordatorios() {
    }

    public Recordatorios(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        if (mensaje == null) {
            mensaje = " ";
        }
        return mensaje.toUpperCase();
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getConsulta() {
        return consulta;
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    public Short getDia() {
        return dia;
    }

    public void setDia(Short dia) {
        this.dia = dia;
    }

    public Short getMes() {
        return mes;
    }

    public void setMes(Short mes) {
        this.mes = mes;
    }

    public Short getAno() {
        return ano;
    }

    public void setAno(Short ano) {
        this.ano = ano;
    }

    public Short getDiasprevios() {
        return diasprevios;
    }

    public void setDiasprevios(Short diasprevios) {
        this.diasprevios = diasprevios;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public String getEstadoAno() {
        if (ano == null) {
            estadoAno = "";
        } else {
            int value = ano.intValue();
            if (value == 2005) {
                estadoAno = "2005";
            } else if (value == 2006) {
                estadoAno = "2006";
            } else if (value == 2007) {
                estadoAno = "2007";
            } else if (value == 2008) {
                estadoAno = "2008";
            } else if (value == 2009) {
                estadoAno = "2009";
            } else if (value == 2010) {
                estadoAno = "2010";
            } else if (value == 2011) {
                estadoAno = "2011";
            } else if (value == 2012) {
                estadoAno = "2012";
            } else if (value == 2013) {
                estadoAno = "2013";
            } else if (value == 2014) {
                estadoAno = "2014";
            } else if (value == 2015) {
                estadoAno = "2015";
            } else if (value == 2016) {
                estadoAno = "2016";
            } else if (value == 2017) {
                estadoAno = "2017";
            } else if (value == 2018) {
                estadoAno = "2018";
            } else if (value == 2019) {
                estadoAno = "2019";
            } else if (value == 2020) {
                estadoAno = "2020";
            } else if (value == 2021) {
                estadoAno = "2021";
            }else if (value == 2022) {
                estadoAno = "2022";
            } else if (value == 2023) {
                estadoAno = "2023";
            } else if (value == 2024) {
                estadoAno = "2024";
            } else if (value == 2025) {
                estadoAno = "2025";
            } else if (value == 0) {
                estadoAno = "TODOS LOS AÑOS";
            }
        }
        return estadoAno;
    }

    public void setEstadoAno(String estadoAno) {
        System.out.println("estadoANo" + estadoAno);
          if (estadoAno.equals("")) {
            setAno(null);
        } else if (estadoAno.equalsIgnoreCase("TODOS LOS MESES")) {
            setAno(new Short("0"));
        } else if (estadoAno.equals("2008")) {
            setAno(new Short("2008"));
        } else if (estadoAno.equals("2009")) {
            setAno(new Short("2009"));
        } else if (estadoAno.equals("2010")) {
            setAno(new Short("2010"));
        } else if (estadoAno.equals("2011")) {
            setAno(new Short("2011"));
        } else if (estadoAno.equals("2012")) {
            setAno(new Short("2012"));
        } else if (estadoAno.equals("2013")) {
            setAno(new Short("2013"));
        } else if (estadoAno.equals("2014")) {
            setAno(new Short("2014"));
        } else if (estadoAno.equals("2015")) {
            setAno(new Short("2015"));
        } else if (estadoAno.equals("2016")) {
            setAno(new Short("2016"));
        } else if (estadoAno.equals("2017")) {
            setAno(new Short("2017"));
        } else if (estadoAno.equals("2018")) {
            setAno(new Short("2018"));
        } else if (estadoAno.equals("2019")) {
            setAno(new Short("2019"));
        }else if (estadoAno.equals("2020")) {
            setAno(new Short("2020"));
        }else if (estadoAno.equals("2021")) {
            setAno(new Short("2021"));
        }else if (estadoAno.equals("2022")) {
            setAno(new Short("2022"));
        }else if (estadoAno.equals("2023")) {
            setAno(new Short("2023"));
        }else if (estadoAno.equals("2024")) {
            setAno(new Short("2024"));
        }else if (estadoAno.equals("2025")) {
            setAno(new Short("2025"));
        }
        this.estadoAno = estadoAno;
    }

    public String getEstadoMes() {

        if (mes == null) {
            estadoMes = "";
        } else {

            int value = mes.intValue();
            if (value == 1) {
                estadoMes = "ENERO";
            } else if (value == 2) {
                estadoMes = "FEBRERO";
            } else if (value == 3) {
                estadoMes = "MARZO";
            } else if (value == 4) {
                estadoMes = "ABRIL";
            } else if (value == 5) {
                estadoMes = "MAYO";
            } else if (value == 6) {
                estadoMes = "JUNIO";
            } else if (value == 7) {
                estadoMes = "JULIO";
            } else if (value == 8) {
                estadoMes = "AGOSTO";
            } else if (value == 9) {
                estadoMes = "SEPTIEMBRE";
            } else if (value == 10) {
                estadoMes = "OCTUBRE";
            } else if (value == 11) {
                estadoMes = "NOVIEMBRE";
            } else if (value == 12) {
                estadoMes = "DICIEMBRE";
            } else if (value == 0) {
                estadoMes = "TODOS LOS MESES";
            }
        }
        return estadoMes;
    }

    public void setEstadoMes(String estadoMes) {
        System.out.println("estadoMes" + estadoMes);
        if (estadoMes.equals("")) {
            setMes(null);
        } else if (estadoMes.equalsIgnoreCase("TODOS LOS MESES")) {
            setMes(new Short("0"));
        } else if (estadoMes.equals("ENERO")) {
            setMes(new Short("1"));
        } else if (estadoMes.equals("FEBRERO")) {
            setMes(new Short("2"));
        } else if (estadoMes.equals("MARZO")) {
            setMes(new Short("3"));
        } else if (estadoMes.equals("ABRIL")) {
            setMes(new Short("4"));
        } else if (estadoMes.equals("MAYO")) {
            setMes(new Short("5"));
        } else if (estadoMes.equals("JUNIO")) {
            setMes(new Short("6"));
        } else if (estadoMes.equals("JULIO")) {
            setMes(new Short("7"));
        } else if (estadoMes.equals("AGOSTO")) {
            setMes(new Short("8"));
        } else if (estadoMes.equals("SEPTIEMBRE")) {
            setMes(new Short("9"));
        } else if (estadoMes.equals("OCTUBRE")) {
            setMes(new Short("10"));
        } else if (estadoMes.equals("NOVIEMBRE")) {
            setMes(new Short("11"));
        } else if (estadoMes.equals("DICIEMBRE")) {
            setMes(new Short("12"));
        }
        this.estadoMes = estadoMes;
    }

    public String getEstadoDia() {
        getDia();
        if (dia == null) {
            estadoDia = "";
        } else {

            int value = dia.intValue();
            if (value == 1) {
                estadoDia = "01";
            } else if (value == 2) {
                estadoDia = "02";
            } else if (value == 3) {
                estadoDia = "03";
            } else if (value == 4) {
                estadoDia = "04";
            } else if (value == 5) {
                estadoDia = "05";
            } else if (value == 6) {
                estadoDia = "06";
            } else if (value == 7) {
                estadoDia = "07";
            } else if (value == 8) {
                estadoDia = "08";
            } else if (value == 9) {
                estadoDia = "09";
            } else if (value == 10) {
                estadoDia = "10";
            } else if (value == 11) {
                estadoDia = "11";
            } else if (value == 12) {
                estadoDia = "12";
            } else if (value == 13) {
                estadoDia = "13";
            } else if (value == 14) {
                estadoDia = "14";
            } else if (value == 15) {
                estadoDia = "15";
            } else if (value == 16) {
                estadoDia = "16";
            } else if (value == 17) {
                estadoDia = "17";
            } else if (value == 18) {
                estadoDia = "18";
            } else if (value == 19) {
                estadoDia = "19";
            } else if (value == 20) {
                estadoDia = "20";
            } else if (value == 21) {
                estadoDia = "21";
            } else if (value == 22) {
                estadoDia = "22";
            } else if (value == 23) {
                estadoDia = "23";
            } else if (value == 24) {
                estadoDia = "24";
            } else if (value == 25) {
                estadoDia = "25";
            } else if (value == 26) {
                estadoDia = "26";
            } else if (value == 27) {
                estadoDia = "27";
            } else if (value == 28) {
                estadoDia = "28";
            } else if (value == 29) {
                estadoDia = "29";
            } else if (value == 30) {
                estadoDia = "30";
            } else if (value == 31) {
                estadoDia = "31";
            } else if (value == 0) {
                estadoDia = "TODOS LOS DIAS";
            }
        }
        return estadoDia;
    }

    public void setEstadoDia(String estadoDia) {
        if (estadoDia.equals("")) {
            setDia(null);
        } else if (estadoDia.equals("TODOS LOS DIAS")) {
            setDia(new Short("0"));
        } else if (estadoDia.equals("01")) {
            setDia(new Short("1"));
        } else if (estadoDia.equals("02")) {
            setDia(new Short("2"));
        } else if (estadoDia.equals("03")) {
            setDia(new Short("3"));
        } else if (estadoDia.equals("04")) {
            setDia(new Short("4"));
        } else if (estadoDia.equals("05")) {
            setDia(new Short("5"));
        } else if (estadoDia.equals("06")) {
            setDia(new Short("6"));
        } else if (estadoDia.equals("07")) {
            setDia(new Short("7"));
        } else if (estadoDia.equals("08")) {
            setDia(new Short("8"));
        } else if (estadoDia.equals("09")) {
            setDia(new Short("9"));
        } else if (estadoDia.equals("10")) {
            setDia(new Short("10"));
        } else if (estadoDia.equals("11")) {
            setDia(new Short("11"));
        } else if (estadoDia.equals("12")) {
            setDia(new Short("12"));
        } else if (estadoDia.equals("13")) {
            setDia(new Short("13"));
        } else if (estadoDia.equals("14")) {
            setDia(new Short("14"));
        } else if (estadoDia.equals("15")) {
            setDia(new Short("15"));
        } else if (estadoDia.equals("16")) {
            setDia(new Short("16"));
        } else if (estadoDia.equals("17")) {
            setDia(new Short("17"));
        } else if (estadoDia.equals("18")) {
            setDia(new Short("18"));
        } else if (estadoDia.equals("19")) {
            setDia(new Short("19"));
        } else if (estadoDia.equals("20")) {
            setDia(new Short("20"));
        } else if (estadoDia.equals("21")) {
            setDia(new Short("21"));
        } else if (estadoDia.equals("22")) {
            setDia(new Short("22"));
        } else if (estadoDia.equals("23")) {
            setDia(new Short("23"));
        } else if (estadoDia.equals("24")) {
            setDia(new Short("24"));
        } else if (estadoDia.equals("25")) {
            setDia(new Short("25"));
        } else if (estadoDia.equals("26")) {
            setDia(new Short("26"));
        } else if (estadoDia.equals("27")) {
            setDia(new Short("27"));
        } else if (estadoDia.equals("28")) {
            setDia(new Short("28"));
        } else if (estadoDia.equals("29")) {
            setDia(new Short("29"));
        } else if (estadoDia.equals("30")) {
            setDia(new Short("30"));
        } else if (estadoDia.equals("31")) {
            setDia(new Short("31"));
        }
        this.estadoDia = estadoDia;
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
        if (!(object instanceof Recordatorios)) {
            return false;
        }
        Recordatorios other = (Recordatorios) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Recordatorios[ secuencia=" + secuencia + " ]";
    }

    public String getNombreimagen() {
        return nombreimagen;
    }

    public void setNombreimagen(String nombreimagen) {
        this.nombreimagen = nombreimagen;
    }

}
