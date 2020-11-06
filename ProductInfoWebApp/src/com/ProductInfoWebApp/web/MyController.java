package com.ProductInfoWebApp.web;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.Properties;


public class MyController extends HttpServlet {

  // request.productBarcode must be tagged as `UNIQUE` in the MySql table
  public void doPost (HttpServletRequest request, HttpServletResponse response)
  throws IOException, ServletException {
    Properties login = new Properties();
    try (FileReader in = new FileReader("/home/user/mysql.properties")) {
      login.load(in);
    }
    String dbusername = login.getProperty("username");
    String dbpassword = login.getProperty("password");

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<HTML><BODY>");
    Connection con;
    PreparedStatement prs;
    String query = "insert into products (productName, productBarcode, productColor, productDescription) values(?,?,?,?)";

    try {
      Class.forName("com.mysql.jdbc.Driver");
      con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ProductInfoWebApp", dbusername, dbpassword);
      prs = con.prepareStatement(query);
      prs.setString(1, request.getParameter("productName"));
      prs.setString(2, request.getParameter("productBarcode"));
      prs.setString(3, request.getParameter("productColor"));
      prs.setString(4, request.getParameter("productDescription"));
      prs.executeUpdate();
      out.println("Your submission has been successful.");
      out.println("<button type=\"button\" onclick=\"window.location.href='./'\" class=\"btn btn-outline-secondary\">Return</button>");
      out.println("</BODY></HTML>");
    } catch (Exception e) {
      out.println(e.getMessage());
      query = "select * from products where productBarcode = " + "'" +request.getParameter("productBarcode") + "'";

      try {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ProductInfoWebApp", "root", "melenemarika"); //TODO:gia onoma tou 8eou allaxto password prin mpei github!
        prs = con.prepareStatement(query);
        ResultSet rs = prs.executeQuery();

        while (rs.next()) {
          out.println("<h3> Product Name: "+ rs.getString("productName") +"</h3>");
          out.println("<p> Product Barcode: "+ rs.getString("productBarcode") +"</p>");
          //out.println("<p> Product Color: "+ rs.getString("productColor") +"</p>");
          out.println("<div class=\"form-group row\" style=\"padding-top:5px;\"><label for=\"productColor\" class=\"col-sm-1 col-form-label\">Color: </label><input type=\"color\" class=\"form-control\" id=\"productColor\" name=\"productColor\" value=\""+ rs.getString("productColor") + "\" style=\"width:80px\" disabled></div>");
          out.println("<p> Product Description: "+ rs.getString("productDescription") +"</p>");
          out.println("<button type=\"button\" onclick=\"window.location.href='./'\" class=\"btn btn-outline-secondary\">Return</button>");
        }
      } catch (Exception z) {
        out.println(e.getMessage());

      }

    }

  }
}
