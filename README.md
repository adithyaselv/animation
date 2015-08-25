# animation

Animations framework in scala using functional programming(which i am learning) based on 
Paul Hudak's HaskelSchoolOfExpression.
 
It is used to show animation on java swt canvas and html canvas(using scala js).
 Examples
 1)Clock
 ![Alt text](/images/clock.gif?raw=true "Clock")
 2)Moving Train
 ![Alt text](/images/train.gif?raw=true "Moving train")

## Run awt animation
Directly execute `drivers.awt.Clock` or `drivers.awt.Train` class to pop up animation window.


## Run html animation
How to run scala-js is taken from [href](https://github.com/lihaoyi/workbench-example-app).

Run `sbt fullOptJS` and open up 
`http://localhost:12345/target/scala-2.11/classes/clock.html` or
`http://localhost:12345/target/scala-2.11/classes/train.html`

