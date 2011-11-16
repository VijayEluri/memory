resolvers += "GWT plugin repo" at "http://thunderklaus.github.com/maven"

addSbtPlugin( "com.github.siasia" % "xsbt-web-plugin" % "0.1.2")

resolvers += "Web plugin repo" at "http://siasia.github.com/maven2"

addSbtPlugin("net.thunderklaus" % "sbt-gwt-plugin" % "1.2-SNAPSHOT")

libraryDependencies ++= Seq(
  "com.samskivert" % "gwt-asyncgen" % "1.0",
  "com.threerings" % "gwt-utils" % "1.5"
)
