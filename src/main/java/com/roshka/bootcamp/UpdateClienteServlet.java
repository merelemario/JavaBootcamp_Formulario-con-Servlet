package com.roshka.bootcamp;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/formUpdateCliente")
public class UpdateClienteServlet extends HttpServlet {
    Connection connection;

    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    context.getInitParameter("dbUrl"),
                    context.getInitParameter("dbUser"),
                    context.getInitParameter("dbPassword")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + " : " + e.getMessage());
            System.exit(0);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("inputNombre");
        String apellido = request.getParameter("inputApellido");
        String cedula = request.getParameter("inputCedula");
        String telefono = request.getParameter("inputTelefono");

        Statement stmt = null;

        try {
            int id = Integer.parseInt(request.getParameter("inputId"));

            stmt = connection.createStatement();

            String sql = "UPDATE cliente" +
                    " SET nombre='"+ nombre + "',apellido='" + apellido + "', nro_cedula='" + cedula +
                    "',telefono='" + telefono + "' WHERE id=" + id + ";";

            stmt.execute(sql);

            stmt.close();

            RequestDispatcher dispatcher = request.getRequestDispatcher("/listaClientes");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher rd = request.getRequestDispatcher("mensajeError.html");
            rd.include(request, response);
        }
    }

    @Override
    public void destroy() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
