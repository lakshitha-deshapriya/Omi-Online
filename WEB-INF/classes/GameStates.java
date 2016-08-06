public enum GameStates{
	Waiting{
		GameStates eval(String command){ //Wait till all of them connect
			if(command.equals("ready"))		
				return Player_01; 
			return Waiting;	
		}
	}
	,Player_01{
		GameStates eval(String command){	//Player 01 plays
			if(command.equals("remove_cards"))
				return Player_02;
			else
				return Player_01;
		}
	} 
	,Player_02{
		GameStates eval(String command){ 	//Player 02 Plays
			if(command.equals("remove_cards"))
				return Player_03;
			else
				return Player_02;
		}
	}
	,Player_03{
		GameStates eval(String command){ 	//Player 03 plays
			if(command.equals("remove_cards"))
				return Player_04;
			else
				return Player_03;
		}
	}
	,Player_04{
		GameStates eval(String command){ 	//Player 04 plays
			if(command.equals("remove_cards"))
				return Player_01;
			else
				return Player_04;
		}
	}
	,Points{								//Give points
		GameStates eval(String command){ 
			if(Omi.trick_winner == 1)					
				return Player_01;
			else if(Omi.trick_winner == 2)
				return Player_02;
			else if(Omi.trick_winner == 3)
				return Player_02;
			else if(Omi.trick_winner == 4)
				return Player_02;
			else
				return Points;
			
		}
	} ;
	abstract GameStates eval(String command);

    public String toString(){	//Convert enum output to a string 
        switch(this){
	        case Waiting :
		        return "Waiting";
		    case Player_01 :
		        return "Player_01";
		     case Player_02 :
		        return "Player_02";
 			case Player_03 :
		        return "Player_03"; 
			case Player_04 :
		        return "Player_04";
			 case Points :
		        return "Points";
        }
        return null;
    }
}




