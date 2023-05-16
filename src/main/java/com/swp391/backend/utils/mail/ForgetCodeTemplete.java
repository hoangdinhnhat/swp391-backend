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
public class ForgetCodeTemplete {

    public static String getTemplete(String shopName, String confirmCode) {
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
                + "        .confirm-code\n"
                + "        {\n"
                + "            background-color: #f3f4f8;\n"
                + "            width: 100%;\n"
                + "            height: 100px;\n"
                + "            color: black;\n"
                + "            font-size: 40px;\n"
                + "            text-align: center;\n"
                + "            line-height: 100px;\n"
                + "            border-radius: 5px;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "    <div id=\"root\">\n"
                + "        <h1 class=\"shopName\">"+ shopName.toUpperCase() +"</h1>\n"
                + "        <h4 class=\"title\">Forget Password Confirmation Code</h4>\n"
                + "        <p class=\"des\">Please enter this confirmation code in the window where you wanted repair your account password:</p>\n"
                + "        <div class=\"confirm-code\">\n"
                + "            "+ confirmCode +"\n"
                + "        </div>\n"
                + "        <p class=\"des\">If you didn't repair a passowrd in "+ shopName +", please ignore this message.</p>\n"
                + "    </div>\n"
                + "</body>\n"
                + "</html>";

        return templete;
    }
}
