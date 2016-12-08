When run on EV3 with JRE compact3:
```
root@EV3:/home/lejos/programs# /home/root/lejos/ejre1.7.0_60/bin/java -version
java version "1.8.0"
Java(TM) SE Embedded Runtime Environment (build 1.8.0-b132, profile compact3, headless)
Java HotSpot(TM) Embedded Client VM (build 25.0-b70, mixed mode, sharing)

root@EV3:/home/lejos/programs# /home/root/lejos/ejre1.7.0_60/bin/java -jar ev3-t
racked-loader.jar
Exception in thread "main" java.lang.NoClassDefFoundError: java/beans/BeanInfo
        at groovy.lang.MetaClassImpl.isBeanDerivative(MetaClassImpl.java:3325)
        at groovy.lang.MetaClassImpl.addProperties(MetaClassImpl.java:3281)
        at groovy.lang.MetaClassImpl.initialize(MetaClassImpl.java:3265)
        at org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl.<init>(MetaClassRegistryImpl.java:122)
        at org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl.<init>(MetaClassRegistryImpl.java:74)
        at groovy.lang.GroovySystem.<clinit>(GroovySystem.java:36)
        at org.codehaus.groovy.reflection.ClassInfo.isValidWeakMetaClass(ClassInfo.java:265)
        at org.codehaus.groovy.reflection.ClassInfo.getMetaClassForClass(ClassInfo.java:235)
        at org.codehaus.groovy.reflection.ClassInfo.getMetaClass(ClassInfo.java:280)
        at org.codehaus.groovy.runtime.callsite.ClassMetaClassGetPropertySite.<init>(ClassMetaClassGetPropertySite.java:38)
        at org.codehaus.groovy.runtime.callsite.AbstractCallSite.createClassMetaClassGetPropertySite(AbstractCallSite.java:373)
        at org.codehaus.groovy.runtime.callsite.AbstractCallSite.createGetPropertySite(AbstractCallSite.java:325)
        at org.codehaus.groovy.runtime.callsite.AbstractCallSite.acceptGetProperty(AbstractCallSite.java:312)
        at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callGetProperty(AbstractCallSite.java:296)
        at pl.rafalmag.ev3.trackedloader.Main.<clinit>(Main.groovy)
Caused by: java.lang.ClassNotFoundException: java.beans.BeanInfo
        at java.net.URLClassLoader$1.run(Unknown Source)
        at java.net.URLClassLoader$1.run(Unknown Source)
        at java.security.AccessController.doPrivileged(Native Method)
        at java.net.URLClassLoader.findClass(Unknown Source)
        at java.lang.ClassLoader.loadClass(Unknown Source)
        at sun.misc.Launcher$AppClassLoader.loadClass(Unknown Source)
        at java.lang.ClassLoader.loadClass(Unknown Source)
        ... 15 more
```

It looks like Full JRE is required by groovy:
```
C:\Program Files\Java\jdk1.8.0_91\bin>jdeps -P -cp C:\Users\Rafal\.m2\repository\org\codehaus\groovy\groovy-all\2.4.7\groovy-all-2.4.7.jar g
roovy.lang.MetaClassImpl
groovy-all-2.4.7.jar -> C:\Program Files\Java\jdk1.8.0_91\jre\lib\rt.jar (Full JRE)
   groovy.lang (groovy-all-2.4.7.jar)
      -> java.beans                                         Full JRE
      -> java.lang                                          compact1
      -> java.lang.reflect                                  compact1
      -> java.net                                           compact1
      -> java.security                                      compact1
      -> java.util                                          compact1
      -> java.util.concurrent                               compact1
      -> org.codehaus.groovy                                groovy-all-2.4.7.jar
      -> org.codehaus.groovy.ast                            groovy-all-2.4.7.jar
      -> org.codehaus.groovy.classgen.asm                   groovy-all-2.4.7.jar
      -> org.codehaus.groovy.control                        groovy-all-2.4.7.jar
      -> org.codehaus.groovy.reflection                     groovy-all-2.4.7.jar
      -> org.codehaus.groovy.reflection.android             groovy-all-2.4.7.jar
      -> org.codehaus.groovy.runtime                        groovy-all-2.4.7.jar
      -> org.codehaus.groovy.runtime.callsite               groovy-all-2.4.7.jar
      -> org.codehaus.groovy.runtime.metaclass              groovy-all-2.4.7.jar
      -> org.codehaus.groovy.runtime.typehandling           groovy-all-2.4.7.jar
      -> org.codehaus.groovy.runtime.wrappers               groovy-all-2.4.7.jar
      -> org.codehaus.groovy.util                           groovy-all-2.4.7.jar
```
