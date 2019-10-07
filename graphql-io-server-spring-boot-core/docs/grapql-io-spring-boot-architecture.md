# GraphQL IO Server Spring Boot Architecture 

## Open Points 

1. How can we integrate GraphQL Java Tools in our implementation? 
2. How can we convert Low Level Formats (Subprotocols)?
3. How can we realize a context object?
4. How are the Subcription Queries work? 
5. How are the Subscription Schema work? 


## Context View 
* System Graphic & Description 

Describes the GraphQL IO Client here 
Describes the GraphQL IO Server here 


## Functional View 
* Block Graphic & Description 
* Packaging 

### GrapQL IO WebSocket  

* RFC 
* Welche Spring Versionen 
* Welche Spring Konzepte 

In Spring there are three different Concepts (**Standard**, Support and STOMP) to implement WebSockets. 
The GraphQL IO Server based on the Support Concept. 

The arguments are:

1. The Support Concept based on Spring and not on the Java EE Interfaces and Annotations 
2. It based on the standard protocol formats (Text, Binary, ...) and use no Supprotocol like STOMP 
3. New Features and Improvements are expected in the Support Concept  


#### GraphQL IO Web Socket Handler  
The **GraphQL IO Web Socket Handler** ist responsible to handle the whole **Query Processing**. 

The Handler implementation ```GrapQLIOWebSocketHandler``` based on the ```AbstractWebSocketHandler`` which 
connect the Handler to the WebSocket *Inbount* and *Outbound Channels* and map the protocoll types TEXT, BINARY ... 
on Methods. 

The Processing Steps are 

1. Message Converter 


#### GraphQL IO Message 2 Frame Converter 
The **GraphQL IO Message 2 Frame Converter** is responeable to convert the **GraphQL IO Sub Protocol Frame** in a GraphQL IO Message. 

##### GraphQL IO Sub Protocol Frame 
The structure of the GraphQL IO Sub Protocol Frame is shown in table ... 

```Frame: [ fid: number, rid: number, type: string, data: any ]```

+--------+--------+--------+--------+
|  fid   |  rid   |  type  |  data  |
+--------+--------+--------+--------+

The ```fid``` is the frame id, a numeric unique id between 0 and 2^32 the sending side manages per connection.
The ```rid``` is the reply id, the frame id of a previously received frame the current sent frame references.
The type is the frame type, a string identifying the type of data.
The data is the arbitrary data structure send in the frame. Any JSON is valid here.


##### 





## Data 

### Query 
* Die Queries sind soweit klar, müssen aber noch dokumentiert werden. 

### Schema 
**ToDo:** Wie sieht das Schema aus? 

### Protocoll 
* Das Protokoll ist soweit klar, muss allerdings noch dokumentiert werden. 

``` [fid:value,rid,]```


V



* Für die Konvertierung des GraphQL .... in ... 

**ToDo:** Hier muss ich nochmal Forschen, Begriffe, Formate, Was geht rein, was geht raus etc. 




## Deployment

### Starter 
@Fixme

### Autoconfiguration 
@Fixme

### Public Repository 
@Fixme

## Source Code 

### Github 
@Fixme

### License 
@Fixme

### Rules
@Fixme


## Appendix 

### Protocaoll

#### Subprotocoll 

The RFC for WebSocket defines subprotocols and protocol negotiation between client and server. 

We should also use a custom subprotocol for the graphql communication protocol between a grapql io client and server. 

This has some advantages:

1. Protocol Negotiation 
2. Identify a graphql io server 
2. Protocol version 


The Subprotocols build on top of the WebSocket Frames and changes nother of the underlying protocol. WebSocket Extentions has a other ... Over WebSocket Extention is is possible to change the WebSocket Frame Structure.  


#### Serialization 

The frame itself is encoded with either the object serialization format JavaScript Object Notation (JSON, RFC4627), Concise Binary Object Representation (CBOR, RFC7049) or MsgPack.




#### Web Socket Session 

##### Life Cycle
The sequence of interactions between an endpoint instance and remote peer is in Java API for WebSocket modelled by javax.websocket.Session instance. This interaction starts by mandatory open notification, continues by 0 - n websocket messages and is finished by mandatory closing notification.

##### 



## GraphQL Java Tools 
Integration 



## Open Points 

1. Wird das gemate Protokoll cbor codiert und dekodiert?
Schaltet man das cbor Protokoll an, wird aus der JSON Text Message 
eine CBOR Binary Message. 
2. Kann man das GraphQL CBOR in JSON konvertieren?    