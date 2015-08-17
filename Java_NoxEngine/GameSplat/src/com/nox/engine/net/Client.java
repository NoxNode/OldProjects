package com.nox.engine.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.nox.engine.util.Serializer;

public class Client {
    public static final byte[] ServerIP = new byte[] { 127, 0, 0, 1 };
    public static final int PORT = 1332;

    private DatagramSocket socket;
    private DatagramSocket receiverSocket;
    private Receiver receiver;
    private InetAddress ipAddress;
    private final int serverPort, clientPort, maxPacketSize;
    private final long millisToSleep;

    public Client(byte[] ipAddress, int serverPort, int clientPort,
	    long millisToSleep, int maxPacketSize) {
	try {
	    this.socket = new DatagramSocket();
	    this.receiverSocket = new DatagramSocket(clientPort);
	    this.ipAddress = InetAddress.getByAddress(ipAddress);
	} catch (SocketException e1) {
	    e1.printStackTrace();
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}
	this.serverPort = serverPort;
	this.clientPort = clientPort;
	this.maxPacketSize = maxPacketSize;
	this.millisToSleep = millisToSleep;
    }

    public void start() {
	this.receiver = new Receiver(receiverSocket, millisToSleep,
		maxPacketSize);
	receiver.start();
    }

    public void stop() {
	receiver.stop();
	// send a packet to self to get the receiver to stop blocking
	DatagramPacket packet;
	try {
	    packet = new DatagramPacket(new byte[] { 0 }, 1,
		    InetAddress.getByName("localhost"), clientPort);
	    socket.send(packet);
	} catch (UnknownHostException e1) {
	    e1.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public synchronized void send(byte[] data) {
	byte[] routedData = new byte[data.length + 4]; // 4 byte in the int
						       // receiver port
	Serializer.addIntToByteBuffer(routedData, clientPort, 0, 4);

	for (int i = 4; i < routedData.length; i++) {
	    routedData[i] = data[i - 4];
	}

	DatagramPacket routedPacket = new DatagramPacket(routedData,
		routedData.length, ipAddress, serverPort);
	try {
	    socket.send(routedPacket);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public synchronized ArrayList<DatagramPacket> getPackets(boolean clear) {
	return receiver.getPackets(clear);
    }

    public synchronized void clearPackets() {
	receiver.clearPackets();
    }
}
