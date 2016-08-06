import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.json.simple.*;

// Extend HttpServlet class
public class Omi extends HttpServlet{
    public static int player = 0;		//Number of players connected
    public static String[] trick = new String[4];
    public static String trumpSuit;
	public static int trick_winner = 1;
	public static int trickState = 0; 
	GameStates state;
	
	public static String[] cardpack=new String[52];
    
    public void init() throws ServletException{
		allCards();
    	mix_cards(cardpack);	
		state = GameStates.Waiting;	
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
		
		response.setContentType("application/json");  
        PrintWriter out = response.getWriter();  

		String responce_2 =new String();

        HttpSession session = request.getSession();
    
        if(request.getParameter("type").equals("adding_Players")){
        	if(state.toString() == "Waiting"){
				responce_2 = adding_Players(request.getParameter("name"), session);
			}
            else{
				
            	responce_2 = "4 players Connected";
			}
        }
		else if(request.getParameter("type").equals("remove_cards")){
			
			responce_2 = remove_cards(request.getParameter("card"), session);
			state.eval("remove_cards");
			trickState++;
		}
		else if(request.getParameter("type").equals("update")){
			responce_2 = update();
		}
		
        out.write(responce_2);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException{  

    }
    public void destroy()
    {
      
    }
	
	public static void allCards(){	
    	int suit = 0 , val = 1;		//0-diamond, 1-club, 2-heart, 3-spade
    	
    	for(int i = 0; i<52; i++){
    		cardpack[i] = "cards/" + suit + "_" + val + ".png";
    		if(val == 13){
    			val = 0;
    			suit++;
    		}
    		val++;
    	}
		
    }
    
    public static String[] mix_cards(String[] cardpack){
    	String temp;
    	int random_place;
    	for(int i = 0; i<cardpack.length; i++){
    		random_place = (int )(Math.random() * 52);
    		temp = cardpack[i];
    		cardpack[i] = cardpack[random_place];
    		cardpack[random_place] = temp;
    	}
		String[] temp1=cardpack[cardpack.length-1].split("_");
		
		
    	return cardpack;
    }
    
    /*return cards of the player*/
    public String adding_Players(String name, HttpSession session){
		
    	session.setAttribute("player_name", name);
        player++;
		session.setAttribute("player_id", player);

        if(player==4){
            state.eval("ready");
		}

    	String[] mycards = new String[13];
		
    	JSONArray cards = new JSONArray();
   
    	for(int i = 0; i<13; i++)
    	{
    		JSONObject ob1 = new JSONObject();
			ob1.put("image",cardpack[13*(player-1)+i]);
			cards.add(ob1);
    		mycards[i] = cardpack[13*(player-1)+i];
    	}
		
		JSONObject ob2 = new JSONObject();
		ob2.put("cards",cards);
		
		//store player's cards in session data
        session.setAttribute("cards", mycards);
		
		String ob3=ob2.toString();
		String ob4=ob3.replace("\\","");
		
    	return ob4;
		
    }
    
    
    public String remove_cards(String played_card, HttpSession session){		//Remove the played cards
		int card_val=(int)session.getAttribute("player_id")-1;
		trick[card_val] = played_card.replace("\"","");
		
        String[] cards = (String[])session.getAttribute("cards");
        if(cards == null)
            return "NULL";
        
        JSONArray card_array = new JSONArray();
   
    	for(int i = 0; i<13; i++){
    		JSONObject ob1 = new JSONObject();
    		if(!cards[i].equals("") && (played_card.replace("\"","")).equals(cards[i])){
			    cards[i] = null;
			}    
			else{
				ob1.put("image",cards[i]);
			    card_array.add(ob1);
			}
		}
		
		JSONObject ob2 = new JSONObject();
		ob2.put("cards",card_array);
		
		String ob3=ob2.toString();
		String ob4=ob3.replace("\\","");
		
    	return ob4;
    }
    
    public static String update(){		
    	JSONArray cards = new JSONArray();
   
    	for(int i = 0; i<4; i++){
    		JSONObject ob1 = new JSONObject();
			if(trick[i] != null)
				ob1.put("image",trick[i]);
			else
				ob1.put("image","");
			cards.add(ob1);
		}
		
		JSONObject ob2 = new JSONObject();
		ob2.put("cards",cards);
		
		String ob3=ob2.toString();
		String ob4=ob3.replace("\\","");
		
    	return ob4;
	}
}


