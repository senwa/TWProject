import java.net.*;
import java.sql.*;
import java.io.*;

import com.mysql.jdbc.Driver;

public class ThreadServer extends Thread {
	 private PrintWriter out = null;
	 private BufferedReader in = null;
	private Socket socket= null;
	Connection con = null;
	static Statement stmt = null;
	ResultSet rs = null;
	private int ant=0;
	String kID= null;
	
	public ThreadServer(Socket socket) {

	        super("ThreadServer");
	        this.socket = socket;
	        try {
	    	    // Setze Reader und Writer f�r den ClientSocket
	    		out = new PrintWriter(socket.getOutputStream(), true);
	    		in = new BufferedReader(
	    			    new InputStreamReader(
	    			    socket.getInputStream()));
	    		
	    		////////////Verbindung zu Datenbank
	    		try{
	    		Class.forName("com.mysql.jdbc.Driver");
	    		Connection con = DriverManager.getConnection( "jdbc:mysql://localhost/mydb","root","" );
	    		System.out.println("Verbindung erfolgreich hergestellt !");
	    		stmt = con.createStatement();
	    		}
	    		catch (SQLException sqle) {
	    			System.out.println(sqle.toString());
	    			}
	    	} catch (Exception e) {
	    	    e.printStackTrace();
	    	}
	    }
///////////////////
	public void run() {   	
		try {
			//if (!in.ready())
			
			
			String Id = in.readLine();
			int intId = Integer.parseInt(Id);
			if(intId>500 && intId<1000) //////Kassse
			{
			out.println("Bitte vierstelligen Passwort eingeben:");
			
			String pw =in.readLine();
			
			/////////////////////
			if(pw.contains("1234") )//////// kasse
			{
				out.println("Ready");
			
				
				//kontrolle();
				zeigeWarenkorb();
				bezahlen();
			}
			else
			{
				out.println("Falsch Idiot");
			}
			
			}
			
			////////////////////
			else ///////Infostand
			{
				String antw=in.readLine();
				ant = Integer.parseInt(antw);
				if(ant==1) /////Kunde hat sich angemeldet
				{  
				kontrolle();
				schreibeWakorb();
				}
				if(ant==2)///////neuKunde m�chte sich registrieren
				{
					registration();
				}
			}
				
			
			out.close();
		    in.close();
			
		}
		catch (Exception e) {
    	    e.printStackTrace();
    	}
	}
/////////hier Methoden
	

public void kontrolle() throws IOException
	{
	String pw1 =null;
	String ktestID=null;
	kID = in.readLine();
	String text=("Select KID from kunde where KID="+kID);
	try {
		ResultSet results =stmt.executeQuery(text);
		while ( results.next() ){
			
			ktestID= results.getString(1);
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		out.println("Bitte registrieren Sie sich");
	}
	/////
	 
	if(ktestID!=null)
	{
	String pw= in.readLine();
	String text1 =("Select Passwort from kunde where KID="+kID);
	try {
		ResultSet results1 =stmt.executeQuery(text1);
		while ( results1.next() ){
		
		 pw1= results1.getString(1);
		 System.out.println(pw1);
		
		}
		if(pw1.equals(pw))
		{
			
			out.println("Sie sind angemeldet.");
		}
	
		else{
			out.println("Falsches Passwort. Versuch Sie es noch einmal.");
		}
		
		
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	///////
	else
	{System.out.println("halo");
	out.println("Bitte registrieren Sie sich");
	
	}
	}

//////////////////////////

public void registration() throws IOException
	{
	
	
	try {
		String selectkaid= in.readLine();
	System.out.println(selectkaid);
		ResultSet results =stmt.executeQuery(selectkaid);
while ( results.next() ){
			
			out.println(results.getString(1));
			
			break;
			}
		String insertkadresse= in.readLine();
		System.out.println(insertkadresse);
		stmt.execute(insertkadresse);
		
		String insertkunde= in.readLine();
		System.out.println(insertkunde);
		stmt.execute(insertkunde);
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	                                                                                                
	}
	
	////////
	///////Infostand eingabeW() zum eingeben der Waren in den Warenkorb
	public void schreibeWakorb() throws IOException
{
	try {
		zeigeWaren();
		korbID();
		String insertware=in.readLine();	
		System.out.println(insertware);
		stmt.execute(insertware);
		System.out.println(insertware);
		System.out.println("Update erfolgreich durchgef�hrt!");
		holepreis();
		String wahl =in.readLine();
		System.out.println("Die wahl:"+wahl);
		if(wahl.contains("1"))////Kasseweiterleiten
		{
		System.out.println("kasse weiterleiten");
		}
		if(wahl.contains("2"))//// Warenkorb �ndern
		{
			zeigeWarenkorb();
			andern();
			
		}
		
	} catch ( SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
public void zeigeWaren() throws IOException
{
	String waren = in.readLine();
	System.out.println(waren);
	try {
		ResultSet results =stmt.executeQuery(waren);
		out.println("WarenID\t Warenname\t Warenpreis");
		while ( results.next() ){
			
			out.println(results.getString(1)+"\t"+results.getString(2)+"\t"+ results.getString(3) );
			}
		out.println("ende");
		//out.println(results);
	} catch (SQLException sqle) {
		// TODO Auto-generated catch block
		System.out.println(sqle.toString());
	}
	
}

////////////////////
public void zeigeWarenkorb() throws IOException
{
	String wakorb=in.readLine();
	
	try {
		ResultSet results =stmt.executeQuery(wakorb);
		
		while ( results.next() ){
			
			out.println(results.getString(1)+"\t"+results.getString(2)+"\t"+ results.getString(3) +"\t"+ results.getString(4));
			}
		out.println("ende");
	}
	catch (SQLException sqle) {
		// TODO Auto-generated catch block
		System.out.println(sqle.toString());
	}
}


/////////////////////////
public void korbID() throws IOException  ///zum �ndern der KorbID
{
	String korbID = in.readLine();
	System.out.println(korbID);	
	try {
		ResultSet results =stmt.executeQuery(korbID);
		while ( results.next() ){
				out.println(results.getString(1));
			}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		System.out.println(e.toString());
	}

}
public int kundenid(){
	String KID=("Select KID from kunde order by KID desc limit 1");
	int kuID = Integer.parseInt(KID);
	kuID=kuID+1;
	return kuID;
}
//////////////////kassse bezahlen
private void bezahlen() throws IOException, SQLException {
	String newbez = null;
	String bez = in.readLine();
	
	try {
		ResultSet results =stmt.executeQuery(bez);
		while ( results.next() ){
				out.println(results.getString(1));
				newbez= results.getString(1);
			}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		System.out.println(e.toString());
	}
String income = ("Update kasse set KAktBest="+newbez+"+KAAnfBest");
System.out.println(income);
stmt.execute(income);

String leeren= in.readLine();
System.out.println(leeren);
stmt.execute(leeren);

}

//////////////////////zum �ndern vom warenkorb
public void andern() throws IOException
{
	String befehl = in.readLine();
	try {
		stmt.executeQuery(befehl);
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		System.out.println(e.toString());
	}
}


/////////////////////hole Preis
public void holepreis() throws IOException
{
String preis = in.readLine();
float menge2 = 0;
float preis2 = 0;
try {
	ResultSet results =stmt.executeQuery(preis);
	while ( results.next() ){
		String preis1=results.getString(1);
		 preis2 = Float.parseFloat(preis1);
	}
} catch (SQLException e) {
	// TODO Auto-generated catch block
	System.out.println(e.toString());
}
////
String menge = in.readLine();
try {
	ResultSet results =stmt.executeQuery(menge);
	while ( results.next() ){
		String menge1=results.getString(1);
		menge2= Float.parseFloat(menge1);
	}
} catch (SQLException e) {
	// TODO Auto-generated catch block
	System.out.println(e.toString());
}
String wareid= in.readLine();

float gespreis= preis2*menge2;
String neupreis=("Update warenkorb set preis="+gespreis+" where WID="+wareid);
	try {
		stmt.execute(neupreis);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	

}

//////////////////////Ende
}