# animation
 
It is used to show animation on java swt canvas and html canvas(using scala js).
 <br/>
 Examples
 <br/>
 1)Clock
 <br/>
 ![Alt text](/images/clock.gif?raw=true "Clock")
 <br/>
 2)Moving Train
 <br/>
 ![Alt text](/images/train.gif?raw=true "Moving train")

## Run awt animation
Directly execute `drivers.awt.Clock` or `drivers.awt.Train` class to pop up animation window.


## Run html animation
How to run scala-js is taken from [href](https://github.com/lihaoyi/workbench-example-app).
<br>
<br>
Run `sbt` to enter sbt console.
<br>
Run `clean` command.
<br>
Run `fastOptJS` command.
<br>
`http://localhost:12345/target/scala-2.11/classes/clock.html` or
<br>
`http://localhost:12345/target/scala-2.11/classes/train.html`

