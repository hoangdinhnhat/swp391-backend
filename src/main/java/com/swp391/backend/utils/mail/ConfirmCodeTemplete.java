/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swp391.backend.utils.mail;

/**
 *
 * @author Lenovo
 */
public class ConfirmCodeTemplete {

    public static String getTemplete(String platform, String confirmLink) {
        String templete = "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>Confirm Code Templete</title>\n"
                + "    <style>\n"
                + "        body\n"
                + "        {\n"
                + "            width: 100%;\n"
                + "            height: 100vh;\n"
                + "            font-family: monospace;\n"
                + "            background-color: #f3f4f8;\n"
                + "        }\n"
                + "\n"
                + "        #root\n"
                + "        {\n"
                + "            width: 40%;\n"
                + "            height: fit-content;\n"
                + "            padding: 10px 20px;\n"
                + "            border: 1px solid black;\n"
                + "            background-color: white;\n"
                + "            border-radius: 10px;\n"
                + "            text-align: center;\n"
                + "        }\n"
                + "\n"
                + "        .shopName\n"
                + "        {\n"
                + "            font-size: 60px;\n"
                + "        }\n"
                + "\n"
                + "        .title\n"
                + "        {\n"
                + "            font-size: 30px;\n"
                + "        }\n"
                + "\n"
                + "        .des\n"
                + "        {\n"
                + "            font-size: 19px;\n"
                + "            text-align: justify;\n"
                + "        }\n"
                + "\n"
                + "        .verify-btn\n"
                + "        {\n"
                + "            background-color: rgb(11, 222, 11);\n"
                + "            color: #ccc;\n"
                + "            padding: 10px;\n"
                + "            border-radius: 10px;\n"
                + "            text-transform: uppercase;\n"
                + "            font-size: 15px;\n"
                + "            font-weight: 600;\n"
                + "            cursor: pointer;\n"
                + "            text-decoration: none;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "    <div id=\"root\">\n"
                + "        <h1 class=\"shopName\">"+ platform.toUpperCase() +"</h1>\n"
                + "        <h4 class=\"title\">Complete registration</h4>\n"
                + "        <p class=\"des\">Please enter this confirmation code in the window where you wanted creating your account:</p>\n"
                + "        <a class=\"verify-btn\" href=\""+ confirmLink +"\">Verify your account</a>"
                + "        <p class=\"des\">If you didn't create an account in "+ platform +", please ignore this message.</p>\n"
                + "    </div>\n"
                + "</body>\n"
                + "</html>";

        return templete;
    }
}
