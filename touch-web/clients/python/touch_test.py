import touch_conn

conn = touch_conn.Connection('http://localhost:8080/touch-web/resources', '1', '');

list = conn.getchats()
print list
print '----------------------'
conn.getchat('0')