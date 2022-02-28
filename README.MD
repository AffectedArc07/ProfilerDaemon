# Paradise Profiler Daemon

Java application designed to take BYOND Profiler results and split them apart to be indexed at a later date, allowing multiple procs from multiple rounds to easily be visualised alongside eachother over time. An example SS13 codebase implementation can be found at <https://github.com/ParadiseSS13/Paradise/pull/17459>

This project itself does not provide a data extraction method, but that can be found at <https://github.com/ParadiseSS13/ParadisePublicAPI>

## Provided Projects

- `ProfilerDaemon.Core` - Core application which handles data ingest and sending it to the DB.
- `ProfilerDaemon.Database` - Database project automatically generated with JOOQ (Use `mvn package -P update-db` to update).

## Building

To build, run `mvn package` to generate the JAR file. This will then appear in the `out` directory.

## Running

1. Compile the codebase (See `Building`).
2. Copy the jar and the example `config.toml` in the repository root to a running location.
3. Import the `schema.sql` schema to your database server.
4. Setup the configuration to specify the webserver port, database credentials and redis host.
  4.1 Your database user should have access to the `paradise_profilerdaemon` database only. It should not have anything else.
5. Start the application up, it will now wait for profiler data to be sent to it.

## Code Guidelines

- Code must be tested
- Code must adhere to the style guidelines set in `checkstyle.xml`
  - These are google's guidelines but with minor tweaks for my own needs

## License

This project is licensed under GPL-3. You can find a copy of this license in the file `LICENSE.MD`.
