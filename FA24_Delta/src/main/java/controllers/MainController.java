package controllers;

import java.util.ArrayList;
import java.util.List;


/// MainController class, used to map all URLs within the application and handle calls to the DAO to perform various data operations with the database.
/// Author(s): Jamie Mizelle, Lisa Gehrt
/// Date: 9/29/24
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DAO;
import models.ControllerResponse;
import models.Event;
import models.EventItem;
import models.EventParticipation;
import models.User;
import models.Event;

@Controller
public class MainController
{
  @Autowired
  private DAO dao;

//  @Autowired
//  private ServiceLayer service;


  @RequestMapping("/index")
  public String redirectToMainPage(Model model, HttpServletRequest request)
  {
	    System.out.println("in redirectToMainPage");
      return "index";
  }
  
  @GetMapping({"/", "/login"})
  public String redirectToLogin(Model model, HttpServletRequest request)
  {
      System.out.println("in redirectToLogin");
      return "login";
      
  }
  
  
  // register mapping, links to registration page
  @GetMapping({"/register"})
  public String redirectToRegister(Model model, HttpServletRequest request)
  {
      System.out.println("in redirectToRegister");
      return "register";
      
  }
  
  // loginAttempt controller, called from login.js to try to login a user.
  @PostMapping("/loginAttempt")
  @ResponseBody
  public ControllerResponse loginAttempt(Model model,@RequestParam(name="email") String email, @RequestParam(name="password") String password, HttpServletRequest request)
  {
    System.out.println("got here in loginAttempt");
    System.out.println("email = " + email);
    System.out.println("password = " + password);
    
    // Init ControllerResponse
    ControllerResponse cr = new ControllerResponse();
    // pull user from DB, real user will be in the CR returned by dao
    // if no user exists, a default user (id = -1) will be returned within the CR
    cr = dao.getUserByEmailAndPassword(email, password);
    
    // if user is not empty, put it in the session
    // this will be used later by an authentication filter to ensure users are logged in when trying to perform actions
    if(cr.getUser().getId() != -1)
    {
      HttpSession session = request.getSession();
      session.setAttribute("user", cr.getUser());
    }
    
    
    return cr;
  }
  

  
  /*
  // template example controller, loads some dummy data into a Model object, then loads the template JSP
  @GetMapping("/template")
  public String templateExample(Model model)
  {
    // add attribute to Model form: <key, value>
    model.addAttribute("name", "Jamie");
    
    // add attribute to Model, will be used to example c:if
    model.addAttribute("conditionalValue", "I exist!");
    
    // create dummy list
    ArrayList<String> arrList = new ArrayList<String>();
    arrList.add("abc");
    arrList.add("def");
    arrList.add("ghi");
    
    // add attribute to Model, will be used to example c:forEach
    model.addAttribute("stringList", arrList);
    
    // go to the template JSP
    return "template";
  }
  */
  

  //MM 10/2/24
  //RegistrationAttempt mapping, used to map controller to dao registration function.
  @PostMapping("/registrationAttempt")
  @ResponseBody
  public ControllerResponse registerAttempt(Model model, @ModelAttribute User user, HttpServletRequest request) {
	  System.out.println("got here in registerAttempt");

	  
	  System.out.println(user);
	  //Initialize Controller
	  ControllerResponse cr = new ControllerResponse();
	  
	  
	  // try to register user, will return a CR with a user with id = -1 if failure, and id != -1 if successful
	  cr = dao.tryToRegisterUser(user); 

	  
	  return cr;

  }
  

 
  //JM 10/22/24
 // newEvent mapping, used to test create_event.jsp & associated functionality
 @GetMapping({"/newEvent"})
 public String newEvent(Model model, HttpServletRequest request)
 {
     System.out.println("in newEvent");
   
     model.addAttribute(new Event());
     
     
     User user = getUserFromSession(request);

     if(user != null)
     {
       int userId = user.getId();
       model.addAttribute("currentUserId", userId);
       
       return "create_event";
     }
     
     
     // TODO: remove this as auth filter will make this impossible
     return "login";
 }
  
  
  // eventCreationAttempt controller, used to create an event and supporting data, probably too much logic for a controller but it works so im not changing it :)
  @PostMapping("/eventCreationAttempt")
  @ResponseBody
  //public ControllerResponse eventCreationAttempt(Model model, @ModelAttribute Event event, @RequestParam List<EventItem> itemList, HttpServletRequest request) 
  public ControllerResponse eventCreationAttempt(Model model, @ModelAttribute Event event, @RequestParam String itemListJson, HttpServletRequest request) 
  {
    System.out.println("got here in eventCreationAttempt");
    
    ControllerResponse cr = new ControllerResponse();
    
    User user = getUserFromSession(request);

    if(user != null)
    {
      int userId = user.getId();
      
      ObjectMapper objectMapper = new ObjectMapper();
      try 
      {
          // Deserialize the JSON string into a List<EventItem>
          List<EventItem> eventItems = objectMapper.readValue(itemListJson, new TypeReference<List<EventItem>>() {});
          
          
          // Process the list of EventItem objects
          // insert event first
          ControllerResponse eventCR = dao.insertEvent(event, userId);
          System.out.println("eventCR lastInsertedId = " + eventCR.getLastInsertedId());
          
          
          // insert eventItems once event is inserted
          if(eventCR.getLastInsertedId() != -1)
          {
            // insert EventParticipation
            ControllerResponse eventParticipationCR = dao.insertEventParticipation(eventCR.getLastInsertedId(), userId);
            
            // add errors if needed
            cr.getErrors().addAll(eventParticipationCR.getErrors());
            
            // insert items
            ControllerResponse itemsCR = dao.insertEventItems(eventCR.getLastInsertedId(), eventItems);
            
            // add errors if needed
            cr.getErrors().addAll(itemsCR.getErrors());
            
          }
          else
          {
            cr.getErrors().add("Event Creation failed");
          }
      } 
      catch (JsonProcessingException e) 
      {
         cr.getErrors().add("Failed to process Event Items, creation canceled");
      }
      
    }
    
    System.out.println(event);
    System.out.println(itemListJson);
    
    return cr;

  }
  
  
  // get user from session method, grabs the user object off the session
  private User getUserFromSession(HttpServletRequest request)
  {
    HttpSession session = request.getSession();
    
    User user = (User) session.getAttribute("user");
    
    
    
    return user;
  }


