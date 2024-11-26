package supermarket.inventory.api.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import supermarket.inventory.api.dao.CategoryDAO;
import supermarket.inventory.api.database.DatabaseConnection;
import supermarket.inventory.api.gson.GsonProvider;
import supermarket.inventory.api.model.Category;

@WebServlet("/category/*")
public class CategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    DatabaseConnection con;
    CategoryDAO categoryDAO;
    Gson gson;
    
    public CategoryServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        con = new DatabaseConnection();
        categoryDAO = new CategoryDAO(con.getSessionFactory());
        gson = GsonProvider.getGson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                List<Category> categories = categoryDAO.readAllCategories();

                if (categories == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{ \"error\": \"Categories not found.\" }");
                    return;
                }

                if (categories.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{ \"message\": \"No category available.\" }");
                    return;
                }

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson(categories));
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

            Category category = categoryDAO.readCategory(id);

            if (category == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{ \"error\": \"Category not found.\" }");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(category));
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
            Category category = gson.fromJson(request.getReader(), Category.class);

            if (category == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid JSON data.\"}");
                return;
            }

            category = categoryDAO.createCategory(category);

            if (category == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Category creation failed.\"}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(category));
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
                response.getWriter().write("{\"error\": \"Category ID not found in URL.\"}");
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

            Category oldCategory = categoryDAO.readCategory(id);

            if (oldCategory == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{ \"error\": \"Category not found.\" }");
                return;
            }

            Category newCategory = gson.fromJson(request.getReader(), Category.class);

            if (newCategory == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid JSON data.\"}");
                return;
            }

            newCategory = categoryDAO.updateCategory(id, newCategory);

            if (newCategory == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Category update failed.\"}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(newCategory));
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
                response.getWriter().write("{\"error\": \"Category ID not found in URL.\"}");
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

            Category oldCategory = categoryDAO.readCategory(id);

            if (oldCategory == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{ \"error\": \"Category not found.\" }");
                return;
            }

            Category patchedCategory = gson.fromJson(request.getReader(), Category.class);

            if (patchedCategory == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid JSON data.\"}");
                return;
            }

            Category newCategory = oldCategory;

            if (patchedCategory.getName() != null) {
                newCategory.setName(patchedCategory.getName());
            }

            newCategory = categoryDAO.updateCategory(id, newCategory);

            if (newCategory == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Patch category fail\"}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(newCategory));
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
                response.getWriter().write("{\"error\": \"Category ID not found in URL.\"}");
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

            Category category = categoryDAO.readCategory(id);

            if (category == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{ \"error\": \"Category ID not found.\" }");
                return;
            }

            category = categoryDAO.deleteCategory(id);

            if (category == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{ \"error\": \"Category delete failed.\" }");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(category));
            return;
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{ \"error\": \"Invalid Category ID in URL.\" }");
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
