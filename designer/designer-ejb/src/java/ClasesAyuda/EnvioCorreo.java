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
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class EnvioCorreo {

   private static Logger log = Logger.getLogger(EnvioCorreo.class);

   public EnvioCorreo() {
   }

   public static boolean enviarCorreo(ConfiguracionCorreo cfc, String destinatario, String asunto, String mensaje, String pathAdjunto, String[] resultado) {
      log.warn("EnvioCorreo.enviarCorreo()");
      log.warn("cfc smtp: " + cfc.getServidorSmtp());
      log.warn("cfc port: " + cfc.getPuerto());
      log.warn("cfc rem: " + cfc.getRemitente());
      log.warn("cfc pwd: " + cfc.getClave());
      log.warn("cfc auth: " + cfc.getAutenticado());
      log.warn("cfc starttls: " + cfc.getStarttls());
      log.warn("cfc ssl: " + cfc.getUsarssl());
      log.warn("destinatario : " + destinatario);
      log.warn("asunto: " + asunto);
      log.warn("mensaje: " + mensaje);
      log.warn("pathAdjunto: " + pathAdjunto);
//        try {
      boolean resEnvio = false;
      // Propiedades de la conexión
      Properties propiedadesConexion = new Properties();
      propiedadesConexion.setProperty("mail.smtp.host", cfc.getServidorSmtp()); //IP DEL SERVIDOR SMTP
      propiedadesConexion.setProperty("mail.smtp.port", cfc.getPuerto());
      if (resultado == null) {
         resultado = new String[1];
      }
      if (cfc.getAutenticado().equalsIgnoreCase("S")) {
         log.warn("Se debe autenticar.");
         propiedadesConexion.setProperty("mail.smtp.auth", "true");
         if (cfc.getUsarssl().equalsIgnoreCase("S")) {
            log.warn("Se debe utilizar SSL");
            propiedadesConexion.put("mail.smtp.socketFactory.port", cfc.getPuerto());
            propiedadesConexion.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
         } else if (cfc.getStarttls().equalsIgnoreCase("S")) {
            log.warn("Se debe utilizar STARTTLS");
            propiedadesConexion.setProperty("mail.smtp.starttls.enable", "true");
         }
      }

      // Preparamos la sesion
      Session session = Session.getDefaultInstance(propiedadesConexion);
      log.warn("session: " + session);
      /*Session session = Session.getDefaultInstance(propiedadesConexion, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(cfc.getRemitente(), cfc.getClave());
                }
            });*/
      try {
         log.warn("Ingrese al try");
         //Mensaje que va en el correo
         BodyPart texto = new MimeBodyPart();
         texto.setText(mensaje);

         //Archivo adjunto
         BodyPart adjunto = null;
         if (pathAdjunto != null && !pathAdjunto.isEmpty()) {
            log.warn("Ingrese al primer if");
            adjunto = new MimeBodyPart();
            FileDataSource archivo = new FileDataSource(pathAdjunto);
            adjunto.setDataHandler(new DataHandler(archivo));
            adjunto.setFileName(archivo.getFile().getName());
            log.warn("archivo " + archivo);
            log.warn("adjunto.setFileName : " + adjunto.getFileName());
         }

         //Estructura del contenido (Texto y Adjnto)
         MimeMultipart multiParte = new MimeMultipart();
         multiParte.addBodyPart(texto);

         if (adjunto != null) {
            log.warn("Ingrese segundo IF");
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
            log.warn("ingrese if autenticado = 'S'");
            log.warn("cfc.getRemitente(): " + cfc.getRemitente());
            log.warn("cfc.getClave(): " + cfc.getClave());
            t.connect(cfc.getRemitente(), cfc.getClave());
         } else {
            log.warn("Ingtrese a else autenticado = 'S'");
            t.connect();
         }

         //Enviamos el mensaje
         t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

         // Cierre de la conexion.
         t.close();

         //log.warn("CORREO ENVIADO EXITOSAMENTE");
//            return true;
         resEnvio = true;
         resultado[0] = "CORREO ENVIADO";
      } catch (NoSuchProviderException nspe) {
         log.error("Ingrese primer catch");
         log.error("Error enviarCorreo: " + nspe.getMessage());
         resEnvio = false;
         resultado[0] = nspe.getMessage();
      } catch (MessagingException e) {
         log.error("Ingrese segundo catch");
         log.error("Error enviarCorreo: " + e.getMessage());
         resEnvio = false;
         resultado[0] = e.getMessage();
      }

      log.warn("resEnvio: " + resEnvio);
      return resEnvio;
   }
}
