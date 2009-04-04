import touch_conn
import unittest

class TestConn(unittest.TestCase):
    def setUp(self):
        self.conn = touch_conn.Connection('http://localhost:8080/touch-web/resources', 'testuser', '');
    
    def testGetchats(self):
        list = self.conn.getchats()
        self.assertTrue(list )
        print list
    
    def testGetchat(self):
        print self.conn.getchat('chat1')
    
    def testCreateAddchatReq(self):
        self.assertEqual('http://localhost:8080/touch-web/resources/users/testuser/chats', self.conn.create_addchat_uri())
        
    def testAddchat(self):
        if not self.conn.getchat('chat3'):
            ref = self.conn.addchat('chat3', 'test');
            print ref
        else:
            self.fail()
            
if __name__ == '__main__':
    unittest.main()