  //fetchEvents controller, returns all events in the DB within a CR object
  @PostMapping("/fetchEvents")
  @ResponseBody
  public ControllerResponse fetchEvents(HttpServletRequest request) 
  {
    ControllerResponse cr = new ControllerResponse();
    
    User user = getUserFromSession(request);
    
    // check user is in session
    if(user != null)
    {
      // grab events
      ArrayList<Event> events = dao.getAllEvents();
      
      cr.setEvents(events);
    }

	  return cr;
  }
  
  // LG 11/6/24 
  // /myEvents mapping, calls myEvents.jsp
  @GetMapping("/myEvents")
  public String myEvents(Model model, HttpServletRequest request)
  {
    User user = getUserFromSession(request);
    
    if(user != null)
    {
      return "myEvents";
    }
    
    return "login";
  }
  
  //LG 11/6/24
  //fetchMyEvents controller, returns all the user's events in the DB within a CR object
  @PostMapping("/fetchMyEvents")
  @ResponseBody
  public ControllerResponse fetchMyEvents(HttpServletRequest request) 
  {
    ControllerResponse cr = new ControllerResponse();
    
    User user = getUserFromSession(request);
    
    // check user is in session
    if(user != null)
    {
      // grab events
      ArrayList<Event> events = dao.getUserEvents(user);
      
      cr.setEvents(events);
    }

	  return cr;
  }
  
  //MM 10/25/24
  //FetchNotCreatedEvents controller, returns all event in the DB ,that were not created by the user, as a CR object. 
  @PostMapping("/fetchNotCreatedEvents")
  @ResponseBody
  public ControllerResponse fetchNotCreatedEvents(HttpServletRequest request){
	  
	  ControllerResponse cr = new ControllerResponse();
	  User user = getUserFromSession(request);
	    
	    // check user is in session
	    if(user != null)
	    {
	      // grab events
	      ArrayList<Event> events = dao.getEventsNotOwnedByUser(user.getId());
	      
	      cr.setEvents(events);
	    }
	    return cr;
	  
  }

