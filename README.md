# tcpchat

PROTOCOL:

1. Server waits for first HELO message
2. Server responds with a 100 message and names the first client "Green Client"
3. Server and Green Client wait for HELO message from Yellow Client
4. Server responds with a 200 message that is sent to both Green and Yellow Client
5. Server listens for data from Green Client and sends whatever it gets to Yellow Client
6. Server listens for data from Yellow Client and sends whatever it gets to Green Client
7. Repeat steps 5 and 6 unless one of the Clients sends a 300 message
8. When a 300 message is recieved, the Server forwards it to the other Client before shutting down

My Server's current address is 192.168.0.9 port 6789
