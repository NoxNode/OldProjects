package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {
	volatile private DatagramSocket socket;
	private ArrayList<InetAddress> clientIps;
	private ArrayList<Integer> clientPorts;
	private int nClients;
	private Receiver receiver;

	public Server(int port, int receiverSleepTime) {
		try {
			this.socket = new DatagramSocket(port);
		} catch(SocketException e) {
			e.printStackTrace();
		}
		clientIps = new ArrayList<InetAddress>();
		clientPorts = new ArrayList<Integer>();
		nClients = 0;
		receiver = new Receiver(socket, receiverSleepTime);
	}

	public void send(byte[] id, byte[] data, InetAddress ip, int port) {
		byte[] dataWithId = new byte[id.length + data.length];
		for(int i = 0; i < id.length; i++) {
			dataWithId[i] = id[i];
		}
		for(int i = 0; i < data.length; i++) {
			dataWithId[id.length + i] = data[i];
		}
		DatagramPacket packet = new DatagramPacket(dataWithId,
				dataWithId.length, ip, port);
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
		for(DatagramPacket packet : packets) {
			InetAddress ip = packet.getAddress();
			int port = packet.getPort();

			if(nClients == 0) {
				clientIps.add(ip);
				clientPorts.add(port);
				nClients++;
			}
			else {
				boolean already_in_array = false;
				for(int i = 0; i < nClients; i++) {
					if(clientIps.get(i) == ip && clientPorts.get(i) == port) {
						already_in_array = true;
					}
				}
				if(!already_in_array) {
					clientIps.add(ip);
					clientPorts.add(port);
					nClients++;
				}
			}
		}

		return packets;
	}

	public ArrayList<InetAddress> getClientIps() {
		return clientIps;
	}

	public ArrayList<Integer> getClientPorts() {
		return clientPorts;
	}

	public int getnClients() {
		return nClients;
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
