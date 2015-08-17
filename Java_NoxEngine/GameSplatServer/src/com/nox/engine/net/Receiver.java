package com.nox.engine.net;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class Receiver implements Runnable {
	private Thread receiverThread;
	private final DatagramSocket socket;
	private final ArrayList<DatagramPacket> packets;
	private final long millisToSleep;
	private final int maxPacketSize;
	private boolean running;

	public Receiver(DatagramSocket socket, long millisToSleep, int maxPacketSize) {
		this.socket = socket;
		this.packets = new ArrayList<DatagramPacket>();
		this.millisToSleep = millisToSleep;
		this.maxPacketSize = maxPacketSize;
		this.running = false;
	}

	public synchronized void start() {
		if(receiverThread != null) {
			return;
		}
		receiverThread = new Thread(this);
		running = true;
		receiverThread.start();
	}

	public synchronized void stop() {
		if(receiverThread == null) {
			return;
		}
		running = false;
	}

	@Override
	public void run() {
		while(running) {
			byte[] data = new byte[maxPacketSize];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
				addPacket(packet);
			} catch(IOException e) {
				e.printStackTrace();
			}
			if(millisToSleep > 0) {
				try {
					Thread.sleep(millisToSleep);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized ArrayList<DatagramPacket> getPackets(boolean clear) {
		ArrayList<DatagramPacket> copiedPackets = new ArrayList<DatagramPacket>();
		for(int i = 0, size = packets.size(); i < size; i++) {
			copiedPackets.add(packets.get(i));
		}
		if(clear) {
			packets.clear();
		}
		return copiedPackets;
	}

	public synchronized void clearPackets() {
		packets.clear();
	}

	private synchronized void addPacket(DatagramPacket packet) {
		packets.add(packet);
	}
}
