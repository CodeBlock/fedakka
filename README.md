# Fedakka

This is a simple Scala Play 2.1 app that uses Akka to subscribe to the Fedora
Fedmsg ZeroMQ feed and play the messages to the user's browser.

It is not an official app, and there's no plan to run this on Fedora Infra, but
it can pretty easily run on OpenShift.

As of right now it just spits out JSON to the browser, but we can easily style
it and make it nicer-looking.

# License

Apache 2.0
