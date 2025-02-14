package tech.jhipster.lite.generator.ci.github.actions.application;

import static tech.jhipster.lite.TestUtils.tmpProjectWithBuildGradle;
import static tech.jhipster.lite.TestUtils.tmpProjectWithPomXml;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tech.jhipster.lite.IntegrationTest;
import tech.jhipster.lite.generator.project.domain.Project;

@IntegrationTest
class GitHubActionsApplicationServiceIT {

  @Autowired
  GitHubActionsApplicationService gitHubActionsApplicationService;

  @Test
  void shouldAddGitHubActionsForMaven() {
    Project project = tmpProjectWithPomXml();

    gitHubActionsApplicationService.addGitHubActions(project);

    GitHubActionsAssertFiles.assertFilesYml(project);
  }

  @Test
  void shouldAddGitHubActionsForGradle() {
    Project project = tmpProjectWithBuildGradle();

    gitHubActionsApplicationService.addGitHubActions(project);

    GitHubActionsAssertFiles.assertFilesYml(project);
  }
}
