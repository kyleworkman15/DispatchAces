# DispatchAces
About

This application was designed for an ACES dispatcher and driver to utilize while accepting rides from Augustana College students using the 
ACES service. This is meant to replace the phone call that was previously required for students to request rides and for ACES to accept rides.
The dispatcher is able to edit the wait time on a given ride and then send that ride through to the driver. This acts as a notification for the
rider as their ride request screen will be updated with the new wait time and they will be given an ETA. Previously, there was no fluid method
of communication between the two parties after the ride had been requested. 

How it works

OVERALL PROCESS:

The dispatcher and driver will need to sign in using authenticated email accounts. Once they are signed in, there will be two views--one list
of pending ride and one list of active rides. The dispatcher can send rides from pending to active, and the driver can clear out those active
rides once he completes them. There will also be a button at the top of the screen for turning ACES on and off. One of the workers can
turn this off at the end of the day and on at the start of the day, to indicate that ACES is accepting rides. 

IN THE BACKGROUND:

The pending and active ride lists displayed in the interface pull data from a firebase database that will be updated in realtime. Whenever
a user requests a ride and sends an entry to the database, the dispatch app realizes this change and updates the list display to show the new ride(s)
requested. When the dispatcher clicks "Send" for a pending ride, the ride is deleted from the pending rides node in the database and moved to
an active rides node. When the driver clicks "Clear" for a previously active ride, the ride is deleted from the active rides node and sent
to the archived rides node. This archived rides node will be used by ACES for analyzing ride data and improving their services. 

Testing

We tested our application on a few different devices. We used emulators, Tablets running API 20, Tablet running API 23, and a phone running the most recent version. 

1.) 
