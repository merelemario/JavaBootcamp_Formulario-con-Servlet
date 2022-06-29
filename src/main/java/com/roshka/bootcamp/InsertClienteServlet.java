package com.roshka.bootcamp;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.concurrent.ExecutionException;

@WebServlet(urlPatterns = "/formInsertCliente")
public class InsertClienteServlet extends HttpServlet {
    Connection connection;

    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.
                    getConnection(context.getInitParameter("dbUrl"),
                            context.getInitParameter("dbUser"),
                            context.getInitParameter("dbPassword"));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String nombre = req.getParameter("inputNombre");
        String apellido = req.getParameter("inputApellido");
        String nroCedula = req.getParameter("inputCedula");
        String nroTelefono = req.getParameter("inputTelefono");
        ResultSet rs = null;

        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT MAX(id) + 1 AS id FROM cliente;");
            int id = 0;

            if(rs.next()) {
                id = rs.getInt("id");
            } else {
                throw new IOException();
            }

            String sql = "INSERT INTO cliente(id, nombre, apellido, nro_cedula, telefono)" +
                    "VALUES (" + id + ",'" +nombre + "','" + apellido + "','" + nroCedula + "','" + nroTelefono + "')";

            stmt.execute(sql);

            stmt.close();
            rs.close();

            //res.sendRedirect();
            /*
             * res.setContentType("text/html");
             * PrintWriter out = res.getWriter();
             * out.println(texto);
             * */
            RequestDispatcher dispatcher = req.getRequestDispatcher("/listaClientes");
            dispatcher.forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher rd = req.getRequestDispatcher("mensajeError.html");
            rd.include(req, res);
        }
    }

    @Override
    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
