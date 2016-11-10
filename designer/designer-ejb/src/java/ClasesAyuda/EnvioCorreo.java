package ClasesAyuda;

import Entidades.ConfiguracionCorreo;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author user
 */
public class EnvioCorreo {

    public EnvioCorreo() {
    }

    public static boolean enviarCorreo(ConfiguracionCorreo cfc, String destinatario, String asunto, String mensaje, String pathAdjunto, String[] resultado) {
        System.out.println("EnvioCorreo.enviarCorreo()");
        System.out.println("cfc smtp: " + cfc.getServidorSmtp());
        System.out.println("cfc port: " + cfc.getPuerto());
        System.out.println("cfc rem: " + cfc.getRemitente());
        System.out.println("cfc pwd: " + cfc.getClave());
        System.out.println("cfc auth: " + cfc.getAutenticado());
        System.out.println("cfc starttls: " + cfc.getStarttls());
        System.out.println("cfc ssl: " + cfc.getUsarssl());
        System.out.println("destinatario : " + destinatario);
        System.out.println("asunto: " + asunto);
        System.out.println("mensaje: " + mensaje);
        System.out.println("pathAdjunto: " + pathAdjunto);
//        try {
        boolean resEnvio = false;
        // Propiedades de la conexión
        Properties propiedadesConexion = new Properties();
        propiedadesConexion.setProperty("mail.smtp.host", cfc.getServidorSmtp()); //IP DEL SERVIDOR SMTP
        propiedadesConexion.setProperty("mail.smtp.port", cfc.getPuerto());
        if (resultado == null){
            resultado = new String[1];
        }
        if (cfc.getAutenticado().equalsIgnoreCase("S")) {
            System.out.println("Se debe autenticar.");
            propiedadesConexion.setProperty("mail.smtp.auth", "true");
            if (cfc.getUsarssl().equalsIgnoreCase("S")) {
                System.out.println("Se debe utilizar SSL");
                propiedadesConexion.put("mail.smtp.socketFactory.port", cfc.getPuerto());
                propiedadesConexion.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            } else if (cfc.getStarttls().equalsIgnoreCase("S")) {
                System.out.println("Se debe utilizar STARTTLS");
                propiedadesConexion.setProperty("mail.smtp.starttls.enable", "true");
            }
        }

        // Preparamos la sesion
        Session session = Session.getDefaultInstance(propiedadesConexion);
        System.out.println("session: " + session);
        /*Session session = Session.getDefaultInstance(propiedadesConexion, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(cfc.getRemitente(), cfc.getClave());
                }
            });*/
        try {
            System.out.println("Ingrese al try");
            //Mensaje que va en el correo
            BodyPart texto = new MimeBodyPart();
            texto.setText(mensaje);

            //Archivo adjunto
            BodyPart adjunto = null;
            if (pathAdjunto != null && !pathAdjunto.isEmpty()) {
                System.out.println("Ingrese al primer if");
                adjunto = new MimeBodyPart();
                FileDataSource archivo = new FileDataSource(pathAdjunto);
                adjunto.setDataHandler(new DataHandler(archivo));
                adjunto.setFileName(archivo.getFile().getName());
                System.out.println("archivo " + archivo);
                System.out.println("adjunto.setFileName : " + adjunto.getFileName());
            }

            //Estructura del contenido (Texto y Adjnto)
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);

            if (adjunto != null) {
                System.out.println("Ingrese segundo IF");
                multiParte.addBodyPart(adjunto);
            }

            // Construimos la estructura del correo final
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(cfc.getRemitente())); //REMITENTE
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario)); //DESTINATARIO
            message.setSubject(asunto); //ASUNTO
            message.setContent(multiParte); //CONTENIDO

            //Preparamos la conexion con el servidor SMTP
            Transport t = session.getTransport("smtp");

            //Validamos si requiere autenticacion o no.
            if (cfc.getAutenticado().equalsIgnoreCase("S")) {
                System.out.println("ingrese if autenticado = 'S'");
                System.out.println("cfc.getRemitente(): " + cfc.getRemitente());
                System.out.println("cfc.getClave(): " + cfc.getClave());
                t.connect(cfc.getRemitente(), cfc.getClave());
            } else {
                System.out.println("Ingtrese a else autenticado = 'S'");
                t.connect();
            }

            //Enviamos el mensaje
            t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

            // Cierre de la conexion.
            t.close();

            //System.out.println("CORREO ENVIADO EXITOSAMENTE");
//            return true;
            resEnvio = true;
            resultado[0] = "CORREO ENVIADO";
        } catch (NoSuchProviderException nspe) {
            System.out.println("Ingrese primer catch");
            System.out.println("Error enviarCorreo: " + nspe.getMessage());
            resEnvio = false;
            resultado[0] = nspe.getMessage();
        } catch (MessagingException e) {
            System.out.println("Ingrese segundo catch");
            System.out.println("Error enviarCorreo: " + e.getMessage());
            resEnvio = false;
            resultado[0] = e.getMessage();
        }

        System.out.println("resEnvio: " + resEnvio);
        return resEnvio;
    }
}
