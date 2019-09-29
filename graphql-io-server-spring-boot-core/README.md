## Spring Web Socket Showcase 


## Build & Run 

mvn jetty:run 


## Build & Run to Debug 

* Environment Setting 

export MAVEN_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000 -Xnoagent -Djava.compiler=NONE"

* Visual Stuidoe Code .vscode/launch.json

Press F5 an set the launch parameter 

{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug (Attach)",
            "request": "attach",
            "hostName": "localhost",
            "port": 8000
        }
    ]
}

Press F5 to Debug 


## Web Socket Client 
Install Web Socket Client 

npm install -g wscat

Using Web Socket Client to test Web Socket Server 

wscat --connect localhost:8080/spring-websocket-showcase/graphql



## Test simple web socket server  
The implemented handler upports no protocol 
mvn jetty:run 
wscat --connect localhost:8080/spring-websocket-showcase/graphql
> send a simple text 

## Test simple web socket server with protocol   
The implemented web socket hanlder supports the myprotocol protocol


mvn jetty:run 
wscat --subprotocol tp --connect localhost:8080/spring-websocket-showcase/graphql/protocol
> send a simple text 
