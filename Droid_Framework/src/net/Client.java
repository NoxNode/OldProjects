package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
	volatile private DatagramSocket socket;
	private InetAddress ipAddress;
	private int port;
	private Receiver receiver;

	public Client(String ipAddress, int port, int receiverSleepTime) {
		this.port = port;
		try {
			socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch(SocketException e) {
			e.printStackTrace();
		} catch(UnknownHostException e) {
			e.printStackTrace();
		}
		receiver = new Receiver(socket, receiverSleepTime);
	}

	public void send(byte[] id, byte[] data) {
		byte[] dataWithId = new byte[id.length + data.length];
		for(int i = 0; i < id.length; i++) {
			dataWithId[i] = id[i];
		}
		for(int i = 0; i < data.length; i++) {
			dataWithId[id.length + i] = data[i];
		}
		DatagramPacket packet = new DatagramPacket(dataWithId,
				dataWithId.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<DatagramPacket> getPacketsWithId(byte[] id,
			boolean removePackets) {
		ArrayList<DatagramPacket> packets = receiver.getPacketsWithId(id,
				removePackets);
		return packets;
	}

	public boolean isReceiverRunning() {
		return receiver.isRunning();
	}

	public void restartReceiver(int receiverSleepTime) {
		if(receiver != null) {
			stopReceiver();
		}
		receiver = new Receiver(socket, receiverSleepTime);
	}

	public void stopReceiver() {
		receiver.stopRunning();
	}

	public ArrayList<DatagramPacket> getAllPackets() {
		return receiver.getAllPackets();
	}
}
