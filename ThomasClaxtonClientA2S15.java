import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.*;

/**
 * NtpClient - an NTP client for Java.  This program connects to an NTP server
 * and prints the response to the console.
 * 
 * The local clock offset calculation is implemented according to the SNTP
 * algorithm specified in RFC 2030.  
 * 
 * Note that on windows platforms, the curent time-of-day timestamp is limited
 * to an resolution of 10ms and adversely affects the accuracy of the results.
 * 
 * 
 * This code is copyright (c) Adam Buckley 2004
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.  A HTML version of the GNU General Public License can be
 * seen at http://www.gnu.org/licenses/gpl.html
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
 * more details.
 *  
 * @author Adam Buckley
 */
public class ThomasClaxtonClientA2S15
{
    public static void main(String[] args) throws IOException
    {
	
	//Start Part A
	System.out.println("PART A");
    //TODO: Change this to the NTP SERVER NAME!!	
	String serverName = "towson.edu";
	
	// Send request
	DatagramSocket socket = new DatagramSocket();
	InetAddress address = InetAddress.getByName(serverName);
	byte[] buf = new ThomasClaxtonServerA2S15().toByteArray();
	DatagramPacket packet =
	    new DatagramPacket(buf, buf.length, address, 123);
	
	// Set the transmit timestamp *just* before sending the packet
	// ToDo: Does this actually improve performance or not?
	ThomasClaxtonServerA2S15.encodeTimestamp(packet.getData(), 40,
				   (System.currentTimeMillis()/1000.0) + 2208988800.0);
	
	socket.send(packet);
	
	
	// Get response
	System.out.println("NTP request sent, waiting for response...\n");
	packet = new DatagramPacket(buf, buf.length);
	socket.receive(packet);
	
	// Immediately record the incoming timestamp
	double destinationTimestamp =
	    (System.currentTimeMillis()/1000.0) + 2208988800.0;
	
	
	// Process response
	ThomasClaxtonServerA2S15 msg = new ThomasClaxtonServerA2S15(packet.getData());
	
	// Corrected, according to RFC2030 errata
	double roundTripDelay = (destinationTimestamp-msg.originateTimestamp) -
	    (msg.transmitTimestamp-msg.receiveTimestamp);
	
	double localClockOffset =
	    ((msg.receiveTimestamp - msg.originateTimestamp) +
	     (msg.transmitTimestamp - destinationTimestamp)) / 2;
	
	
	// Display response
	System.out.println("NTP server: " + serverName);
	System.out.println(msg.toString());
	
	System.out.println("Dest. timestamp:     " +
			   ThomasClaxtonServerA2S15.timestampToString(destinationTimestamp));
	
	System.out.println("T3-T1: Round-trip delay: " +
			   new DecimalFormat("0.00").format(roundTripDelay*1000) + " ms");
	
	System.out.println("Local clock offset: " +
			   new DecimalFormat("0.00").format(localClockOffset*1000) + " ms");
			   
	socket.close();
	
	//Start part B
	System.out.println("PART B");
	Scanner console = new Scanner(System.in);
	System.out.println("Enter filename in the form: c:\\path\\filename.mp3");
	//Collects file to send 
	String fileName = console.nextLine();
	
	// Send request
	 socket = new DatagramSocket();
	 address = InetAddress.getByName(fileName);
	 buf = new ThomasClaxtonServerA2S15().toByteArray();
	
	//Sends to 33312
	 packet = new DatagramPacket(buf, buf.length, address, 33312);
	ThomasClaxtonServerA2S15.encodeTimestamp(packet.getData(), 40,
				   (System.currentTimeMillis()/1000.0) + 2208988800.0);
	
	socket.send(packet);
	
	//Response from server
	packet = new DatagramPacket(buf, buf.length);
	socket.receive(packet);
	
	// Immediately record the incoming timestamp
	destinationTimestamp =
	    (System.currentTimeMillis()/1000.0) + 2208988800.0;
	
	// Process response
	 msg = new ThomasClaxtonServerA2S15(packet.getData());
	
	// Corrected, according to RFC2030 errata
	roundTripDelay = (destinationTimestamp-msg.originateTimestamp) -
	    (msg.transmitTimestamp-msg.receiveTimestamp);
	
	 localClockOffset =
	    ((msg.receiveTimestamp - msg.originateTimestamp) +
	     (msg.transmitTimestamp - destinationTimestamp)) / 2;
	
	// Display response
	System.out.println("NTP server: " + fileName);
	System.out.println(msg.toString());
	
	System.out.println("Dest. timestamp:     " +
			   ThomasClaxtonServerA2S15.timestampToString(destinationTimestamp));
			   
	socket.send(packet);
	
	
	
    }
    
    
    
    /**
     * Prints usage
     */
    static void printUsage()
    {
	System.out.println(
			   "NtpClient - an NTP client for Java.\n" +
			   "\n" +
			   "This program connects to an NTP server and prints the response to the console.\n" +
			   "\n" +
			   "\n" +
			   "Usage: java NtpClient server\n" +
			   "\n" +
			   "\n" +
			   "This program is copyright (c) Adam Buckley 2004 and distributed under the terms\n" +
			   "of the GNU General Public License.  This program is distributed in the hope\n" +
			   "that it will be useful, but WITHOUT ANY WARRANTY; without even the implied\n" +
			   "warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU\n" +
			   "General Public License available at http://www.gnu.org/licenses/gpl.html for\n" +
			   "more details.");
	
    }
}