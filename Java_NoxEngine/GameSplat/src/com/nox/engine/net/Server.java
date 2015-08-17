package com.nox.engine.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.nox.engine.util.Serializer;

public class Server {
	public static final int PORT = 1331;

	private DatagramSocket socket;
	private DatagramSocket receiverSocket;
	private Receiver receiver;
	private final int port, maxPacketSize;
	private final long millisToSleep;

	public static void main(String[] args) {
		Server server = new Server(Server.PORT, 1, 1024);
		server.start();
		while(true) {
			ArrayList<DatagramPacket> packets = server.getRoutedPackets(true);
			while(packets.size() > 0) {
				DatagramPacket packet = packets.get(0);
				byte[] data = Server.getDataFromRoutedPacket(packet);
				System.out.println(new String(data));
				if(data.length > 0 && data[0] == "2".getBytes()[0]) { // exit packet
					server.stop();
					System.exit(0);
				}
				server.replyToRoutedPacket(packet, "1".getBytes());
				packets.remove(0);
			}
			try {
				Thread.sleep(1);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Server(int port, long millisToSleep, int maxPacketSize) {
		try {
			this.socket = new DatagramSocket();
			this.receiverSocket = new DatagramSocket(port);
		} catch(SocketException e1) {
			e1.printStackTrace();
		}
		this.port = port;
		this.maxPacketSize = maxPacketSize;
		this.millisToSleep = millisToSleep;
	}

	public void start() {
		this.receiver = new Receiver(receiverSocket, millisToSleep, maxPacketSize);
		receiver.start();
	}

	public void stop() {
		receiver.stop();
		// send a packet to self to get the receiver to stop blocking
		DatagramPacket packet;
		try {
			packet = new DatagramPacket(new byte[] { 0 }, 1, InetAddress.getByName("localhost"), port);
			socket.send(packet);
		} catch(UnknownHostException e1) {
			e1.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void replyToRoutedPacket(DatagramPacket packet, byte[] data) {
		InetAddress address = packet.getAddress();
		int port = Server.getPortFromRoutedPacket(packet);
		send(data, address, port);
	}

	public void send(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * remember that these are routed packets, so you need to call Server.getDataFromRoutedPacket() to get the actual data
	 */
	public synchronized ArrayList<DatagramPacket> getRoutedPackets(boolean clear) {
		return receiver.getPackets(clear);
	}

	public synchronized void clearPackets() {
		receiver.clearPackets();
	}

	public static int getPortFromRoutedPacket(DatagramPacket packet) {
		byte[] data = packet.getData();
		int port = Serializer.getIntFromByteBuffer(data, 0, 4);
		return port;
	}

	public static byte[] getDataFromRoutedPacket(DatagramPacket packet) {
		byte[] data = packet.getData();

		int retDataLen = data.length - 4;
		byte[] retData = new byte[retDataLen];

		for(int i = 0; i < retDataLen; i++) {
			retData[i] = data[i + 4];
		}

		return retData;
	}
}
