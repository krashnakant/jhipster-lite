package tech.jhipster.lite.generator.server.springboot.mvc.security.common.domain;

import tech.jhipster.lite.generator.project.domain.Project;

public interface CommonSecurityService {
  void updateExceptionTranslator(Project project);
  void updateIntegrationTestWithMockUser(Project project);
}
