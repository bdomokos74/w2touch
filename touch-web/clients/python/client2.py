#!/usr/bin/python
# delicious-httplib2.py
import sys
from xml.etree import ElementTree
import httplib2

def print_my_recent_bookmarks():
    client = httplib2.Http(".cache")
    #client.add_credentials(username, password)
    # Make the HTTP request, and fetch the response and the entity-body.
    response, xml = client.request('http://localhost:3000/users')
    # Turn the XML entity-body into a data structure.
    print xml
    doc = ElementTree.fromstring(xml)
    print doc
    # Print information about every bookmark.
    for post in doc.findall('post'):
        print "%s: %s" % (post.attrib['description'], post.attrib['href'])

# Main program
#if len(sys.argv) != 3:
#    print "Usage: %s [username] [password]" % sys.argv[0]
#    sys.exit()
#username, password = sys.argv[1:]
#print_my_recent_bookmarks(username, password)

print_my_recent_bookmarks()
