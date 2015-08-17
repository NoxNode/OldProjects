package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class Receiver extends Thread {
	private DatagramSocket socket;
	private int sleepTime;
	private ArrayList<DatagramPacket> packets;

	Receiver(DatagramSocket socket, int sleepTime) {
		this.socket = socket;
		this.sleepTime = sleepTime;
		this.packets = new ArrayList<DatagramPacket>();
		this.start();
	}

	void stopRunning() {
		this.interrupt();
	}

	boolean isRunning() {
		return !this.isInterrupted();
	}

	synchronized ArrayList<DatagramPacket> getAllPackets() {
		return packets;
	}

	synchronized void clearAllPackets() {
		packets.clear();
	}

	private synchronized void addPacket(DatagramPacket packet) {
		packets.add(packet);
	}

	synchronized ArrayList<DatagramPacket> getPacketsWithId(byte[] id,
			boolean removePackets) {
		ArrayList<DatagramPacket> validPackets = new ArrayList<DatagramPacket>();
		for(int j = 0; j < packets.size(); j++) {
			DatagramPacket dp = packets.get(j);
			byte[] data = dp.getData();
			if(data.length >= id.length) {
				int nValidBytes = 0;
				for(int i = 0; i < id.length; i++) {
					if(id[i] == data[i]) {
						nValidBytes++;
					}
				}
				if(nValidBytes == id.length) {
					validPackets.add(dp);
					if(removePackets) {
						packets.remove(j);
						j--;
					}
				}
			}
		}
		return validPackets;
	}

	@Override
	public void run() {
		while(!this.isInterrupted()) {
			if(socket != null) {
				byte[] data = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);

				try {
					socket.receive(packet);
					if(packet != null) {
						addPacket(packet);
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(sleepTime);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
