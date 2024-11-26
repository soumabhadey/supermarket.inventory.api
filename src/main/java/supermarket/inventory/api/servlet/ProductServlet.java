package supermarket.inventory.api.servlet;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import supermarket.inventory.api.dao.CategoryDAO;
import supermarket.inventory.api.dao.ProductDAO;
import supermarket.inventory.api.dao.SupplierDAO;

import supermarket.inventory.api.database.DatabaseConnection;
import supermarket.inventory.api.gson.GsonProvider;
import supermarket.inventory.api.model.Category;
import supermarket.inventory.api.model.Product;
import supermarket.inventory.api.model.Supplier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

@WebServlet("/product/*")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    DatabaseConnection con;
    ProductDAO productDAO;
    CategoryDAO categoryDAO;
    SupplierDAO supplierDAO;
    Gson gson;
    
    public ProductServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        con = new DatabaseConnection();
        productDAO = new ProductDAO(con.getSessionFactory());
        categoryDAO = new CategoryDAO(con.getSessionFactory());
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
                List<Product> products = productDAO.readAllProducts();

                if (products == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{ \"error\": \"Products not found.\" }");
                    return;
                }

                if (products.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{ \"message\": \"No product available.\" }");
                    return;
                }

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson(products));
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

            Product product = productDAO.readProduct(id);

            if (product == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{ \"error\": \"Product not found.\" }");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(product));
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
            Product product = gson.fromJson(request.getReader(), Product.class);

            if (product == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid JSON data.\"}");
                return;
            }

            if (product.getCategory() != null) {
                Category category = categoryDAO.readCategory(product.getCategory().getId());
                if (category == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Category not found.\"}");
                    return;
                }
                product.setCategory(category);
            }

            if (product.getSupplier() != null) {
                Supplier supplier = supplierDAO.readSupplier(product.getSupplier().getId());
                if (supplier == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Supplier not found.\"}");
                    return;
                }
                product.setSupplier(supplier);
            }

            product = productDAO.createProduct(product);

            if (product == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Product creation failed.\"}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(product));
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
                response.getWriter().write("{\"error\": \"Product ID not found in URL.\"}");
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

            Product oldProduct = productDAO.readProduct(id);

            if (oldProduct == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{ \"error\": \"Product not found.\" }");
                return;
            }

            Product newProduct = gson.fromJson(request.getReader(), Product.class);

            if (newProduct == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid JSON data.\"}");
                return;
            }

            if (newProduct.getCategory() != null) {
                Category category = categoryDAO.readCategory(newProduct.getCategory().getId());
                if (category == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Category not found.\"}");
                    return;
                }
                newProduct.setCategory(category);
            }

            if (newProduct.getSupplier() != null) {
                Supplier supplier = supplierDAO.readSupplier(newProduct.getSupplier().getId());
                if (supplier == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Supplier not found.\"}");
                    return;
                }
                newProduct.setSupplier(supplier);
            }

            newProduct = productDAO.updateProduct(id, newProduct);

            if (newProduct == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Product update failed.\"}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(newProduct));
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
                response.getWriter().write("{\"error\": \"Product ID not found in URL.\"}");
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

            Product oldProduct = productDAO.readProduct(id);

            if (oldProduct == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{ \"error\": \"Product not found.\" }");
                return;
            }

            Product patchedProduct = gson.fromJson(request.getReader(), Product.class);

            if (patchedProduct == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid JSON data.\"}");
                return;
            }

            Product newProduct = oldProduct;

            if (patchedProduct.getName() != null) {
                newProduct.setName(patchedProduct.getName());
            }

            if (patchedProduct.getMrp() != oldProduct.getMrp()) {
                newProduct.setMrp(patchedProduct.getMrp());
            }

            if (patchedProduct.getCategory() != null) {
                Category category = categoryDAO.readCategory(patchedProduct.getCategory().getId());
                if (category == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{ \"error\": \"Category not found.\" }");
                    return;
                }

                newProduct.setCategory(category);
            }

            if (patchedProduct.getCount() != newProduct.getCount()) {
                newProduct.setCount(patchedProduct.getCount());
            }

            if (patchedProduct.getExpiryDate() != null) {
                newProduct.setExpiryDate(patchedProduct.getExpiryDate());
            }

            if (patchedProduct.getSupplier() != null) {
                Supplier supplier = supplierDAO.readSupplier(patchedProduct.getSupplier().getId());
                if (supplier == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{ \"error\": \"Supplier not found.\" }");
                    return;
                }

                newProduct.setSupplier(supplier);
            }

            newProduct = productDAO.updateProduct(id, newProduct);

            if (newProduct == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Patch product fail\"}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(newProduct));
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
                response.getWriter().write("{\"error\": \"Product ID not found in URL.\"}");
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

            Product product = productDAO.readProduct(id);

            if (product == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{ \"error\": \"Product ID not found.\" }");
                return;
            }

            product = productDAO.deleteProduct(id);

            if (product == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{ \"error\": \"Product delete failed.\" }");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(product));
            return;
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{ \"error\": \"Invalid Product ID in URL.\" }");
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
