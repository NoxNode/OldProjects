pushd src
g++ -o../bin/main.exe -std=c++11 ^
 ^
Boss.cpp ^
Droplet.cpp ^
Enemy.cpp ^
Entity.cpp ^
LinkedList.cpp ^
main.cpp ^
Node.cpp ^
Player.cpp ^
Umbrella.cpp ^
 ^
-I../include -L../lib -lsfml-graphics -lsfml-system -lsfml-window
popd
