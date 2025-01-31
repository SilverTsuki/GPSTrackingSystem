
package rp.sg.GPSTrackingService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import rp.sg.GPSTrackingDatabase.*;
import rp.sg.GPSTrackingEntities.*;
/**
 *
 * @author Nguyen Tuan Viet
 */
public class retrieveDeliveryRecord extends HttpServlet {
   
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        DatabaseAdapter dbAdapter = new DatabaseAdapter(this.getServletContext());
        ArrayList<DeliveryRecord> recordList = new ArrayList<DeliveryRecord>();
        recordList = dbAdapter.retrieveDeliveryRecordList();
        dbAdapter.closeDatabase();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.createObjectNode();
            ArrayNode parentNode = rootNode.putArray("AllDeliveryRecord");
            for(DeliveryRecord r:recordList){
                ObjectNode deliveryRecordInfo = parentNode.addObject();
                deliveryRecordInfo.put("id",r.getId());
                deliveryRecordInfo.put("driverId", r.getDriver().getDriverId());
                deliveryRecordInfo.put("vehicleId",r.getVehicle().getVehicleID());
                deliveryRecordInfo.put("orderListSize", r.getOrderList().size());
                String status = "all delivered";
                if(r.isAllOrderDelivered()==false){
                    status = "delivering";
                }
                deliveryRecordInfo.put("status",status);
            }
            out.print(rootNode);
        } finally {            
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
