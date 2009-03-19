curl -d "name=testuser&pw=123" http://localhost:8080/touch-web/resources/users
curl -d "chatName=chat1&partyName=anotheruser" http://localhost:8080/touch-web/resources/users/1/chats
curl -d "text=hello&direction=0" http://localhost:8080/touch-web/resources/users/1/chats/chat1
curl -d "text=hi_there&direction=1" http://localhost:8080/touch-web/resources/users/1/chats/chat1
