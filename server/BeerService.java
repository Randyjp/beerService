package server;

import java.util.*;
import javax.jws.*;
import business.*;

/** 
   * Example of how to implement a simple SOAP webservice, for simplicity sake  
   * SOAPFAULTS are not used but you should in production. The service layer contains 
   * all the methods that will be exposed, those that form the WSDL. User need a valid 
   * username and password in order to get a token and access the services. 
   * layer.
   * <p>
   * @author      Randy Perez 
   * @version     %I%, %G%
   * @since       1.0
*/

@WebService(serviceName="BeerService")
public class BeerService
{
   private BusinessLayer bl;     
   
   public BeerService()
   {
      bl = new BusinessLayer();
   }
   
   @WebMethod
   public ArrayList<String> getMethods()
   {
      return bl.getMethods(); 
   }
   
   @WebMethod
   public double getPrice(String beer,String token)
   {
      return bl.getPrice(beer,token);
   }
   
   @WebMethod
   public boolean setPrice(String beer,double price,String token)
   {
      return bl.setPrice(beer,price,token);      
   }
   
   @WebMethod
   public ArrayList<String> getBeers(String token)
   {
      return bl.getBeers(token);
   }
   
   @WebMethod
   public String getCheapest(String token)
   {
      return bl.getCheapest(token);
   }
   
   @WebMethod
   public String getCostliest(String token)
   {
      return bl.getCostliest(token);
   }
   
   @WebMethod
   public String getToken(String username,String password)
   {
      return bl.getToken(username,password);
   }
   
   
}