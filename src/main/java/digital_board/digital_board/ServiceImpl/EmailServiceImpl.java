package digital_board.digital_board.ServiceImpl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl {

  @Autowired
  private JavaMailSender emailSender;

  // public void sendSimpleMessage(
  // String to, String subject, String text) {

  // SimpleMailMessage message = new SimpleMailMessage();
  // message.setFrom("sahilk.bca2021@ssismg.org");
  // // message.setFrom("sultans.bca2021@ssism.org");
  // message.setTo(to);
  // message.setSubject(subject);
  // message.setText("<html><body><h1>Hello, this is HTML
  // content!</h1></body></html>");
  // emailSender.send(message);
  // }

  public void sendSimpleMessage(String to, String subject, String userName) {
    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper;

    try {
      helper = new MimeMessageHelper(message, true);
      helper.setFrom("sahilk.bca2021@ssismg.org");
      // helper.setFrom("sultans.bca2021@ssism.org");
      helper.setTo(to);
      helper.setSubject("New Notifiaction");
      helper.setText("<!DOCTYPE html>\n" +
          "<html lang=\"en\">\n" +
          "<head>\n" +
          "  <meta charset=\"UTF-8\">\n" +
          "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
          "  <title>Your Page Title</title>\n" +
          "  <style>\n" +
          "    body {\n" +
          "      font-family: 'Arial', sans-serif;\n" +
          "      background-color: #f4f4f4;\n" +
          "      text-align: center;\n" +
          "    }\n" +
          "\n" +
          "    img{\n" +
          "      width: 200px;\n" +
          "    }\n" +
          "\n" +
          "    .container {\n" +
          "      max-width: 600px;\n" +
          "      margin: 0 auto;\n" +
          "      padding: 20px;\n" +
          "    }\n" +
          "\n" +
          "    .header {\n" +
          "      background-color: #4B49AC;\n" +
          "      color: #fff;\n" +
          "      padding: 10px;\n" +
          "      display: flex;\n" +
          "      justify-content: center;\n" +
          "      align-items: center;\n" +
          "    }\n" +
          "    .footer {\n" +
          "      background-color: #4B49AC;\n" +
          "      color: #fff;\n" +
          "      padding: 25px;\n" +
          "      display: flex;\n" +
          "      justify-content: center;\n" +
          "      align-items: center;\n" +
          "    }\n" +
          "\n" +
          "    .content {\n" +
          "      background-color: #e1e1e1;\n" +
          "      padding: 20px;\n" +
          "    }\n" +
          "\n" +
          "    .button {\n" +
          "      display: inline-block;\n" +
          "      padding: 10px 20px;\n" +
          "      border: 2px solid #4B49AC;\n" +
          "      text-decoration: none;\n" +
          "      border-radius: 20px;\n" +
          "    }\n" +
          "  </style>\n" +
          "</head>\n" +
          "<body>\n" +
          "  <div class=\"container\">\n" +
          "    <div class=\"header\">\n" +
          "      <img src=\"https://res.cloudinary.com/ddshgvfw9/image/upload/v1705294225/hvuf81ahd0uqg7utnfyy.png\"/>\n"
          +
          "    </div>\n" +
          "    <center>\n" +
          "      <div class=\"content\">\n" +
          "        <p>Hello, " + userName + " !</p>\n" +
          "        <p>We wanted to inform you about a new notice that has been posted. Please click the button below to view the details:</p>\n"
          +

          "        <p>Thank you for being a valued member of our community.</p>\n" +
          "        <p>Best regards,</p>\n" +
          "        <p>Digital Dashboard</p>\n" +
          "        <a class=\"button\" href=\"http://localhost:4200/staticboard\">View Notification</a>\n" +
          "      </div>\n" +
          "      <div class=\"footer\">\n" +
          "      </div>\n" +
          "    </center>\n" +
          "  </div>\n" +
          "</body>\n" +
          "</html>", true); // true indicates HTML content

      emailSender.send(message);
    } catch (MessagingException e) {
      // Handle the exception appropriately
      e.printStackTrace();
    }
  }

  public void sendSimpleMessageForPassword(String to, String name, String Password) {
    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper;

    try {
      helper = new MimeMessageHelper(message, true);
      helper.setFrom("sahilk.bca2021@ssismg.org");
      // helper.setFrom("sultans.bca2021@ssism.org");
      helper.setTo(to);
      helper.setSubject("Welcome Aboard - Your Login Details");
      helper.setText("<html><head><style>"
          + "body { font-family: 'Arial', sans-serif; background-color: #f4f4f4; text-align: center; }"
          + "a {color: white}" +
          " img{\n" +
          "      width: 200px;\n" +
          "    }\n" +
          "\n" +
          "    .container {\n" +
          "      max-width: 600px;\n" +
          "      margin: 0 auto;\n" +
          "      padding: 20px;\n" +
          "    }\n" +
          "\n" +
          "    .header {\n" +
          "      background-color: #4B49AC;\n" +
          "      color: #fff;\n" +
          "      padding: 10px;\n" +
          "      display: flex;\n" +
          "      justify-content: center;\n" +
          "      align-items: center;\n" +
          "    }\n" +
          "    .footer {\n" +
          "      background-color: #4B49AC;\n" +
          "      color: #fff;\n" +
          "      padding: 25px;\n" +
          "      display: flex;\n" +
          "      justify-content: center;\n" +
          "      align-items: center;\n" +
          "    }\n" +
          "\n"
          + ".content { background-color: #e1e1e1; padding: 20px; }"
          + "img {padding-left: 12px;}"
          + ".button { display: inline-block; padding: 10px 20px; border: 2px solid #4B49AC;; color: #white; text-decoration: none; border-radius: 20px;}"
          + "</style></head><body>"
          + "<center>"
          + "<div class='container'>"

          + "    <div class=\"header\">\n"
          + "      <img src=\"https://res.cloudinary.com/ddshgvfw9/image/upload/v1705294225/hvuf81ahd0uqg7utnfyy.png\"/>\n"
          + "    </div>\n"
          + "<div class='content'>"
          + "<p>Hello, " + name + "!</p>"
          + "<p>Your login username is: <strong>" + to + "</strong></p>"
          + "<p>Your password is: <strong>" + Password + "</strong></p>"
          + "<p>Thank you for being a valued member of our community.</p>"
          + "<p>Best regards,</p>"
          + "<p>Digital Dashboard</p>"
          + "</div>"
          + "</div>"

          + "</div>"
          + "</center>"
          + "</body></html>", true); // true indicates HTML content

      emailSender.send(message);
    } catch (MessagingException e) {
      // Handle the exception appropriately
      e.printStackTrace();
    }
  }

}