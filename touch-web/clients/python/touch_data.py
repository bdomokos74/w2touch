class Chat:
    def __init__(self, id, name, otherParty):
        self.id = id
        self.name = name
        self.otherParty = otherParty

    def __init__(self, name, otherParty):
        self.id = -1
        self.name = name
        self.otherParty = otherParty

class Post:
    def __init__(self, id, chatname, text, dir):
        self.id = id
        self.chatname = chatname
        self.text = text
        self.dir = dir

    def __init__(self, chatname, text, dir):
        self.id = -1
        self.chatname = chatname
        self.text = text
        self.dir = dir