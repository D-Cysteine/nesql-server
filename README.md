## Not Enough SQL Server

The server module for NESQL. The exporter module can be found
[here](https://github.com/D-Cysteine/nesql-exporter). Still very WIP.

### Instructions

1. Place `nesql-server-*.jar` in the same directory as the `nesql-repository`
   folder that was exported by the exporter. The name must be
   `nesql-repository`, at least until a customization option is added.
2. Start the server with `java -jar nesql-server-*.jar`.
3. Visit `http://localhost:8080/index` to access the server.
4. You can shut down the server by visiting `http://localhost:8080/shutdown`, or
   just with `Ctrl-C` on the command line.
