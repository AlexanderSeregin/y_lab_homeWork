package website.ylab.learningplatform;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import website.ylab.learningplatform.config.LiquibaseConfig;
import website.ylab.learningplatform.web.servlet.*;

public class Main {
    public static void main(String[] args) {
        // Run database migrations
        LiquibaseConfig.getInstance().migrate();

        // Create and configure the server
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Register servlets
        context.addServlet(new ServletHolder(new AuthServlet()), "/api/auth/*");
        context.addServlet(new ServletHolder(new UserServlet()), "/api/users/*");
        context.addServlet(new ServletHolder(new TransactionServlet()), "/api/transactions/*");
        context.addServlet(new ServletHolder(new BudgetServlet()), "/api/budgets/*");
        context.addServlet(new ServletHolder(new GoalServlet()), "/api/goals/*");
        context.addServlet(new ServletHolder(new NotificationServlet()), "/api/notifications/*");

        try {
            // Start the server
            server.start();
            System.out.println("Server started on port 8080");
            server.join();
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}