  //MM 10/9/24
  //LogoutAttempt controller, logs user out of session and returns to login page
  @GetMapping("/logout")
  public String logoutAttempt(Model model, HttpServletRequest request) {
	  System.out.println("got here in logoutAttempt");
	  
	  HttpSession session = request.getSession(false);
	  if (session == null) {
		  System.out.println("LOGOUT FAIL");
		  
		  return "login";
	  } else {
		  
	  System.out.println("Logout Success");
	  session.invalidate();
	  model.addAttribute("logoutMessage", "Logout Successful");
	  
	  return "login";
 	}

  }
  
  
  @GetMapping("/searchEvents")
  public String searchEvents(Model model, HttpServletRequest request)
  {
    User user = getUserFromSession(request);
    
    if(user != null)
    {
      return "searchEvents";
    }
    
    return "login";
  }
  
  
  // JM - eventClicked mapping
  // this controller handles the view that should be presented to a user when they click an event
  // this will route them to editEvent if they own the event, otherwise will route to eventDetails
  @GetMapping("/eventClicked")
  public String eventClicked(Model model, HttpServletRequest request, @RequestParam int eventId)
  {
    
    System.out.println("in eventClicked");
    System.out.println("eventId = " + eventId);
    
    User user = getUserFromSession(request);
    boolean userInEvent = true;
    
    if(user != null)
    {
      Event event = dao.getEventById(eventId);
      
      // ensure event exists, if it doesnt then send them back to Search Events
      if(event.getId() == -1)
      {
        return "searchEvents";
      }
      
      System.out.println(event);
      
      
      if(event.getOwningUserId() == user.getId())
      {
        // event owned by user
    	  
       // Returns the event details owned by current user (Nas)
    	  
    	ArrayList<EventItem> eventItems = dao.getEventItemsByEventId(event.getId());
    	ArrayList<EventItem> eventFreeformItems = dao.getFreeformEventItemsByEventId(event.getId());
    	
    	
    	model.addAttribute("eventItems", eventItems);
    	
    	model.addAttribute("event", event);	
    	
    	model.addAttribute("currentUserId", user.getId());
    	
    	model.addAttribute("eventFreeformItems", eventFreeformItems);  
    	
    	return "editEvent";
      }
      else
      {
        // get EP for user and event
        EventParticipation ep = dao.getEventParticipationByEventIdAndUserId(eventId, user.getId());
        
        // see if user is in event
        if(ep.getId() == -1)
        {
          // not in the event
          userInEvent = false;
          
          
        }
        // go to eventDetails JSP, send userInEvent
        return eventDetails(model, request, event, userInEvent);
      }
      
    }
    
  
    
    return "login";
    
  }
  
  
  // adds all needed attributes to the model object then returns the event_details JSP
  public String eventDetails(Model model, HttpServletRequest request, Event event, boolean userInEvent)
  {
    
    
    User user = getUserFromSession(request);

    if(user != null)
    {
      ArrayList<EventItem> eventItems = dao.getEventItemsByEventId(event.getId());
      ArrayList<EventItem> freeformEventItems = dao.getFreeformEventItemsByEventId(event.getId());
      
      for(EventItem ei : eventItems)
      {
        System.out.println(ei);
      }
      
      int userId = user.getId();
      
      
      model.addAttribute("currentUserId", userId);
      model.addAttribute("event", event);
      model.addAttribute("eventItems", eventItems);
      model.addAttribute("userInEvent", userInEvent);
      model.addAttribute("userFirstName", user.getFirstName());
      model.addAttribute("userLastName", user.getLastName());
      model.addAttribute("eventFreeformItems", freeformEventItems);
      
      
      return "event_details";
    }
    
    
    return "login";
    
    
  }
  
  
  
  // attempt to join event controller
  // tries to join an event, can return errors if insert fails or user is already in the event
  @PostMapping("/attemptJoinEvent")
  @ResponseBody
  public ControllerResponse attemptJoinEvent(Model model,  @RequestParam int eventId, HttpServletRequest request) 
  {
    System.out.println("got here in attemptJoinEvent");
    
    
    ControllerResponse cr = new ControllerResponse();
    
    
    User user = getUserFromSession(request);

    // check if user in session
    if(user != null)
    {
      int userId = user.getId();
      
      System.out.println("eventId = " + eventId);
      
      // try to join event
      cr = dao.joinEvent(eventId, userId);
     
      
    }
    
    
    return cr;

  }
  
  
  
  // attemptLeaveEvent, leaves an event upon user confirmation
  @PostMapping("/attemptLeaveEvent")
  @ResponseBody
  public ControllerResponse attemptLeaveEvent(Model model,  @RequestParam int eventId, HttpServletRequest request) 
  {
    System.out.println("got here in attemptJoinEvent");
    
    
    ControllerResponse cr = new ControllerResponse();
    
    
    User user = getUserFromSession(request);
    
    // check user is in session
    if(user != null)
    {
      int userId = user.getId();
      
      System.out.println("eventId = " + eventId);
      
      // call dao to leave event
      cr = dao.leaveEvent(eventId, userId);
      
      
    }
    
    
    return cr;

  }
  
  
  //attemptEditEventItem controller, alters an event item based on a 'claim' or 'unclaim' action
  @PostMapping("/attemptEditEventItem")
  @ResponseBody
  public ControllerResponse attemptClaimItem(Model model,  @RequestParam int eventItemId, @RequestParam String action, HttpServletRequest request) 
  {
    System.out.println("got here in attemptJoinEvent");
    System.out.println("eventItemId = " + eventItemId);
    
    ControllerResponse cr = new ControllerResponse();
    
    
    User user = getUserFromSession(request);

    // check user in session
    if(user != null)
    {
      // find out the action and edit item appropiately
      if(action.equals("claim"))
      {
        int userId = user.getId();
         
        cr = dao.editEventItem(eventItemId, userId);
      }
      else if(action.equals("unclaim"))
      {
        cr = dao.editEventItem(eventItemId, -1);
      }
      else
      {
        cr.getErrors().add("Error, invalid action");
      }
        
  
      
    }
    
    
    return cr;

  }


