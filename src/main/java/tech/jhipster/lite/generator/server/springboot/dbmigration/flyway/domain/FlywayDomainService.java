package tech.jhipster.lite.generator.server.springboot.dbmigration.flyway.domain;

import static tech.jhipster.lite.common.domain.FileUtils.getPath;
import static tech.jhipster.lite.generator.project.domain.Constants.MAIN_RESOURCES;
import static tech.jhipster.lite.generator.server.springboot.dbmigration.flyway.domain.Flyway.DEFAULT_FLYWAY_ENABLED;
import static tech.jhipster.lite.generator.server.springboot.dbmigration.flyway.domain.Flyway.DEFAULT_FLYWAY_LOCATIONS;
import static tech.jhipster.lite.generator.server.springboot.dbmigration.flyway.domain.Flyway.DEFAULT_SQL_FILES_FOLDER;

import java.time.Clock;
import java.util.TreeMap;
import tech.jhipster.lite.common.domain.TimeUtils;
import tech.jhipster.lite.error.domain.GeneratorException;
import tech.jhipster.lite.generator.buildtool.generic.domain.BuildToolService;
import tech.jhipster.lite.generator.project.domain.Project;
import tech.jhipster.lite.generator.project.domain.ProjectFile;
import tech.jhipster.lite.generator.project.domain.ProjectRepository;
import tech.jhipster.lite.generator.server.springboot.common.domain.SpringBootCommonService;

public class FlywayDomainService implements FlywayService {

  private static final String SQL_INIT_FILE_SOURCE = "server/springboot/dbmigration/flyway";
  private static final String SQL_INIT_FILE_NAME = "V%s__init.sql";
  private static final String SQL_USER_AUTHORITY_FILE_NAME = "V%s__create_user_authority_tables.sql";

  private final BuildToolService buildToolService;
  private final ProjectRepository projectRepository;
  private final SpringBootCommonService springBootCommonService;
  private final Clock clock;

  public FlywayDomainService(
    BuildToolService buildToolService,
    ProjectRepository projectRepository,
    SpringBootCommonService springBootCommonService,
    Clock clock
  ) {
    this.buildToolService = buildToolService;
    this.projectRepository = projectRepository;
    this.springBootCommonService = springBootCommonService;
    this.clock = clock;
  }

  @Override
  public void init(Project project) {
    addFlywayDependency(project);
    addChangelogSql(project);
    addProperties(project);
  }

  @Override
  public void addFlywayDependency(Project project) {
    buildToolService
      .getVersion(project, "flyway")
      .ifPresentOrElse(
        version -> buildToolService.addProperty(project, "flyway.version", version),
        () -> {
          throw new GeneratorException("Version not found: flyway");
        }
      );
    buildToolService.addDependency(project, Flyway.flywayDependency());
    if (springBootCommonService.isSetWithMySQLOrMariaDBDatabase(project)) {
      buildToolService.addDependency(project, Flyway.additionalFlywayMysqlAndMariaDBDependency());
    }
  }

  @Override
  public void addChangelogSql(Project project) {
    projectRepository.add(
      ProjectFile
        .forProject(project)
        .withSource(getPath(SQL_INIT_FILE_SOURCE, "resources"), "V00000000000000__init.sql")
        .withDestination(getPath(MAIN_RESOURCES, DEFAULT_SQL_FILES_FOLDER), buildFileNameWithTimestamp(SQL_INIT_FILE_NAME, 0))
    );
  }

  @Override
  public void addProperties(Project project) {
    getFlywayProperties().forEach((k, v) -> springBootCommonService.addProperties(project, k, v));
  }

  @Override
  public void addUserAuthorityChangelog(Project project) {
    String sqlFileSource = buildSqlFileSource(project);

    projectRepository.add(
      ProjectFile
        .forProject(project)
        .withSource(getPath(SQL_INIT_FILE_SOURCE, "resources/user"), sqlFileSource)
        .withDestination(getPath(MAIN_RESOURCES, DEFAULT_SQL_FILES_FOLDER), buildFileNameWithTimestamp(SQL_USER_AUTHORITY_FILE_NAME, 1))
    );
  }

  private String buildSqlFileSource(Project project) {
    String sqlFileSourcePrefix = "V00000000000000__create_user_authority_tables";
    if (isDatabaseUseSequences(project)) {
      return sqlFileSourcePrefix + "_postgresql.sql";
    }
    return sqlFileSourcePrefix + ".sql";
  }

  private TreeMap<String, Object> getFlywayProperties() {
    TreeMap<String, Object> result = new TreeMap<>();

    result.put("spring.flyway.enabled", DEFAULT_FLYWAY_ENABLED);
    result.put("spring.flyway.locations", DEFAULT_FLYWAY_LOCATIONS);

    return result;
  }

  private String buildFileNameWithTimestamp(String fileNameFormat, int idxScript) {
    return fileNameFormat.formatted(TimeUtils.getDateString(clock, idxScript));
  }

  private boolean isDatabaseUseSequences(Project project) {
    return springBootCommonService.isDatabaseUseSequences(project);
  }
}
