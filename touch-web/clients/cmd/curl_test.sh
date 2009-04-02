curl -d "name=testuser&pw=123" http://localhost:8080/touch-web/resources/users
curl -d "chatName=chat1&partyName=anotheruser" http://localhost:8080/touch-web/resources/users/testuser/chats
curl -d "text=hello&dir=0" http://localhost:8080/touch-web/resources/users/testuser/chats/chat1
curl -d "text=hi_there&dir=1" http://localhost:8080/touch-web/resources/users/testuser/chats/chat1
