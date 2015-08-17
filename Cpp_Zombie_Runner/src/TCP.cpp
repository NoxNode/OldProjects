#include "TCP.h"
#include <stdio.h>

void TCP::IPAddress()
{
	FILE* pFile;
	char ip[100];

	pFile = fopen("Docs/IPAddress.txt", "r");

	fscanf(pFile, "%s", &ip);

	fclose(pFile);

	addr.sin_addr.s_addr = inet_addr(ip);
}

void TCP::Port()
{
	FILE* pFile;
	char ip[100];

	pFile = fopen("Docs/Port.txt", "r");

	fscanf(pFile, "%s", &ip);

	fclose(pFile);

	port = atoi(ip);
}

void TCP::Accept()
{		
	sListen = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP);

	error = bind( sListen,(SOCKADDR*)&addr,sizeof(addr));
	if( error != 0 )
	{
		error = WSAGetLastError();
		SockError = true;
	}

	error = listen( sListen,SOMAXCONN );
	if( error != 0 )
	{
		error = WSAGetLastError();
		SockError = true;
	}

	sConnect = accept( sListen,(SOCKADDR*)&addr,&addrlen);
	if( sConnect == INVALID_SOCKET )
	{
		SockError = true;
		error = WSAGetLastError();
	}
	else
	{
		SockError = false;
		error = 2;
	}
}

void TCP::Connect()
{
	error = connect( sConnect,(SOCKADDR*)&addr,sizeof(addr) );
	if( error == SOCKET_ERROR )
	{
		error = WSAGetLastError();
		SockError = true;
	}
	else
	{
		error = 2;
		SockError = false;
	}
}

void TCP::Recieve( char* buf, short len )
{
	error = recv( sConnect,buf,len,NULL );
	if( error == SOCKET_ERROR )
	{
		error = WSAGetLastError();
		SockError = true;
	}
}

void TCP::Send( char* buf, short len )
{
	error = send( sConnect,buf,len,NULL );
	if( error == SOCKET_ERROR )
	{
		error = WSAGetLastError();
		SockError = true;
	}
}

void TCP::StartSock()
{
	SockError = false;
	error = 0;
	addrlen = sizeof(addr);

	DLLVERSION = MAKEWORD(2,1);

	error = WSAStartup(DLLVERSION,&wsaData);
	if( error != 0 )
	{
		error = WSAGetLastError();
		SockError = true;
	}
	
	sConnect = socket(AF_INET,SOCK_STREAM,NULL);

	IPAddress();
	Port();

	if( addr.sin_addr.s_addr == INADDR_NONE || addr.sin_addr.s_addr == INADDR_ANY )
	{
		error = WSAGetLastError();
		SockError = true;
	}
	addr.sin_family = AF_INET;
	addr.sin_port = htons(port);
}

void TCP::EndSock()
{
	shutdown( sConnect,2 );
	closesocket( sConnect );
	shutdown( sListen,2 );
	closesocket( sListen );
	WSACleanup();
}