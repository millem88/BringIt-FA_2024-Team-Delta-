package models;


/// User model class, used to represent a User within the database.
/// Author(s): Jamie Mizelle
/// Date: 9/29/24
public class User
{
  private int id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  
  
  
  
  @Override
  public String toString()
  {
    return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password=" + password + "]";
  }



  public User()
  {
    this.id = -1;
    this.firstName = "";
    this.lastName = "";
    this.email = "";
    this.password = "";
  }
  
  
  
  public User(int id, String firstName, String lastName, String email, String password)
  {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }
  
  
  public int getId()
  {
    return id;
  }
  public void setId(int id)
  {
    this.id = id;
  }
  public String getFirstName()
  {
    return firstName;
  }
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }
  public String getLastName()
  {
    return lastName;
  }
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }
  public String getEmail()
  {
    return email;
  }
  public void setEmail(String email)
  {
    this.email = email;
  }
  public String getPassword()
  {
    return password;
  }
  public void setPassword(String password)
  {
    this.password = password;
  }
  
  
}
