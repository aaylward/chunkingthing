# chunkingingthing

a little RxJava example

![demo](WATCHME.gif)

##### start the app
```shell
$ mvn clean package
$ java -jar target/TestingThing-1.0-SNAPSHOT.jar server example.yaml
```

##### meanwhile, in another tab...

```shell
$ curl -v -XPOST http://localhost:8080/v1/tasks
```

##### and don't forget!
```shell
$ curl -v http://localhost:8080/v1/tasks
```
