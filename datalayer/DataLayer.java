package datalayer;

import java.util.*;
import java.sql.*;
import data.*;
import helper.*;

/** 
   * Class in charge of retrieving all the information from the database.
   * Interacts with the business layer on order to receiver the parameters
   * and return the corresponding results to it. 
   * <p>
   * @author      Randy Perez 
   * @version     %I%, %G%
   * @since       1.0
*/

public class DataLayer
{
   private String dbName = "";
   private String user = "";
   private String pswd = "";
   private String host = "localhost";
   private String port = "3306"; //default MySQL port
   private DatabaseAccess db = null; 
   private String sql = "";
   private ArrayList<String> params=null;
   
  /**
   * Default constructor instanciates a new DatabaseAccess object with the 
   * given parameters and does the same with the params ArrayList.
   */      
   public DataLayer()
   {
      params = new ArrayList<String>();
      
      try
      {
         db = new DatabaseAccess(dbName,user,pswd,host,port);
      }
      catch(SQLException e)
      {
         e.printStackTrace();
      }
      catch(ClassNotFoundException e)
      {
         e.printStackTrace();
      }
   }
   
  /**
   * Recieves a a beer name and returns it's price. 
   * @param beer the name of the beer
   * @return a string with the beer price     
   */
   public String getPrice(String beer)
   {
      sql = "SELECT beerprice FROM beers WHERE beername = ?";
      params.clear();
      params.add(beer);
      ArrayList<String>  result = ExecuteSelect(sql, params);
      if(result.size()==0) return "0.0" ;     
      return result.get(0);
   }
   
   /**
   * Recieves a a beer name and a price. Sets the given price to the
   * beer provided and returns a boolean stating if the change was made. 
   * @param beer the name of the beer
   * @param beer new price of the beer.
   * @return a boolean = true if the change was made.     
   */
   public boolean setPrice(String beer,double price)
   {
      sql = "UPDATE beers " +
            "SET beerprice = ? " +
            "WHERE beername = ?";
            
      params.clear();
      params.add(Double.toString(price));
      params.add(beer);      
      return ExecuteUpdate(sql,params);
   }
   
   /**
   * Returns the name of all the beer available.    
   * @return an ArrayList of strings with all the beers  
   */
   public ArrayList<String> getBeers()
   {
      sql = "SELECT beername FROM beers";
      return ExecuteSelect(sql, null);
   }
   
   /**
   * Returns the name of the beer with the lowest price.    
   * @return a String with the name of the cheapest beer 
   */
   public String getCheapest()
   {
      sql = "SELECT beername FROM beers ORDER BY beerprice asc LIMIT 1";
      ArrayList<String> result = ExecuteSelect(sql,null);
      return result.get(0);
   }
   
   /**
   * Returns the name of the beer with the highest price.    
   * @return a String with the name of the costliest beer 
   */
   public String getCostliest()
   {
      sql = "SELECT beername FROM beers ORDER BY beerprice desc LIMIT 1";
      ArrayList<String> result = ExecuteSelect(sql,null);      
      return result.get(0);
   }
   
   /**
   * Returns the name of the beer with the highest price.
   * @param  userName the name of the user to search. 
   * @return an User object with all the user data. 
   */
   public User getUser(String userName)
   {
      sql = "SELECT * FROM users WHERE username = ?";
      params.clear();
      params.add(userName);
      ArrayList<String> result = ExecuteSelect(sql,params);
      if(result.size()==0) return new User(-1,"","",-1,-1); // returning a dummy user
      return new User(Integer.parseInt(result.get(0)),result.get(1),result.get(2),Integer.parseInt(result.get(3)),Integer.parseInt(result.get(4)));
   }
   
   public boolean insertToken(int userId,String token, String date)
   {
      sql = "INSERT INTO token VALUES (? , ? , ?)";
      params.clear();
      params.add(token);
      params.add(Integer.toString(userId));
      params.add(date);
      return ExecuteUpdate(sql,params);
   }
   
   public String getTokeDateByUserId(String userId)
   {
      sql = "SELECT expiration FROM token WHERE  user_id= ?";
      params.clear();
      params.add(userId);
      ArrayList<String>  result = ExecuteSelect(sql, params);
      if(result.size()==0) return "" ;     
      return result.get(0);
   }
   
   public String getTokeDateByToken(String token)
   {
      sql = "SELECT expiration FROM token WHERE  token= ?";
      params.clear();
      params.add(token);
      ArrayList<String>  result = ExecuteSelect(sql, params);
      if(result.size()==0) return "" ;     
      return result.get(0);
   }
   
   public String getTokeByUserId(String userId)
   {
      sql = "SELECT token FROM token WHERE  user_id= ?";
      params.clear();
      params.add(userId);
      ArrayList<String>  result = ExecuteSelect(sql, params);
      if(result.size()==0) return "" ;     
      return result.get(0);
   }
   
   public String getUserAdminByUserToken(String token)
   {
      sql = "SELECT admin FROM users WHERE  id = (SELECT user_id FROM token WHERE  token= ?)";
      params.clear();
      params.add(token);
      ArrayList<String>  result = ExecuteSelect(sql, params);
      if(result.size()==0) return "" ;     
      return result.get(0);
   }
   
   public boolean deleteTokeByUserId(String userId)
   {
      sql = "DELETE FROM token WHERE  user_id= ?";
      params.clear();
      params.add(userId);
      return ExecuteUpdate(sql, params);
   }
   
   public boolean deleteTokenByToken(String token)
   {
      sql = "DELETE FROM token WHERE  token= ?";
      params.clear();
      params.add(token);
      return ExecuteUpdate(sql, params);
   }
   
   private ArrayList<String> ExecuteSelect(String _sql, ArrayList<String> _params)
   {
      ArrayList<ArrayList<String>> res = null;
      ArrayList<String> toReturn = new ArrayList<String>();   
      
      try
      {
         if(_params==null)
         {
            res = db.getData(sql);
         }
         else
         {
            res = db.getDataPS(sql,_params);
         }
      }
      catch(SQLException e)
      {
         e.printStackTrace();
      }
      
      if(res == null) return toReturn; 
    
      
      if(res.size()>1)
      {
         for(ArrayList<String>result:res)
         {       
            toReturn.add(result.get(0));
         }
         return toReturn; 
      }

      return res.get(0); 
   }
   
   private boolean ExecuteUpdate(String _sql,ArrayList<String> _params)
   {
      int update = 0 ;
      
      try
      {
         update = db.nonSelect(_sql,_params);
      }
      catch(SQLException e)
      {
         e.printStackTrace();
      }
      
      if(update>0) return true;
      return false;
   }
}