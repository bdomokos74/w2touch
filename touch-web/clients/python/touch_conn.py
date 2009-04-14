import sys
import re
from elementtree import ElementTree

import httplib2
import urllib

from touch_data import Chat, Post

class Connection:
    def __init__(self, url, userid, token):
        self.userid = userid;
        self.token = token;
        self.url = url;
        self.client = httplib2.Http(".cache")
    
    def getRoot(self):
        return self.url + '/users/' + self.userid
        
    def getChatlist(self):
        response, xml = self.client.request( self.createChatlistURI() )
        if response['status'] != '200':
            return None()
        tree = ElementTree.XML(xml)
        list = tree.findall(".//{http://www.w3.org/1999/xhtml}a[@class='chat']")
        retval = []
        for each in list:
            retval.append(each.text)
        return retval
        
    def getChat(self, chatname):
        response, xml = self.client.request(self.createChatURI(chatname))
        if response['status']  == '200':
            element = ElementTree.XML(xml)
            name = element.find(".//{http://www.w3.org/1999/xhtml}dd[@class='chatname']").text
            partner = element.find(".//{http://www.w3.org/1999/xhtml}dd[@class='chatpartner']").text
            return Chat(name, partner)
        else:
            raise Exception('Not found', chatname)
        
    def createChatlistURI(self):
        return self.getRoot()+'/chats'
    
    def createChatURI(self, chatName):
        return self.createChatlistURI()+'/'+urllib.quote(chatName)
        
    def createChat(self, chatname, partyname):
        #h.add_credentials('name', 'password')
        response, content = self.client.request(self.createChatlistURI(), 
                                  "POST", 
                                  body=urllib.urlencode({'chatName': chatname, 'partyName': partyname}), 
                                  headers= {'Content-type': 'application/x-www-form-urlencoded'} )
        if response['status'] != '201':
            return None
        else:
            return Chat(chatname, partyname)
            
    def deleteChat(self, chatname):
        response, content = self.client.request(self.createChatURI(chatname), 
                                  "DELETE" )

    def createPost(self, chatname, text, dir):
        #h.add_credentials('name', 'password')
        print("creating post - "+self.createChatURI(chatname))
        response, content = self.client.request(self.createChatURI(chatname), 
                                  "POST", 
                                  body=urllib.urlencode({'text': text, 'dir': dir}), 
                                  headers= {'Content-type': 'application/x-www-form-urlencoded'} )
        print("ret - " + content)
        if response['status'] != '200':
            raise Exception('failed request', response['status'])
        else:
            return Post(chatname, text, dir)
        
    def getPostlist(self, chatname):
        response, xml = self.client.request( self.createChatURI(chatname) )
        if response['status'] != '200':
            return None()
        tree = ElementTree.XML(xml)
        list = tree.findall('.//{http://www.w3.org/1999/xhtml}a')
        retval = []
        for each in list:
            retval.append( Post(chatname, each.text, 0) )
        return retval
        