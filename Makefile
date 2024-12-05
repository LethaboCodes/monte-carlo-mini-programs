JFLAG = -g
COMPILE = javac
SRCDIR = src/MonteCarloMini
BINDIR = bin
DOCDIR = doc

.SUFFIXES: .java .class
.java.class:
	$(COMPILE) $(JFLAG) -cp $(BINDIR) $*.java -d $(BINDIR)

CLASSES = $(SRCDIR)/*.java

default: classes

classes:
	$(COMPILE) $(JFLAG) -cp $(BINDIR) $(CLASSES) -d $(BINDIR)

clean:
	$(RM) $(BINDIR)/*.class

javadoc:
	javadoc -d $(DOCDIR) $(SRCDIR)/*.javaJFLAG = -g


