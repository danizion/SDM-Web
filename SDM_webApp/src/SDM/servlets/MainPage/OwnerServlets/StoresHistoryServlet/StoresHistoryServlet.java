package SDM.servlets.MainPage.OwnerServlets.StoresHistoryServlet;

import SDM.utils.DTO.OrderHistory.OrderHistoryDto;
import SDM.utils.ServletUtils;
import com.google.gson.Gson;
import users.Owner;
import users.UserManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static SDM.Constants.Constants.USERNAME;

/**
 * servlet for getting the order history of all the owner stores
 */
public class StoresHistoryServlet extends HttpServlet {
    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("application/json");
        try (PrintWriter out = res.getWriter()) {
            String userName = req.getSession(false).getAttribute(USERNAME).toString();
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            Owner owner = (Owner) (userManager.getUsers().get(userName));
            ArrayList<OrderHistoryDto> orderDtoMap = new ArrayList<>();
            Map<Integer, String> allStores = new HashMap<>();
            owner.getAllStores().forEach((id, store) -> allStores.put(id, store.getName()));
            owner.getAllOrders().forEach(storeOrder -> orderDtoMap.add(new OrderHistoryDto(storeOrder)));
            Gson gson = new Gson();
            out.println(gson.toJson(new OrderAndStores(allStores, orderDtoMap)));
            out.flush();
        }
        catch (Exception e){
            System.out.println("error in OrderHistoryServlet");
            e.printStackTrace();
        }
    }

    public class OrderAndStores {
        Map<Integer, String> allStores;
        ArrayList<OrderHistoryDto> allOrders;

        public OrderAndStores(Map<Integer, String> allStores, ArrayList<OrderHistoryDto>  orderDtoMap){
            this.allStores = allStores;
            this.allOrders = orderDtoMap;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
