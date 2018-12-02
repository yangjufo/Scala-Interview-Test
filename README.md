# Scala-Interview-Test
#### Author: Jian Yang

#### File organization
The project is built by sbt in Intellij IDEA.

The source code files are under /src/main/scala/ and the test code file is in /src/test/scala/.<br />For convience, all code files are duplicated in /source_code.

All responses to questions and explanation of solutions are in below.

## Project Preference

## Warmup
Assumption: the parameter is an integer.
1. Time complexity: O(2^n).<br />Space complexity:  O(n) if we consider the function call stack size, otherwise O(1).
2. Yes, there are better solutions.<br /> First solution can be dynamic programming. We store the f(x) of every x from 0 to n, and thus we only need to calculate f(x) once for every x. Therefore, the time complexity will be O(n) while the space complexity is still O(n). <br />Futhermore, in this specific problem, f(n) = 2^n, so we can use the power function directly, then the time complexity is O(n) but the space complexity becomes O(1).
 
## Using APIs and testing: JSON serialization
### JSON serialization and deserialization
The Json4s (http://json4s.org/) library is used. 

<br />Json format:
<br />And: {"jsonClass": "And", "e1": JObject, "e2": JObject}
<br />Or: {"jsonClass": "Or", "e1": JObject, "e2", JObject}
<br />Not: {"jsonClass": "Not", "e": JObject}
<br />Variable: {"jsonClass": "Variable", "symbol", JString}
<br />True: {"jsonClass": "True"}
<br />False: {"jsonClass": "False"}

As shown above, a key named "jsonClass" is used to distinguish between different Boolean Expressions
<br />A custom serializer is used to process Boolean Expressions and Json Expressions recursively.

### Test
The ScalaTest (http://www.scalatest.org/) library is used.

The Boolean Expressions and Json Expressions are cheked recursively. The checking is based on type, for example, "e1" should be a JObject in a Json Expression while an extracted Boolean Expression should belongs to (And, Or, Not, Variable, True, False).


## Bonus assignment
### Algebraic transformation
First, pattern match is used to convert a Boolean Expression into an Algebra Expression recursively.
And then, simplication is done on the algebra expression.
### Boolean algebra server
The library socket is used.

The server is bound to a localhost port and accepting connection. When a message is received, it will convert the message into a Algebra Expression and send it to the client. If an expection is thrown, it will send the error message to the client.

The client established a connection to the server. It will read input from console, send the content to the server, and print the output.
### Bells and whistles

