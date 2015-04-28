package helper;

/** 
   * This is the simplest class possible to store users, basically 
   * it's just a collection of getters and setters, no validation.
   * <p>
   * @author      Randy Perez 
   * @version     %I%, %G%
   * @since       1.0
*/

public class User
{
   private int id;
   private String userName; 
   private String password;
   private int admin;
   private int age;
   
   
   /**
   * Default constructor creates an user calling all the require setters.   
   * @param _id unique identifier of the user. 
   * @param  _userName User's username.
   * @param _password User's password. 
   * @param  _admin permission level(1= admin, 0= normal).
   * @param _age User's age. 
   */  
   public User(int _id, String _userName, String _password,int _admin,int _age)
   {
      setId(_id);
      setUserName(_userName);
      setPassword(_password);
      setAdmin(_admin);
      setAge(_age);
   }
   
   /**
   * Sets the user id  
   * @param _id unique identifier of the user.     
   */ 
   public void setId(int _id)
   {
     id =_id;
   }
   
  /**
   * Sets the username     
   * @param  _userName User's _userName.
   */ 
   public void setUserName(String _userName)
   {
     userName =_userName;
   }
   
  /**
   * Sets user's password  
   * @param _password User's password.  
   */ 
   public void setPassword(String _password)
   {
     password = _password;
   }
   
  /**
   * Sets the permission level for the user     
   * @param  _admin permission level(1= admin, 0= normal).
   */ 
   public void setAdmin(int _admin)
   {
     admin =_admin;
   }
   
  /**
   * Sets user's age.   
   * @param _age User's age. 
   */ 
   public void setAge(int _age)
   {
     age = _age;
   }
   
  /**
   * Returns the user id    
   * @returns an integer containing the user's id. 
   */ 
   public int getId()
   {
     return id;
   }
   
  /**
   * Returns the username   
   * @returns a string containing the username. 
   */ 
   public String getUserName()
   {
     return userName;
   }
   
   /**
   * Returns the password   
   * @returns a string containing the password. 
   */ 
   public String getPassword()
   {
     return password;
   }
   
   /**
   * Returns the permission leve   
   * @returns an intenger containing the permission level(1=admin). 
   */ 
   public int getAdmin()
   {
     return admin;
   }
   
   /**
   * Returns the user's age   
   * @returns an integer containing the user's age. 
   */ 
   public int getAge()
   {
     return age;
   }
   
}//end of class User