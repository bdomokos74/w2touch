import touch_conn
import unittest
from elementtree import ElementTree

SAMPLE_XML = ElementTree.XML("""
<body>
  <tag class='a'>text</tag>
  <tag class='b' />
  <section>
    <tagx class='b' id='inner'>subtext</tagx>
  </section>
</body>
""")

class TestConn(unittest.TestCase):
    def setUp(self):
        self.conn = touch_conn.Connection('http://localhost:8080/touch-web/resources', 'testuser', '');
        self.deleteChats()
        self.conn.createChat('chat1', 'party1')
    
    def tearDown(self):
        self.deleteChats()
    
    def testElementTree(self):
        retval = SAMPLE_XML.find(".//tagx[@id]")
        self.assertEqual('tagx', retval.tag)
    
    def deleteChats(self):
        chats = self.conn.getChatlist()
        for ch in chats:
            self.conn.deleteChat(ch)
        
    def testGetChatlist(self):
        list = self.conn.getChatlist()
        self.assertTrue( list )
        self.assertEqual(1, len(list))
        self.assertEqual(['chat1'], list)
    
    def testGetChat(self):
        chat = self.conn.getChat('chat1')
        self.assertEquals('chat1', chat.name)
        self.assertEquals('party1', chat.otherParty)

    def testGetChat_none(self):
        chat = self.conn.getChat('chat2')
        if not chat is None:
            self.fail("Exception expected")
            
    def testCreateChatlistURI(self):
        self.assertEqual('http://localhost:8080/touch-web/resources/users/testuser/chats', 
                         self.conn.createChatlistURI())
        
    def testCreateChatURI(self):
        self.assertEqual('http://localhost:8080/touch-web/resources/users/testuser/chats/chatname1', 
                         self.conn.createChatURI('chatname1'))
        
    def testCreateChat(self):
        ref = self.conn.createChat('chat3', 'test')
        #self.assertEqual(2, len(list))
        self.assertEqual('test', self.conn.getChat('chat3').otherParty)
        
    def testCreateChat_specialchar(self):
        ref = self.conn.createChat('chat3$ 2', 'test')
        
        list = self.conn.getChatlist()
        for elem in list:
            print elem + " | "
            
        #self.assertEqual(2, len(list))
        self.assertEqual('test', self.conn.getChat('chat3$ 2').otherParty)
        
    def testDeleteChat(self):
        ref = self.conn.deleteChat('chat1')
        self.assertEqual(0, len(self.conn.getChatlist()))
    
    def testCreatePost(self):
        self.conn.createPost('chat1', 'hi there', 0)
        posts = self.conn.getPostlist('chat1')
        self.assertEqual(1, len(posts))
        self.assertEqual('hi there', posts[0].text)
        
if __name__ == '__main__':
    unittest.main()
