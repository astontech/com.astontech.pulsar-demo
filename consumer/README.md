# Getting Started

- Install Pulsar. If you're using linux or mac, you can follow the standard install documentation from Apache by using *wget* to download and *tar* to extract. Then run Pulsar locally in standalone mode with *bin/pulsar standalone*.

- If you're using windows, first install Docker Desktop and then run Pulsar within a Docker container, being sure to map ports 6650 and 8080 of the container consistently:

```
docker run -it -p 6650:6650 -p 8080:8080 apachepulsar/pulsar:2.10.2 bin/pulsar standalone
```

- Once Pulsar is running, you can run this demo producer app and the demo consumer app separately.

# Usage

Once everything's running, use Postman to send a POST request to the other app's endpoint *localhost:8081/producer*. Whatever you put in the request body of that will be produced to a Pulsar topic and consumed by this consumer app. You can send a GET request to this app's endpoint *localhost:8082/consumer* to get back a list of all messages that have been received by the consumer app. You can also check the console/logs of each app to see messages explaining what's happening as it happens.

# Config

- By default, Pulsar will run on port 6650 so these demo apps target this port. If for some reason you need to run Pulsar on a different port, make sure you update the port number in the service URL specified in both demo apps' *src > main > resources > application.yml* to match the port you're running Pulsar on.

- These demo apps are producing to and consuming a topic defined in *src > main > resources > application.yml*. If you change the topic name in one, be sure to change it in both apps so they're looking at the same topic.

- This consumer is configured to run on port 8082. If your 8082 is already taken, simply change the server port specified in *src > main > resources > application.yml*.

# App Walkthrough

### DemoMessageListener

This component implements Pulsar's MessageListener interface, which requires us to define what should happen whenever a message is received. In our implementation, we simply log each step as it happens, and then pass the message content off to our simple consumer service. In a "real" app, this service could do any number of things, such as save the message to a repository, do some crazy business logic and respond by telling another producer to kick off some messages to subsequent Puslar topics, and so on. Here, we're just saving the message to an in-memory list that we can view later.

### PulsarConfig

The config class for this app first registers a PulsarClient bean with the appropriate configs for our use. By doing this, whenever a method or class tries to autowire a parameter or field of type PulsarClient, Spring will know to use this specific Pulsar client that's configured to meet our needs.

The config class also registers a Consumer bean using the above PulsarClient. It also autowires a MessageListener and tells our consumer to have that component "listen" to all messages this consumer receives. Since we've defined a message listener of our own and annotated it as a component, Spring knows that's the one we want to be injected into this bean method as "demoMessageListener". 

The final part of this config class is a pre-destroy hook that ensures our consumer and client get properly closed before the app finishes shutting down.

### ConsumerController

The controller simply adds a GET mapping for *localhost:8082/consumer* that asks our simple consumer service for all the messages it's recorded so far and passes those back to the client.

### ConsumerService

As described above, this service is a stand-in for a "real" service that could do anything you might want to do in reponse to receiving a message from Pulsar. In this case, we're just adding messages to a list so we can view them later.
