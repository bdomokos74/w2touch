import sys
import re
from xml.etree import ElementTree


#from xml.etree.ElementTree import ElementTree
import httplib2

class Connection:
    def __init__(self, url, userid, token):
        self.userid = userid;
        self.token = token;
        self.url = url;
        self.client = httplib2.Http(".cache")
    
    def getroot(self):
        return self.url + '/users/' + self.userid
        
    def getchats(self):
        response, xml = self.client.request( self.getroot() + '/chats')
        trim_xmldecl = re.sub(r'^.*DOCTYPE[^>]*>\n', '', xml)
        trim_namespace = re.sub(r'<html[^>]*>', '<html>', trim_xmldecl)
        #print trim_namespace
        doc = ElementTree.fromstring(trim_namespace)
        tree = ElementTree.ElementTree(doc)
        it = doc.getiterator()
        #for each in it:
        #    print each.tag
        list = tree.findall('//ul/li/a') #[@class=\'chat\']')
        retval = []
        for each in list:
            retval.append(each.text)
            #print each.text
        return retval
        
    def getchat(self, chatname):
        response, xml = self.client.request(self.getroot() + '/chats/' + chatname)
        if response['status']  == '200':
            print xml
            #return Chat()
        #doc = ElementTree.fromstring(xml)
        
    