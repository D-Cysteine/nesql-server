## Not Enough SQL Server

The server module for NESQL. The exporter module can be found
[here](https://github.com/D-Cysteine/nesql-exporter). Still WIP.

### Instructions

1. Place `nesql-server-*.jar` in the same directory as the `nesql-repository`
   folder that was exported by the exporter. The name must be
   `nesql-repository`, at least until a customization option is added.
2. Start the server with `java -jar nesql-server-*.jar`.
3. Visit `http://localhost:8080/index` to access the server.
4. You can shut down the server by visiting `http://localhost:8080/shutdown`, or
   just with `Ctrl-C` on the command line.

### Configuration

Configurable options are listed in
[this file](https://github.com/D-Cysteine/nesql-server/blob/main/src/main/resources/application.properties).
To configure them, make a copy of that file in the same directory as the JAR,
modify any options that you like, and delete any options that you don't want to
modify.

This is still WIP, and some options (like repository name) don't work yet! The
most useful option to configure is probably the SQL query timeout, which
defaults to 1 min.

### Faster startup

NESQL Server reads from an [HSQLDB database](http://hsqldb.org/) database. For
speed reasons, NESQL Exporter currently exports these databases in `TEXT`
format. If you'd like to greatly reduce the NESQL Server start-up time, you can
convert the exported database to `CACHED` format, though this will also increase
query times. The documentation for this topic can be found
[here](http://www.hsqldb.org/web/hsqlPerformance.html).

To perform the conversion, you will need to open the database with the HSQLDB
client, which can be downloaded from the link above (you may want to make a copy
of the database first, just to be safe). Then, run this command to list all of
the tables in the database:

```
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.SYSTEM_TABLES WHERE TABLE_TYPE = 'TABLE'
```

Finally, run this block of commands, with an entry for each table:

```
SET TABLE <table name> TYPE CACHED
SHUTDOWN COMPACT
```

For example, to compact the `ITEM` and `FLUID` tables, you would run:
```
SET TABLE ITEM TYPE CACHED
SET TABLE FLUID TYPE CACHED
SHUTDOWN COMPACT
```

This process is likely to take a while, and may moderately increase disk space
usage, but you will see a massive reduction in server start-up time, and a
moderate increase in query time.