  /// Receives form data from the editEvent frontend page and updates the data - (Nas)
  // update event controller
  // handles complete updating process for an event
  // see DAO.handleUpdateEvent for more details
  @PostMapping("/updateEvent")
  @ResponseBody
  public ControllerResponse updateEvent(Model model, HttpServletRequest request, @ModelAttribute Event event, @RequestParam String freeformItemListJson, @RequestParam String eventItemListJson) 
  {
	  
	  
	  ControllerResponse cr = new ControllerResponse();
	  
	  
	  User user = getUserFromSession(request);
	  
	  // check user in session
	  if(user != null) {
		  
		  cr = dao.updateEvent(event);
		  
		  ObjectMapper objectMapper = new ObjectMapper();
		  try 
	    {
	         // Deserialize the JSON strings into a List<EventItem>
	         List<EventItem> freeformEventItems = objectMapper.readValue(freeformItemListJson, new TypeReference<List<EventItem>>() {});
	         List<EventItem> eventItemList = objectMapper.readValue(eventItemListJson, new TypeReference<List<EventItem>>() {});
	         

	         // update the event
	         ControllerResponse itemsCR = dao.handleUpdateEvent(event, freeformEventItems, eventItemList);
	           
	         // add errors if needed
	         cr.getErrors().addAll(itemsCR.getErrors());
	           
	        
	     } 
	     catch (JsonProcessingException e) 
	     {
	        cr.getErrors().add("Failed to process Event Items, creation canceled");
	     }
	  }
	  
	  
	  
	  return cr;
  }
  

  
  
  //attemptDeleteFreeformEventItem
 // tries to delete a freeform event item
 @PostMapping("/attemptDeleteFreeformEventItem")
 @ResponseBody
 public ControllerResponse attemptDeleteFreeformEventItem(Model model,  @RequestParam int freeformEventItemId, HttpServletRequest request) 
 {
   System.out.println("got here in attemptDeleteFreeformEventItem");
   
   
   ControllerResponse cr = new ControllerResponse();
   
   
   User user = getUserFromSession(request);

   // ensure user in valid session
   if(user != null)
   {
     int userId = user.getId();
     
     
     System.out.println("freeformEventItemId = " + freeformEventItemId);
     
     // delete freeform item
     cr = dao.deleteFreeformEventItem(freeformEventItemId);
     
     
   }
   
   
   return cr;

 }
  
 
 
 //attemptSaveFreeformEventItems
 // tries to save a list of freeform event items to the DB
 @PostMapping("/attemptSaveFreeformEventItems")
 @ResponseBody
 public ControllerResponse attemptSaveFreeformEventItems(Model model, @RequestParam int eventId, @RequestParam String freeformItemListJson, HttpServletRequest request) 
 {
   System.out.println("got here in attemptSaveFreeformEventItems");
   
   ControllerResponse cr = new ControllerResponse();
   
   User user = getUserFromSession(request);

   if(user != null)
   {
     int userId = user.getId();
     
     ObjectMapper objectMapper = new ObjectMapper();
     try 
     {
         // Deserialize the JSON string into a List<EventItem>
         List<EventItem> freeformEventItems = objectMapper.readValue(freeformItemListJson, new TypeReference<List<EventItem>>() {});
         

         // insert items
         ControllerResponse itemsCR = dao.insertFreeformEventItems(eventId, freeformEventItems);
           
         // add errors if needed
         cr.getErrors().addAll(itemsCR.getErrors());
           
        
     } 
     catch (JsonProcessingException e) 
     {
        cr.getErrors().add("Failed to process Event Items, creation canceled");
     }
     
   }
   

   System.out.println(freeformItemListJson);
   
   return cr;

 }
 
 
//attemptDeleteEvent
// attempts to delete an event
@PostMapping("/attemptDeleteEvent")
@ResponseBody
public ControllerResponse attemptDeleteEvent(Model model, @ModelAttribute Event event, HttpServletRequest request) 
{
  System.out.println("got here in attemptDeleteEvent");
  
  ControllerResponse cr = new ControllerResponse();
  
  User user = getUserFromSession(request);

  // check user is in session and that user is the one that owns the event
  if(user != null && user.getId() == event.getOwningUserId())
  {

    // delete the event, see DAO.handleDeleteEvent() for more info
    cr = dao.handleDeleteEvent(event.getId());
    
  }
  

  
  return cr;

}

}
