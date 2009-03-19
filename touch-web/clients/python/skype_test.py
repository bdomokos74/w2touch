
# ----------------------------------------------------------------------------------------------------
#  Python / Skype4Py example that prints out chat messages
#
#  Tested with  Skype4Py version 0.9.28.5 and Skype verson 3.8.0.96

import sys
import Skype4Py
import touch_conn

# ----------------------------------------------------------------------------------------------------
# Fired on attachment status change. Here used to re-attach this script to Skype in case attachment is lost. Just in case.
def OnAttach(status):
    print 'API attachment status: ' + skype.Convert.AttachmentStatusToText(status)
    if status == Skype4Py.apiAttachAvailable:
        skype.Attach();

    if status == Skype4Py.apiAttachSuccess:
       print('******************************************************************************')
       onlineSet = set()
       for user in skype.Friends:
           if user.OnlineStatus == 'ONLINE':
               onlineSet.add(user)
       for user in onlineSet:
           print '->    ', user.Handle
       #skype.SendMessage('nagyjutka', 'hallo 123');


# ----------------------------------------------------------------------------------------------------
# Fired on chat message status change. 
# Statuses can be: 'UNKNOWN' 'SENDING' 'SENT' 'RECEIVED' 'READ'        

def OnMessageStatus(Message, Status):
    if Status == 'RECEIVED':
        print(Message.ChatName + '(' + Message.FromDisplayName + ')->Myself: ' + Message.Body);
        
    if Status == 'SENT':
        print('Myself->:' + Message.ChatName + ' ' + Message.Body);


# ----------------------------------------------------------------------------------------------------

# create access object to touch-web
conn = touch_conn.Connection('http://localhost:8080/touch-web/resources', '1', '');

# Creating instance of Skype object, assigning handler functions and attaching to Skype.
skype = Skype4Py.Skype();
skype.OnAttachmentStatus = OnAttach;
skype.OnMessageStatus = OnMessageStatus;

print('******************************************************************************');
print 'Connecting to Skype..'
skype.Attach();

# ----------------------------------------------------------------------------------------------------
# Looping until user types 'exit'
Cmd = '';
while not Cmd == 'exit':
    if Cmd == 'getchat':
        conn.getchat('0')
    Cmd = raw_input('');
    

