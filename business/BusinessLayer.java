package business;

import java.util.*;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import datalayer.*;
import helper.*;

/** 
   * Class in charge communicating with the service layer and the data layer.
   * Recieves the requests from the service layer and validates all the logic 
   * and ask for information to the data layer. Sends the information to the service
   * layer.
   * <p>
   * @author      Randy Perez 
   * @version     %I%, %G%
   * @since       1.0
*/

public class BusinessLayer
{

   private DataLayer dl; 
   private  final String LOW_VALID_HOUR = "00:00:00";
   private  final String HIGH_VALID_HOUR = "10:00:00";   

   
   public BusinessLayer()
   {
      dl  = new DataLayer();
   }
   
   public ArrayList<String> getMethods()
   {
      if(!isValidTime()) return new ArrayList<String>();
      ArrayList<String> methods = new ArrayList<String>();
      methods.add("String getToken(String username,String Password)");
      methods.add("String[] getBeers()");
      methods.add("Double getPrice(String beerName)");
      methods.add("String getCheapest()");
      methods.add("String getCostliest()");
      return methods; 
   }
   
   public double getPrice(String beer,String token)
   {
      if(!isValidToken(token) || !isValidTime()) return 0.0;
      deleteToken(token);
      return Double.parseDouble(dl.getPrice(beer));
   }
   
   public boolean setPrice(String beer,double price,String token)
   {
      if(!isValidToken(token)|| !dl.getUserAdminByUserToken(token).equals("1") || !isValidTime())
      { 
         return false;
      }
           
      deleteToken(token);
      if(price > 0) return dl.setPrice(beer,price);
      return false;
   }
   
   public ArrayList<String> getBeers(String token)
   {
      if(!isValidToken(token) || !isValidTime()) return new ArrayList<String>();
      deleteToken(token);
      return dl.getBeers();
   }
   
   public String getCheapest(String token)
   {
      if(!isValidToken(token) || !isValidTime()) return "";
      deleteToken(token);
      return dl.getCheapest();
   }
   
   public String getCostliest(String token)
   {
      if(!isValidToken(token) || !isValidTime()) return "";
      deleteToken(token);
      return dl.getCostliest();
   }
   
   public String getToken(String userName,String password)
   {
     //System.out.println(isValidTime());
     if(!isValidTime()) return "";
     User user =  dl.getUser(userName);
     //System.out.println(user.getId());
     String token = "";
     
     //find old token and validate
     String oldTokenDate = dl.getTokeDateByUserId(Integer.toString(user.getId()));
     if(!oldTokenDate.equals("")) //if the date is not empty that means there is a token
     {
         if(isExpired(oldTokenDate)) //if it is expired we should delete it in order to generate new one
         { 
            dl.deleteTokeByUserId(Integer.toString(user.getId()));
         }
         
         else  //if alredy has token and it is not expired then just return previous token
         {
           return  dl.getTokeByUserId(Integer.toString(user.getId()));
         }
     }
     
     if(password.equals(user.getPassword()) && user.getAge() >20)
     {
         token = generateToken();         
         boolean inserted = dl.insertToken(user.getId(),token,getDate());
         if(!inserted) token ="";
         
     }
      return token;
   }
   
   private String generateToken()
   {
      SecureRandom random = new SecureRandom();
      return new BigInteger(130, random).toString(32);
   }
   
   private String getDate()
   {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = new Date();
      return dateFormat.format(date);
   }
   
   private boolean isExpired(String tokenDate)
   {  
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date now = castDate(getDate(),"yyyy-MM-dd HH:mm:ss");
      Date past = castDate(tokenDate,"yyyy-MM-dd HH:mm:ss");;     
      
      long difference = now.getTime() - past.getTime();
      if(difference > 300000) return true;
      return false;   
   }
   
   private Date castDate(String date,String format)
   {
      DateFormat dateFormat = new SimpleDateFormat(format);
      Date castedDate = null;
      try
      {   
         castedDate = dateFormat.parse(date);         
      }
      catch(Exception pe)
      {
         pe.printStackTrace();
      }
      return castedDate;
   }
   
   private boolean isValidToken(String token)
   {
      String tokenDate = dl.getTokeDateByToken(token);
      if(tokenDate.equals("")) return false; 
      return !isExpired(tokenDate);
   } 
   
   private boolean deleteToken(String token)
   {
      return dl.deleteTokenByToken(token);
   }
   
   private boolean isValidTime()
   {
      Date currentHour = castDate(getDate().substring(11,19),"HH:mm:ss");     
      Date lowValidHour = castDate(LOW_VALID_HOUR,"HH:mm:ss");
      Date highValidHour = castDate(HIGH_VALID_HOUR,"HH:mm:ss");      
      if(!(currentHour.after(lowValidHour) && currentHour.before(highValidHour))) return true;
      return false;
   }
}