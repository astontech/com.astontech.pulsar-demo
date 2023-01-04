# Getting Started

- Install Pulsar. If you're using linux or mac, you can follow the standard install documentation from Apache by using *wget* to download and *tar* to extract. Then run Pulsar locally in standalone mode with *bin/pulsar standalone*. 
  
- If you're using windows, first install Docker Desktop and then run Pulsar within a Docker container, being sure to map ports 6650 and 8080 of the container consistently:

```
docker run -it -p 6650:6650 -p 8080:8080 apachepulsar/pulsar:2.10.2 bin/pulsar standalone
```

- Once Pulsar is running, you can run this demo producer app and the demo consumer app separately.

# Usage

Once everything's running, use Postman to send a POST request to *localhost:8081/producer*. Whatever you put in the request body of that will be produced to a Pulsar topic and consumed by the consumer app. You can send a GET request to the other app's endpoint *localhost:8082/consumer* to get back a list of all messages that have been received by the consumer app. You can also check the console/logs of each app to see messages explaining what's happening as it happens.

# Config

- By default, Pulsar will run on port 6650 so these demo apps target this port. If for some reason you need to run Pulsar on a different port, make sure you update the port number in the service URL specified in both demo apps' *src > main > resources > application.yml* to match the port you're running Pulsar on.

- These demo apps are producing to and consuming a topic defined in *src > main > resources > application.yml*. If you change the topic name in one, be sure to change it in both apps so they're looking at the same topic.

- This producer is configured to run on port 8081. If your 8081 is already taken, simply change the server port specified in *src > main > resources > application.yml*.

# App Walkthrough

### PulsarConfig

The config class for this app first registers a PulsarClient bean with the appropriate configs for our use. By doing this, whenever a method or class tries to autowire a parameter or field of type PulsarClient, Spring will know to use this specific Pulsar client that's configured to meet our needs. 

The config class also registers a Producer bean using the above PulsarClient. Again, since this producer is being registered as a bean, we can use dependency injection to make use of this specific producer from any other component of our app.

The final part of this config class is a pre-destroy hook that ensures our producer and client get properly closed before the app finishes shutting down.

### ProducerController

The controller simply adds a POST mapping for *localhost:8081/producer* that takes a String payload from the POST request's body and passes it off to the producer service. This allows us to produce messages to Pulsar by simply POSTing the message to this endpoint.

### ProducerService

Our producer service uses constructor dependency injection to grab the producer we set up earlier in the config class and then exposes a method that we can use to ask our producer to produce a message to its Pulsar topic.
