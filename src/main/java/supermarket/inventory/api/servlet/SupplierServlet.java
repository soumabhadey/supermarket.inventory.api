package supermarket.inventory.api.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import supermarket.inventory.api.dao.SupplierDAO;
import supermarket.inventory.api.database.DatabaseConnection;
import supermarket.inventory.api.gson.GsonProvider;
import supermarket.inventory.api.model.Supplier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

@WebServlet("/supplier/*")
public class SupplierServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    DatabaseConnection con;
    SupplierDAO supplierDAO;
    Gson gson;

    public SupplierServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        con = new DatabaseConnection();
        supplierDAO = new SupplierDAO(con.getSessionFactory());
        gson = GsonProvider.getGson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                List<Supplier> suppliers = supplierDAO.readAllSuppliers();

                if (suppliers == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{ \"error\": \"Suppliers not found.\" }");
                    return;
                }

                if (suppliers.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{ \"message\": \"No supplier available.\" }");
                    return;
                }

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson(suppliers));
                return;
            }

            List<String> urlVars = new ArrayList<>(Arrays.asList(pathInfo.split("/")));
            urlVars.removeAll(Arrays.asList(""));

            if (urlVars.size() > 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid URL.\"}");
                return;
            }

            int id = Integer.parseInt(urlVars.get(0));

            Supplier supplier = supplierDAO.readSupplier(id);

            if (supplier == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{ \"error\": \"Supplier not found.\" }");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(supplier));
            return;
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An error occurred.\"}");
            return;
        } finally {
            // pass;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            Supplier supplier = gson.fromJson(request.getReader(), Supplier.class);

            if (supplier == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid JSON data.\"}");
                return;
            }

            supplier = supplierDAO.createSupplier(supplier);

            if (supplier == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Supplier creation failed.\"}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(supplier));
            return;
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An error occurred.\"}");
            return;
        } finally {
            // pass;
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Supplier ID not found in URL.\"}");
                return;
            }

            List<String> urlVars = new ArrayList<>(Arrays.asList(pathInfo.split("/")));
            urlVars.removeAll(Arrays.asList(""));

            if (urlVars.size() > 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid URL.\"}");
                return;
            }

            int id = Integer.parseInt(urlVars.get(0));

            Supplier oldSupplier = supplierDAO.readSupplier(id);

            if (oldSupplier == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{ \"error\": \"Supplier not found.\" }");
                return;
            }

            Supplier newSupplier = gson.fromJson(request.getReader(), Supplier.class);

            if (newSupplier == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid JSON data.\"}");
                return;
            }

            newSupplier = supplierDAO.updateSupplier(id, newSupplier);

            if (newSupplier == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Supplier update failed.\"}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(newSupplier));
            return;
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An error occurred.\"}");
            return;
        } finally {
            // pass;
        }
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Supplier ID not found in URL.\"}");
                return;
            }

            List<String> urlVars = new ArrayList<>(Arrays.asList(pathInfo.split("/")));
            urlVars.removeAll(Arrays.asList(""));

            if (urlVars.size() > 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid URL.\"}");
                return;
            }

            int id = Integer.parseInt(urlVars.get(0));

            Supplier oldSupplier = supplierDAO.readSupplier(id);

            if (oldSupplier == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{ \"error\": \"Supplier not found.\" }");
                return;
            }

            Supplier patchedSupplier = gson.fromJson(request.getReader(), Supplier.class);

            if (patchedSupplier == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid JSON data.\"}");
                return;
            }

            Supplier newSupplier = oldSupplier;

            if (patchedSupplier.getName() != null) {
                newSupplier.setName(patchedSupplier.getName());
            }
            
            if (patchedSupplier.getPhone() != null) {
                newSupplier.setPhone(patchedSupplier.getPhone());
            }
            
            if (patchedSupplier.getEmail() != null) {
                newSupplier.setEmail(patchedSupplier.getEmail());
            }

            newSupplier = supplierDAO.updateSupplier(id, newSupplier);

            if (newSupplier == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Patch supplier fail\"}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(newSupplier));
            return;
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An error occurred\"}");
            return;
        } finally {
            // pass;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Supplier ID not found in URL.\"}");
                return;
            }

            List<String> urlVars = new ArrayList<>(Arrays.asList(pathInfo.split("/")));
            urlVars.removeAll(Arrays.asList(""));

            if (urlVars.size() > 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid URL.\"}");
                return;
            }

            int id = Integer.parseInt(urlVars.get(0));

            Supplier supplier = supplierDAO.readSupplier(id);

            if (supplier == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{ \"error\": \"Supplier ID not found.\" }");
                return;
            }

            supplier = supplierDAO.deleteSupplier(id);

            if (supplier == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{ \"error\": \"Supplier delete failed.\" }");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(supplier));
            return;
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{ \"error\": \"Invalid Supplier ID in URL.\" }");
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{ \"error\": \"An error occurred.\" }");
            return;
        } finally {
            // pass;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (con != null) {
            con.close();
        }
    }
}
