#pragma once
#pragma comment (lib, "Ws2_32.lib")

#include <WinSock.h>

class TCP
{	
private:

	SOCKET sConnect;

	SOCKET sListen;

	SOCKADDR_IN addr;

	int port;

	WSAData wsaData;

	WORD DLLVERSION;

	void IPAddress(); //Uses the address the user typed into the IPAddress.txt file

	void Port(); //Uses the port the user typed into the Port.txt file

	bool SockError; //true if error starting, sending, or recieving with socket

	int error; //If error, error code is stored in error and displayed

	int addrlen;

	bool Connected;
	
public:
		
	char sendbuf[22];
	char recvbuf[22];

	bool GetCon() const { return Connected; };

	void SetCon(bool set) { Connected = set; };

	bool GetError() const { return SockError; };

	int GetErrorCode() const { return error; };
	
	void Recieve( char* buf, short len );

	void Send( char* buf, short len );

	void Accept();

	void Connect();

	void StartSock();

	void EndSock();

};