package com.nox.game;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

import com.nox.engine.net.Server;
import com.nox.engine.util.Serializer;

public class GSServer {
    private Server server;
    private boolean running;

    public static void main(String[] args) {
	GSServer server = new GSServer(new Server(Server.PORT, 1, 10));
	server.getServer().start();
	while (server.running) {
	    server.update();

	    try {
		Thread.sleep(1);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }

    private final int MAXCLIENTS = 5;
    private InetAddress[] ips = new InetAddress[MAXCLIENTS];
    private int[] ports = new int[MAXCLIENTS];
    private int[] xCoords = new int[MAXCLIENTS];
    private int[] yCoords = new int[MAXCLIENTS];
    int nClients = 0;

    public GSServer(Server server) {
	this.server = server;
	this.running = true;
    }

    public Server getServer() {
	return server;
    }

    // TODO update position instead of movement - use lag compensation
    public void update() {
	ArrayList<DatagramPacket> packets = server.getRoutedPackets(true);
	for (int i = 0, size = packets.size(); i < size; i++) {
	    DatagramPacket packet = packets.get(i);
	    byte[] data = Server.getDataFromRoutedPacket(packet);

	    final int speed = 5;

	    if (data.length > 0) {
		if (data[0] == 'E') {
		    server.stop();
		    running = false;
		    System.exit(0);
		} else if (data[0] == 'R' && nClients < MAXCLIENTS - 1) {
		    if (data[1] == 1) {
			ips[nClients] = packet.getAddress();
			ports[nClients] = Server
				.getPortFromRoutedPacket(packet);
			server.replyToRoutedPacket(packet, new byte[] { 'R',
				(byte) nClients });
			nClients++;
		    }
		} else if (data[0] == 'W') {
		    InetAddress ip = packet.getAddress();
		    int port = Server.getPortFromRoutedPacket(packet);
		    for (int j = 0; j < nClients; j++) {
			if (ips[j].equals(ip)) {
			    if (ports[j] == port) {
				yCoords[j] -= speed;
				byte[] dataToSend = new byte[9];
				dataToSend[0] = (byte) j;
				Serializer.addIntToByteBuffer(dataToSend,
					xCoords[j], 1, 4);
				Serializer.addIntToByteBuffer(dataToSend,
					yCoords[j], 5, 4);
				for (int k = 0; k < nClients; k++) {
				    server.send(dataToSend,
					    packet.getAddress(), ports[k]);
				}
			    }
			}
		    }
		} else if (data[0] == 'A') {
		    InetAddress ip = packet.getAddress();
		    int port = Server.getPortFromRoutedPacket(packet);
		    for (int j = 0; j < nClients; j++) {
			if (ips[j].equals(ip)) {
			    if (ports[j] == port) {
				xCoords[j] -= speed;
				byte[] dataToSend = new byte[9];
				dataToSend[0] = (byte) j;
				Serializer.addIntToByteBuffer(dataToSend,
					xCoords[j], 1, 4);
				Serializer.addIntToByteBuffer(dataToSend,
					yCoords[j], 5, 4);
				for (int k = 0; k < nClients; k++) {
				    server.send(dataToSend,
					    packet.getAddress(), ports[k]);
				}
			    }
			}
		    }
		} else if (data[0] == 'S') {
		    InetAddress ip = packet.getAddress();
		    int port = Server.getPortFromRoutedPacket(packet);
		    for (int j = 0; j < nClients; j++) {
			if (ips[j].equals(ip)) {
			    if (ports[j] == port) {
				yCoords[j] += speed;
				byte[] dataToSend = new byte[9];
				dataToSend[0] = (byte) j;
				Serializer.addIntToByteBuffer(dataToSend,
					xCoords[j], 1, 4);
				Serializer.addIntToByteBuffer(dataToSend,
					yCoords[j], 5, 4);
				for (int k = 0; k < nClients; k++) {
				    server.send(dataToSend,
					    packet.getAddress(), ports[k]);
				}
			    }
			}
		    }
		} else if (data[0] == 'D') {
		    InetAddress ip = packet.getAddress();
		    int port = Server.getPortFromRoutedPacket(packet);
		    for (int j = 0; j < nClients; j++) {
			if (ips[j].equals(ip)) {
			    if (ports[j] == port) {
				xCoords[j] += speed;
				byte[] dataToSend = new byte[9];
				dataToSend[0] = (byte) j;
				Serializer.addIntToByteBuffer(dataToSend,
					xCoords[j], 1, 4);
				Serializer.addIntToByteBuffer(dataToSend,
					yCoords[j], 5, 4);
				for (int k = 0; k < nClients; k++) {
				    server.send(dataToSend,
					    packet.getAddress(), ports[k]);
				}
			    }
			}
		    }
		}
	    }
	}
    }
}
