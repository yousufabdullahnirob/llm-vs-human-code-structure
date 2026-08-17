package com.example.metadata;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.jdbi.v3.core.Jdbi;

/**
 * Application entry point. Wires together the database, background jobs,
 * and REST resources, then hands control to the embedded server.
 *
 * The overall shape: initialize() registers Dropwizard bundles (config
 * substitution, static assets, etc.) before the server starts; run() does
 * an ordered bootstrap once configuration is available — connect to the
 * database, run migrations, wire up the shared DAO, register resources
 * and health checks, then kick off any background jobs.
 */
public class MetadataApplication extends Application<MetadataApplicationConfig> {

  public static void main(String[] args) throws Exception {
    new MetadataApplication().run(args);
  }

  @Override
  public String getName() {
    return "metadata-service";
  }

  @Override
  public void initialize(Bootstrap<MetadataApplicationConfig> bootstrap) {
    // Bundles that need to hook in before configuration is parsed, e.g.
    // environment-variable substitution in the config file, or serving
    // the web UI's static assets.
    bootstrap.addBundle(new ConfigSubstitutionBundle());
    bootstrap.addBundle(new StaticAssetBundle());
  }

  @Override
  public void run(MetadataApplicationConfig config, Environment environment) throws Exception {
    Jdbi jdbi = connectToDatabase(config);

    runDatabaseMigrations(jdbi, config);

    CollectionDao collectionDao = jdbi.onDemand(CollectionDao.class);
    EntityRegistry.setCollectionDao(collectionDao);

    registerExceptionMappers(environment);
    registerResources(environment, collectionDao);
    registerHealthChecks(environment, jdbi);

    startBackgroundJobs(config, jdbi);
  }

  private Jdbi connectToDatabase(MetadataApplicationConfig config) {
    Jdbi jdbi = Jdbi.create(config.getDatabase().getUrl());
    jdbi.installPlugin(new org.jdbi.v3.sqlobject.SqlObjectPlugin());
    return jdbi;
  }

  private void runDatabaseMigrations(Jdbi jdbi, MetadataApplicationConfig config) {
    // A real implementation runs Flyway/Liquibase-style migration scripts
    // here before anything else touches the schema.
    new MigrationRunner(jdbi, config.getMigrationsPath()).runAll();
  }

  private void registerExceptionMappers(Environment environment) {
    environment.jersey().register(new NotFoundExceptionMapper());
    environment.jersey().register(new ValidationExceptionMapper());
  }

  private void registerResources(Environment environment, CollectionDao collectionDao) {
    environment.jersey().register(new UserResource(collectionDao));
    environment.jersey().register(new TeamResource(collectionDao));
  }

  private void registerHealthChecks(Environment environment, Jdbi jdbi) {
    environment.healthChecks().register("database", new DatabaseHealthCheck(jdbi));
  }

  private void startBackgroundJobs(MetadataApplicationConfig config, Jdbi jdbi) {
    if (config.isSearchIndexingEnabled()) {
      new SearchIndexingJob(jdbi).scheduleRecurring();
    }
  }
}
