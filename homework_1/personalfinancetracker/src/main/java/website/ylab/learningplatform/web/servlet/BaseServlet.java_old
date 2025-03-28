package website.ylab.learningplatform.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class BaseServlet extends HttpServlet {
    protected final ObjectMapper objectMapper;

    public BaseServlet() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Read the request body and convert it to the specified class
     *
     * @param request the HTTP request
     * @param clazz   the class to convert to
     * @param <T>     the type of the class
     * @return the converted object
     * @throws IOException if an error occurs while reading the request body
     */
    protected <T> T readRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return objectMapper.readValue(sb.toString(), clazz);
    }

    /**
     * Write the response body as JSON
     *
     * @param response the HTTP response
     * @param object   the object to write
     * @throws IOException if an error occurs while writing the response
     */
    protected void writeResponse(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(object));
        }
    }

    /**
     * Write an error response
     *
     * @param response the HTTP response
     * @param status   the HTTP status code
     * @param message  the error message
     * @throws IOException if an error occurs while writing the response
     */
    protected void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(new ErrorResponse(message)));
        }
    }

    /**
     * Error response class
     */
    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}
